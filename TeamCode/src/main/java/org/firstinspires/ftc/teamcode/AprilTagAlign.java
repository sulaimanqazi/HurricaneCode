package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name="limelightTest")
public class AprilTagAlign extends OpMode {
    // hardware objects
    private Limelight3A limelight;
    //private IMU imu;
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;


    // Tuning constants
    private static final double kP = 0.02;     // proportional gain
    private static final double MIN_POWER = 0.05; // minimum motor power when moving
    private static final double TX_TOLERANCE = 1.0; // the reference for the proportional controller

    @Override
    public void init() {

        // hardware init
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        //imu = hardwareMap.get(IMU.class, "imu");

        frontLeft  = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight  = hardwareMap.get(DcMotorEx.class, "backRight");

        //make sure the voltage is sent to motors
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        //frontLeft.setDirection(DcMotor.Direction.FORWARD);
        //backLeft.setDirection(DcMotor.Direction.FORWARD);
        //frontRight.setDirection(DcMotor.Direction.REVERSE);
        //backRight.setDirection(DcMotor.Direction.REVERSE);

        // limelight pipeline #1
        limelight.pipelineSwitch(0);
    }

    @Override
    public void start(){
        limelight.start();
    }

    @Override
    public void loop() {
        // don't worry about this part but don't delete it yet
        //YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        //limelight.updateRobotOrientation(orientation.getYaw());

        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            double tx = llResult.getTx();


            telemetry.addData("tx", tx);

            if (gamepad1.a) {
                double rotationPower = kP * tx; // basic P control FOR tx

                if (Math.abs(rotationPower) < MIN_POWER && Math.abs(tx) > TX_TOLERANCE) {
                    rotationPower = Math.copySign(MIN_POWER, rotationPower);

                    // Tx > 0 -> Robot is facing Right
                    // Tx < 0 -> Robot is facing Left
                }
                // this is basically where the "error" is when it reaches 1 degree tx or smaller the code stops
                if (Math.abs(tx) <= TX_TOLERANCE) {
                    stopMotors();
                    telemetry.addData("status", "Aligned");

                } else {
                    rotationPower = Math.max(-1.0, Math.min(1.0, rotationPower));
                    rotateInPlace(rotationPower);
                    telemetry.addData("status", "Rotating");
                    telemetry.addData("rotationPower", rotationPower);
                }
            } else {
                stopMotors();
            }
        } else {
            stopMotors();
            telemetry.addData("status", "No target");
        }
        telemetry.update();
    }

    private void rotateInPlace(double power) {
        // Tx > 0 -> Robot is facing Right
        // Tx < 0 -> Robot is facing Left
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
