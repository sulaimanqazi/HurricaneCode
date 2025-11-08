package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;

/**
 * The flywheel subsystem implemented from
 * <a href="https://v1.nextftc.dev/guide/subsystems/flywheel">docs</a>.
 */
public class Flywheel implements Subsystem {
    public static final Flywheel INSTANCE = new Flywheel();
    private Flywheel() { }

    private final MotorEx motor = new MotorEx("flywheelMotor");

    private final ControlSystem controller = ControlSystem.builder()
            .velPid(0.005, 0, 0)
            .basicFF(0.01, 0.02, 0.03)
            .build();

    public final Command off = new RunToVelocity(controller, 0.0).requires(this).named("FlywheelOff");

    public final Command flywheel1500RPM = new RunToVelocity(controller, 500.0).requires(this).named("Flywheel1500");
    public final Command flywheel2000RPM = new RunToVelocity(controller, 2000).requires(this).named("Flywheel2000RPM");
    public final Command flywheel2500RPM = new RunToVelocity(controller, 2500).requires(this).named("Flywheel2500RPM");



    @Override
    public void periodic() {
        motor.setPower(controller.calculate(motor.getState()));
    }
}