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
public class DoNothing extends Command {
  public double angle;

  public final static double ENCODER_RANGE = 4.972;
  public final static double CONVERSION_FACTOR = 360 / ENCODER_RANGE; 
  public final static double initVoltage = RobotMap.pivotEncoder.getVoltage();


  public DoNothing() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.pivotArmSubsystem);
  }


  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
  }
  
  
 
  @Override
  protected boolean isFinished() {
    return false;
  }
}
