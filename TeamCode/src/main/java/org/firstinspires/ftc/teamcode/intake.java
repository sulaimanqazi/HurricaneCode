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
@TeleOp(name = "intake")
public class intake extends OpMode {

    public static double kP = 0.01;       // proportional gain
    public static double kF = 0.00042;    // feedforward gain
    public static double maxTargetTPS = 1500; // target speed in ticks/sec
    private static final double OUTPUT_MAX = 1.0;

    private DcMotorEx flywheelMotor;
    private DcMotorEx intakeMotor;
    private DcMotorEx transferMotor;
    private DcMotorEx frontLeftMotor;
    private DcMotorEx frontRightMotor;
    private DcMotorEx backRightMotor;
    private DcMotorEx backLeftMotor;

    private Servo gate;
    private boolean flywheelOn = false;
    private boolean lastButtonState = false;
    public static double openPos = 0.6, closePos = 0.76;
    private boolean lastGateState = false;
    private boolean servoToggled = false;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        transferMotor = hardwareMap.get(DcMotorEx.class, "transferMotor");


         frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeft");
         backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeft");
         frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRight");
         backRightMotor = hardwareMap.get(DcMotorEx.class, "backRight");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        transferMotor.setDirection(DcMotor.Direction.REVERSE);

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");

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
        if (gamepad1.left_bumper){
        intakeMotor.setPower(1);
        } else{
            intakeMotor.setPower(0);
        }

        if (gamepad1.right_bumper){
            transferMotor.setPower(1);
        }
        else{
            transferMotor.setPower(0);

        }

        boolean currentButtonState = gamepad1.b;  // change button as needed

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

        double y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

    }


    @Override
    public void stop() {
        if (flywheelMotor != null) flywheelMotor.setPower(0.0);
    }
}
