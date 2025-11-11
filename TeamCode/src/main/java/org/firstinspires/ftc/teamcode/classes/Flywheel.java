package org.firstinspires.ftc.teamcode.classes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Flywheel {
    private DcMotorEx flywheelMotor;

    private DcMotorEx TransferMotor;
    private Telemetry telemetry;

    // PID coefficients
    private static final double kP = 0.0012;
    private static final double kI = 0.0;
    private static final double kD = 0.0002;

    // Feedforward coefficient (tune this)
    private static final double kF = 0.00035;

    private static final double TARGET_RPM = 2500.0;
    private double integralSum = 0.0;
    private double lastError = 0.0;

    public Flywheel(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
    }

    public void update(boolean isRunning) {
        if (!isRunning) {
            flywheelMotor.setPower(0);
            integralSum = 0;
            lastError = 0;
            telemetry.addData("Flywheel", "Off");
            return;
        }

        double velocity = flywheelMotor.getVelocity() * 60.0 / 28.0; // ticks/sec → RPM (adjust 28 for your motor’s ticks/rev)
        double error = TARGET_RPM - velocity;

        // PID
        integralSum += error;
        double derivative = error - lastError;
        lastError = error;

        double pidOutput = kP * error + kI * integralSum + kD * derivative;

        // Feedforward
        double feedforward = kF * TARGET_RPM;

        double output = pidOutput + feedforward;
        output = Math.max(0, Math.min(1, output)); // Clamp between 0–1

        flywheelMotor.setPower(output);

        telemetry.addData("Target RPM", TARGET_RPM);
        telemetry.addData("Current RPM", velocity);
        telemetry.addData("Output Power", output);
        telemetry.addData("Error", error);
    }
}