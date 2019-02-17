package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.RobotMap;

public class OpenIntake extends InstantCommand {
    protected void execute() {
        RobotMap.intakeClose.set(false);
    	RobotMap.intakeOpen.set(true);
    }
}