package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.RobotMap;

public class HatchOff extends InstantCommand {
    private boolean goingOut;
    private boolean first;

    public HatchOff(boolean goingOut, boolean first){
        this.goingOut = goingOut; 
        this.first = first;
    }

    protected void execute() {

        if(first){    
            RobotMap.hatchPistonLeftOut.set(goingOut);
            RobotMap.hatchPistonLeftIn.set(!goingOut);
        }else{
            RobotMap.hatchPistonRightOut.set(goingOut);
            RobotMap.hatchPistonRightIn.set(!goingOut);
        }
    }
}
