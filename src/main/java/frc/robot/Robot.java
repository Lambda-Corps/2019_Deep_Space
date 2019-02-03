package frc.robot;

import frc.robot.commands.autonomous.Lvl1RtoCB1;
import frc.robot.commands.drivetrain.TestDrive;
import frc.robot.oi.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.vision.Vision;
import frc.robot.commands.DriveHatch;
import frc.robot.oi.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hatch;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 *  List of all sensors used: 
 *  
 *  List of what's displayed to SmartDashboard:
 *  
 *  
 */
public class Robot extends TimedRobot {

	public static Drivetrain drivetrain;
	public static OI oi;
	public static Hatch hatch;
	public static InternalButton retryButton;
	public static Vision vision;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();


	@Override
	public void robotInit() {
		// Build the subsystems first before the OI.  Otherwise, if the subsystems
		// are instantiated after the OI then it's likely the default command for 
		// the subsystem will not properly be scheduled and run in the way our
		// command based robot should run.
		drivetrain = new Drivetrain();
		vision = new Vision();
		hatch = new Hatch();
		
		
		// ALWAYS INSTANTIATE THE OI LAST
		oi = new OI();
	}

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
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
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		// autonomousCommand = chooser.getSelected();
		// autonomousCommand = new Lvl1RtoCB1();
		autonomousCommand = new TestDrive();
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
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
		// this line or comment it out.
		

        if (autonomousCommand != null)
            autonomousCommand.cancel();

	}
 
	@Override
	public void testInit(){
	}

	/*
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		// SmartDashboard.putData(new Lvl1RtoCB1());

		Scheduler.getInstance().run();		
		// hatch.driveMotor(.25);	
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
	}
}
