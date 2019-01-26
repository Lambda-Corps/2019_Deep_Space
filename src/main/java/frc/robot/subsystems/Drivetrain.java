package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.DefaultDriveCommand;

/**
 * Changelog:
 * 
 */

public class Drivetrain extends Subsystem {

	// Instance variables. There should only be one instance of Drivetrain, but
	// we are
	// assuming the programmer will not accidently create multiple instances

	// Create WPI_TalonSRXs here so we can access them in the future, if we need to
	private TalonSRX left_motor_master;
	private TalonSRX left_motor_slave;
	// private WPI_TalonSRXleft_motor3;
	private TalonSRX right_motor_master;
	private TalonSRX right_motor_slave;
	// private WPI_TalonSRXright_motor3;

	//sensors
	private Encoder l_encoder;
	private Encoder r_encoder;

	private static final double TALON_RAMP_RATE = 48.0;

	// The two motors mounted as a mirror to one another do not output the
	// exact same force. This value will modify the the dominant side to
	// help the robot drive straight
	private static final double TANK_DRIVE_SCALAR = .94;


	private boolean manualOverride = false;
	private boolean teleopEnabled = false;
	
	// Gyro
	private AHRS ahrs;

	// Instantiate all of the variables, and add the motors to their respective
	public Drivetrain() {

	
		left_motor_master = new TalonSRX(RobotMap.LEFT_TALON_MASTER);
		left_motor_slave = new TalonSRX(RobotMap.LEFT_TALON_FOLLOWER);
	
		right_motor_master = new TalonSRX(RobotMap.RIGHT_TALON_MASTER);
		right_motor_slave = new TalonSRX(RobotMap.RIGHT_TALON_FOLLOWER);

		left_motor_master.setInverted(true);
		left_motor_slave.setInverted(true);

		right_motor_master.setSensorPhase(true);
		
		left_motor_slave.follow(left_motor_master);
		right_motor_slave.follow(right_motor_master);

		// l_encoder = new Encoder(RobotMap.LEFT_ENCODER_PORT1, RobotMap.LEFT_ENCODER_PORT2);
		// r_encoder = new Encoder(RobotMap.RIGHT_ENCODER_PORT1, RobotMap.RIGHT_ENCODER_PORT2);

		// Print Talon Encoder Values
		left_motor_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
		right_motor_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);


		// analog sensors
		try {
			/* Communicate w/navX-MXP via the MXP SPI Bus. */
			/*
			 * Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB
			 */
			/*
			 * See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for
			 * details.
			 */
			ahrs = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX-MXP: " + ex.getMessage(), true);
		}
		resetAHRSGyro();
		resetTalonEncoders();
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
		// If yaw is at full, and transitional is at 0, then we want motors to
		// go different speeds.
		// Since motors physically are turned around, then setting both motors
		// to the same speed
		// will have this effect. If the transitional is at full and yaw at 0,
		// then motors need to
		// go the same direction, so one is a minus to cancel the effect of
		// mirrored motors.
		// double left_speed = trans_speed - yaw;
		// double right_speed = yaw + trans_speed;

		trans_speed = normalize(trans_speed);
		yaw = normalize(yaw);

		double left_speed;
		double right_speed;

		// This determines the variable with the greatest magnitude. If the
		// magnitude
		// is greater than 1.0, then divide each variable by the largest so that
		// the largest is 1.0 (or -1.0), and that all other variables are
		// less than that.

		double maxInput = Math.copySign(Math.max(Math.abs(trans_speed), Math.abs(yaw)), trans_speed);

		if(trans_speed>=0.0){
			//Forward
			if(yaw>=0.0){
				//Fwd, Right
				left_speed = maxInput;
				right_speed = trans_speed - yaw;
			} else {
				left_speed = trans_speed + yaw;
				right_speed = maxInput;
			}
		} else {
			//Backward
			if(yaw>=0.0){
				//Bwd
				left_speed = trans_speed + yaw;
				right_speed = maxInput;
			} else {
				left_speed = maxInput;
				right_speed = trans_speed - yaw;
			}
		}

		//System.out.println("L: " + left_speed + ", R: " + right_speed);

		left_motor_master.set(ControlMode.PercentOutput, left_speed);
		right_motor_master.set(ControlMode.PercentOutput, right_speed);
	}

	public double normalize(double value){
		if(value>1.0){
			value = 1.0;
		} else if(value<-1.0){
			value = -1.0;
		}
		if(value>-0.01&&value<0.01){
			value = 0.0;
		}
		return value;
	}

	// ==FOR PID
	// DRIVING========================================================================================

	/**
	 * 
	 * 
	 * @param brake
	 *            - whether to set to brake (true) mode or coast (false)
	 */


	public void setRobotTeleop(boolean teleopEnabled) {
		this.teleopEnabled = teleopEnabled;
	}

// Encoders read by the Talons
public int ReadLeftEncoder()
{
	return left_motor_master.getSelectedSensorPosition(0);
};

public int ReadRightEncoder()
{
	return right_motor_master.getSelectedSensorPosition(0);
};
	public void resetLeftTalonEncoder(){
		left_motor_master.setSelectedSensorPosition(0, 0, 0);
	}
	public void resetRightTalonEncoder(){
		right_motor_master.setSelectedSensorPosition(0, 0, 0);
	}
	public void resetTalonEncoders(){
		resetLeftTalonEncoder();
		resetRightTalonEncoder();
	}
	// ==Gyro
	// Code====================================================================================
	public double getAHRSGyroAngle() {
		return ahrs.getAngle();
	}

	public void resetAHRSGyro() {
		ahrs.reset();
	}

	public void setAHRSAdjustment(double adj) {
		ahrs.setAngleAdjustment(adj);
	}
	// ==DEFAULT COMMAND AND MOTOR GROUPS
	// CLASS=================================================================
	public void initDefaultCommand() {
		// Allows for tele-op driving in arcade or tank drive
		setDefaultCommand(new DefaultDriveCommand());
	}

	// ==METHODS FOR ACCESSING VALUES AND TESTING
	// THINGS========================================================
	

}
