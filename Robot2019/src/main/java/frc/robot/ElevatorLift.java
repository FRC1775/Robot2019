package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorLift extends Command{
    public ElevatorLift(){
        requires(Robot.liftSubsystem);
    }

    @Override
    protected void execute() {
        double turnValue = 0;
        double leftTrigger = OI.stick.getRawAxis(2);
        double rightTrigger = OI.stick.getRawAxis(3);
        if(leftTrigger > 0){
            turnValue = -leftTrigger;
        }
        if(rightTrigger > 0){
            turnValue = rightTrigger;
        }
        if(rightTrigger > 0 && leftTrigger > 0){
            //Might wanna change this part later to "turnValue = 0"
            turnValue = rightTrigger - leftTrigger;
        }
        RobotMap.liftMotor.set(turnValue);
        SmartDashboard.putNumber("turnValue", turnValue);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}