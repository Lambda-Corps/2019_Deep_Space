package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
/*
 * Changelog:
 * 
 *
 *
 */

public class RobotMap {
	
	//CAN Bus
	public static final int LEFT_TALON_MASTER = 4;
	public static final int LEFT_TALON_FOLLOWER = 3;
	public static final int RIGHT_TALON_MASTER = 5;
	public static final int RIGHT_TALON_FOLLOWER = 6;
	public static final int HATCH_TALON = 1; //Bosch
	public static final int CLIMBER_TALON = 2;
	public static final int ARM_TALON = 7;
	public static final int INTAKE_TALON = 8;
	
	//Driver's Station IO
	public static final int DRIVER_GAMEPAD_PORT = 0;
	public static final int PARTNER_GAMEPAD_PORT = 1;
		
	// Hatch Subsystem
	public static final int HATCH_ENCODER = 9;

	// Climber Subsystem
	//find ports

	// Pneumatics Control
	
	//Soleniods
	public static final int DRIVETRAIN_SOLENOID_PORT_A = 0;
	public static final int DRIVETRAIN_SOLENOID_PORT_B = 1;
	public static final int CLIMBER_DOUBLE_SOLENOID_1_PORT_A = 2;
	public static final int CLIMBER_DOUBLE_SOLENOID_1_PORT_B = 3;
	public static final int CLIMBER_DOUBLE_SOLENOID_2_PORT_A = 4;
	public static final int CLIMBER_DOUBLE_SOLENOID_2_PORT_B = 5;
	public static final int HATCH_SOLENOID_PORT_A = 6;
	public static final int HATCH_SOLENOID_PORT_B = 7;
	
	//Analog Inputs
	public static final int ARM_ABSOLUTE_ENCODER = 0;
	public static final int INTAKE_BALL_DETECTOR = 1;
	public static final int INTAKE_BALL_DETECTOR_2 = 2;
	public static final int HATCH_DISTANCE_FINDER = 3;
	public static final int CLIMBER_RANGEFINDER = 4;

	//DigitalInput
	public static final int LIMIT_SWITCH=0; //Don't know port number yet
	public static final int ARM_REVERSE_LIMIT_SWITCH = 4;
	public static final int HATCH_LIMIT_SWITCH = 5;

	//DigitalOutput
	public static final int LED_0 = 0;
	public static final int LED_1 = 1;
	public static final int LED_2 = 2;
	public static final int LED_3 = 3;
	
}