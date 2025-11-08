package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.*;
@TeleOp(name = "NextFTC TeleOp Program Java")
public class MecanumTeleop2025 extends NextFTCOpMode {
    public MecanumTeleop2025() {
        addComponents(
                new SubsystemComponent(Transfer.INSTANCE, TransferServo.INSTANCE, Flywheel.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }


    @Override
    public void onStartButtonPressed() {
        Gamepads.gamepad1().a()
                .whenBecomesTrue(Flywheel.INSTANCE.highPower)
                .whenBecomesFalse(Flywheel.INSTANCE.off);

        // Transfer controls - B button
        Gamepads.gamepad1().b()
                .whenBecomesTrue(Transfer.INSTANCE.on)
                .whenBecomesFalse(Transfer.INSTANCE.off);

        // Transfer Servo controls - X button
        Gamepads.gamepad1().x()
                .whenBecomesTrue(TransferServo.INSTANCE.open)
                .whenBecomesFalse(TransferServo.INSTANCE.close);


    }
}


/*
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import
@TeleOp
public class MecanumTeleop2025 extends OpMode {

    private DcMotorEx frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    AprilTagAlignHelper aprilTagAlign;

    @Override
    public void init() {


         frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeft");
         backLeftMotor = hardwareMap.get(DcMotorEx.class,"backLeft");
         frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRight");
         backRightMotor = hardwareMap.get(DcMotorEx.class, "backRight");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        aprilTagAlign = new AprilTagAlignHelper(hardwareMap,telemetry);
        
    }
    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
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

        if (gamepad1.a) {
            aprilTagAlign.alignToAprilTag(true);
            // run apriltag code
        }
    }




}
*/
