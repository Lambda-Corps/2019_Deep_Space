// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.commands.arm;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

/**
 *
 */
public class ArmSetPosition extends Command {
    
    double currentPosition;
    int desiredPosition;

    
    public ArmSetPosition(int position) {
  
        requires(Robot.arm);
        desiredPosition = position;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
         currentPosition = Robot.arm.getRelativeEncoder();
         if(desiredPosition - currentPosition > 0){
            Robot.arm.setMotor(0.25);
         }
         else{
            Robot.arm.setMotor(-0.25);
         }
        SmartDashboard.putNumber("currentPosition", currentPosition);
    }


    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    if(Math.abs(desiredPosition - currentPosition) <= 2) {
            return true;
        }
        else{
            return false;
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.arm.setMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
