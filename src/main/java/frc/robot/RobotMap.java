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
		public static final int LEFT_TALON_FOLLOWER = 5;
		public static final int RIGHT_TALON_MASTER = 1;
		public static final int RIGHT_TALON_FOLLOWER = 2;
		
	
	//Driver's Station IO
		public static final int GAMEPAD_PORT = 0;
		public static final int GAMEPAD2_PORT = 1;

	//Encoders
		public static final int LEFT_ENCODER_PORT1 = 2;
		public static final int LEFT_ENCODER_PORT2 = 3;
		public static final int RIGHT_ENCODER_PORT1 = 0;
		public static final int RIGHT_ENCODER_PORT2 = 1;
		
}
