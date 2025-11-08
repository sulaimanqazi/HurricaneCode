//Subsystem


package org.firstinspires.ftc.teamcode.subsystems;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

public class Transfer implements Subsystem {
    public static final Transfer INSTANCE = new Transfer();
    private Transfer() { }

    private final MotorEx motor = new MotorEx("transferMotor");

    private final ControlSystem controller = ControlSystem.builder()
            .velPid(0.005, 0, 0)
            .basicFF(0.01, 0.02, 0.03)
            .build();

    public final Command off = new RunToVelocity(controller, 0.0).requires(this).named("TransferOff");
    public final Command on = new RunToVelocity(controller, 500.0).requires(this).named("TransferOn");

    //attempting a reverse command
    public final Command reverse = new RunToVelocity(controller, -500.0).requires(this).named("TransferReverse");

    @Override
    public void periodic() {
        motor.setPower(controller.calculate(motor.getState()));

        KineticState state = motor.getState();
        double currentVel = state.getVelocity();

        //Servo shoulddd automatically open after a certain velocity?
        if (currentVel >= 400) {
            // spin fast enough → open servo
            TransferServo.INSTANCE.open.schedule();
        } else {
            // slow → close servo
            TransferServo.INSTANCE.close.schedule();
        }
    }
}