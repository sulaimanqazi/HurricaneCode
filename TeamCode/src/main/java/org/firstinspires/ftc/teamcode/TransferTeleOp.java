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
import org.firstinspires.ftc.teamcode.Transfer;
import org.firstinspires.ftc.teamcode.TransferServo;

@TeleOp(name = "NextFTC TeleOp Program Java")
public class TransferTeleOp extends NextFTCOpMode {
    public TransferTeleOp() {
        addComponents(
                new SubsystemComponent(Transfer.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }


    @Override
    public void onStartButtonPressed() {
        Gamepads.gamepad1().a()
                .whenBecomesTrue(Transfer.INSTANCE.on)
                .whenBecomesFalse(Transfer.INSTANCE.off);

        Gamepads.gamepad1().b()
                .whenBecomesTrue(Transfer.INSTANCE.reverse)
                .whenBecomesFalse(Transfer.INSTANCE.off);
/*controllable button for servo?
        Gamepads.gamepad1().x()
                .whenBecomesTrue(TransferServo.INSTANCE.open)
                .whenBecomesFalse(TransferServo.INSTANCE.close);
    }
 */


    }
}