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
		public static final int LEFT_MOTOR1_PORT = 4;
		public static final int LEFT_MOTOR2_PORT = 5;
		public static final int LEFT_MOTOR3_PORT = 6;
		public static final int RIGHT_MOTOR1_PORT = 1;
		public static final int RIGHT_MOTOR2_PORT = 2;
		public static final int RIGHT_MOTOR3_PORT = 3;
		public static final int WINCH_MOTOR_PORT = 7;
		public static final int REGULATOR_MOTOR_PORT = 8;
		public static final int SHOOTER_MOTOR_PORT = 9;
		
	
	//Driver's Station IO
		public static final int  LEFT_JOYSTICK_PORT = 0;
		public static final int RIGHT_JOYSTICK_PORT = 1;
		public static final int GAMEPAD_PORT = 0;
		public static final int GAMEPAD2_PORT = 1;
		
}
