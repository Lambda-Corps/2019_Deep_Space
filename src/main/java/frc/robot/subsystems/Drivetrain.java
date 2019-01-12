package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.DefaultDriveCommand;
import frc.robot.oi.F310;


import com.ctre.phoenix.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Changelog: 
 * 
 */

public class Drivetrain extends Subsystem {

	// Instance variables. There should only be one instance of Drivetrain, but
	// we are
	// assuming the programmer will not accidently create multiple instances

	// Create WPI_TalonSRXs here so we can access them in the future, if we need to
	private WPI_TalonSRX left_motor1;
	private WPI_TalonSRX left_motor2;
	// private WPI_TalonSRXleft_motor3;
	private WPI_TalonSRX right_motor1;
	private WPI_TalonSRX right_motor2;
	// private WPI_TalonSRXright_motor3;

	private static final double TALON_RAMP_RATE = 48.0;

	// The two motors mounted as a mirror to one another do not output the
	// exact same force. This value will modify the the dominant side to
	// help the robot drive straight
	private static final double TANK_DRIVE_SCALAR = .94;

	// Motorgroups
	private MotorGroup<WPI_TalonSRX> left_motorgroup;
	private MotorGroup<WPI_TalonSRX> right_motorgroup;


	private boolean manualOverride = false;
	private boolean teleopEnabled = false;
	

	// Instantiate all of the variables, and add the motors to their respective
	// MotorGroup.
	public Drivetrain() {

	
		left_motor1 = new WPI_TalonSRX(RobotMap.LEFT_MOTOR1_PORT);
		left_motor2 = new WPI_TalonSRX(RobotMap.LEFT_MOTOR2_PORT);
	
		right_motor1 = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR1_PORT);
		right_motor2 = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR2_PORT);
		left_motorgroup = new MotorGroup<WPI_TalonSRX>(left_motor1, left_motor2);
		right_motorgroup = new MotorGroup<WPI_TalonSRX>(right_motor1, right_motor2);

	}


	// ==FOR TELE-OP
	// DRIVING=======================================================================================
	// For: DefaultDrive Command
	// Sensors: None
	// Description: A basic tank drive method. The two parameters are expected
	// to be within the range -1.0 to 1.0
	// If not, they are limited to be within that range. The parameters will set
	// their respective
	// side of robot to the given value.
	public void tankDrive(double left, double right) {
		if (left > 1.0)
			left = 1.0;
		if (left < -1.0)
			left = -1.0;
		if (right > 1.0)
			right = 1.0;
		if (right < -1.0)
			right = -1.0;
		left_motorgroup.set(left);
		right_motorgroup.set(-right);

		// Check to see if gear shifting is necessary. if it is, then shift
		// shiftGears();
	}

	// For: DefaultDrive Command
	// Sensors: None
	// Description: A basic arcade drive method. The two parameters are expected
	// to be within the range -1.0 to 1.0
	// If not, they are limited to be within that range. The transitional speed
	// and yaw are combined
	// to be applied to the left motor and right motor. Trans_speed
	// (transitional velocity) will
	// set the robot's forward speed, and yaw (angular velocity) will set the
	// robot turning. Having a
	// combination of the two will make the robot drive on an arc.
	public void arcadeDrive(double trans_speed, double yaw) {
		// Currently, when trying to turn, the left and right turning functions
		// are backward, so I'm
		// going to invert them.
		yaw *= -1.0;
		trans_speed *= -1.0;
		// If yaw is at full, and transitional is at 0, then we want motors to
		// go different speeds.
		// Since motors physically are turned around, then setting both motors
		// to the same speed
		// will have this effect. If the transitional is at full and yaw at 0,
		// then motors need to
		// go the same direction, so one is a minus to cancel the effect of
		// mirrored motors.
		double left_speed = yaw - trans_speed;
		double right_speed = yaw + trans_speed;

		// This determines the variable with the greatest magnitude. If the
		// magnitude
		// is greater than 1.0, than divide each variable by the largest so that
		// the largest is 1.0 (or -1.0), and that all other variables are
		// less than that.
		double max_speed = Math.max(Math.abs(left_speed), Math.abs(right_speed));
		if (Math.abs(max_speed) > 1.0) {
			left_speed /= max_speed;
			right_speed /= max_speed;
		}
		
		
		
		//original code ***********
		left_motorgroup.set(left_speed);
		right_motorgroup.set(right_speed);
	}

	// ==FOR PID
	// DRIVING========================================================================================

	/**
	 * 
	 * 
	 * @param brake
	 *            - whether to set to brake (true) mode or coast (false)
	 */
	/*public void setBrake(boolean brake) {
		left_motorgroup.enableBrake(brake);
		right_motorgroup.enableBrake(brake);
	}*/

	public void setRobotTeleop(boolean teleopEnabled) {
		this.teleopEnabled = teleopEnabled;
	}

	// ==DEFAULT COMMAND AND MOTOR GROUPS
	// CLASS=================================================================
	public void initDefaultCommand() {
		// Allows for tele-op driving in arcade or tank drive
		setDefaultCommand(new DefaultDriveCommand());
	}

	// ==METHODS FOR ACCESSING VALUES AND TESTING
	// THINGS========================================================
	

	// "Thar be dragons when motors on the same gearbox are set differently"
	// (Scott 2017), so
	// a MotorGroup will handle setting multiple motors to the same value as if
	// it were one motor.
	private class MotorGroup<T extends SpeedController> {

		// Create a list of type T, which of type SpeedController. All of the
		// motors assigned to this instance of MotorGroup will be held in
		// this list.
		private List<T> list = new ArrayList<T>();

		// Add a variable number of SpeedControllers, since the gearbox can
		// either have two or
		// three motors attached. For every element in parameter list, add it to
		// list.
		private MotorGroup(T... t) {
			for (T i : t) {
				list.add(i);
			}
		}

		// This method calls the set() method for all SpeedControllers in list.
		// This is better to
		// call because it is impossible to set motors to different values.
		// Other than this, it
		// acts like a normal set method of SpeedController.
		private void set(double v) {
			for (T i : list) {
				i.set(v);
			}
		}
		
		//private void enableBrake(boolean toBrake) {
		//	for (T i : list) {
		//		((WPI_TalonSRX) i).setBrakeMode(toBrake);
		//	}
		//}
	}
}
