package frc.robot;

import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.drivetrain.testcommands.*;
import frc.robot.commands.drivetrain.TurnMM;
import frc.robot.commands.drivetrain.testcommands.DriveMM_Test;
import frc.robot.commands.drivetrain.testcommands.TestArmSetPosition;
import frc.robot.commands.drivetrain.testcommands.TurnMM_Test;
import frc.robot.commands.testcommands.TestDeployCargo;
import frc.robot.commands.testcommands.TestGrabCargo;
import frc.robot.commands.vision.testcommands.DriveWithVisionAuto;
import frc.robot.oi.OI;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.vision.Vision;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import frc.robot.commands.DriveHatch;
import frc.robot.commands.climber.ClimbingSequence;
import frc.robot.commands.climber.DriveClimberMotor;
import frc.robot.commands.climber.ExtendSolenoids;
import frc.robot.commands.climber.RetractBackSolenoid;
import frc.robot.commands.climber.RetractFrontSolenoid;
import frc.robot.commands.climber.RetractSolenoids;
import frc.robot.commands.arm.ArmSetPosition;
import frc.robot.oi.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hatch;
import frc.robot.subsystems.arm.Arm;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * 
 * DIFFERENCES between this (Steamworks) and the 2019 Deep Space robot
 * - right_motor_master has sensor phase set to FALSE here (TRUE for 2019)
 * 
 */


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
	private EncoderFollower left_follower;
 	private EncoderFollower right_follower;
	private Notifier follower_notifier;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	public static NetworkTable testTabTable;

	//Pathweaver constants 
	private static final int k_ticks_per_rev = 1024;
	private static final double k_wheel_diameter = 6; //check
	private static final double k_max_velocity = 1512;  
	private static final String k_path_name = "SimpleArc";

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

		testTabTable = NetworkTableInstance.getDefault().getTable("/Shuffleboard").getSubTable("Testing");
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

		Trajectory left_trajectory = PathfinderFRC.getTrajectory(k_path_name + ".left");
		Trajectory right_trajectory = PathfinderFRC.getTrajectory(k_path_name + ".right");

		left_follower = new EncoderFollower(left_trajectory);
		right_follower = new EncoderFollower(right_trajectory);

		left_follower.configureEncoder(Robot.drivetrain.readLeftEncoder(), k_ticks_per_rev, k_wheel_diameter);
		// You must tune the PID values on the following line!
		left_follower.configurePIDVA(1.0, 0.0, 0.0, 1 / k_max_velocity, 0);

		right_follower.configureEncoder(Robot.drivetrain.readRightEncoder(), k_ticks_per_rev, k_wheel_diameter);
		// You must tune the PID values on the following line!
		right_follower.configurePIDVA(1.0, 0.0, 0.0, 1 / k_max_velocity, 0);
		
		follower_notifier = new Notifier(this::followPath);
		follower_notifier.startPeriodic(left_trajectory.get(0).dt);
 

		autonomousCommand = new TestingSequence();

		autonomousCommand.start();

	}
	
	private void followPath() {
		if (left_follower.isFinished() || right_follower.isFinished()) {
		  follower_notifier.stop();
		} else {
		  double left_speed = left_follower.calculate(Robot.drivetrain.readLeftEncoder());
		  double right_speed = right_follower.calculate(Robot.drivetrain.readRightEncoder());
		  double heading = Robot.drivetrain.getAHRSGyroAngle();
		  double desired_heading = Pathfinder.r2d(left_follower.getHeading());
		  double heading_difference = Pathfinder.boundHalfDegrees(desired_heading - heading);
		  double turn =  0.8 * (-1.0/80.0) * heading_difference;
		  Robot.drivetrain.tankDrivePathweaver(left_speed + turn, right_speed - turn);
		}
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

        if (autonomousCommand != null)
			autonomousCommand.cancel();

		if( follower_notifier != null ){
			follower_notifier.stop();
		}

		//Drivetrain testing
		SmartDashboard.putData("DriveMM_Test", new DriveMM_Test());
		SmartDashboard.putNumber("DriveMM_Test Goal", 0);
		SmartDashboard.putData("TurnMM_Test", new TurnMM_Test());
		SmartDashboard.putNumber("TurnMM_Test Goal", 0);
		SmartDashboard.putData("TestDrive", new TestDrive());
		SmartDashboard.putNumber("TestDrive Speed", 0);
		SmartDashboard.putData("TestingSequence", new TestingSequence());
		SmartDashboard.putData("Turn Without PID", new TurnWithoutPID_Test());
		SmartDashboard.putNumber("TWP Turn Angle", 0);

		SmartDashboard.putData("TestDrive", new TestDrive());
		SmartDashboard.putNumber("TestDrive Speed", Double.valueOf(0.0));

		SmartDashboard.putData("Drive to RF Distance", new DriveToDistanceRF_Test());
		SmartDashboard.putNumber("RF Distance", 0);

		SmartDashboard.putData("TestingSequence", new TestingSequence());

		SmartDashboard.putNumber("motorspeed", Double.valueOf(0.0));

		SmartDashboard.putData("grabcargo", new TestGrabCargo());
		SmartDashboard.putData("deploy cargo", new TestDeployCargo());

		// Climber testing
		SmartDashboard.putData("Extend Solenoids", new ExtendSolenoids());
		SmartDashboard.putData("Retract Back Solenoids", new RetractBackSolenoid());
		SmartDashboard.putData("RetractFrontSolenoids", new RetractFrontSolenoid());
		SmartDashboard.putData("Retract Solenoids", new RetractSolenoids());
		SmartDashboard.putData("Climbing Sequence", new ClimbingSequence());
		SmartDashboard.putData("DriveClimberMotor", new DriveClimberMotor());
		
		//visionTesting//
		SmartDashboard.putData("Vision", new DriveWithVisionAuto());

		//Arm testing
		SmartDashboard.putData("ArmSetPosition", new TestArmSetPosition());

	}
 

	@Override
	public void testInit(){

		
		
        if (autonomousCommand != null)
            autonomousCommand.cancel();

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
		Scheduler.getInstance().run();		

	}
}
