package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.autonomous.ExampleAutonomous;
import frc.robot.commands.rumble.RumbleCommand;
import frc.robot.commands.testcommands.ExampleTestCommand;
import frc.robot.oi.F310;
import frc.robot.oi.OI;
import frc.robot.subsystems.Drivetrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 * List of all sensors used:
 * 
 * List of what's displayed to SmartDashboard:
 * 
 * 
 */
public class Robot extends TimedRobot {

	// Define subsystems.
	public static Drivetrain drivetrain;
	public static InternalButton retryButton;
	public static OI oi;

	Command autonomousCommand;

	SendableChooser<Command> chooser = new SendableChooser<>();

	// public static NetworkTable testTabTable; //???

	@Override
	public void robotInit() {
		// Build the subsystems first before the OI. Otherwise, if the subsystems
		// are instantiated after the OI then it's likely the default command for
		// the subsystem will not properly be scheduled and run in the way our
		// command based robot should run.
		drivetrain = new Drivetrain();
		// ALWAYS INSTANTIATE THE OI LAST because it most likely uses commands depending on
		// other subsystems
		oi = new OI();

	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		Robot.drivetrain.resetAHRSGyro();

		// Set your autonomous command here.
		autonomousCommand = new ExampleAutonomous();

		// This line actually tells the autonomous to start!
		autonomousCommand.start();

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

		Scheduler.getInstance().removeAll();

		if (autonomousCommand != null)
			autonomousCommand.cancel();

		// Start teleop in LOW gear
		Robot.drivetrain.changeToLowGear();

		Command rumbleCommand = new RumbleCommand();
		rumbleCommand.start();

		// You can add test commands here to run from Shuffleboard
		SmartDashboard.putData("Example Test Command", new ExampleTestCommand());

		// You can add values you might want to change using Shuffleboard in testing here
		SmartDashboard.putNumber("Value", 0);

	}

	@Override
	public void testInit() {

		if (autonomousCommand != null)
			autonomousCommand.cancel();

	}

	/*
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		Scheduler.getInstance().run();

		// You can add values you might want to view in Shuffleboard here
		// (or, a lot of them you can find on the left menu on Shuffleboard)
		SmartDashboard.putNumber("Gyro value", Robot.drivetrain.getAHRSGyroAngle());

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();

	}
}
