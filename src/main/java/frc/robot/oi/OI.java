package frc.robot.oi;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.hatch.DeployHatch;
import frc.robot.commands.hatch.DriveHatchToLimit;
import frc.robot.commands.hatch.PickupHatch;
import frc.robot.commands.hatch.RetractHatch;
import frc.robot.commands.intake.DeployCargo;
import frc.robot.commands.intake.DeployCargoManual;
import frc.robot.commands.intake.GrabCargo;
import frc.robot.commands.intake.GrabCargoGroup;
import frc.robot.commands.intake.GrabCargoWhileHeld;
import frc.robot.commands.testcommands.TestArmSetPositionMM;
/** 
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
import frc.robot.commands.vision.DriveToTarget;
import frc.robot.commands.vision.SetStreamMode;
import frc.robot.commands.vision.SwitchPipelines;
import frc.robot.subsystems.Arm;

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
	 * Gamepad 0 -- Driver remote Inputs Available -- B, X, LT, RT Inputs Taken --
	 * LY - Transitional Speed arcade drive RX - Yaw Speed arcade drive A - Drive to
	 * target Y - Toggle CamMode RB - Switch Pipelines(CargoShip Pipeline) LB -
	 * Switch Pipelines(OrangeBall Pipeline)
	 * 
	 * Gamepad 1 -- Partner remote Inputs Available -- L Axis, RX Inputs Taken -- RY
	 * - Arm control -- RB - Deploy Cargo -- RT - Grab Cargo -- LB - Retract Hatch
	 * -- LT - Deploy Hatch -- A - Arm To Pickup Cargo position -- B - Arm to
	 * scoring cargo position -- X - Toggle Climbing Sequence -- Y - Pickup Hatch
	 */
	
	public F310 driverRemote; 
	public F310 partnerRemote;
	//driver remote
	public JoystickButton buttonA_J1;
	public JoystickButton buttonB_J1;
	public JoystickButton buttonX_J1;
	public JoystickButton buttonY_J1;

	public JoystickButton rTrigger_J1;
	public JoystickButton lTrigger_J1;
	public JoystickButton rBumper_J1;
	public JoystickButton lBumper_J1;

	//partner remote
	public JoystickButton rbDeployCargo;
	public JoystickButton rtIntakeCargo;
	public JoystickButton lbRetractHatch;
	public JoystickButton ltDeployHatch;
	public JoystickButton xGrabWhileHeld;
	public JoystickButton bArmToScoreCargo;
	public JoystickButton aArmToGrabCargo;
	public JoystickButton yPickupHatch;
	
	public OI() {
		driverRemote = new F310(RobotMap.DRIVER_GAMEPAD_PORT);
		partnerRemote = new F310(RobotMap.PARTNER_GAMEPAD_PORT);

		//DRIVER CONTROLS//

		//Vision
		buttonA_J1 = new JoystickButton(driverRemote, F310.A);
		buttonA_J1.whileHeld(new DriveToTarget());

		buttonY_J1 = new JoystickButton(driverRemote, F310.Y);
		buttonY_J1.whenPressed(new SetStreamMode());

		// rBumper_J1 = new JoystickButton(driverRemote, F310.RB);
		// rBumper_J1.whenPressed(new SwitchPipelines(Robot.vision.CargoShipPipeLine));

		// lBumper_J1 = new JoystickButton(driverRemote, F310.LB);
		// lBumper_J1.whenPressed(new SwitchPipelines(Robot.vision.OrangeBallPipeline));

		//PARTNER CONTROLS

		//Cargo Management

		rtIntakeCargo = new JoystickButton(partnerRemote, F310.LB);
		rtIntakeCargo.whenPressed(new GrabCargoGroup());

		rbDeployCargo = new JoystickButton(partnerRemote, F310.RB);
		rbDeployCargo.whenPressed(new DeployCargoManual());

		//Arm Positioning
		bArmToScoreCargo = new JoystickButton(partnerRemote, F310.B);
		bArmToScoreCargo.whenPressed(new TestArmSetPositionMM(Arm.ARM_POSITION_SCORING_CARGO));

		aArmToGrabCargo = new JoystickButton(partnerRemote, F310.A);
		aArmToGrabCargo.whenPressed(new TestArmSetPositionMM(Arm.ARM_POSITION_PICKUP_CARGO));

		//save x for rocket positioning?

		//Hatch Management
		lbRetractHatch = new JoystickButton(partnerRemote, F310.L_AXIS_PRESS);
		lbRetractHatch.whenPressed(new RetractHatch());

		ltDeployHatch = new JoystickButton(partnerRemote, F310.R_AXIS_PRESS);
		ltDeployHatch.whenPressed(new DeployHatch());
		
		yPickupHatch = new JoystickButton(partnerRemote, F310.Y);
		yPickupHatch.whenPressed(new DriveHatchToLimit());


	}
	public double getGainOI() {
		return F310.getGain();
	}
}
