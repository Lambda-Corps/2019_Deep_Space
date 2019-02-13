package frc.robot;

import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.drivetrain.testcommands.TestDrive;
import frc.robot.commands.drivetrain.testcommands.TestingSequence;
import frc.robot.commands.drivetrain.TurnMM;
import frc.robot.commands.drivetrain.testcommands.DriveMM_Test;
import frc.robot.commands.drivetrain.testcommands.TurnMM_Test;
import frc.robot.oi.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.vision.Vision;
import frc.robot.commands.DriveHatch;
import frc.robot.commands.autonomous.AutoCommandBuilder;
import frc.robot.commands.autonomous.Autonomous;
import frc.robot.commands.autonomous.CommandHolder;
import frc.robot.commands.autonomous.Lvl1P1toLt1;
import frc.robot.commands.autonomous.Lvl1P2toCL0;
import frc.robot.commands.autonomous.Lvl1P2toCR0;
import frc.robot.commands.autonomous.ScoreHatchOnGoal;
import frc.robot.oi.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hatch;
import frc.robot.subsystems.arm.Arm;

import java.util.ArrayList;

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

	public static final int CROSS_AUTO_LINE = 0;
	public static final int CLOSER_CARGO_BAY = 1;
	public static final int MIDDLE_CARGO_BAY = 2;
	public static final int FARTHER_CARGO_BAY = 3;
	public static final int CENTER_LEFT_CARGO_BAY = 4;
	public static final int CENTER_RIGHT_CARGO_BAY = 5;

	public static Drivetrain drivetrain;
	public static OI oi;
	public static Hatch hatch;
	public static InternalButton retryButton;
	public static Vision vision;
	public static Arm arm;

	Command autonomousCommand;
	SendableChooser<Integer> positionChooser;
	SendableChooser<Integer> primaryGoalChooser;
	SendableChooser<Integer> secondaryGoalChooser;

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
		
		// ALWAYS INSTANTIATE THE OI LAST
		oi = new OI();

		positionChooser = new SendableChooser<>();
		primaryGoalChooser = new SendableChooser<>();
		secondaryGoalChooser = new SendableChooser<>();

		//Start position chooser
		SmartDashboard.putData("Start Position", positionChooser);
		positionChooser.addObject("Cross Auto Line", 0);
		positionChooser.addObject("Pos One", 1);
		positionChooser.addDefault("Pos Two", 2);
		positionChooser.addObject("Pos Three", 3);

		//Primary goal chooser
		SmartDashboard.putData("Primary Goal", positionChooser);
		positionChooser.addObject("None", CROSS_AUTO_LINE
);
		positionChooser.addObject("Closer Cargo Bay", CLOSER_CARGO_BAY);
		positionChooser.addDefault("Middle Cargo Bay", MIDDLE_CARGO_BAY);
		positionChooser.addObject("Farther Cargo Bay", FARTHER_CARGO_BAY);
		positionChooser.addObject("CENTER Closer Cargo Bay", CENTER_LEFT_CARGO_BAY
);
		positionChooser.addObject("CENTER Farther Cargo Bay", CENTER_RIGHT_CARGO_BAY);

		//Secondary goal chooser
		SmartDashboard.putData("Secondary Goal", positionChooser);
		positionChooser.addObject("None", CROSS_AUTO_LINE
);
		positionChooser.addObject("Closer Cargo Bay", CLOSER_CARGO_BAY);
		positionChooser.addDefault("Middle Cargo Bay", MIDDLE_CARGO_BAY);
		positionChooser.addObject("Farther Cargo Bay", FARTHER_CARGO_BAY);
		positionChooser.addObject("CENTER Closer Cargo Bay", CENTER_LEFT_CARGO_BAY
);
		positionChooser.addObject("CENTER Farther Cargo Bay", CENTER_RIGHT_CARGO_BAY);
		
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
		autonomousCommand = buildAutonomous();

		autonomousCommand.start();

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {	
		Scheduler.getInstance().run();		
	}

	public Command buildAutonomous(){
		int startPos = positionChooser.getSelected();
		int primaryGoal = primaryGoalChooser.getSelected();
		int secondaryGoal = secondaryGoalChooser.getSelected();

		ArrayList<CommandHolder> commandList = new ArrayList<CommandHolder>();
		commandList.clear();

		//PRIMARY: CROSS AUTO LINE
		if(primaryGoal==CROSS_AUTO_LINE){
			//TODO: drive forward to cross auto line
		} else {
			//PRIMARY: 1 -> SCORE ON CLOSER CB
			if(startPos==1&&primaryGoal==CLOSER_CARGO_BAY){
				commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
					new Lvl1P1toLt1()));
				commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
					new ScoreHatchOnGoal()));
				if(secondaryGoal==CROSS_AUTO_LINE){
					//do nothing, since we already crossed the auto line
				} else {
					commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
						new CloserCBtoLS()));
					commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
						new PickupFromLS()));
				}
			}
			//PRIMARY: 2 -> SCORE ON LEFT CB
			if(startPos==2&&primaryGoal==CENTER_LEFT_CARGO_BAY){
				commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
					new Lvl1P2toCL0()));
				commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
					new ScoreHatchOnGoal()));
				if(secondaryGoal==CROSS_AUTO_LINE){
					//do nothing, since we already crossed the auto line
				} else {
					commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
						new CenterLeftCBtoLS()));
					commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
						new PickupFromLS()));
				}
			}
			//PRIMARY: 3 -> SCORE ON RIGHT CB
			if(startPos==2&&primaryGoal==CENTER_RIGHT_CARGO_BAY){
				commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
					new Lvl1P2toCR0()));
				commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
					new ScoreHatchOnGoal()));
				if(secondaryGoal==CROSS_AUTO_LINE){
					//do nothing, since we already crossed the auto line
				} else {
					commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
						new CenterRightCBtoLS()));
					commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
						new PickupFromLS()));
				}				
			}
			//SCORE HATCH
			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
					new ScoreHatchOnGoal()));

		}

		Command autoCommand = new AutoCommandBuilder(commandList);
		return autoCommand;

	}
 
	@Override
	public void teleopInit() {

		SmartDashboard.putData("DriveMM_Test", new DriveMM_Test());
		SmartDashboard.putNumber("DriveMM_Test Goal", 0);

		SmartDashboard.putData("TurnMM_Test", new TurnMM_Test());
		SmartDashboard.putNumber("TurnMM_Test Goal", 0);

		SmartDashboard.putData("TestDrive", new TestDrive());
		SmartDashboard.putNumber("TestDrive Speed", 0);

		SmartDashboard.putData("TestingSequence", new TestingSequence());

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

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		
	}
}
