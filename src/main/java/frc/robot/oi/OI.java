package frc.robot.oi;

import frc.robot.oi.F310;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Robot;
import frc.robot.RobotMap;
/** 
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
import frc.robot.commands.vision.DriveToTarget;
import frc.robot.commands.vision.GetTargetCommand;
import frc.robot.commands.vision.SwitchPipelines;
import frc.robot.commands.vision.toggleCamMode;
import frc.robot.commands.arm.DeployCargo;
import frc.robot.commands.arm.GrabCargo;
import frc.robot.commands.hatch.DeployHatch;
import frc.robot.commands.hatch.DriveHatch;
import frc.robot.commands.hatch.RetractHatch;

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
	 * Inputs Taken -- RY - Arm control
	 * 				-- Y - Deploy Cargo
	 * 				-- A - Grab Cargo
	 */
	
	public F310 gamepad; 
	public F310 gamepad2;
	public JoystickButton buttonA_J1;
	public JoystickButton buttonB_J1;
	public JoystickButton buttonX_J1;
	public JoystickButton buttonY_J1;

	public JoystickButton rTrigger_J1;
	public JoystickButton lTrigger_J1;
	public JoystickButton rBumper_J1;
	public JoystickButton lBumper_J1;
	public JoystickButton partnerA; 
	public JoystickButton yDeploy;
	public JoystickButton aIntake;
	public JoystickButton xRetractHatch;
	public JoystickButton bDeployHatch;
	
	public OI() {
		gamepad = new F310(RobotMap.GAMEPAD_PORT);
		gamepad2 = new F310(RobotMap.GAMEPAD2_PORT);

		//DRIVER CONTROLS//
		buttonA_J1 = new JoystickButton(gamepad, F310.A);
		buttonA_J1.whileHeld(new DriveToTarget());

		buttonY_J1 = new JoystickButton(gamepad, F310.Y);
		buttonY_J1.whenPressed(new toggleCamMode());

		rBumper_J1 = new JoystickButton(gamepad, F310.RB);
		rBumper_J1.whenPressed(new SwitchPipelines(Robot.vision.CargoShipPipeLine));

		lBumper_J1 = new JoystickButton(gamepad, F310.LB);
		lBumper_J1.whenPressed(new SwitchPipelines(Robot.vision.OrangeBallPipeline));

		//ARM CONTROLS//
		yDeploy = new JoystickButton(gamepad2, F310.Y);
		yDeploy.whenPressed(new DeployCargo());

		aIntake = new JoystickButton(gamepad2, F310.A);
		aIntake.whenPressed(new GrabCargo());

		//OTHER CONTROLS//
		partnerA = new JoystickButton(gamepad2, F310.A);
		partnerA.toggleWhenPressed(new DriveHatch());

		//Hatch deploy
		xRetractHatch = new JoystickButton(gamepad2, F310.X);
		xRetractHatch.whenPressed(new RetractHatch());
		bDeployHatch = new JoystickButton(gamepad2, F310.X);
		bDeployHatch.whenPressed(new DeployHatch());


	}
	public double getGainOI() {
		return F310.getGain();
	}
}
