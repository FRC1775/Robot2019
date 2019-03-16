package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class HatchOffReal extends CommandGroup {
    private boolean goingOut;

    public HatchOffReal(boolean goingOut){
        this.goingOut = goingOut;

        addParallel(new HatchOff(goingOut, true));
        // addSequential(new WaitCommand(0.1));
        addParallel(new HatchOff(goingOut, false));
    }
}
