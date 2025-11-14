package org.firstinspires.ftc.teamcode;
import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.MecanumDrive;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@Autonomous(name = "BLUE_TEST_AUTO_PIXEL", group = "Autonomous")
public class Centerstage extends LinearOpMode {
    public class Intake {
        private DcMotorEx intakeMotor;

        public Intake(HardwareMap hardwareMap) {
            intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
            intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //may change to float?
            intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //added
        }

        public class IntakeOn implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                intakeMotor.setPower(0.8);
                return false; // Chatgpt keeps on going back on forth on whether I return true or false, not sure what to do here
            }
        }

        public Action IntakeOn() {
            return new IntakeOn();
        }

        public class IntakeOff implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {


                intakeMotor.setPower(0); // no PID loop for these simple motors
                return false; //Return false for off
            }

        }

        public Action IntakeOff() {
            return new IntakeOff();
        }
    }

    public class Transfer {
        private DcMotorEx transferMotor;

        public Transfer(HardwareMap hardwareMap) {
            transferMotor = hardwareMap.get(DcMotorEx.class, "transferMotor");
            transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //changed to float
            transferMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            transferMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //added
        }

        public class TransferOn implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                transferMotor.setPower(0.8);
                return false;  // Chatgpt keeps on going back on forth on whether I return true or false, not sure what to do here
            }
        }

        public Action TransferOn() {
            return new TransferOn();
        }

        public class TransferOff implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {


                transferMotor.setPower(0); // No PID for these simple motors
                return false; //Return false immediately
            }

        }

        public Action TransferOff() {
            return new TransferOff();
        }
    }
    @Override
    public void runOpMode() {
        // Define poses using your friend's approach
        final Pose2d startPose = new Pose2d(-51, -48, Math.toRadians(230));
        final Pose2d scorePose = new Pose2d(-25, -20, Math.toRadians(230));
        final Pose2d firstLinePose = new Pose2d(-12, -22, Math.toRadians(270));
        final Pose2d secondLinePose = new Pose2d(12, -22, Math.toRadians(270));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        Intake intake = new Intake(hardwareMap);
        Transfer transfer = new Transfer(hardwareMap);

        telemetry.addData("Status", "Ready!");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // Single chained path with intake actions - your friend's style
        Actions.runBlocking(
                new SequentialAction(
                        intake.IntakeOn(),
                        transfer.TransferOn(),
                        drive.actionBuilder(startPose)
                                .strafeToLinearHeading(scorePose.position, scorePose.heading)
                                .splineToLinearHeading(new Pose2d(firstLinePose.position, firstLinePose.heading), firstLinePose.heading)
                                .lineToY(-55)
                                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                                .splineToLinearHeading(new Pose2d(secondLinePose.position, secondLinePose.heading), secondLinePose.heading)
                                .lineToY(-55)
                                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                                .waitSeconds(2)
                                .build(),
                        intake.IntakeOff(),
                        transfer.TransferOff()
                )
        );
    }
}