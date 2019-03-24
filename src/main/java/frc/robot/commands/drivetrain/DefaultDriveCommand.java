package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.oi.F310;

/**
 * Changelog: (Maddy Seputro) Description: Where the user can pick whether to
 * drive tank or arcade drive. Also where the axes for driving can be picked to
 * match the driver's preferences. - Desired arguments: speed, distance To do
 * still: - Fill in execute method and other methods if needed - Add arcade
 * drive and gamepad use 2/11/2017 Added arcade driving capabilities. It is
 * currently commented out but it works.
 */

public class DefaultDriveCommand extends Command {
	public DefaultDriveCommand() {
		// Use requires() here to declare subsystem dependencies.
		// Since this Command only needs to access motors from the drivetrain,
		// we call requires to block other commands from using the same
		// subsystem, and trying to set motors to different values.
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {
	//	Robot.drivetrain.setBrake(true);
	}

	@Override
	protected void execute() {
		// Get the value of the LY axis on the driver controller, invert it, Get the value of the RX axis on the 
		// driver controller.
		// Pass both of those values to the arcade drive method of the drive train.
		// LY axis is the forward back movement, RX represents the turning
		// Values are normalized here because we optionally choose arcade or curve drive based on the non-zero
		// y-axis, we don't want Deadband to affect that so normalize first.
		// double yAxis = Robot.drivetrain.normalize(-Robot.oi.driverRemote.getAxis(F310.LY));
		// double xAxis = Robot.drivetrain.normalize(Robot.oi.driverRemote.getAxis(F310.RX));
		double yAxis = Robot.driver_LY;
		double xAxis = Robot.driver_RX;

		if( yAxis == 0.0 && xAxis != 0.0 ){
			// This is the pivot condition
			Robot.drivetrain.arcadeDrive(0, xAxis, false);
		}
		else {
			if(Robot.oi.driverRemote.getRawAxis(F310.RT)>0.2){
				yAxis *=-1;
				// xAxis *=-1;
			}
			Robot.drivetrain.curvatureDrive(yAxis , xAxis, false, true);
		}

		boolean inHigh = Robot.drivetrain.inHigh();
		// if(inHigh==true){
		// 	SmartDashboard.putBoolean("kForward", true);
		// } else {
		// 	SmartDashboard.putBoolean("kForward", false);
    	// }
		
	}

	// Make this return true when this Command no longer needs to run execute()
	// Since this is a default command, this Command (usually) never needs to end
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		// Call the tankDrive method() to set all the motors to zero. If the robot is not meant to
		// be moving, most likely the program will stop the motors for us, but this should help prevent
		// any cases of something weird happening.
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run.
	@Override
	protected void interrupted() {
	}
}
