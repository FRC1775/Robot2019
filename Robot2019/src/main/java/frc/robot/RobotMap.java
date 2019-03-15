/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Encoder;

import com.kauailabs.navx.frc.AHRS;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  
  public static int rightMotorPWM = 0;
  public static int leftMotorPWM = 1;
  public static int pivotMotorPWM = 2;
  public static int intakeMotorPWM = 3;


  public static Talon leftDrive;
  public static Talon rightDrive;
  public static Talon pivotMotor;
  public static Talon intakeMotor;

  public static DifferentialDrive drive;
  public static AHRS gyro;
  public static Encoder driveEncoderLeft;
  public static Encoder driveEncoderRight; 

  public static void init(){

    leftDrive = new Talon(leftMotorPWM);
    rightDrive = new Talon(rightMotorPWM);

    pivotMotor = new Talon(pivotMotorPWM);
    intakeMotor = new Talon(intakeMotorPWM);

    drive = new DifferentialDrive(leftDrive, rightDrive);

    gyro = new AHRS(Port.kOnboard);

    // double distancePerPulse = ((6*Math.PI)/250.0);
		// double liftDistancePerPulse = (((1.375*Math.PI)/250.0) * 2);
		
		// driveEncoderLeft = new Encoder(2, 3, false, Encoder.EncodingType.k1X);
		// driveEncoderLeft.setDistancePerPulse(distancePerPulse);

		// driveEncoderRight = new Encoder(4, 5, false, Encoder.EncodingType.k1X);
		// driveEncoderRight.setDistancePerPulse(distancePerPulse);
  }

}
