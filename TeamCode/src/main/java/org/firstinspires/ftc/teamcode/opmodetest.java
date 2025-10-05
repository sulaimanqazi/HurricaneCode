package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class opmodetest extends OpMode {

    private DcMotorEx intakemotor1,intakemotor2;

    public void init(){
        intakemotor1 = hardwareMap.get(DcMotorEx.class,"frontLeft");
        intakemotor2 = hardwareMap.get(DcMotorEx.class,"frontRight");
    }
    public void loop(){



    }
}
