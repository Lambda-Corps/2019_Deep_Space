// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.arm.DefaultArm;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class Arm extends Subsystem {

    // arm encoder positions
    public static final int ARM_POSITION_ZERO = 0;
    public static final int ARM_POSITION_PICKUP_CARGO = 5000;
    public static final int ARM_POSITION_SCORING_CARGO = 150;
    public static final int ARM_POSITION_CLIMB = 5010;
    public static final int ARM_POSITION_LOW_ROCKET = 0; //MEASURE
    public static final int ARM_POSITION_MID_ROCKET = 0; //MEASURE

    public static final double MMFEEDFORWARD = 0.07;

    public static final double kF = 1.5;
    private static double kP = 0.25;
    private static double kI = 0;
    private static double kD = 25;

    // /*
    // * kF = full forward value * duty-cycle (%) / runtime calculated target
    // (ticks,
    // * velocity units/100 ms) = 1023 * 100% / 1525 =
    // *
    // * ARM UP
    // * kF = 1023 * 0.5 / (250.5) = 2.0419161676646706586826347305389
    // *
    // * ARM DOWN kF =
    // *
    // */

    private static int kTimeoutMs = 5;

    private TalonSRX armMotor;
    private AnalogInput absoluteEncoder;
    private DigitalInput reverseLimitSwitch;

    // softlimits
    private final int ARM_TALON_REVERSE_SOFT_LIMIT = 0;
    private final int ARM_TALON_FORWARD_SOFT_LIMIT = ARM_POSITION_CLIMB;

    private final double ARM_SPEED_MAX_VALUE = 0.25;
    // upper 4.3652
    // lower 0.91308

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Arm() {

        reverseLimitSwitch = new DigitalInput(RobotMap.ARM_REVERSE_LIMIT_SWITCH);

        armMotor = new TalonSRX(RobotMap.ARM_TALON);
        armMotor.configFactoryDefault();
        armMotor.setInverted(true);

        absoluteEncoder = new AnalogInput(RobotMap.ARM_ABSOLUTE_ENCODER);

        armMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        armMotor.configForwardSoftLimitThreshold(ARM_TALON_FORWARD_SOFT_LIMIT);
        armMotor.configReverseSoftLimitThreshold(ARM_TALON_REVERSE_SOFT_LIMIT);
        armMotor.configForwardSoftLimitEnable(true);
        armMotor.configReverseSoftLimitEnable(true);

        armMotor.configMotionCruiseVelocity(251, kTimeoutMs); // determined with PhoenixTuner, for motor output 99.22%
        armMotor.configMotionAcceleration(1004, kTimeoutMs); // cruise velocity / 2, so it will take 2 seconds to reach

        armMotor.configPeakCurrentLimit(0);
        armMotor.configContinuousCurrentLimit(2);

        armMotor.config_kP(0, kP, 0); 
        armMotor.config_kI(0, kI, 0); 
        armMotor.config_kD(0, kD, 0); 
        armMotor.config_kF(0, kF, 0);
        
        armMotor.configAllowableClosedloopError(0, 10, kTimeoutMs);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new DefaultArm());
    }

    @Override
    public void periodic() {
        if (getArmLimit()) {
            armMotor.setSelectedSensorPosition(0);
        }
    }

    public void setMotor(double speed) {

        if (Math.abs(speed) > ARM_SPEED_MAX_VALUE) {
            if (speed < 0) {
                speed = -ARM_SPEED_MAX_VALUE;
            } else {
                speed = ARM_SPEED_MAX_VALUE;
            }
        }
        armMotor.set(ControlMode.PercentOutput, speed);

    }

    public boolean getArmLimit() {
        return !reverseLimitSwitch.get();
    }

    public void configStart_MM(double targetPos) {
        // if (targetPos > getRelativeEncoder()) {
            // forward slot
            armMotor.selectProfileSlot(0, 0);
            // armMotor.config_kP(0, kP, 0); // find values
            // armMotor.config_kI(0, kI, 0); // find values
            // armMotor.config_kD(0, kD, 0); // find values
            // armMotor.config_kF(0, kF, 0); // find values
            // armMotor.config_kF(0, 1, 0); // find values - auxiliary feed forward
        // } else {
        //     // backward slot
        //     armMotor.selectProfileSlot(1, 0);
        //     // armMotor.config_kP(1, kP, 0); // find values
        //     // armMotor.config_kI(1, kI, 0); // find values
        //     // armMotor.config_kD(1, kD, 0); // find values
        //     // armMotor.config_kF(1, kF, 0); // find values
        //     // armMotor.config_kF(1, 1, 0); // find values - auxiliary feed forward
        // }
    }

    public void move_MM(int targetPos) {

        switch(targetPos){
            case ARM_POSITION_ZERO:
            case ARM_POSITION_PICKUP_CARGO:
            case ARM_POSITION_CLIMB:
                // We don't need arbitrary feed forward to maintain the arm position if 
                // we are in the zero or the cargo pickup positions
                armMotor.set(ControlMode.MotionMagic, targetPos);
                break;
            case ARM_POSITION_SCORING_CARGO:
                armMotor.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, MMFEEDFORWARD);
                break;
            default:
                // This shouldn't happen, if so we should just keep the motors off
                break;
        }
    }

    public boolean onTarget_MM(double targetPos) {
        double tolerance = 10;

        double currentPos = armMotor.getSelectedSensorPosition();

        return Math.abs(currentPos - targetPos) < tolerance;

    }

    public double getArmPosition(){
        return armMotor.getSelectedSensorPosition();
    }

    public double getArmCurrent(){
        return armMotor.getOutputCurrent();
    }

    public double getRelativeEncoder() {
        return armMotor.getSelectedSensorPosition(0);
    }

    public double getAbsoluteEncoder() {
        return absoluteEncoder.getAverageVoltage();
    }

}
