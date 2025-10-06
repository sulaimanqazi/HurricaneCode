package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Intake")
public class Intake extends OpMode {

    DcMotor leftMotor, rightMotor;

    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotor.class, "intake_left");
        rightMotor = hardwareMap.get(DcMotor.class, "intake_right");
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }
}
