//Subsystem

package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;


public class TransferServo implements Subsystem {
    public static final TransferServo INSTANCE = new TransferServo();
    private TransferServo() { }

    private ServoEx servo = new ServoEx("transfer_servo");

    public Command open = new SetPosition(servo, 0.9).requires(this);
    public Command close = new SetPosition(servo, 0.55).requires(this);


    //INTAKING COMMAND - Here its just off
    public final Command intaking = new SetPosition(servo, 0.55).requires(this).named("intaking");

    //DRIVING COMMAND - Here its just off

    public final Command driving = new SetPosition(servo, 0.55).requires(this).named("driving");


    //SHOOTING COMMAND - here its just ON

    public final Command shooting1500 = new SetPosition(servo, 0.9).requires(this).named("shooting1500");
    public final Command shooting2000 = new SetPosition(servo, 0.9).requires(this).named("shooting2000"); // i added names to these, not sure if itll work as intended
    public final Command shooting2500 = new SetPosition(servo, 0.9).requires(this).named("shooting2500");


}