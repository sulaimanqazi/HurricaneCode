package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
@TeleOp (name = "FlywheelPID")
public class FlywheelPID extends OpMode {

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static double targetVelocity = 0; // ticks/sec

    private DcMotorEx frontLeft;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");

        frontLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(p, i, d, f));
    }
 

    @Override
    public void loop() {

        frontLeft.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(p, i, d, f));


        targetVelocity = gamepad1.right_stick_y * 1000;

        frontLeft.setVelocity(targetVelocity);

        double currentVelocity = frontLeft.getVelocity();

        telemetry.addData("velocity", currentVelocity);
        telemetry.addData("target", targetVelocity);
        telemetry.update();
    }
}