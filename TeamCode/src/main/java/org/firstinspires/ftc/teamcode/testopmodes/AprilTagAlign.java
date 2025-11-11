package org.firstinspires.ftc.teamcode.testopmodes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp(name="AprilTagAlign")
public class AprilTagAlign extends OpMode {
    // hardware objects
    private Limelight3A limelight;
    private IMU imu;

    // motors must be class-level so all methods can access them
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;

    // ----- tuning constants -----
    private static final double kP = 0.04;     // proportional gain
    private static final double MIN_POWER = 0.05; // minimum motor power when moving
    private static final double TX_TOLERANCE = 1.0; // degrees: when |tx| <= this, consider aligned

    @Override
    public void init() {
        // link software objects to robot config names
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        imu = hardwareMap.get(IMU.class, "imu");

        frontLeft  = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight  = hardwareMap.get(DcMotorEx.class, "backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // choose the limelight pipeline you want
        limelight.pipelineSwitch(0);
    }

    @Override
    public void start(){
        limelight.start();
    }

    @Override
    public void loop() {
        // update robot orientation (some Limelight APIs use this for pose math)
        //YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        //limelight.updateRobotOrientation(orientation.getYaw());

        // get the most recent Limelight result
        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            double tx = llResult.getTx(); // horizontal offset in degrees
            telemetry.addData("tx", tx);

            if (gamepad1.a) { // while A is held, do auto-align
                // ---------- PROPORTIONAL CONTROLLER ----------
                double rotationPower = kP * tx; // basic P control

                // if power is too small to move the robot, but we're still outside tolerance,
                // give it a minimum nudge (preserve direction with copySign)
                if (Math.abs(rotationPower) < MIN_POWER && Math.abs(tx) > TX_TOLERANCE) {
                    rotationPower = Math.copySign(MIN_POWER, rotationPower);

                    // Tx > 0 -> Right
                    // Tx < 0 -> Left
                }

                // if inside deadband (tolerance) stop; otherwise rotate
                if (Math.abs(tx) <= TX_TOLERANCE) {
                    stopMotors();
                    telemetry.addData("status", "Aligned");
                } else {
                    // optional: clamp to motor range [-1, 1]
                    rotationPower = Math.max(-1.0, Math.min(1.0, rotationPower));
                    rotateInPlace(rotationPower);
                    telemetry.addData("status", "Rotating");
                    telemetry.addData("rotationPower", rotationPower);
                }
            } else {
                // not auto-aligning: stop rotation (or allow driver control here)
                stopMotors();
            }
        } else {
            // no valid target seen: stop motors (or you could keep last command)
            stopMotors();
            telemetry.addData("status", "No target");
        }

        telemetry.update();
    }

    // rotation for mecanum (left side forward, right side backward => clockwise for +power)
    private void rotateInPlace(double power) {

        // if tx < 0 that means power , then robot is facing left

        frontLeft.setPower(power);
        backLeft.setPower(power);
        frontRight.setPower(power * -1);
        backRight.setPower(power * -1);
    }

    private void stopMotors() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}