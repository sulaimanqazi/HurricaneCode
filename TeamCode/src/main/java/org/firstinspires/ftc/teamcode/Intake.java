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
public class Intake extends LinearOpMode {

    public static double Kp = 0.5;
    public static double Ki = 0.0;
    public static double Kd = 0.1;
    public static double TPS = 1000;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);

        boolean spinning = false;

        double integralSum = 0;
        double lastError = 0;
        double out;

        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x) {
                spinning = !spinning;
                sleep(300);
            }

            if (spinning) {

                double encoderVelocity = intakeMotor.getVelocity();

                double error = TPS - encoderVelocity;

                double dt = timer.seconds();
                if (dt <= 1e-6) dt = 1e-3;

                double derivative = (error - lastError) / dt;

                integralSum += error * dt;
                double integralMax = 1000.0;
                if (integralSum > integralMax) integralSum = integralMax;
                if (integralSum < -integralMax) integralSum = -integralMax;

                out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

                out = Math.max(-1.0, Math.min(out, 1.0));

                intakeMotor.setPower(out);

                lastError = error;
                timer.reset();

            }

            else {
                intakeMotor.setPower(0);
                integralSum = 0;
                lastError = 0;
                timer.reset();
            }

            telemetry.addData("Spinning", spinning);
            telemetry.addData("TPS (ticks/sec)", TPS);
            telemetry.addData("Velocity (ticks/sec)", intakeMotor.getVelocity());
            telemetry.addData("Kp", Kp);
            telemetry.addData("Ki", Ki);
            telemetry.addData("Kd", Kd);
            telemetry.update();

            sleep(10);
        }
    }
}
