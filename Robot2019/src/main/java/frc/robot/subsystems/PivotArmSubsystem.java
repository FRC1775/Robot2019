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
import frc.robot.commands.CheckPivotEncoder;
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
  public void setSpeed(double speed){
        double distanceFromCenter;
        double adjustedSpeed = speed;
    if(speed < 0 && conversion() < 45){
        distanceFromCenter = 45 - conversion();
        adjustedSpeed = speed/distanceFromCenter;
    } 
    if (speed > 0 && conversion() > 45){
        distanceFromCenter = conversion() - 45;
        adjustedSpeed = speed/distanceFromCenter;
    }
    RobotMap.pivotMotor.set(adjustedSpeed);
  }

    public double conversion(){
        double volts = RobotMap.pivotEncoder.getVoltage() - Robot.initVolts();
        if (volts < 0){
            volts += ENCODER_RANGE;
        }
        double angle = volts * CONVERSION_FACTOR;

        SmartDashboard.putNumber("encoder voltage", volts);
        SmartDashboard.putNumber("angle of pivot", angle);
        SmartDashboard.putNumber("initial voltage", Robot.initVolts());   
        return angle; 
    }
}
