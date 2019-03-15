package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.RobotMap;

public class HatchIn extends InstantCommand {
    protected void execute() {
        RobotMap.intakeClose.set(false);
        RobotMap.intakeOpen.set(true);
        
        RobotMap.secondSolenoidClose.set(false);
        RobotMap.secondSolenoidOpen.set(true);
    }
}