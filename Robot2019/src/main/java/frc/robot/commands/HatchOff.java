package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.RobotMap;

public class HatchOff extends InstantCommand {
    private boolean goingOut;

    public HatchOff(boolean goingOut){
        this.goingOut = goingOut; 
    }
    protected void execute() {
        RobotMap.intakeOpen.set(!goingOut);
        RobotMap.intakeClose.set(goingOut);
        
        RobotMap.secondSolenoidOpen.set(!goingOut);
        RobotMap.secondSolenoidClose.set(goingOut);
    }
}