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
                new SubsystemComponent(Transfer.INSTANCE, TransferServo.INSTANCE, org.firstinspires.ftc.teamcode.guide.java.subsystems.Flywheel.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }


    @Override
    public void onStartButtonPressed() {

        // flywheel control - A button
        Gamepads.gamepad1().a()
                .whenBecomesTrue(Flywheel.INSTANCE.highPower)
                .whenBecomesFalse(org.firstinspires.ftc.teamcode.subsystems.Flywheel.INSTANCE.off);

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

