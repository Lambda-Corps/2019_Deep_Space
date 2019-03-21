package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.DefaultDriveCommand;

/**
 * Changelog:
 * 
 */

public class Drivetrain extends Subsystem {
	// Class constants
	private final double CONTROLLER_DEADBAND = .1;
	// private final double OPEN_LOOP_RAMP_RATE = 0.45;
	private final double OPEN_LOOP_RAMP_RATE = 0;
	private final double OPEN_LOOP_PEAK_OUTPUT_F = 1.0;
	private final double OPEN_LOOP_PEAK_OUTPUT_B = -1.0;// Remember to make this negative

	// Instance variables. There should only be one instance of Drivetrain, but
	// we are assuming the programmer will not accidently create multiple instances

	private TalonSRX left_motor_master;
	private TalonSRX left_motor_slave;
	private TalonSRX right_motor_master;
	private TalonSRX right_motor_slave;

	// Motion Magic values
	/*
	 * kF calculation: kF = full forward value * duty-cycle (%) / runtime calculated
	 * target (ticks, velocity units/100 ms) = 1023 * 100% / 1525 =
	 * 0.67081967213114754098360655737705 (1525 determined through PhoenixTuner
	 * self-test)
	 */
	private static double kF = 0.67081967213114754098360655737705;
	private static double kP_drive = 5; // determined via pid tuning
	private static double kI = 0;
	private static double kD = 0;

	// private static double kP_turn = 10;
	private static double kP_turn = 5;// for steamworks

	private static int kPIDLoopIdx = 0;
	private static int kTimeoutMs = 5;
	private static int kSlotIdx = 0;

	// Gyro, accelerometer
	private AHRS ahrs;

	// Solenoids
	private DoubleSolenoid transmissionSolenoid;
	private int counter;

	private double m_quickStopAccumulator;
	public static final double kDefaultQuickStopAlpha = 0.1;

	private int lowGearCount;
	private int highGearCount;

