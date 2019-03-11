/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;
  public static int leftMotor = 1;
  public static int rightMotor = 0;
  public static Talon leftDrive = new Talon(leftMotor);
  public static Talon rightDrive = new Talon(rightMotor);
  public static DifferentialDrive drive = new DifferentialDrive(leftDrive, rightDrive);
  public static AHRS gyro = new AHRS(SPI.Port.i2c);
  public static Encoder driveEncoderLeft;
  public static Encoder driveEncoderRight; 

  public static void init(){
    double distancePerPulse = ((6*Math.PI)/250.0);
		double liftDistancePerPulse = (((1.375*Math.PI)/250.0) * 2);
		
		driveEncoderLeft = new Encoder(2, 3, false, Encoder.EncodingType.k1X);
		driveEncoderLeft.setDistancePerPulse(distancePerPulse);

		driveEncoderRight = new Encoder(4, 5, false, Encoder.EncodingType.k1X);
		driveEncoderRight.setDistancePerPulse(distancePerPulse);
  }

}
