package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class IntakeTest extends OpMode {

    public static double kP = 0.01;       // proportional gain
    public static double kF = 0.00042;    // feedforward gain
    public static double maxTargetTPS = 1500; // target speed in ticks/sec
    private static final double OUTPUT_MAX = 1.0;

    private DcMotorEx flywheelMotor;
    private DcMotorEx intakeMotor;
    private DcMotorEx transferMotor;
    private Servo gate;
    private boolean flywheelOn = false;
    private boolean lastButtonState = false;
    private double openPos = 0, closePos = 1;
    private boolean lastGateState = false;
    private boolean servoToggled = false;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flyWheelMotor");
        transferMotor = hardwareMap.get(DcMotorEx.class, "transferMotor");

        gate = hardwareMap.get(Servo.class, "gate");


        flywheelMotor.setDirection(DcMotor.Direction.FORWARD);
        flywheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

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
        double measuredTPS = flywheelMotor.getVelocity(); // ticks/sec
        double power = kF * targetTPS + kP * (targetTPS - measuredTPS);
        power = Math.max(0, Math.min(OUTPUT_MAX, power));
        flywheelMotor.setPower(power);

        // Telemetry
        telemetry.addData("Target TPS", targetTPS);
        telemetry.addData("Measured TPS", measuredTPS);
        telemetry.addData("Power", power);

        telemetry.update();

        intakeMotor.setPower(gamepad1.right_stick_x);
        transferMotor.setPower(gamepad1.left_stick_y);

        boolean currentButtonState = gamepad1.a;  // change button as needed

        // Toggle only when the button is pressed down (edge detection)
        if (currentButtonState && !lastGateState) {
            servoToggled = !servoToggled; // flips between true and false
        }

        // Update servo position
        if (servoToggled) {
            gate.setPosition(openPos);
        } else {
            gate.setPosition(closePos);
        }

        // Remember current button state
        lastGateState = currentButtonState;

    }


    @Override
    public void stop() {
        if (flywheelMotor != null) flywheelMotor.setPower(0.0);
    }
}
