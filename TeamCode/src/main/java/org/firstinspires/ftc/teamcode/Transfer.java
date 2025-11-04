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
public class Transfer extends LinearOpMode {

    public static double Kp = 0.5;
    public static double Ki = 0.0;
    public static double Kd = 0.1;

    public static double TPS = 800;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        DcMotorEx transferMotor = hardwareMap.get(DcMotorEx.class, "transfer");
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transferMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        transferMotor.setDirection(DcMotor.Direction.REVERSE);

        boolean spinning = false;

        double integralSum = 0;
        double lastError = 0;
        double out;

        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.b) {
                spinning = !spinning;
                sleep(300);
            }

            if (spinning) {

                double encoderVelocity = transferMotor.getVelocity();
                double error = TPS - encoderVelocity;
                double derivative = (error - lastError) / timer.seconds();
                integralSum += error * timer.seconds();

                out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);
                out = Math.max(-1.0, Math.min(out, 1.0));

                transferMotor.setPower(out);

                lastError = error;
                timer.reset();

            }

            else {
                transferMotor.setPower(0);
                integralSum = 0;
                lastError = 0;
                timer.reset();
            }

            telemetry.addData("Spinning", spinning);
            telemetry.addData("TPS (ticks/sec)", TPS);
            telemetry.addData("Velocity (ticks/sec)", transferMotor.getVelocity());
            telemetry.addData("Kp", Kp);
            telemetry.addData("Ki", Ki);
            telemetry.addData("Kd", Kd);
            telemetry.update();

            sleep(10);
        }
    }
}
