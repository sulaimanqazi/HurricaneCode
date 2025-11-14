package org.firstinspires.ftc.teamcode.teleops;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
@Autonomous(name = "Blue_Auto", group = "Autonomous")
public class autonomous_Blue extends LinearOpMode {

    // ===================== SHOOTER SUBSYSTEM =====================
    public static class Shooter {
        // --- Dashboard tunables ---
        public static double kP = 0.5;
        public static double kF = 0.00042;
        public static double OUTPUT_MAX = 1.0;
        public static double targetTPS = 1500.0;   // requested 1500 TPS

        public static double INTAKE_POWER   = 1.0;
        public static double TRANSFER_POWER = 0.4;

        private DcMotorEx flywheel;
        private DcMotorEx transfer;
        private DcMotorEx intake;

        private boolean flywheelEnabled = false;

        public Shooter(HardwareMap hardwareMap) {
            flywheel  = hardwareMap.get(DcMotorEx.class, "flywheel");
            transfer  = hardwareMap.get(DcMotorEx.class, "transfer");
            intake    = hardwareMap.get(DcMotorEx.class, "intake");

            // (optional but recommended) configure modes/behaviors here
            flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        // ========= your TPS controller wrapped into a method =========
        public void updateFlywheel() {
            if (!flywheelEnabled) return;

            double measuredTPS = flywheel.getVelocity(); // ticks/sec
            double power = kF * targetTPS + kP * (targetTPS - measuredTPS);
            power = Math.max(0, Math.min(OUTPUT_MAX, power));
            flywheel.setPower(power);
        }

        private void enableFlywheel() {
            flywheelEnabled = true;
        }

        public void stopAll() {
            flywheelEnabled = false;
            flywheel.setPower(0);
            transfer.setPower(0);
            intake.setPower(0);
        }

        // ========= Actions =========

        // 1) Shooter action: spin flywheel with PIDF for a fixed time
        public Action spinFlywheelForSeconds(double durationSeconds) {
            return new SpinFlywheelAction(durationSeconds);
        }

        // 2) Transfer action: run transfer motor for a fixed time
        public Action runTransferForSeconds(double durationSeconds, double power) {
            return new TransferAction(durationSeconds, power);
        }

        // 3) Intake action: run intake motor for a fixed time
        public Action runIntakeForSeconds(double durationSeconds, double power) {
            return new IntakeAction(durationSeconds, power);
        }

        // ----- Inner Action classes -----

        private class SpinFlywheelAction implements Action {
            private final double duration;
            private boolean initialized = false;
            private final ElapsedTime timer = new ElapsedTime();

            SpinFlywheelAction(double durationSeconds) {
                this.duration = durationSeconds;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {

                if (!initialized) {
                    initialized = true;
                    timer.reset();
                    enableFlywheel();
                }

                // update PIDF every loop
                updateFlywheel();

                packet.put("flywheelVel", flywheel.getVelocity());
                packet.put("targetTPS", targetTPS);

                if (timer.seconds() < duration) {
                    return true;    // keep running
                } else {
                    // just stop flywheel, leave intake/transfer alone
                    flywheelEnabled = false;
                    flywheel.setPower(0);
                    return false;
                }
            }
        }

        private class TransferAction implements Action {
            private final double duration;
            private final double power;
            private boolean initialized = false;
            private final ElapsedTime timer = new ElapsedTime();

            TransferAction(double durationSeconds, double power) {
                this.duration = durationSeconds;
                this.power = power;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {

                if (!initialized) {
                    initialized = true;
                    timer.reset();
                    transfer.setPower(power);
                }

                if (timer.seconds() < duration) {
                    return true;
                } else {
                    transfer.setPower(0);
                    return false;
                }
            }
        }

        private class IntakeAction implements Action {
            private final double duration;
            private final double power;
            private boolean initialized = false;
            private final ElapsedTime timer = new ElapsedTime();

            IntakeAction(double durationSeconds, double power) {
                this.duration = durationSeconds;
                this.power = power;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {

                if (!initialized) {
                    initialized = true;
                    timer.reset();
                    intake.setPower(power);
                }

                if (timer.seconds() < duration) {
                    return true;
                } else {
                    intake.setPower(0);
                    return false;
                }
            }
        }
    }

    // ===================== MAIN AUTO =====================
    @Override
    public void runOpMode() {
        final Pose2d startPose      = new Pose2d(-51,-48, Math.toRadians(230));
        final Pose2d scorePose      = new Pose2d(-25, -20,Math.toRadians(230));
        final Pose2d firstLinePose  = new Pose2d(-12, -22, Math.toRadians(270));
        final Pose2d secondLinePose = new Pose2d(12, -22, Math.toRadians(270));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        Shooter shooter = new Shooter(hardwareMap);

        int visionOutputPosition = 1; // fake vision result for now

        // Build the full auto path as one trajectory action
        TrajectoryActionBuilder autoPathBuilder = drive.actionBuilder(startPose)
                .strafeToLinearHeading(scorePose.position, scorePose.heading)

                .splineToLinearHeading(
                        new Pose2d(firstLinePose.position, firstLinePose.heading),
                        firstLinePose.heading
                )
                .lineToY(-55)

                .splineToLinearHeading(
                        new Pose2d(scorePose.position, scorePose.heading),
                        scorePose.heading
                )

                .splineToLinearHeading(
                        new Pose2d(secondLinePose.position, secondLinePose.heading),
                        secondLinePose.heading
                )
                .lineToY(-55)

                .splineToLinearHeading(
                        new Pose2d(scorePose.position, scorePose.heading),
                        scorePose.heading
                );

        Action autoPath = autoPathBuilder.build();

        // INIT LOOP (vision etc.)
        while (!isStopRequested() && !opModeIsActive()) {
            int position = visionOutputPosition;
            telemetry.addData("Position during Init", position);
            telemetry.update();
        }

        int startPosition = visionOutputPosition;
        telemetry.addData("Starting Position", startPosition);
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // Example: spin up + feed while also running the path
        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                autoPath,
                                shooter.spinFlywheelForSeconds(2.0),
                                shooter.runTransferForSeconds(2.0, Shooter.TRANSFER_POWER),
                                shooter.runIntakeForSeconds(2.0, Shooter.INTAKE_POWER)
                        )
                )
        );

        shooter.stopAll();
    }
}