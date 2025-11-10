package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import dev.nextftc.core.subsystems.Subsystem;

public class AprilTagAlignHelper implements Subsystem {

    private Limelight3A limelight;

    private IMU imu;

    private DcMotorEx frontLeft;

    private DcMotorEx frontRight;

    private DcMotorEx backLeft;

    private DcMotorEx backRight;

    private final Telemetry telemetry;

    private static final double kP = 0.04;
    private static final double MIN_POWER = 0.05;
    private static final double TX_TOLERANCE = 1.0;

    private boolean aligning = false;

    public AprilTagAlignHelper(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void initialize() {
        limelight.pipelineSwitch(0);
        limelight.start();
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);
    }

    public void setAligning(boolean align) {
        this.aligning = align;
    }

    @Override
    public void periodic() {
        LLResult result = limelight.getLatestResult();

        if (result == null || !result.isValid()) {
            stopMotors();
            telemetry.addData("Status", "No target");
            return;
        }

        double tx = result.getTx();
        telemetry.addData("tx", tx);

        if (!aligning) {
            stopMotors();
            telemetry.addData("Status", "Idle");
            return;
        }

        double rotationPower = kP * tx;

        if (Math.abs(rotationPower) < MIN_POWER && Math.abs(tx) > TX_TOLERANCE) {
            rotationPower = Math.copySign(MIN_POWER, rotationPower);
        }

        if (Math.abs(tx) <= TX_TOLERANCE) {
            stopMotors();
            telemetry.addData("Status", "Aligned");
        } else {
            rotationPower = Math.max(-1.0, Math.min(1.0, rotationPower));
            rotateInPlace(rotationPower);
            telemetry.addData("Status", "Rotating");
            telemetry.addData("RotationPower", rotationPower);
        }
    }
    private void rotateInPlace(double power) {
        frontLeft.setPower(power);
        backLeft.setPower(power);
        frontRight.setPower(-power);
        backRight.setPower(-power);
    }

    private void stopMotors() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}
