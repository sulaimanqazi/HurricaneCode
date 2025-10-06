package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp(name = "Flywheel PIDF 100Hz Test", group = "Test")
public class FlywheelTeleOp extends OpMode {

    public static double maxTargetTPS = 2000.0;
    public static String motorName = "flywheel_motor";

    private Flywheel flywheel;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        flywheel = new Flywheel(hardwareMap, motorName);
        flywheel.start();
    }

    @Override
    public void loop() {

        double target = -gamepad1.left_stick_y * maxTargetTPS;
        flywheel.setTargetTicksPerSec(target);


        telemetry.addData("target tps", flywheel.getTargetTicksPerSec());
        telemetry.addData("measured tps", flywheel.getMeasuredTicksPerSec());
        telemetry.addData("applied power", flywheel.getAppliedPower());
        telemetry.addData("dt (s)", flywheel.getLastDt());
        telemetry.update();
    }

    @Override
    public void stop() {
        if (flywheel != null) flywheel.stop();
    }
}
