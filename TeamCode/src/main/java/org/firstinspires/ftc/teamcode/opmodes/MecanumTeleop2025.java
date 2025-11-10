package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.classes.AprilTagAlignHelper;
import org.firstinspires.ftc.teamcode.classes.Flywheel;
import org.firstinspires.ftc.teamcode.classes.transferGate;
@TeleOp
public class MecanumTeleop2025 extends OpMode {


    private DcMotorEx frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;


    AprilTagAlignHelper aprilTagAlign;
    Flywheel FlywheelPID;


    transferGate Gate;


    boolean toggle = false;
    boolean GateState = false;






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


        // create objects
        aprilTagAlign = new AprilTagAlignHelper(hardwareMap,telemetry);
        FlywheelPID = new Flywheel(hardwareMap, telemetry);
        Gate = new transferGate(hardwareMap, telemetry);


    }
    @Override


    //boolean currentPress = gamepad1.B;
    public void loop() {








        // if gamepad1.a is held returns true, if not it returns false
        aprilTagAlign.alignToAprilTag(gamepad1.a);


        // hold x to activate flywheel
        FlywheelPID.update(gamepad1.x);


        // click B to open gate, then click B to close gate.








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




    }


}

