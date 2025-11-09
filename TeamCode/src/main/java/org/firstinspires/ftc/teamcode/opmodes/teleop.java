
package org.firstinspires.ftc.teamcode.opmodes;

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

public class teleop extends NextFTCOpMode{
    public teleop() {
        addComponents(
                new SubsystemComponent(Transfer.INSTANCE, TransferServo.INSTANCE, org.firstinspires.ftc.teamcode.subsystems.Flywheel.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    private final MotorEx frontLeftmotor = new MotorEx("frontLeft").reversed();
    private final MotorEx frontRightMotor = new MotorEx("frontLeft").reversed();
    private final MotorEx backLeftMotor = new MotorEx("frontLeft").reversed();
    private final MotorEx backRightMotor = new MotorEx("frontLeft").reversed();


    @Override
    public void onStartButtonPressed() {

        // mecanum drivetrain control
        Command driverControlled = new MecanumDriverControlled(
                frontLeftmotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );

        driverControlled.schedule();

        // flywheel control - A button
        Gamepads.gamepad1().a()
                .whenBecomesTrue(Flywheel.INSTANCE.highPower)
                .whenBecomesFalse(org.firstinspires.ftc.teamcode.subsystems.Flywheel.INSTANCE.off);

        // Transfer controls - B button
        Gamepads.gamepad1().b()
                .whenBecomesTrue(Transfer.INSTANCE.on)
                .whenBecomesTrue(Transfer.INSTANCE.on)

                .whenBecomesFalse(Transfer.INSTANCE.off)
                .whenBecomesFalse(Transfer.INSTANCE.off);




        // Transfer Servo controls - X button
        Gamepads.gamepad1().x()
                .whenBecomesTrue(TransferServo.INSTANCE.open)
                .whenBecomesFalse(TransferServo.INSTANCE.close);


    }




}
