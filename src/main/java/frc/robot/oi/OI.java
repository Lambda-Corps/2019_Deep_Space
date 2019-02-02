package frc.robot.oi;

import frc.robot.oi.F310;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.RobotMap;
/** 
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
import frc.robot.commands.vision.DriveToTarget;

/**
 * Changelog:
 * 
 * 
 *
 */

public class OI {
	/*
	 *  This class describes the controller interface the drive team will use. Here we will keep track of the 
	 * available buttons that exist on each gamepad.  When a new gamepad button is assigned a command, it 
	 * needs to be taken off the available button list, and added to the taken list.  That way we eliminate
	 * the duplication of buttons as we move.
	 * 
	 * Gamepad 0 -- Driver remote
	 * Inputs Available  --  A, B, X, Y, LB, RB, LT, RT
	 * Inputs Taken -- LY - Transitional Speed arcade drive
	 * 				   RX - Yaw Speed arcade drive
	 * 
	 * Gamepad 1 -- Partner remote
	 * Inputs Available  -- A, B, X, Y, LB, RB, LT, RT, L Axis, R Axis  
	 */
	
	public F310 gamepad;
	public F310 gamepad2;
	public JoystickButton drivewithCamera;
	
	public OI() {
		gamepad = new F310(RobotMap.GAMEPAD_PORT);
		gamepad2 = new F310(RobotMap.GAMEPAD2_PORT);
		drivewithCamera = new JoystickButton(gamepad, 1);

		drivewithCamera.whileHeld(new DriveToTarget());
	}
	
	public double getGainOI() {
		return F310.getGain();
	}
}