	// Instantiate all of the variables, and add the motors to their respective
	public Drivetrain() {

		// solenoid1 = new DoubleSolenoid(RobotMap.DRIVETRAIN_GEAR_PORT_A,
		// RobotMap.DRIVETRAIN_GEAR_PORT_B);
		counter = 0;
		lowGearCount = 0;
		highGearCount = 0;
		transmissionSolenoid = new DoubleSolenoid(RobotMap.DRIVETRAIN_SOLENOID_PORT_A,
				RobotMap.DRIVETRAIN_SOLENOID_PORT_B);

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

		left_motor_master.setNeutralMode(NeutralMode.Brake);
		right_motor_master.setNeutralMode(NeutralMode.Brake);
		left_motor_slave.setNeutralMode(NeutralMode.Brake);
		right_motor_slave.setNeutralMode(NeutralMode.Brake);

		// left_motor_master.configOpenloopRamp(OPEN_LOOP_RAMP_RATE, 0);
		// right_motor_master.configOpenloopRamp(OPEN_LOOP_RAMP_RATE, 0);
		// left_motor_slave.configOpenloopRamp(OPEN_LOOP_RAMP_RATE, 0);
		// right_motor_slave.configOpenloopRamp(OPEN_LOOP_RAMP_RATE, 0);

		// left_motor_master.configClosedloopRamp(0.1);
		// right_motor_master.configClosedloopRamp(0.1);
		// left_motor_slave.configClosedloopRamp(0.1);
		// right_motor_slave.configClosedloopRamp(0.1);

		// Using the Phoenix Tuner we observed the left side motors need to be inverted
		// in order to be in phase
		left_motor_master.setInverted(true);

		// Set the followers to follow the inversion setting of their masters
		left_motor_slave.setInverted(InvertType.FollowMaster);
		right_motor_slave.setInverted(InvertType.FollowMaster);

		// Reverse the right side encoder to be in phase with the motors
		right_motor_master.setSensorPhase(true);

		// Set up Motion Magic
		// nominal output forward (0)
		left_motor_master.configNominalOutputForward(0, kTimeoutMs);
		right_motor_master.configNominalOutputForward(0, kTimeoutMs);
		// nominal output reverse (0)
		left_motor_master.configNominalOutputReverse(0, kTimeoutMs);
		right_motor_master.configNominalOutputReverse(0, kTimeoutMs);
		// peak output forward (1)
		left_motor_master.configPeakOutputForward(OPEN_LOOP_PEAK_OUTPUT_F, kTimeoutMs);
		right_motor_master.configPeakOutputForward(OPEN_LOOP_PEAK_OUTPUT_F, kTimeoutMs);
		// peak output reverse (-1)
		left_motor_master.configPeakOutputReverse(OPEN_LOOP_PEAK_OUTPUT_B, kTimeoutMs);
		right_motor_master.configPeakOutputReverse(OPEN_LOOP_PEAK_OUTPUT_B, kTimeoutMs);
		// select profile slot
		left_motor_master.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		right_motor_master.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		// config pidf values
		left_motor_master.config_kF(kSlotIdx, kF, kTimeoutMs);
		left_motor_master.config_kP(kSlotIdx, kP_drive, kTimeoutMs);
		left_motor_master.config_kI(kSlotIdx, kI, kTimeoutMs);
		left_motor_master.config_kD(kSlotIdx, kD, kTimeoutMs);
		right_motor_master.config_kF(kSlotIdx, kF, kTimeoutMs);
		right_motor_master.config_kP(kSlotIdx, kP_drive, kTimeoutMs);
		right_motor_master.config_kI(kSlotIdx, kI, kTimeoutMs);
		right_motor_master.config_kD(kSlotIdx, kD, kTimeoutMs);
		// config cruise velocity, acceleration
		left_motor_master.configMotionCruiseVelocity(1512, kTimeoutMs); // determined with PhoenixTuner, for motor
																		// output 99.22%
		left_motor_master.configMotionAcceleration(756, kTimeoutMs); // cruise velocity / 2, so it will take 2 seconds
																		// to reach cruise velocity
		right_motor_master.configMotionCruiseVelocity(1512, kTimeoutMs); // determined with PhoenixTuner, for motor
																			// output 99.22%
		right_motor_master.configMotionAcceleration(756, kTimeoutMs); // cruise velocity / 2, so it will take 2 seconds
																		// to reach cruise velocity

		left_motor_master.configAllowableClosedloopError(0, 10, 3);
		right_motor_master.configAllowableClosedloopError(0, 10, 3);

		// Set the quadrature encoders to be the source feedback device for the talons
		left_motor_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		right_motor_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		// reset sensors
		left_motor_master.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
		right_motor_master.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);

		left_motor_slave.follow(left_motor_master);
		right_motor_slave.follow(right_motor_master);

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

		// Reset the Encoders for the Talons as well as Zero the Gyro so we start fresh
		// with our
		// sensor data in the robot
		left_motor_master.setSelectedSensorPosition(0);
		right_motor_master.setSelectedSensorPosition(0);

		ahrs.reset();

