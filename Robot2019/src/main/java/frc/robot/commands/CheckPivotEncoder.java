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
public class CheckPivotEncoder extends Command {
  public final static double ENCODER_RANGE = 4.972;
  public final static double CONVERSION_FACTOR = 360 / ENCODER_RANGE; 
  public static double init_volts = 0;
  public static double init_angle = 0;
  public CheckPivotEncoder() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.pivotEncoderSubsystem);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    //RobotMap.pivotEncoder.initAccumulator();
    //RobotMap.pivotEncoder.resetAccumulator();
    init_volts = RobotMap.pivotEncoder.getVoltage();
    init_angle = init_volts * CONVERSION_FACTOR;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    double volts = RobotMap.pivotEncoder.getVoltage() - init_volts;
    if (volts < 0){
      volts += ENCODER_RANGE;
    }
    double angle = volts * CONVERSION_FACTOR;
    SmartDashboard.putNumber("encoder voltage", volts);
    SmartDashboard.putNumber("angle of pivot", angle);
    SmartDashboard.putNumber("initial voltage", init_volts);
  }
 
  @Override
  protected boolean isFinished() {
    return false;
  }
}
