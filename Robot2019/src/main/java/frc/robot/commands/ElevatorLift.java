package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.LiftSubsystem;

public class ElevatorLift extends Command{
    public ElevatorLift(){
        requires(Robot.liftSubsystem);
    }

    @Override
    protected void execute() {
        double speed = 0;
        double leftTrigger = OI.getLeftTrigger();
        double rightTrigger = OI.getRightTrigger();
        if(leftTrigger > .15){
            speed = leftTrigger;
            //Robot.liftSubsystem.setSpeed(-leftTrigger);

        }
        if(rightTrigger > .15){
            speed = -rightTrigger;
            //Robot.liftSubsystem.setSpeed(rightTrigger);

        }

        Robot.liftSubsystem.setSpeedNoEncoder(speed);
        // RobotMap.secondLiftMotorController.set(0.95 * speed);
        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("motorcontroller?", RobotMap.liftMotorController.get());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}