package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.RobotMap;

/**
 * An example command.  You can replace me with your own command.
 */
public class ResetGyro extends InstantCommand {
  
  public ResetGyro() {
    // Use requires() here to declare subsystem dependencies
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
        RobotMap.gyro.reset();
        RobotMap.gyro.zeroYaw();
  }

  // Make this return true when this Command no longer needs to run execute()

  // Called once after isFinished returns true
}
