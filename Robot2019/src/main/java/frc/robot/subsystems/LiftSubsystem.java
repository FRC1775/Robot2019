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
import frc.robot.commands.ElevatorLift;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class LiftSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
 	 public static final double UP_MIN_SPEED = 0.55;
	public static final double DOWN_MIN_SPEED = 0.25;
	//public static final double DOWN_MAX_SPEED = 0.4;
	//public static final double UP_MAX_SPEED = 0.75;
	public static final double DOWN_MAX_SPEED = 0.5;
	public static final double UP_MAX_SPEED = 0.8;
	
	private static final double MIN_HEIGHT_START_RAMP = 30.0;
	private static final double MAX_HEIGHT_START_RAMP = 70.0;
  	private static final double MAX_HEIGHT = 84.0;
 	private static final double MIN_HEIGHT = 0;
	
	private static final double TARGET_TOLERANCE = 2;
	private static final double ON_TARGET_MIN_TIME = 500;
	
	private static final double START_RAMP_TIME_MS = 500.0;
  
 	private double startTime = System.currentTimeMillis();
	private boolean hasSeenBottomLimitSwitch;
	 
	private static final double BAD_HEIGHT_MAX = -2;
	private static final double BAD_HEIGHT_MIN = -1;


  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ElevatorLift());
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  
  public void setSpeedNoEncoder(double speed){
	double noEncoderSpeed = speed;

	if (!RobotMap.liftBottomLimitSwitch.get() && speed < 0) {
		noEncoderSpeed = 0;
	} 
	if (RobotMap.liftEncoder.getDistance() >= BAD_HEIGHT_MIN && RobotMap.liftEncoder.getDistance()
	<= BAD_HEIGHT_MAX && (Robot.pivotArmSubsystem.getAngle() <= 10 || Robot.pivotArmSubsystem.getAngle() >= 350)){
		noEncoderSpeed = 0;
	}
	RobotMap.liftMotorController.set(noEncoderSpeed);
	SmartDashboard.putNumber("encoder height", RobotMap.liftEncoder.getDistance());
	SmartDashboard.putBoolean("Top Endddcode", RobotMap.liftTopLimitSwitch.get());
	SmartDashboard.putBoolean("Bottom Endddcode", RobotMap.liftBottomLimitSwitch.get() );

  }

  public void setSpeed(double speed) {
		double outputSpeed = 0;

		if (isAllowedToGoUp(speed) || isAllowedToGoDown(speed)) {
			outputSpeed = getAdjustedSpeed(speed);
		} else {
			outputSpeed = 0;
			startTime = System.currentTimeMillis();
		}
		
		if (Robot.liftSubsystem.checkBottomLimitSwitch()) {
			RobotMap.liftEncoder.reset();
		}
    
		SmartDashboard.putNumber("lift encoder", RobotMap.liftEncoder.getDistance());
		SmartDashboard.putBoolean("is seeing bottom limit switch", !RobotMap.liftBottomLimitSwitch.get());
		RobotMap.liftMotorController.set(outputSpeed); // Must call set, not setSpeed, to take into account the setInverted on the controller
  }
  
  private boolean isAllowedToGoUp(double inputLiftSpeed) {
		return inputLiftSpeed >= UP_MIN_SPEED && RobotMap.liftEncoder.getDistance() <= MAX_HEIGHT;
	}
	
	private boolean isAllowedToGoDown(double inputLiftSpeed) {
		if(hasSeenBottomLimitSwitch) {
			return inputLiftSpeed <= -DOWN_MIN_SPEED && RobotMap.liftEncoder.getDistance() > MIN_HEIGHT;
		}
		return inputLiftSpeed <= -DOWN_MIN_SPEED;
  }
  
  private double getAdjustedSpeed(double inputLiftSpeed) {
		double ramp = Math.min((System.currentTimeMillis() - startTime) / START_RAMP_TIME_MS, 1);
		
		if (inputLiftSpeed <= -DOWN_MIN_SPEED) { // Going down
			if (RobotMap.liftEncoder.getDistance() < MIN_HEIGHT_START_RAMP) {
				ramp = Math.max(RobotMap.liftEncoder.getDistance() / MIN_HEIGHT_START_RAMP, 0);
			}
			return -DOWN_MIN_SPEED + ramp * (Math.max(inputLiftSpeed, -DOWN_MAX_SPEED) + DOWN_MIN_SPEED);
		} else if (inputLiftSpeed >= UP_MIN_SPEED) {
			if (RobotMap.liftEncoder.getDistance() > MAX_HEIGHT_START_RAMP) {
				ramp = Math.max(1 - (RobotMap.liftEncoder.getDistance() - MAX_HEIGHT_START_RAMP) / (MAX_HEIGHT - MAX_HEIGHT_START_RAMP), 0);
			}
			return UP_MIN_SPEED + ramp * (Math.min(inputLiftSpeed, UP_MAX_SPEED) - UP_MIN_SPEED);
		}
		return 0;
  }
  
  public boolean checkBottomLimitSwitch() {
		boolean bottomLimitSwitchHit = !RobotMap.liftBottomLimitSwitch.get();
		
		if(bottomLimitSwitchHit) {
			hasSeenBottomLimitSwitch = true;
		}
		
		return bottomLimitSwitchHit;
	}
	
	public boolean checkTopLimitSwitch() {
		return !RobotMap.liftTopLimitSwitch.get();
	}
}
