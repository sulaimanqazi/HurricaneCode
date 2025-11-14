package org.firstinspires.ftc.teamcode.classes;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class shooterClass {
    public DcMotorEx flywheelMotor;

    public  Shooter(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");

    }
}

// claw class
