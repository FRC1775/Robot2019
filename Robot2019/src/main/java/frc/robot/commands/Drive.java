/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.OI;

/**
 * Gets the values of the joysticks on the first driver's controller.
 * The left joystick controls our forward/backward movement, and the right joystick controls turning. 
 */
public class Drive extends Command {

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

    double yVal = OI.getLeftJoystick();
    double xVal = OI.getRightJoystick();

    Robot.motorSubsystem.drive(yVal, xVal);
     
  }
 
  @Override
  protected boolean isFinished() {
    return false;
  }
}
