/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Climber extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public TalonSRX climberMotor;
  public DoubleSolenoid climberSolenoidFront;
  public DoubleSolenoid climberSolenoidRear;
  public AnalogInput climberRangefinder;

  public Climber() {
    climberMotor = new TalonSRX(RobotMap.CLIMBER_TALON);
    climberSolenoidFront = new DoubleSolenoid(RobotMap.CLIMBER_DOUBLE_SOLENOID_1_PORT_A,
        RobotMap.CLIMBER_DOUBLE_SOLENOID_1_PORT_B);
    climberSolenoidRear = new DoubleSolenoid(RobotMap.CLIMBER_DOUBLE_SOLENOID_2_PORT_A,
        RobotMap.CLIMBER_DOUBLE_SOLENOID_2_PORT_B);
    climberRangefinder = new AnalogInput(RobotMap.CLIMBER_RANGEFINDER);

  }

  // TODO -- This will not work, needs to be an ultrasonic linear voltage calculation
  public double getclimberRangefinder() {
    double outputValue = climberRangefinder.getAverageVoltage();
    if (outputValue > 2.4 || outputValue < 0.4) { // code currently only accurate from 0.4-2.4 volts
      return -1;
    }
    double voltage = Math.pow(outputValue, -1.16);
    double coefficient = 10.298;
    double d = voltage * coefficient;
    return d;
  }

  public void setMotor(double speed) {
    climberMotor.set(ControlMode.PercentOutput, speed);
  }

  public void extendFrontSolenoid() {
    climberSolenoidFront.set(DoubleSolenoid.Value.kForward);
  }

  public void extendRearSolenoid() {
    climberSolenoidRear.set(DoubleSolenoid.Value.kForward);
  }

  public void retractFrontSolenoid() {
    climberSolenoidFront.set(DoubleSolenoid.Value.kReverse);
  }

  public void retractRearSolenoid() {
    climberSolenoidRear.set(DoubleSolenoid.Value.kReverse);

  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
