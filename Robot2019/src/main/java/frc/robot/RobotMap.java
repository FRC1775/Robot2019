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

  public static void init(){
    compressor = new Compressor();
		intakeOpen = new Solenoid(1);
    intakeClose = new Solenoid(0);
    
    secondSolenoidOpen = new Solenoid(3);
    secondSolenoidClose = new Solenoid(2);
  }
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
  public static int liftMotor = 2;
  public static int secondLiftMotor = 3;
  public static Talon leftDrive = new Talon(leftMotor);
  public static Talon rightDrive = new Talon(rightMotor);
  public static DifferentialDrive drive = new DifferentialDrive(leftDrive, rightDrive);

  public static Talon liftMotorController = new Talon(liftMotor);
  // public static Talon secondLiftMotorController = new Talon(secondLiftMotor);
  public static Encoder liftEncoder;
  public static DigitalInput liftBottomLimitSwitch;
  public static DigitalInput liftTopLimitSwitch;

  }
