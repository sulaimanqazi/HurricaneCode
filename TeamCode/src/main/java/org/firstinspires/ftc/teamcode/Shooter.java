package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
@Config
public class Shooter extends LinearOpMode {

    public static double Kp = 0.01;
    public static double Ki = 0.0;
    public static double Kd = 0.001;
    public static double Kf = 0.00045;

    public static double TPS = 2500;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setDirection(DcMotor.Direction.FORWARD);

        boolean spinning = false;

        double integralSum = 0;
        double lastError = 0;
        double out;

        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                spinning = !spinning;
                sleep(300);
            }

            if (spinning) {
                double velocity = shooterMotor.getVelocity();
                double error = TPS - velocity;
                double derivative = (error - lastError) / timer.seconds();
                integralSum += error * timer.seconds();

                out = (Kp * error) + (Ki * integralSum) + (Kd * derivative) + (Kf * TPS);
                out = Math.max(-1.0, Math.min(out, 1.0));

                shooterMotor.setPower(out);

                lastError = error;
                timer.reset();

            }

            else {
                shooterMotor.setPower(0);
                integralSum = 0;
                lastError = 0;
                timer.reset();
            }

            telemetry.addData("Shooter", spinning);
            telemetry.addData("Target TPS", TPS);
            telemetry.addData("Velocity", shooterMotor.getVelocity());
            telemetry.addData("Kp", Kp);
            telemetry.addData("Ki", Ki);
            telemetry.addData("Kd", Kd);
            telemetry.addData("Kf", Kf);
            telemetry.update();

            sleep(10);
        }
    }
}
