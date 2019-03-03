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
import frc.robot.commands.arm.ArmSetPositionMM;
import frc.robot.commands.autonomous.AutoCommandBuilder;
import frc.robot.commands.autonomous.CommandHolder;
import frc.robot.commands.autonomous.LLStoR_CB0;
import frc.robot.commands.autonomous.L_CB0toLLS;
import frc.robot.commands.autonomous.P1toL_CB1;
import frc.robot.commands.autonomous.P1toL_CB2;
import frc.robot.commands.autonomous.P1toL_CB3;
import frc.robot.commands.autonomous.P2toL_CB0;
import frc.robot.commands.autonomous.P2toR_CB0;
import frc.robot.commands.autonomous.P3toR_CB1;
import frc.robot.commands.autonomous.P3toR_CB2;
import frc.robot.commands.autonomous.P3toR_CB3;
import frc.robot.commands.autonomous.RLStoL_CB0;
import frc.robot.commands.autonomous.R_CB0toRLS;
import frc.robot.commands.climber.ClimbingSequence;
import frc.robot.commands.climber.DriveClimberMotor;
import frc.robot.commands.climber.ExtendFrontAndBackSolenoids;
import frc.robot.commands.climber.RetractBackSolenoid;
import frc.robot.commands.climber.RetractFrontSolenoid;
import frc.robot.commands.climber.RetractSolenoids;
import frc.robot.commands.drivetrain.DrivetrainClimb;
import frc.robot.commands.drivetrain.TurnMM;
import frc.robot.commands.hatch.PickupHatch;
import frc.robot.commands.rumble.RumbleCommand;
import frc.robot.commands.vision.DriveAndScoreCargo;
import frc.robot.commands.vision.DriveAndScoreHatch;
import frc.robot.oi.OI;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.ArmIntake;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hatch;
import frc.robot.subsystems.LEDSignal;
import frc.robot.subsystems.Vision;

