package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp(name = "IntakeTest")
public class IntakeTest extends OpMode {

    private DcMotorEx intakeMotor1;
    private DcMotorEx intakeMotor2;


    @Override
    public void init(){

        intakeMotor1 = hardwareMap.get(DcMotorEx.class,"frontLeft");
        intakeMotor2 = hardwareMap.get(DcMotorEx.class,"frontRight");
    }

    @Override
    public void loop(){
        double y = -gamepad1.left_stick_y;

        intakeMotor1.setPower(y);
        intakeMotor2.setPower(y);

    }
}
