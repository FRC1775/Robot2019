/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.OI;

/**
 * An example command.  You can replace me with your own command.
 */
public class Drive extends Command {
  private final static long RAMP_TIME = 400; 
  public Drive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.motorSubsystem);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    double yVal=OI.stick.getRawAxis(1);
    double xVal=OI.stick.getRawAxis(4);
    double moveValue = yVal; 
    double turnValue = xVal * moveValue;
    long startTime; 
 


    if(yVal<-.1||yVal>.1||xVal<-.1||xVal>.1){
      startTime = System.currentTimeMillis();
      long rampFactor = Math.min( 1, ((System.currentTimeMillis() - startTime) / RAMP_TIME));
      moveValue = moveValue * rampFactor;
        RobotMap.drive.arcadeDrive(moveValue, turnValue);
    } else {
      startTime = 0;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
