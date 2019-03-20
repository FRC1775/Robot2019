/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.DoNothing;
import frc.robot.commands.Pivot;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class PivotArmSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public final static double ENCODER_RANGE = 4.972;
  public final static double CONVERSION_FACTOR = 360 / ENCODER_RANGE; 
  @Override
  public void initDefaultCommand() {
      setDefaultCommand(new DoNothing());
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public double getAngle(){
    double volts = RobotMap.pivotEncoder.getVoltage() - DoNothing.initVoltage;
    if (volts < 0){
        volts += ENCODER_RANGE;
    }
    double angle = volts * CONVERSION_FACTOR;

    SmartDashboard.putNumber("encoder voltage", volts);
    SmartDashboard.putNumber("angle of pivot", angle);
    SmartDashboard.putNumber("initial voltage", DoNothing.initVoltage);
    return angle;
  }
     
  public void setSpeed(double speed){
    /*
    if (getAngle() <= 270 && getAngle() >= 180 && speed<0 ){
        speed = 0;
    }
    if ((getAngle() <= 10 || getAngle() >= 350) && speed>0){
        speed = 0; 
    }*/
    if(!RobotMap.liftTopLimitSwitch.get() || !RobotMap.liftBottomLimitSwitch.get()){
        speed = 0;
    }
    RobotMap.pivotMotor.set(speed);
  }

}
