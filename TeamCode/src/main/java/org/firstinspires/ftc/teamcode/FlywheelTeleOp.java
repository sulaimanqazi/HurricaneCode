package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
@TeleOp
public class FlywheelTeleOp extends OpMode {

    // ---------- FTC Dashboard tuning ----------
    public static double kP = 0.01;       // proportional gain
    public static double kF = 0.00042;    // feedforward gain
    public static double maxTargetTPS = 1500; // target speed in ticks/sec

    private static final double OUTPUT_MAX = 1.0;
    private static final String MOTOR_NAME = "flywheel_motor";

    private DcMotorEx motor;
    private boolean flywheelOn = false;
    private boolean lastButtonState = false;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor = hardwareMap.get(DcMotorEx.class, MOTOR_NAME);
        motor.setDirection(DcMotor.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addLine("Flywheel ready");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Toggle flywheel on/off with A button
        boolean buttonPressed = gamepad1.a;
        if (buttonPressed && !lastButtonState) flywheelOn = !flywheelOn;
        lastButtonState = buttonPressed;

        double targetTPS = flywheelOn ? maxTargetTPS : 0.0;

        // Use motor.getVelocity() for measured speed
        double measuredTPS = motor.getVelocity(); // ticks/sec
        double power = kF * targetTPS + kP * (targetTPS - measuredTPS);
        power = Math.max(0, Math.min(OUTPUT_MAX, power));
        motor.setPower(power);

        // Telemetry
        telemetry.addData("Target TPS", targetTPS);
        telemetry.addData("Measured TPS", measuredTPS);
        telemetry.addData("Power", power);
        telemetry.update();
    }

    @Override
    public void stop() {
        if (motor != null) motor.setPower(0.0);
    }
}
