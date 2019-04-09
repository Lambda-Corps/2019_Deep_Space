package frc.robot.oi;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.DrivetrainCancel;

/**
 * Changelog:
 * 
 * 
 *
 */

public class OI {
	/*
	 * This class describes the controller interface the drive team will use. Here
	 * we will keep track of the available buttons that exist on each gamepad. When
	 * a new gamepad button is assigned a command, it needs to be taken off the
	 * available button list, and added to the taken list. That way we eliminate the
	 * duplication of buttons as we move.
	 * 
	 * Gamepad 0 -- Driver remote 
	 * Inputs Available -- 
	 * Inputs Taken --
	 * LY - Transitional Speed arcade drive 
	 * RX - Yaw Speed arcade drive 
	 * RT - flip inputs (forward <--> backward) 
	 * 
	 * Gamepad 1 -- Partner remote 
	 * Inputs Available -- L Axis, RX 
	 * Inputs Taken --
	 * 
	 */

	public F310 driverRemote;
	public F310 partnerRemote;

	// driver remote
	public JoystickButton driverA;
	public JoystickButton driverB;
	public JoystickButton driverX;
	public JoystickButton driverY;
	public JoystickButton driverRT;
	public JoystickButton driverLT;
	public JoystickButton driverRB;
	public JoystickButton driverLB;
	public JoystickButton driverStart;
	public JoystickButton driverBack;
	public JoystickButton driverShift;

	// partner remote
	public JoystickButton partnerRB;
	public JoystickButton partnerRT;
	public JoystickButton partnerLB;
	public JoystickButton partnerLT;
	public JoystickButton partnerX;
	public JoystickButton partnerB;
	public JoystickButton partnerA;
	public JoystickButton partnerY;
	public JoystickButton partnerL_AXIS;
	public JoystickButton partnerR_AXIS;
	public JoystickButton partnerStart;
	public JoystickButton partnerBack;

	public OI() {
		driverRemote = new F310(RobotMap.DRIVER_GAMEPAD_PORT);
		partnerRemote = new F310(RobotMap.PARTNER_GAMEPAD_PORT);


		// TODO: instantiate the buttons that are defined above
		// 		 and map them to the desired actions (example: drivetrain cancel)

		//--------------- DRIVER CONTROLS ---------------

		driverStart = new JoystickButton(driverRemote, F310.START);
		driverStart.whenPressed(new DrivetrainCancel());

		/* Manual Gear Shifting - disabled for now. This will give the driver
		* manual control over toggling low gear / high gear
		*/
		// driverShift = new JoystickButton(driverRemote, F310.A);
		// driverShift.whenPressed(new ToggleShiftGears());

		//--------------- PARTNER CONTROLS ---------------


	}
	public double getGainOI() {
		return F310.getGain();
	}
}
