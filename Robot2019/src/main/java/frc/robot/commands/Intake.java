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
public class Intake extends Command {

  private double intakeSpeed;
  public Intake(double intakeSpeed) {
    // Use requires() here to declare subsystem dependencies
    this.intakeSpeed = intakeSpeed;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
     RobotMap.intakeMotor.set(intakeSpeed);
  }
 
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    super.end();
    RobotMap.intakeMotor.set(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
      super.interrupted();
      RobotMap.intakeMotor.set(0);
  }
}
