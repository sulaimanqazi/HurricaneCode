//Subsystem

package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;


public class TransferServo implements Subsystem {
    public static final TransferServo INSTANCE = new TransferServo();
    private TransferServo() { }

    private ServoEx servo = new ServoEx("transfer_servo");

    public Command open = new SetPosition(servo, 0.9).requires(this);
    public Command close = new SetPosition(servo, 0.55).requires(this);
}