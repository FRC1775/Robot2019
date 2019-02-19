package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class ElevatorLift extends Command{
    public ElevatorLift(){
        requires(Robot.liftSubsystem);
    }

    @Override
    protected void execute() {
        double speed = 0;
        double leftTrigger = OI.stick.getRawAxis(2);
        double rightTrigger = OI.stick.getRawAxis(3);
        if(leftTrigger > .15){
            speed = -leftTrigger;
            //Robot.liftSubsystem.setSpeed(-leftTrigger);

        }
        if(rightTrigger > .15){
            speed = rightTrigger;
            //Robot.liftSubsystem.setSpeed(rightTrigger);

        }

        RobotMap.liftMotor.set(0.65 * speed);
        SmartDashboard.putNumber("speed", speed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}