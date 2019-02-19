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
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Hatch extends Subsystem {
  private TalonSRX hatchMotor;
  private AnalogInput hatchRangefinder;
  private DoubleSolenoid hatchSolenoid;
  private DigitalInput hatchLimitSwitch;

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public static final double MOTOR_SPEED_UP = -0.93;
  public static final double MOTOR_SPEED_DOWN = 0.75;

public Hatch(){
  hatchMotor = new TalonSRX(RobotMap.HATCH_TALON);
  hatchRangefinder = new AnalogInput(RobotMap.HATCH_DISTANCE_FINDER);
  hatchSolenoid = new DoubleSolenoid(RobotMap.HATCH_SOLENOID_PORT_A, RobotMap.HATCH_SOLENOID_PORT_B);
  hatchLimitSwitch = new DigitalInput(RobotMap.HATCH_LIMIT_SWITCH);

}
public void driveMotor (double speed){
  hatchMotor.set(ControlMode.PercentOutput,speed);
}

public void deployHatch(){
  hatchSolenoid.set(DoubleSolenoid.Value.kReverse);
}

public void retractHatch(){
  hatchSolenoid.set(DoubleSolenoid.Value.kForward);
  }

public boolean hatchLimit(){
  return !hatchLimitSwitch.get();
}


  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new DriveBoschMotor());
  }

  // for finding the distance from the hatch rangefinder to the hatch opening - Quinten S.
	public double getHatchDistance() {
		double rangefinderVoltage = hatchRangefinder.getAverageVoltage();
    double slope = 0.012;
    double distanceInInches = rangefinderVoltage / slope;
		return distanceInInches;
    }
}


