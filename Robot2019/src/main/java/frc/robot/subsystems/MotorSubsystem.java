/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.commands.Drive;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class MotorSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private final static double RAMP_TIME = 400;
  long startTime = 0;
  double rampFactor;  
 
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new Drive());
  }
  
  public void drive(double yVal , double xVal){
    double moveValue = -yVal; 
    double turnValue = xVal;
    // long rampFactor; 

    // we're trying to move
    if(yVal < -0.1 || yVal > 0.1){
      turnValue = moveValue * xVal;
      System.out.println("we're trying to move");
    }else{
    
      // we aren't trying to move or turn
      if(xVal < 0.1 && xVal > -0.1 ){
        startTime = System.currentTimeMillis();
        rampFactor = 0;
        System.out.println("we aren't trying to move or turn");

      }
      
      // we're trying to turn but not move
      System.out.println("we're trying to turn but not move");

      rampFactor = Math.min(1, (System.currentTimeMillis() - startTime) / RAMP_TIME);
      turnValue = rampFactor * xVal;
      moveValue = 0;
      SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
      SmartDashboard.putNumber("Start Time", startTime);
      SmartDashboard.putNumber("Ramp Factor", (double) rampFactor);
    }
    // runs regardless of whether we want to move or turn
    RobotMap.drive.arcadeDrive(moveValue, turnValue);
  }  
}
