package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class HatchOffReal extends CommandGroup {
    private boolean goingOut;

    public HatchOffReal(boolean goingOut){
        this.goingOut = goingOut;

        addSequential(new HatchOff(goingOut, true));
        addSequential(new WaitCommand(1));
        addSequential(new HatchOff(goingOut, false));
    }
}
