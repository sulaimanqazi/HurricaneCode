package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
@TeleOp
public class OldFlywheelPID extends OpMode {

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static double targetVelocity = 0; // ticks/sec

    private DcMotorEx flywheel_motor;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        flywheel_motor = hardwareMap.get(DcMotorEx.class, "flywheel_motor");

        flywheel_motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        flywheel_motor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(p, i, d, f));
    }

    @Override
    public void loop() {

        flywheel_motor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(p, i, d, f));
        


        flywheel_motor.setVelocity(targetVelocity);

        double currentVelocity = flywheel_motor.getVelocity();

        telemetry.addData("velocity", currentVelocity);
        telemetry.addData("target", targetVelocity);
        telemetry.update();
    }
}