package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.RobotMap;

public class CloseIntake extends InstantCommand {
    protected void execute() {
        RobotMap.intakeOpen.set(false);
    	RobotMap.intakeClose.set(true);
    }
}