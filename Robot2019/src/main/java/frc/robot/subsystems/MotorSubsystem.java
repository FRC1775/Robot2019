/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.commands.Drive;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class MotorSubsystem extends Subsystem implements PIDSource {
  private PIDController rotateToAnglePidController; 
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private final static double RAMP_TIME = 400;
  long startTime = 0;
  double rampFactor;  
 
  public void MotorSubsystem(){
    
		rotateToAnglePidController = new PIDController(0.15, 0, 0.45,(PIDSource) RobotMap.gyro,
    (value) ->  {
      if (rotateToAnglePidController.isEnabled()) {
        RobotMap.drive.arcadeDrive(0, value);
      }
    }, 0.02);

  rotateToAnglePidController.setInputRange(-180, 180);
  rotateToAnglePidController.setOutputRange(-0.75, 0.75);
  rotateToAnglePidController.setAbsoluteTolerance(2);
  rotateToAnglePidController.setContinuous();
  }
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

  public double getRotateAngle () {
		return rotateToAnglePidController.getSetpoint();
	}
  public void setRotateAngle(double angle){

		RobotMap.gyro.reset();
		RobotMap.gyro.zeroYaw();
		
		rotateToAnglePidController.setSetpoint(angle);
		rotateToAnglePidController.enable();
  }
 
  @Override
	public void initSendable(SendableBuilder builder) {
		builder.addDoubleProperty("navx/angle", () -> { return RobotMap.gyro.getAngle(); }, null);
		builder.addBooleanProperty("resetGyro", () -> { return false; }, (value) -> {
			if (value) {
				rotateToAnglePidController.reset();
				RobotMap.gyro.reset();
				RobotMap.gyro.zeroYaw();
			}
    });
  }

  public double getDistance() {
		SmartDashboard.putNumber("LeftEncoder", RobotMap.driveEncoderLeft.getDistance());
		SmartDashboard.putNumber("RightEncoder", -RobotMap.driveEncoderRight.getDistance());
		return ((RobotMap.driveEncoderLeft.getDistance() - RobotMap.driveEncoderRight.getDistance()) / 2.0);
  }
  
  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {

  }

	@Override
	public PIDSourceType getPIDSourceType() {
		// This is for the drive train encoders
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		// This is for the drive train encoders
		return getDistance();
	} 
}
