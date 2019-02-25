package frc.robot.oi;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.RobotMap;
import frc.robot.commands.arm.ArmCancelOperations;
import frc.robot.commands.arm.ArmSetPositionMM;
import frc.robot.commands.hatch.DeployHatch;
import frc.robot.commands.hatch.DriveHatchToLimit;
import frc.robot.commands.hatch.RetractHatch;
import frc.robot.commands.intake.DeployCargoManual;
import frc.robot.commands.intake.GrabCargoGroup;
import frc.robot.commands.intake.IntakeCancelOperations;
import frc.robot.commands.vision.DriveToTargetGroup;
import frc.robot.commands.vision.PivotToTargetAuto;
import frc.robot.commands.vision.SetStreamMode;
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
	// driver remote
	public JoystickButton driver_A;
	public JoystickButton driverB;
	public JoystickButton driverX;
	public JoystickButton driverY;
	public JoystickButton driverRT;
	public JoystickButton driverLT;
	public JoystickButton driverRB;
	public JoystickButton driverLB;

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

		// DRIVER CONTROLS//
		// Vision
		driver_A = new JoystickButton(driverRemote, F310.LB);
		driver_A.whileHeld(new PivotToTargetAuto());

		driverY = new JoystickButton(driverRemote, F310.Y);
		driverY.whenPressed(new SetStreamMode());

		driverRB = new JoystickButton(driverRemote, F310.RB);
		driverRB.whileHeld(new DriveToTargetGroup());

		// lBumper_J1 = new JoystickButton(driverRemote, F310.LB);
		// lBumper_J1.whenPressed(new SwitchPipelines(Robot.vision.OrangeBallPipeline));


		//PARTNER CONTROLS
		//Cargo Management
		partnerLB = new JoystickButton(partnerRemote, F310.LB);
		partnerLB.whenPressed(new GrabCargoGroup());

		partnerRB = new JoystickButton(partnerRemote, F310.RB);
		partnerRB.whenPressed(new DeployCargoManual());

		partnerStart = new JoystickButton(partnerRemote, F310.START);
		partnerStart.whenPressed(new IntakeCancelOperations());


		//Arm Positioning
		partnerB = new JoystickButton(partnerRemote, F310.B);
		partnerB.whenPressed(new ArmSetPositionMM(Arm.ARM_POSITION_SCORING_CARGO));

		partnerA = new JoystickButton(partnerRemote, F310.A);
		partnerA.whenPressed(new ArmSetPositionMM(Arm.ARM_POSITION_PICKUP_CARGO));

		partnerBack = new JoystickButton(partnerRemote, F310.BACK);
		partnerBack.whenPressed(new ArmCancelOperations());

		//save x for rocket positioning?

		//Hatch Management
		partnerL_AXIS = new JoystickButton(partnerRemote, F310.L_AXIS_PRESS);
		partnerL_AXIS.whenPressed(new RetractHatch());

		partnerR_AXIS = new JoystickButton(partnerRemote, F310.R_AXIS_PRESS);
		partnerR_AXIS.whenPressed(new DeployHatch());
		
		partnerY = new JoystickButton(partnerRemote, F310.Y);
		partnerY.whenPressed(new DriveHatchToLimit());


	}
	public double getGainOI() {
		return F310.getGain();
	}
}
