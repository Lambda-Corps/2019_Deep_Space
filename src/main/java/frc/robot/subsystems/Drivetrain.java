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

	//Motion Magic values
	/*
	kF calculation:
	kF = full forward value * duty-cycle (%) / runtime calculated target (ticks, velocity units/100 ms)
	= 1023 * 100% / 1525 = 0.67081967213114754098360655737705
	(1525 determined through PhoenixTuner self-test)
	*/
	private static final double kF = 0.67081967213114754098360655737705;	private static final double kP = 0;
	private static final double kI = 0;
	private static final double kD = 0;
	
	private static final int kPIDLoopIdx = 0;
	private static final int kTimeoutMs = 0;
	private static final int kSlotIdx = 0;
	
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

		// Set up Motion Magic (TODO: do we need to apply this to followers as well?)
		//set feedback sensor type - commented, we set it to quad encoder later
		// left_motor_master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
		// right_motor_master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
		//nominal output forward (0)
		left_motor_master.configNominalOutputForward(0, kTimeoutMs);
		right_motor_master.configNominalOutputForward(0, kTimeoutMs);
		//nominal output reverse (0)
		left_motor_master.configNominalOutputReverse(0, kTimeoutMs);
		right_motor_master.configNominalOutputReverse(0, kTimeoutMs);
		//peak output forward (1)
		left_motor_master.configPeakOutputForward(1, kTimeoutMs);
		right_motor_master.configPeakOutputForward(1, kTimeoutMs);
		//peak output reverse (-1)
		left_motor_master.configPeakOutputReverse(-1, kTimeoutMs);
		right_motor_master.configPeakOutputReverse(-1, kTimeoutMs);
		//select profile slot
		left_motor_master.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		right_motor_master.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		//config pidf values
		left_motor_master.config_kF(kSlotIdx, kF, kTimeoutMs);
		left_motor_master.config_kP(kSlotIdx, kP, kTimeoutMs);
		left_motor_master.config_kI(kSlotIdx, kI, kTimeoutMs);
		left_motor_master.config_kD(kSlotIdx, kD, kTimeoutMs);
		right_motor_master.config_kF(kSlotIdx, kF, kTimeoutMs);
		right_motor_master.config_kP(kSlotIdx, kP, kTimeoutMs);
		right_motor_master.config_kI(kSlotIdx, kI, kTimeoutMs);
    	right_motor_master.config_kD(kSlotIdx, kD, kTimeoutMs);
		//config cruise velocity, acceleration
    	left_motor_master.configMotionCruiseVelocity(1512, kTimeoutMs);  //determined with PhoenixTuner, for motor output 99.22%
		left_motor_master.configMotionAcceleration(756, kTimeoutMs);  //cruise velocity / 2, so it will take 2 seconds to reach cruise velocity
    	right_motor_master.configMotionCruiseVelocity(1512, kTimeoutMs);  //determined with PhoenixTuner, for motor output 99.22%
		right_motor_master.configMotionAcceleration(756, kTimeoutMs);  //cruise velocity / 2, so it will take 2 seconds to reach cruise velocity
    
		//reset sensors
		left_motor_master.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
		right_motor_master.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
		
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

		System.out.println("L: " + left_speed + ", R: " + right_speed);

		left_motor_master.set(ControlMode.PercentOutput, left_speed);
		right_motor_master.set(ControlMode.PercentOutput, right_speed);
	}

	public void motionMagicDrive(double targetPos) {
		left_motor_master.set(ControlMode.MotionMagic, targetPos);
		right_motor_master.set(ControlMode.MotionMagic, targetPos);		
	}

	public boolean motionMagicOnTarget(){
		double tolerance = 1.0;
		return (left_motor_master.getClosedLoopError()<=tolerance);

		
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

	// ==FOR PID DRIVING========================================================================================

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
