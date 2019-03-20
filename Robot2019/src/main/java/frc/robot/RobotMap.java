/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.Encoder;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  public static Compressor compressor;
  public static Solenoid hatchPistonLeftOut;
  public static Solenoid hatchPistonLeftIn;
  public static Solenoid hatchPistonRightOut;
  public static Solenoid hatchPistonRightIn;
  public static int rightMotorPWM = 0;
  public static int leftMotorPWM = 1;
  public static int pivotMotorPWM = 2;
  public static int intakeMotorPWM = 4;
  public static int liftMotor = 3;
 
  public static int pivotEncoderChannel = 0;

  public static Talon leftDrive;
  public static Talon rightDrive;
  public static Talon pivotMotor;
  public static Talon intakeMotor;
  public static Talon liftMotorController;
  public static Encoder liftEncoder;
  public static DigitalInput liftBottomLimitSwitch;
  public static DigitalInput liftTopLimitSwitch;
  public static DifferentialDrive drive;
  public static AHRS gyro;
  public static Encoder driveEncoderLeft;
  public static Encoder driveEncoderRight; 
  public static AnalogInput pivotEncoder;

  public static void init(){

    compressor = new Compressor();
		hatchPistonLeftOut = new Solenoid(2);
    hatchPistonLeftIn = new Solenoid(3);
    
    hatchPistonRightOut = new Solenoid(0);
    hatchPistonRightIn = new Solenoid(1);
    leftDrive = new Talon(leftMotorPWM);
    rightDrive = new Talon(rightMotorPWM);

    pivotMotor = new Talon(pivotMotorPWM);
    intakeMotor = new Talon(intakeMotorPWM);
    liftMotorController = new Talon(liftMotor);

    drive = new DifferentialDrive(leftDrive, rightDrive);

    gyro = new AHRS(Port.kOnboard);
    gyro.reset();

    double distancePerPulse = ((2*Math.PI)/1000);
    // PPR = pulses per revolution
		// double liftDistancePerPulse = (((1.375*Math.PI)/250.0) * 2);
		
		liftEncoder = new Encoder(6, 7, false, Encoder.EncodingType.k4X);
    liftEncoder.setDistancePerPulse(distancePerPulse);

		driveEncoderRight = new Encoder(4, 5, false, Encoder.EncodingType.k1X);
    driveEncoderRight.setDistancePerPulse(distancePerPulse);


    pivotEncoder = new AnalogInput(pivotEncoderChannel);
    
    liftBottomLimitSwitch = new DigitalInput(1);
    liftTopLimitSwitch = new DigitalInput(0);
 
  }
}
