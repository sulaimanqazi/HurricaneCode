package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name = "FlywheelTeleOp")
public class FlywheelTeleOp extends OpMode {

    // ---------------- Dashboard tunable variables ----------------
    public static double kP = 0.0;               // proportional feedback
    public static double kV = 0.0005;            // velocity feedforward
    public static double kS = 0.03;              // static feedforward
    public static double maxTargetTPS = 4000.0;  // max flywheel speed (ticks/sec)

    // ---------------- Internal constants (fixed) ----------------
    private static final double OUTPUT_MAX = 1.0;     // power limit
    private static final int UPDATE_HZ = 100;         // loop rate (Hz)
    private static final String MOTOR_NAME = "flywheel_motor";

    // ---------------- State ----------------
    private DcMotorEx motor;
    private final ElapsedTime controlTimer = new ElapsedTime();

    private int lastPos = 0;
    private double targetTicksPerSec = 0.0;
    private double measuredTPS = 0.0;
    private double appliedPower = 0.0;
    private double lastDt = 0.0;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor = hardwareMap.get(DcMotorEx.class, MOTOR_NAME);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        lastPos = motor.getCurrentPosition();
        controlTimer.reset();

        telemetry.addLine("Flywheel controller initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Convert stick input to target speed
        double input = -gamepad1.left_stick_y * maxTargetTPS;
        if (input < 0) input = 0;
        targetTicksPerSec = input;

        // Fixed-rate control loop
        double period = 1.0 / UPDATE_HZ;
        double elapsed = controlTimer.seconds();
        if (elapsed >= period) {
            controlStep(elapsed);
            controlTimer.reset();
        }

        // Dashboard telemetry
        telemetry.addData("Target TPS", targetTicksPerSec);
        telemetry.addData("Measured TPS", measuredTPS);
        telemetry.addData("Applied Power", appliedPower);
        telemetry.addData("dt (s)", lastDt);
        telemetry.update();
    }

    @Override
    public void stop() {
        if (motor != null) motor.setPower(0.0);
    }

    // ---------------- Control Step (P + FF) ----------------
    private void controlStep(double dt) {
        if (dt <= 0) dt = 1e-3;
        lastDt = dt;

        // Measure velocity
        int pos = motor.getCurrentPosition();
        int delta = pos - lastPos;
        lastPos = pos;
        measuredTPS = delta / dt;

        // Error
        double error = targetTicksPerSec - measuredTPS;

        // Feedforward + feedback
        double ff = kV * targetTicksPerSec + (targetTicksPerSec > 0 ? kS : 0);
        double cmd = ff + kP * error;

        // Clamp + apply
        cmd = clamp(cmd);
        motor.setPower(cmd);
        appliedPower = cmd;
    }

    // ---------------- Utility ----------------
    private static double clamp(double v) {
        return Math.max(0, Math.min(OUTPUT_MAX, v));
    }
}
