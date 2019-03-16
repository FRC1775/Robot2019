/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.commands.HatchOff;
import frc.robot.commands.HatchOffReal;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Intake;
import frc.robot.commands.Pivot;
import frc.robot.commands.ResetGyro;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

  private static final int A_BUTTON = 1;
  private static final int B_BUTTON = 2;
  private static final int X_BUTTON = 3;
  private static final int Y_BUTTON = 4;
  private static final int LEFT_BUMPER = 5;


  private static Joystick driverJoystick;
  private static Joystick operatorJoystick;
  private static JoystickButton pivotUp;
  private static JoystickButton pivotDown;
  private static JoystickButton intakeInButton;
  private static JoystickButton intakeOutButton;
  private static JoystickButton hatchOffButton; 
  private static JoystickButton resetGyroButton;


  private static final double PIVOT_SPEED_UP = 0.5;
  private static final double PIVOT_SPEED_DOWN = -0.2;
  private static final double INTAKE_SPEED = 0.8;

  public static void init() {

    driverJoystick = new Joystick(0);
    operatorJoystick = new Joystick(1);

    createPivotButtons(operatorJoystick);
    createPivotDownButtons(operatorJoystick);
    createIntakeIn(operatorJoystick);
    createIntakeOut(operatorJoystick);
    createHatchOff(operatorJoystick);
    resetGyro(operatorJoystick);
  }

  public static double getLeftJoystick(){
    return driverJoystick.getRawAxis(1);
  }

  public static double getRightJoystick(){
    return driverJoystick.getRawAxis(4);
  }

  public static double getLeftTrigger(){
    return driverJoystick.getRawAxis(2);
  }

  public static double getRightTrigger(){
    return driverJoystick.getRawAxis(3);
  }

  public static void createPivotButtons(Joystick stick){
    pivotUp = new JoystickButton(stick, A_BUTTON);
    pivotUp.whileHeld(new Pivot(PIVOT_SPEED_UP));
  }

  public static void createPivotDownButtons(Joystick stick){
    pivotDown = new JoystickButton(stick, B_BUTTON);
    pivotDown.whileHeld(new Pivot(PIVOT_SPEED_DOWN));
  }

  public static void createIntakeIn(Joystick stick){
    intakeInButton = new JoystickButton(stick, X_BUTTON);
    intakeInButton.whileHeld(new Intake(-INTAKE_SPEED));
  }

  public static void createIntakeOut(Joystick stick){
    intakeOutButton = new JoystickButton(stick, Y_BUTTON);
    intakeOutButton.whileHeld(new Intake(INTAKE_SPEED));
  }

  public static void createHatchOff(Joystick stick){
    hatchOffButton = new JoystickButton(stick, LEFT_BUMPER);
    hatchOffButton.whileHeld(new HatchOffReal(true));
    hatchOffButton.whenReleased(new HatchOffReal(false));
  }
  public static void resetGyro(Joystick stick){
    resetGyroButton = new JoystickButton(stick, 6);
    resetGyroButton.whenPressed(new ResetGyro());

  }
}
