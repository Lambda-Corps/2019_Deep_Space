package frc.robot.commands.vision;

import frc.robot.Robot;
import frc.robot.oi.F310;

import edu.wpi.first.wpilibj.command.Command;
public class GetTargetCommand extends Command{
    public GetTargetCommand(){
        requires(Robot.drivetrain);
    }

	@Override
	protected void initialize() {
	
	}

    @Override
	protected void execute() {
        //calls vision code to constantly update the fx and fy value.
		Robot.vision.getTarget();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
	protected void end() {
    }
    
    // Called when another command which requires one or more of the same
	// subsystems is scheduled to run.
	@Override
	protected void interrupted() {
	}

}