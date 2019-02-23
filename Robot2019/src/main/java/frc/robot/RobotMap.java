/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

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
  public static Solenoid intakeOpen;
  public static Solenoid intakeClose;
  public static Solenoid secondSolenoidOpen;
  public static Solenoid secondSolenoidClose;
  public static int leftMotor = 1;
  public static int rightMotor = 0;
  public static int liftMotor = 2;
  public static int secondLiftMotor = 3;
  public static Talon leftDrive;
  public static DifferentialDrive drive;
  public static Talon rightDrive;
  public static Talon liftMotorController;
  public static Encoder liftEncoder;
  public static DigitalInput liftBottomLimitSwitch;
  public static DigitalInput liftTopLimitSwitch;

  public static void init(){
    // compressor = new Compressor();
		// intakeOpen = new Solenoid(1);
    // intakeClose = new Solenoid(0);
    
    // secondSolenoidOpen = new Solenoid(3);
    // secondSolenoidClose = new Solenoid(2);
  
  leftDrive = new Talon(leftMotor);
  rightDrive = new Talon(rightMotor);
  drive = new DifferentialDrive(leftDrive, rightDrive);
  liftBottomLimitSwitch = new DigitalInput(0);
  liftTopLimitSwitch = new DigitalInput(1);
  

  liftMotorController = new Talon(liftMotor);
  // public static Talon secondLiftMotorController = new Talon(secondLiftMotor);
 

  }
}
