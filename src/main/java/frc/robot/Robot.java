package frc.robot;

import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.drivetrain.testcommands.TestDrive;
import frc.robot.commands.drivetrain.testcommands.TestingSequence;
import frc.robot.commands.drivetrain.TurnMM;
import frc.robot.commands.drivetrain.testcommands.DriveMM_Test;
import frc.robot.commands.drivetrain.testcommands.TurnMM_Test;
import frc.robot.commands.testcommands.TestGrabCargo;
import frc.robot.oi.OI;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.vision.Vision;
import frc.robot.commands.DriveHatch;
import frc.robot.commands.climber.ClimbingSequence;
import frc.robot.commands.climber.DriveClimberMotor;
import frc.robot.commands.climber.ExtendSolenoids;
import frc.robot.commands.climber.RetractBackSolenoid;
import frc.robot.commands.climber.RetractSolenoids;
import frc.robot.oi.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hatch;
import frc.robot.subsystems.arm.Arm;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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
	public static Arm arm;
	public static Climber climber;

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
		arm = new Arm();
		climber = new Climber();
		
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
		autonomousCommand = new TestingSequence();

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

		// SmartDashboard.putData("DriveMM_Test", new DriveMM_Test());
		// SmartDashboard.putNumber("DriveMM_Test Goal", 0);

		//Drivetrain testing
		Shuffleboard.getTab("Testing").add("DriveMM_Test", new DriveMM_Test());
		Shuffleboard.getTab("Testing").add("DriveMM_Test Goal", 0);
		Shuffleboard.getTab("Testing").add("TurnMM_Test", new TurnMM_Test());
		Shuffleboard.getTab("Testing").add("TurnMM_Test Goal", 0);
		Shuffleboard.getTab("Testing").add("TestDrive", new TestDrive());
		Shuffleboard.getTab("Testing").add("TestDrive Speed", 0);
		Shuffleboard.getTab("Testing").add("TestingSequence", new TestingSequence());

		SmartDashboard.putData("TestDrive", new TestDrive());
		SmartDashboard.putNumber("TestDrive Speed", 0);

		SmartDashboard.putData("TestingSequence", new TestingSequence());
		SmartDashboard.putNumber("motorspeed", 0.0);
		SmartDashboard.putData("grabcargo", new TestGrabCargo());
		// Climber testing
		Shuffleboard.getTab("Testing").add("Extend Solenoids", new ExtendSolenoids());
		Shuffleboard.getTab("Testing").add("Retract Back Solenoids", new RetractBackSolenoid());
		Shuffleboard.getTab("Testing").add("RetractFrontSolenoids", new RetractBackSolenoid());
		Shuffleboard.getTab("Testing").add("Retract Solenoids", new RetractSolenoids());
		Shuffleboard.getTab("Testing").add("Climbing Sequence", new ClimbingSequence());
		Shuffleboard.getTab("Testing").add("DriveClimberMotor", new DriveClimberMotor());




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

		double l_e = Robot.drivetrain.readLeftEncoder();
		double r_e = Robot.drivetrain.readRightEncoder();

		SmartDashboard.putNumber("L Enc", l_e);
		SmartDashboard.putNumber("R Enc", r_e);

		SmartDashboard.putNumber("L inches", l_e/248.92);
		SmartDashboard.putNumber("R inches", r_e/248.92);

		SmartDashboard.putNumber("AHRS", Robot.drivetrain.getAHRSGyroAngle());
		
		if(r_e==0){
			SmartDashboard.putNumber("L/R", -1);
		} else {
			SmartDashboard.putNumber("L/R", Robot.drivetrain.readLeftEncoder()/Robot.drivetrain.readRightEncoder());
		}

		SmartDashboard.putBoolean("Cargo Present", Robot.arm.ballPresent());


	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
	}
}
