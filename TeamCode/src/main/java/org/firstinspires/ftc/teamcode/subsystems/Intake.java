package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

//Subsystem

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    private final MotorEx motor = new MotorEx("intakeMotor").brakeMode(); //brake mode sets 0 values to brake
    private final ControlSystem controller = ControlSystem.builder()
            .velPid(0.005, 0, 0)
            .basicFF(0.01, 0.02, 0.03)
            .build();

    public final Command off = new RunToVelocity(controller, 0.0).requires(this).named("IntakeOff");
    public final Command on = new RunToVelocity(controller, 500.0).requires(this).named("IntakeOn");

    //attempting a reverse command
    public final Command reverse = new RunToVelocity(controller, -500.0).requires(this).named("IntakeReverse");

    //INTAKING COMMAND - Here its the same as simply turning on, its spinning in one direction (not reverse direction)
    public final Command intaking = new RunToVelocity(controller, 500.0).requires(this).named("IntakeIntaking");

    //DRIVING COMMAND - 0 value automatically means brake mode
    public final Command driving = new RunToVelocity(controller, 0.0).requires(this).named("IntakeDriving");


    //SHOOTING COMMAND - - Here its the same as simply turning on, its spinning in one direction (not reverse direction)
    public final Command shooting = new RunToVelocity(controller, 500.0).requires(this).named("IntakeShooting");


    @Override
    public void periodic() {
        motor.setPower(controller.calculate(motor.getState()));
        
    }
}