		// SmartDashboard.putNumber("HG scalar", 0.8);
	}

	public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn, boolean inTeleop) {

		xSpeed = normalize(xSpeed);

		if (inTeleop) {
			xSpeed = Math.pow(xSpeed, 3);
		}

		zRotation = normalize(zRotation);

		double angularPower;
		boolean overPower;

		overPower = false;
		angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

		if (m_quickStopAccumulator > 1) {
			m_quickStopAccumulator -= 1;
		} else if (m_quickStopAccumulator < -1) {
			m_quickStopAccumulator += 1;
		} else {
			m_quickStopAccumulator = 0.0;
		}

		double leftMotorOutput = xSpeed + angularPower;
		double rightMotorOutput = xSpeed - angularPower;

		// If rotation is overpowered, reduce both outputs to within acceptable range
		if (overPower) {
			if (leftMotorOutput > 1.0) {
				rightMotorOutput -= leftMotorOutput - 1.0;
				leftMotorOutput = 1.0;
			} else if (rightMotorOutput > 1.0) {
				leftMotorOutput -= rightMotorOutput - 1.0;
				rightMotorOutput = 1.0;
			} else if (leftMotorOutput < -1.0) {
				rightMotorOutput -= leftMotorOutput + 1.0;
				leftMotorOutput = -1.0;
			} else if (rightMotorOutput < -1.0) {
				leftMotorOutput -= rightMotorOutput + 1.0;
				rightMotorOutput = -1.0;
			}
		}

		// Normalize the wheel speeds
		double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
		if (maxMagnitude > 1.0) {
			leftMotorOutput /= maxMagnitude;
			rightMotorOutput /= maxMagnitude;
		}

		// reduce speed by 0.8 if in high gear
		if (inTeleop && transmissionSolenoid.get() == Value.kForward) {
			leftMotorOutput *= 0.8;
			rightMotorOutput *= 0.8;
			// leftMotorOutput*=SmartDashboard.getNumber("HG scalar", 0.8);
			// rightMotorOutput*=SmartDashboard.getNumber("HG scalar", 0.8);
		}

		if (xSpeed > 0.0) { // forward
			left_motor_master.set(ControlMode.PercentOutput, 0.93*leftMotorOutput);
			right_motor_master.set(ControlMode.PercentOutput, rightMotorOutput);
		} else { // backward
			left_motor_master.set(ControlMode.PercentOutput, 0.965*leftMotorOutput);
			right_motor_master.set(ControlMode.PercentOutput, rightMotorOutput);
		}

		shiftGears();

	}

	public boolean inHigh() {
		return transmissionSolenoid.get() == Value.kForward;
	}

	// ==FOR TELE-OP
	// DRIVING=================================================================
	// For: DefaultDrive Command
	// Sensors: None
	// Description: A basic arcade drive method. The two parameters are expected
	// to be within the range -1.0 to 1.0 If not, they are limited to be within
	// that range. The transitional speed and yaw are combined to be applied to
	// the left motor and right motor. Trans_speed (transitional velocity) will
	// set the robot's forward speed, and yaw (angular velocity) will set the
	// robot turning. Having a combination of the two will make the robot
	// drive on an arc.
	public void arcadeDrive(double trans_speed, double yaw, boolean cubeInputs) {
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

		if (cubeInputs) {
			trans_speed = Math.pow(trans_speed, 3);
		}

		// if(cubeInputs){
		// if(trans_speed<0){
		// trans_speed *= -trans_speed;
		// } else {
		// trans_speed *= trans_speed;
		// }
		// if(yaw<0){
		// yaw *= -yaw;
		// } else {
		// yaw *= yaw;
		// }
		// }

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

		if (trans_speed >= 0.0) {
			// Forward
			if (yaw >= 0.0) {
				// Fwd, Right
				left_speed = maxInput;
				right_speed = trans_speed - yaw;
			} else {
				left_speed = trans_speed + yaw;
				right_speed = maxInput;
			}
		} else {
			// Backward
			if (yaw >= 0.0) {
				// Bwd
				left_speed = trans_speed + yaw;
				right_speed = maxInput;
			} else {
				left_speed = maxInput;
				right_speed = trans_speed - yaw;
			}
		}

		// System.out.println("L: " + left_speed + ", R: " + right_speed);

		// System.out.println("LE: " + readLeftEncoder() + "RE: " + readRightEncoder());

		if (trans_speed > 0) { // forward
			left_motor_master.set(ControlMode.PercentOutput, 0.93*left_speed);
			right_motor_master.set(ControlMode.PercentOutput, right_speed);
		} else { // backward
			left_motor_master.set(ControlMode.PercentOutput, 0.965*left_speed);
			right_motor_master.set(ControlMode.PercentOutput, right_speed);
		}

	}

	public void motionMagicStartConfig_Drive() {
		left_motor_master.configPeakOutputForward(0.96, kTimeoutMs);
		left_motor_master.configPeakOutputReverse(-0.995, kTimeoutMs);
	}

	public void motionMagicEndConfig_Drive() {
		left_motor_master.configPeakOutputForward(1, kTimeoutMs);
		left_motor_master.configPeakOutputReverse(-1, kTimeoutMs);
	}

	public void motionMagicStartConfig_Turn() {
		left_motor_master.selectProfileSlot(1, 0);
		right_motor_master.selectProfileSlot(1, 0);
		left_motor_master.config_kP(1, kP_turn, kTimeoutMs);
		right_motor_master.config_kP(1, kP_turn, kTimeoutMs);
		resetAHRSGyro();
	}

	public void motionMagicEndConfig_Turn() {
		left_motor_master.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		right_motor_master.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
		left_motor_master.config_kP(kSlotIdx, kP_drive, kTimeoutMs);
		right_motor_master.config_kP(kSlotIdx, kP_drive, kTimeoutMs);
	}

	public void motionMagicDrive(double targetPos) {

		left_motor_master.set(ControlMode.MotionMagic, targetPos);
		right_motor_master.set(ControlMode.MotionMagic, targetPos);

	}

	public void motionMagicTurn(double arcLength) {

		left_motor_master.set(ControlMode.MotionMagic, arcLength);
		right_motor_master.set(ControlMode.MotionMagic, -arcLength);

		// System.out.println("motion magic-ing");
	}

	public boolean motionMagicOnTargetDrive(double target) {
		double tolerance = 10;

		double currentPos_L = left_motor_master.getSelectedSensorPosition();
		double currentPos_R = right_motor_master.getSelectedSensorPosition();

		return Math.abs(currentPos_L - target) < tolerance && (currentPos_R - target) < tolerance;
	}

	public boolean motionMagicOnTargetTurn(double arcLength) {
		// double tolerance = 10;
		double tolerance = 20; // STEAMWORKS

		double currentPos_L = left_motor_master.getSelectedSensorPosition();
		double currentPos_R = right_motor_master.getSelectedSensorPosition();

		return Math.abs(currentPos_L - arcLength) < tolerance && (currentPos_R + arcLength) < tolerance;
	}

	public double normalize(double value) {
		if (value > 1.0) {
			value = 1.0;
		} else if (value < -1.0) {
			value = -1.0;
		}
		if (value > -CONTROLLER_DEADBAND && value < CONTROLLER_DEADBAND) {
			value = 0.0;
		}
		return value;
	}

	// ==FOR PID
	// DRIVING========================================================================================

	// Encoders read by the Talons
	public double readLeftEncoder() {

		return left_motor_master.getSelectedSensorPosition(0);
	}

	public double readRightEncoder() {
		return right_motor_master.getSelectedSensorPosition(0);
	}

	public void resetLeftTalonEncoder() {
		left_motor_master.setSelectedSensorPosition(0, 0, 0);
	}

	public void resetRightTalonEncoder() {
		right_motor_master.setSelectedSensorPosition(0, 0, 0);
	}

	public void resetTalonEncoders() {
		resetLeftTalonEncoder();
		resetRightTalonEncoder();
	}

	public void shiftForward() {
		transmissionSolenoid.set(Value.kForward);
	}

	public void shiftReverse() {
		transmissionSolenoid.set(Value.kReverse);
	}

	public void shiftGears() {
		// max speed in low gear is 4.71ft/sec (56.52 inches/sec), max high gear is
		// 12.47 ft/sec
		double UPSHIFT_SPEED = 1055;
		double DOWNSHIFT_SPEED = 2100;

		double current_speed = Math.max(Math.abs(left_motor_master.getSelectedSensorVelocity()),
				Math.abs(right_motor_master.getSelectedSensorVelocity()));

		// double current_speed =
		// Math.abs(left_motor_master.getSelectedSensorVelocity());

		Value current_state = transmissionSolenoid.get();

		if (current_speed > UPSHIFT_SPEED && current_state == Value.kReverse) {
			// low gear -> high gear
			if (highGearCount++ == 10) {
				changeToHighGear();
				highGearCount = 0;
			}
		} else if (current_state == Value.kForward && current_speed < DOWNSHIFT_SPEED) {
			// high gear -> low gear
			if (lowGearCount++ == 5) {
				changeToLowGear();
				lowGearCount = 0;
			}
		}

		// if (transmissionSolenoid.get() == Value.kReverse) {
		// 	SmartDashboard.putBoolean("high gear?", true);
		// } else {
		// 	SmartDashboard.putBoolean("high gear?", false);
		// }

	}

	public void changeToLowGear() {
		transmissionSolenoid.set(Value.kReverse); // find direction
	}

	public void changeToHighGear() {
		transmissionSolenoid.set(Value.kForward); // find direction
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

}
