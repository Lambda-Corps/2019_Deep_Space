package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import frc.robot.oi.F310;

import edu.wpi.first.wpilibj.command.Command;

/** 
 * Changelog:
 * (Maddy Seputro)
 * 		Description: Where the user can pick whether to drive tank or arcade drive. Also where the axes for 
 * 		driving can be picked to match the driver's preferences. 
 * 			- Desired arguments: speed, distance
 * 		To do still:
 * 			- Fill in execute method and other methods if needed
 * 			- Add arcade drive and gamepad use
 * 2/11/2017 Added arcade driving capabilities. It is currently commented out but it works.
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
		// Pass the Gamepad axis values to the robot.  On the Y axis, full forward is -1, we want full forward to 
		// drive forward.  In this case, we want to negate the value before we give it to the drive train.  The 
		// DriveTrain should expect a positive trans input goes forward, and negative trans input goes backward.
		Robot.drivetrain.arcadeDrive(-Robot.oi.gamepad.getAxis(F310.LY), Robot.oi.gamepad.getAxis(F310.RX));
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
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run.
	@Override
	protected void interrupted() {
	}
}
