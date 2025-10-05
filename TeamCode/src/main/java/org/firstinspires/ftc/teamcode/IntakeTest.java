package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp(name = "IntakeTest")
public class IntakeTest extends OpMode {

    private DcMotorEx intakemotor1,intakemotor2;

    @Override
    public void init(){
        intakemotor1 = hardwareMap.get(DcMotorEx.class,"frontLeft");
        intakemotor2 = hardwareMap.get(DcMotorEx.class,"frontRight");
    }

    @Override
    public void loop(){
        double y = -gamepad1.left_stick_y;

        intakemotor1.setPower(y);
        intakemotor2.setPower(y);

    }
}