/**
 * 
 * DIFFERENCES between this (Steamworks) and the 2019 Deep Space robot
 * - right_motor_master in Drivetrain has sensor phase set to TRUE here (FALSE for 2019)
 * 
 */

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

	public enum startPosition {
		POS_1, POS_2, POS_3;
	}

	public enum goal {
		NONE, L_CB0, // center left cargo bay
		L_CB1, L_CB2, L_CB3, R_CB0, // center right cargo bay
		R_CB1, R_CB2, R_CB3, T_180;
	}

	public enum element {
		CARGO, HATCH;
	}

	public static Drivetrain drivetrain;
	public static OI oi;
	public static Hatch hatch;
	public static InternalButton retryButton;
	public static Vision vision;
	public static Arm arm;
	public static ArmIntake armIntake;
	public static Climber climber;
	public static LEDSignal ledSubsystem;

	Command autonomousCommand;
	SendableChooser<startPosition> positionChooser;
	SendableChooser<goal> primaryGoalChooser;
	SendableChooser<element> primaryElementChooser;
	SendableChooser<goal> secondaryGoalChooser;
	SendableChooser<element> secondaryElementChooser;

	SendableChooser<Command> chooser = new SendableChooser<>();

	public static NetworkTable testTabTable;

	// Pathweaver constants
	// private static final int k_ticks_per_rev = 1024;
	// private static final double k_wheel_diameter = 6; // check
	// private static final double k_max_velocity = 1512;
	// private static final String k_path_name = "SimpleArc";

	@Override
	public void robotInit() {
		// Build the subsystems first before the OI. Otherwise, if the subsystems
		// are instantiated after the OI then it's likely the default command for
		// the subsystem will not properly be scheduled and run in the way our
		// command based robot should run.
		drivetrain = new Drivetrain();
		vision = new Vision();
		hatch = new Hatch();
		arm = new Arm();
		climber = new Climber();
		armIntake = new ArmIntake();
		ledSubsystem = new LEDSignal();
		// ALWAYS INSTANTIATE THE OI LAST
		oi = new OI();

		positionChooser = new SendableChooser<>();
		positionChooser.setName("Position");
		primaryGoalChooser = new SendableChooser<>();
		primaryGoalChooser.setName("Primary Goal");
		primaryElementChooser = new SendableChooser<>();
		primaryElementChooser.setName("Primary Element");
		secondaryGoalChooser = new SendableChooser<>();
		secondaryGoalChooser.setName("Secondary Goal");
		secondaryElementChooser = new SendableChooser<>();
		secondaryElementChooser.setName("Secondary Element");

		// Start position chooser
		Shuffleboard.getTab("Autonomous").add(positionChooser).withWidget(BuiltInWidgets.kComboBoxChooser); // splitbutton,
																											// alternately
		positionChooser.addOption("Pos One", startPosition.POS_1);
		positionChooser.addOption("Pos Two", startPosition.POS_2);
		positionChooser.addOption("Pos Three", startPosition.POS_3);

		// Primary goal chooser
		Shuffleboard.getTab("Autonomous").add(primaryGoalChooser).withWidget(BuiltInWidgets.kComboBoxChooser); // splitbutton,
																												// alternately
		primaryGoalChooser.addOption("Cross Auto Line", goal.NONE);
		primaryGoalChooser.addOption("Left Side CB 1", goal.L_CB1);
		primaryGoalChooser.addOption("Left Side CB 2", goal.L_CB2);
		primaryGoalChooser.addOption("Left Side CB 3", goal.L_CB3);
		primaryGoalChooser.addOption("Center Left CB", goal.L_CB0);
		primaryGoalChooser.addOption("Center Right CB", goal.R_CB0);
		primaryGoalChooser.addOption("Right Side CB 1", goal.R_CB1);
		primaryGoalChooser.addOption("Right Side CB 2", goal.R_CB2);
		primaryGoalChooser.addOption("Right Side CB 3", goal.R_CB3);

		// Primary element chooser - cargo or hatch
		Shuffleboard.getTab("Autonomous").add(primaryElementChooser).withWidget(BuiltInWidgets.kComboBoxChooser); // splitbutton,
		// alternately
		primaryElementChooser.addOption("Cargo", element.CARGO);
		primaryElementChooser.addOption("Hatch", element.HATCH);
		testTabTable = NetworkTableInstance.getDefault().getTable("/Shuffleboard").getSubTable("Testing");

		// Secondary goal chooser
		Shuffleboard.getTab("Autonomous").add(secondaryGoalChooser).withWidget(BuiltInWidgets.kComboBoxChooser); // splitbutton,
																													// alternately
		secondaryGoalChooser.addOption("None", goal.NONE);
		secondaryGoalChooser.addOption("Left Side CB 1", goal.L_CB1);
		secondaryGoalChooser.addOption("Left Side CB 2", goal.L_CB2);
		secondaryGoalChooser.addOption("Left Side CB 3", goal.L_CB3);
		secondaryGoalChooser.addOption("Center Left CB", goal.L_CB0);
		secondaryGoalChooser.addOption("Center Right CB", goal.R_CB0);
		secondaryGoalChooser.addOption("Right Side CB 1", goal.R_CB1);
		secondaryGoalChooser.addOption("Right Side CB 2", goal.R_CB2);
		secondaryGoalChooser.addOption("Right Side CB 3", goal.R_CB3);
		secondaryGoalChooser.addOption("Turn 180", goal.T_180);

		// Primary element chooser - cargo or hatch
		Shuffleboard.getTab("Autonomous").add(secondaryElementChooser).withWidget(BuiltInWidgets.kComboBoxChooser); // splitbutton,
		// alternately
		secondaryElementChooser.addOption("Cargo", element.CARGO);
		secondaryElementChooser.addOption("Hatch", element.HATCH);
		testTabTable = NetworkTableInstance.getDefault().getTable("/Shuffleboard").getSubTable("Testing");

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
		autonomousCommand = buildAutonomous();
		// autonomousCommand = new DriveToTargetGroup();

		// SmartDashboard.putBoolean("done with auto", false);
		// SmartDashboard.putBoolean("driving dtta", false);

		autonomousCommand.start();

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public Command buildAutonomous() {
		startPosition startPos = positionChooser.getSelected();
		goal primaryGoal = primaryGoalChooser.getSelected();
		element primaryElement = primaryElementChooser.getSelected();
		goal secondaryGoal = secondaryGoalChooser.getSelected();
		element secondaryElement = secondaryElementChooser.getSelected();

		ArrayList<CommandHolder> commandList = new ArrayList<CommandHolder>();
		commandList.clear();

		// >>>> CROSS AUTO LINE (if that is the only thing to do)
		if (primaryGoal == goal.NONE) {
			// TODO: drive forward to cross auto line
			Command autoCommand = new AutoCommandBuilder(commandList);
			return autoCommand;
		}

		// >>>>>>>> GO TO PRIMARY POSITION FOR SCORING
		// 1 -> L_CB1
		if (startPos == startPosition.POS_1 && primaryGoal == goal.L_CB1) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P1toL_CB1()));
			// System.out.println("adding positioning command to list");
		}
		// 1 -> L_CB2
		if (startPos == startPosition.POS_1 && primaryGoal == goal.L_CB2) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P1toL_CB2()));
		}
		// 1 -> L_CB3
		if (startPos == startPosition.POS_1 && primaryGoal == goal.L_CB3) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P1toL_CB3()));
		}
		// 2 -> L_CB0
		if (startPos == startPosition.POS_2 && primaryGoal == goal.L_CB0) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P2toL_CB0()));
		}
		// 2 -> R_CB0
		if (startPos == startPosition.POS_2 && primaryGoal == goal.R_CB0) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P2toR_CB0()));
		}
		// 3 -> R_CB1
		if (startPos == startPosition.POS_3 && primaryGoal == goal.R_CB1) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P3toR_CB1()));
		}
		// 3 -> R_CB2
		if (startPos == startPosition.POS_3 && primaryGoal == goal.R_CB2) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P3toR_CB2()));
		}
		// 3 -> R_CB3
		if (startPos == startPosition.POS_3 && primaryGoal == goal.R_CB3) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new P3toR_CB3()));
		}
		// TODO: etc

		// // >>>>>>>> SCORE Cargo or Hatch
		// if (primaryElement == element.HATCH) {
		// 	commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new DriveAndScoreHatch()));
		// 	// System.out.println("adding scoring command to list");
		// } else {
		// 	commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new DriveAndScoreCargo()));
		// }

		// --------------------SECONDARY GOAL--------------------

		// >>>> END IF NO SECONDARY GOAL
		if (secondaryGoal == goal.NONE) {
			// System.out.println("no secondary goal, returning command");
			Command autoCommand = new AutoCommandBuilder(commandList);
			return autoCommand;
		}

		// turn 180 degrees
		if (secondaryGoal == goal.T_180) {
			// System.out.println("no secondary goal, returning command");
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new TurnMM(180))); //TODO: how to add timeout?
		}

		// --- GO TO LOADING STATION FOR SECONDARY GOAL ---

		// top priority paths for 2ndary goal:
		// L_CB0 --> LLS
		if (primaryGoal == goal.L_CB0) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new L_CB0toLLS()));
		}
		// R_CB0 --> RLS
		if (primaryGoal == goal.R_CB0) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new R_CB0toRLS()));
		}

		// others - lower priority
		/*
		 * //L_CB1 --> LLS if(primaryGoal==goal.L_CB1){ commandList.add(new
		 * CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new L_CB1toLLS())); }
		 * //R_CB1--> RLS if(primaryGoal==goal.L_CB1){ commandList.add(new
		 * CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new R_CB1toRLS())); }
		 */

		// --- PICKUP FROM LOADING STATION ---
		if (secondaryElement == element.HATCH) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new PickupHatch()));
			// System.out.println("adding scoring command to list");
		} else {
			// commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new PickupCargo()));
		}

		// --- GO TO SECONDARY POSITION FOR SCORING
		// LLS --> R_CB0
		if (primaryGoal == goal.L_CB0) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new LLStoR_CB0()));
		}
		// RLS --> L_CB0
		if (primaryGoal == goal.R_CB0) {
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, new RLStoL_CB0()));
		}

		Command autoCommand = new AutoCommandBuilder(commandList);
		return autoCommand;

	}

	@Override
	public void teleopInit() {

		Scheduler.getInstance().removeAll();

		if (autonomousCommand != null)
			autonomousCommand.cancel();

		//TODO: remove!! when done testing
		// SmartDashboard.putNumber("pov", 0);


		Command rumbleCommand = new RumbleCommand();
		rumbleCommand.start();
		// SmartDashboard.putData("Transmission Forward", new ShiftForward());
		// SmartDashboard.putData("Transmission Reverse", new ShiftReverse());

		// arm coeff
		// SmartDashboard.putNumber("coefficient on arm",0.5);
		// SmartDashboard.putNumber("arm speed cap", 1);
		// SmartDashboard.putData("MM Arm Set", new TestArmSetPositionMM());
		// SmartDashboard.putNumber("Arm Position", 0);

		// Drivetrain testing
		// SmartDashboard.putData("DriveMM_Test", new DriveMM_Test());//TODO
		// SmartDashboard.putNumber("DriveMM_Test Goal", 0);//TODO
		// SmartDashboard.putData("TurnMM_Test", new TurnMM_Test());//TODO
		// SmartDashboard.putNumber("TurnMM_Test Goal", 0);//TODO
		// SmartDashboard.putData("PivotToTarget", new PivotToTargetAuto());//TODO
		// SmartDashboard.putData("DriveToTargetAuto", new DriveToTargetAuto());//TODO
		// SmartDashboard.putData("DriveWithRangeFinder", new DriveWithVisionAuto());
		// SmartDashboard.putData("TestDrive", new TestDrive());
		// SmartDashboard.putNumber("TestDrive Speed", 0);
		// SmartDashboard.putData("TestingSequence", new TestingSequence());
		// SmartDashboard.putData("Turn Without PID", new TurnWithoutPID_Test());
		// SmartDashboard.putNumber("TWP Turn Angle", 0);

		// SmartDashboard.putData("TestDrive", new TestDrive());
		// SmartDashboard.putNumber("TestDrive Speed", Double.valueOf(0.0));

		// SmartDashboard.putData("Drive to RF Distance", new DriveToDistanceRF_Test());
		// SmartDashboard.putNumber("RF Distance", 0);

		// SmartDashboard.putData("TestingSequence", new TestingSequence());

		// SmartDashboard.putNumber("motorspeed", Double.valueOf(0.0));
		// SmartDashboard.putNumber("endspeed", Double.valueOf(0.0));
		// SmartDashboard.putNumber("veryendspeed", Double.valueOf(0.0));
		// SmartDashboard.putNumber("ok_iterations", 0.0);

		// SmartDashboard.putData("grabcargo", new TestGrabCargo());
		// SmartDashboard.putData("deploy cargo", new TestDeployCargo());

		// // Climber testing
		SmartDashboard.putData("Climb MM Arm Set", new ArmSetPositionMM(Arm.ARM_POSITION_CLIMB));
		SmartDashboard.putData("Extend Solenoids", new ExtendFrontAndBackSolenoids());
		SmartDashboard.putData("DriveClimberMotor", new DriveClimberMotor());
		SmartDashboard.putNumber("colson speed", 0);
		SmartDashboard.putData("Drivetrain Climb", new DrivetrainClimb());
		SmartDashboard.putData("RetractFrontSolenoids", new RetractFrontSolenoid());
		SmartDashboard.putData("0 MM Arm Set", new ArmSetPositionMM(Arm.ARM_POSITION_ZERO));
		SmartDashboard.putData("Retract Back Solenoids", new RetractBackSolenoid());

		SmartDashboard.putData("Climbing Sequence", new ClimbingSequence());
		SmartDashboard.putData("Retract Solenoids", new RetractSolenoids());

		// SmartDashboard.putData("Extend Front Solenoids", new ExtendFrontSolenoid());
		// SmartDashboard.putData("Extend Back Solenoids", new ExtendBackSolenoid());
		// SmartDashboard.putData("Retract Front Solenoids", new
		// RetractFrontSolenoid());
		// SmartDashboard.putData("Retract Back Solenoids", new RetractBackSolenoid());

		// SmartDashboard.putNumber("Cargo Distance",
		// Robot.armIntake.getBallDistance());

		// visionTesting//
		// SmartDashboard.putData("Vision", new DriveWithVisionAuto());

		// //Arm testing
		// SmartDashboard.putData("ArmSetPosition", new TestArmSetPosition());

		// //Hatch
		// SmartDashboard.putData("Hatch Hook Up", new DriveHatch());
		// SmartDashboard.putData("Hatch Hook Down", new DriveHatchToLimit());
		// SmartDashboard.putData("Deploy Hatch", new DeployHatch());
		// SmartDashboard.putData("Retract Hatch", new RetractHatch());

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

		// SmartDashboard.putData(new Lvl1RtoCB1());

		Scheduler.getInstance().run();
		// hatch.driveMotor(.25);
		SmartDashboard.putNumber("gyro", Robot.drivetrain.getAHRSGyroAngle());// TODO
		SmartDashboard.putNumber("l_encoder", Robot.drivetrain.readLeftEncoder());// TODO
		SmartDashboard.putNumber("r_encoder", Robot.drivetrain.readRightEncoder());// TODO

		// SmartDashboard.putNumber("Current - Intake Motor",
		// Robot.armIntake.getMotorCurrent());

		SmartDashboard.putNumber("Arm Pos", Robot.arm.getArmPosition());
		SmartDashboard.putNumber("Arm Current", Robot.arm.getArmCurrent());


	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();

	}
}
