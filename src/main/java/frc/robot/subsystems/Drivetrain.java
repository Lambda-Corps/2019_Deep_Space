package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.DefaultDriveCommand;
import frc.robot.commands.vision.GetTargetCommand;

/**
 * Changelog:
 * 
 */

public class Drivetrain extends Subsystem {
	// Class constants 
	private final double CONTROLLER_DEADBAND = .1;
	// Instance variables. There should only be one instance of Drivetrain, but
	// we are assuming the programmer will not accidently create multiple instances

	// Create WPI_TalonSRXs here so we can access them in the future, if we need to
	private TalonSRX left_motor_master;
	private TalonSRX left_motor_slave;
	private TalonSRX right_motor_master;
	private TalonSRX right_motor_slave;

	
	// Gyro, accelerometer
	private AHRS ahrs;

	// Instantiate all of the variables, and add the motors to their respective
	public Drivetrain() {

		// Instantiate the Talons, make sure they start with a clean configuration, then 
		// configure our DriveTrain objects
		left_motor_master = new TalonSRX(RobotMap.LEFT_TALON_MASTER);
		left_motor_master.configFactoryDefault();
		left_motor_slave = new TalonSRX(RobotMap.LEFT_TALON_FOLLOWER);
		left_motor_slave.configFactoryDefault();
		right_motor_master = new TalonSRX(RobotMap.RIGHT_TALON_MASTER);
		right_motor_master.configFactoryDefault();
		right_motor_slave = new TalonSRX(RobotMap.RIGHT_TALON_FOLLOWER);
		right_motor_slave.configFactoryDefault();

		// Using the Phoenix Tuner we observed the left side motors need to be inverted
		// in order to be in phase
		left_motor_master.setInverted(true);
		
		// Set the followers to follow the inversion setting of their masters
		left_motor_slave.setInverted(InvertType.FollowMaster);
		right_motor_slave.setInverted(InvertType.FollowMaster);

		// Reverse the right side encoder to be in phase with the motors
		right_motor_master.setSensorPhase(true);
		
		left_motor_slave.follow(left_motor_master);
		right_motor_slave.follow(right_motor_master);

		// Set the quadrature encoders to be the source feedback device for the talons
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

		// Reset the Encoders for the Talons as well as Zero the Gyro so we start fresh with our 
		// sensor data in the robot
		left_motor_master.setSelectedSensorPosition(0);
		right_motor_master.setSelectedSensorPosition(0);
		
		ahrs.reset();
	}
	
	// ==FOR TELE-OP DRIVING=================================================================
	// For: DefaultDrive Command
	// Sensors: None
	// Description: A basic arcade drive method. The two parameters are expected
	// to be within the range -1.0 to 1.0 If not, they are limited to be within 
	// that range. The transitional speed and yaw are combined to be applied to 
	// the left motor and right motor. Trans_speed (transitional velocity) will
	// set the robot's forward speed, and yaw (angular velocity) will set the
	// robot turning. Having a combination of the two will make the robot 
	// drive on an arc.
	public void arcadeDrive(double trans_speed, double yaw) {
		// If yaw is at full, and transitional is at 0, then we want motors to
		// go in opposite directions (rotate on the Z axis).
		//If the transitional is at full and yaw at 0, then motors need to
		// go the same direction.

		// Normalizing the inputs makes sure they are within the range of -1.0 - 1.0 
		// as well as removes deadband from the controller sticks.
		trans_speed = normalize(trans_speed);
		yaw = normalize(yaw);

		double left_speed;
		double right_speed;

		// This determines the variable with the greatest magnitude. If the
		// magnitude favoring the trans speed.  Meaning, if they are both set to 100%
		// we will take the trans speed as the max input.

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

		left_motor_master.set(ControlMode.PercentOutput, left_speed);
		right_motor_master.set(ControlMode.PercentOutput, right_speed);
	}

	public double normalize(double value){
		if(value>1.0){
			value = 1.0;
		} else if(value<-1.0){
			value = -1.0;
		}
		if(value > -CONTROLLER_DEADBAND && value < CONTROLLER_DEADBAND){
			value = 0.0;
		}
		return value;
	}

	// ==FOR PID
	// DRIVING========================================================================================

	// Encoders read by the Talons
	public int readLeftEncoder()
	{
		return left_motor_master.getSelectedSensorPosition(0);
	}

	public int readRightEncoder()
	{
		return right_motor_master.getSelectedSensorPosition(0);
	}
	
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
		//setDefaultCommand(new GetTargetCommand());
	}

}
