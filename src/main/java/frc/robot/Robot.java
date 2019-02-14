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
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
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

	public enum startPosition{
		POS_1,
		POS_2,
		POS_3;
	}

	public enum goal{
		CROSS_AUTO_LINE,
		L_CB0, //center left cargo bay
		L_CB1,
		L_CB2,
		L_CB3,
		R_CB0, //center right cargo bay
		R_CB1,
		R_CB2,
		R_CB3;
	}

	public enum primaryElement{
		CARGO,
		HATCH;
	}

	public static Drivetrain drivetrain;
	public static OI oi;
	public static Hatch hatch;
	public static InternalButton retryButton;
	public static Vision vision;
	public static Arm arm;

	Command autonomousCommand;
	SendableChooser<startPosition> positionChooser;
	SendableChooser<goal> primaryGoalChooser;
	SendableChooser<primaryElement> primaryElementChooser;
	SendableChooser<goal> secondaryGoalChooser;

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
		positionChooser.setName("Position");
		primaryGoalChooser = new SendableChooser<>();
		primaryGoalChooser.setName("Primary Goal");
		primaryElementChooser = new SendableChooser<>();
		primaryElementChooser.setName("Element to Score");
		secondaryGoalChooser = new SendableChooser<>();
		secondaryGoalChooser.setName("Secondary Goal");

		//Start position chooser
		Shuffleboard.getTab("Autonomous").add(positionChooser).withWidget(BuiltInWidgets.kComboBoxChooser);  //splitbutton, alternately
		positionChooser.addDefault("Pos One", startPosition.POS_1);
		positionChooser.addOption("Pos Two", startPosition.POS_2);
		positionChooser.addOption("Pos Three", startPosition.POS_3);

		//Primary goal chooser
		Shuffleboard.getTab("Autonomous").add(primaryGoalChooser).withWidget(BuiltInWidgets.kComboBoxChooser);  //splitbutton, alternately
		primaryGoalChooser.addDefault("Left Side CB 1", goal.L_CB1);
		primaryGoalChooser.addOption("Left Side CB 2", goal.L_CB2);
		primaryGoalChooser.addOption("Left Side CB 3", goal.L_CB3);
		primaryGoalChooser.addOption("Center Left CB", goal.L_CB0);
		primaryGoalChooser.addOption("Center Right CB", goal.R_CB0);
		primaryGoalChooser.addOption("Right Side CB 1", goal.R_CB1);
		primaryGoalChooser.addOption("Right Side CB 2", goal.R_CB2);
		primaryGoalChooser.addOption("Right Side CB 3", goal.R_CB3);

		//Primary element chooser - cargo or hatch
		Shuffleboard.getTab("Autonomous").add(primaryElementChooser).withWidget(BuiltInWidgets.kComboBoxChooser);  //splitbutton, alternately
		primaryElementChooser.addDefault("Cargo", primaryElement.CARGO);
		primaryElementChooser.addOption("Hatch", primaryElement.HATCH);

		//Secondary goal chooser
		Shuffleboard.getTab("Autonomous").add(secondaryGoalChooser).withWidget(BuiltInWidgets.kComboBoxChooser);  //splitbutton, alternately
		secondaryGoalChooser.addDefault("Left Side CB 1", goal.L_CB1);
		secondaryGoalChooser.addOption("Left Side CB 2", goal.L_CB2);
		secondaryGoalChooser.addOption("Left Side CB 3", goal.L_CB3);
		secondaryGoalChooser.addOption("Center Left CB", goal.L_CB0);
		secondaryGoalChooser.addOption("Center Right CB", goal.R_CB0);
		secondaryGoalChooser.addOption("Right Side CB 1", goal.R_CB1);
		secondaryGoalChooser.addOption("Right Side CB 2", goal.R_CB2);
		secondaryGoalChooser.addOption("Right Side CB 3", goal.R_CB3);
		
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
		// autonomousCommand = buildAutonomous();

		// autonomousCommand.start();

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {	
		Scheduler.getInstance().run();		
	}

	// public Command buildAutonomous(){
	// 	int startPos = positionChooser.getSelected();
	// 	int primaryGoal = primaryGoalChooser.getSelected();
	// 	int primaryElement = primaryElementChooser.getSelected();
	// 	int secondaryGoal = secondaryGoalChooser.getSelected();

	// 	ArrayList<CommandHolder> commandList = new ArrayList<CommandHolder>();
	// 	commandList.clear();

	// 	//>>>> CROSS AUTO LINE (if that is the only thing to do)
	// 	if(primaryGoal==CROSS_AUTO_LINE){
	// 		//TODO: drive forward to cross auto line
	// 		Command autoCommand = new AutoCommandBuilder(commandList);
	// 		return autoCommand;
	// 	}

	// 	//>>>>>>>> GO TO FIRST POSITION FOR SCORING
	// 	//PRIMARY: 1 -> GO TO CLOSER CB
	// 	if(startPos==1&&primaryGoal==CLOSER_CARGO_BAY){
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new Lvl1P1toLt1()));
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new ScoreHatchOnGoal()));
	// 		if(secondaryGoal==CROSS_AUTO_LINE){
	// 		//do nothing, since we already crossed the auto line
	// 		} else {
	// 			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 				new CloserCBtoLS()));
	// 			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 				new PickupFromLS()));
	// 		}
	// 	}
	// 	//PRIMARY: 2 -> GO TO LEFT CB
	// 	if(startPos==2&&primaryGoal==CENTER_LEFT_CARGO_BAY){
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new Lvl1P2toCL0()));
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new ScoreHatchOnGoal()));
	// 		if(secondaryGoal==CROSS_AUTO_LINE){
	// 			//do nothing, since we already crossed the auto line
	// 		} else {
	// 			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 				new CenterLeftCBtoLS()));
	// 			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 				new PickupFromLS()));
	// 		}
	// 	}
	// 	//PRIMARY: 2 -> GO TO RIGHT CB
	// 	if(startPos==2&&primaryGoal==CENTER_RIGHT_CARGO_BAY){
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new Lvl1P2toCR0()));
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new ScoreHatchOnGoal()));
	// 		if(secondaryGoal==CROSS_AUTO_LINE){
	// 			//do nothing, since we already crossed the auto line
	// 		} else {
	// 			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 				new CenterRightCBtoLS()));
	// 			commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 				new PickupFromLS()));
	// 		}				
	// 	}

	// 	//>>>>>>>> SCORE on Cargo or Hatch
	// 	if(primaryElement==CARGO_ELEMENT){
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new ScoreHatchOnGoal()));
	// 	} else {
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new ScoreCargoOnGoal()));
	// 	}

	// 	//>>>>>>>> GO TO LOADING STATION
	// 	//Closer CB to LS
	// 	if(primaryGoal==CLOSER_CARGO_BAY){
	// 		commandList.add(new CommandHolder(CommandHolder.SEQUENTIAL_COMMAND, 
	// 			new LeftCloserCBtoLS()));
	// 	}

	// Command autoCommand = new AutoCommandBuilder(commandList);
	// return autoCommand;

	// }
 
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
