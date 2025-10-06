package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Config
public class Flywheel {

    public static double kP = 0.0;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public static double kV = 0.0005;
    public static double kS = 0.03;

    // Safety / quality-of-life
    public static double integralMax = 1.0;
    public static double outputMax  = 1.0;
    public static double slewPerSec = 3.0;

    // Update rate
    public static int updateHz = 100;

    // Velocity calc
    public static boolean usePositionDiffForVelocity = true;

    private final DcMotorEx motor;
    private volatile double targetTicksPerSec = 0.0;


    private volatile boolean running = false;
    private ScheduledExecutorService executor;
    private final ElapsedTime loopTimer = new ElapsedTime();
    private int lastPos = 0;
    private double lastError = 0.0;
    private double integral = 0.0;
    private double lastCommandedPower = 0.0;


    private volatile double measuredTPS = 0.0;
    private volatile double appliedPower = 0.0;
    private volatile double lastDt = 0.0;

    public Flywheel(HardwareMap hw, String motorName) {
        this.motor = hw.get(DcMotorEx.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void start() {
        if (running) return;
        running = true;

        loopTimer.reset();
        lastPos = motor.getCurrentPosition();
        lastError = 0.0;
        integral = 0.0;
        lastCommandedPower = 0.0;

        int periodMs = Math.max(5, (int) Math.round(1000.0 / Math.max(1, updateHz)));
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this::controlLoop, 0, periodMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        running = false;
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
        motor.setPower(0.0);
    }

    public void setTargetTicksPerSec(double tps) {
        targetTicksPerSec = tps;
    }

    public double getTargetTicksPerSec() { return targetTicksPerSec; }
    public double getMeasuredTicksPerSec() { return measuredTPS; }
    public double getAppliedPower() { return appliedPower; }
    public double getLastDt() { return lastDt; }

    private void controlLoop() {
        if (!running) return;


        double dt = loopTimer.seconds();
        loopTimer.reset();
        if (dt <= 0) dt = 1e-3;
        lastDt = dt;


        double velocityTPS;
        if (usePositionDiffForVelocity) {
            int pos = motor.getCurrentPosition();
            int delta = pos - lastPos;
            lastPos = pos;
            velocityTPS = delta / dt;
        } else {

            velocityTPS = motor.getVelocity();
        }
        measuredTPS = velocityTPS;


        double error = targetTicksPerSec - measuredTPS;


        integral += error * dt;
        integral = clamp(integral, -integralMax, integralMax);


        double derivative = (error - lastError) / dt;
        lastError = error;


        double ff = kV * targetTicksPerSec + kS * sign(targetTicksPerSec);


        double cmd = ff + kP * error + kI * integral + kD * derivative;


        if (slewPerSec > 0) {
            double maxStep = slewPerSec * dt;
            double delta = cmd - lastCommandedPower;
            if (Math.abs(delta) > maxStep) {
                cmd = lastCommandedPower + Math.copySign(maxStep, delta);
            }
        }


        cmd = clamp(cmd, -outputMax, outputMax);
        motor.setPower(cmd);


        lastCommandedPower = cmd;
        appliedPower = cmd;
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private static double sign(double v) {
        if (v > 0) return 1.0;
        if (v < 0) return -1.0;
        return 0.0;
    }
}
