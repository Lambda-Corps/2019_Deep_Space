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
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.commands.DriveHatch;

/**
 * Add your docs here.
 */
public class Hatch extends Subsystem {
  private TalonSRX hatchMotor;
  private DigitalInput hatchEncoder;
  private AnalogInput hatchRangefinder;
  private DoubleSolenoid hatchSolenoid;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
public Hatch(){
  hatchMotor = new TalonSRX(RobotMap.HATCH_TALON);
  hatchEncoder = new DigitalInput(RobotMap.HATCH_ENCODER);
  hatchRangefinder = new AnalogInput(RobotMap.HATCH_DISTANCE_FINDER);
  hatchSolenoid = new DoubleSolenoid(RobotMap.HATCH_SOLENOID_PORT_A, RobotMap.HATCH_SOLENOID_PORT_B);
 

}
public void driveMotor (double speed){
  hatchMotor.set(ControlMode.PercentOutput,speed);
}

public void deployHatch(){
hatchSolenoid.set(DoubleSolenoid.Value.kForward);
}

public void retractHatch(){
  hatchSolenoid.set(DoubleSolenoid.Value.kReverse);
  }


  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new DriveBoschMotor());
  }

  // for finding the distance from the hatch rangefinder to the hatch opening - Quinten S.
	public double getHatchDistance() {
		double outputValue = hatchRangefinder.getAverageVoltage();
		if (outputValue > 2.4 || outputValue < 0.4) { // code currently only accurate from 0.4-2.4 volts
			return 25;
		}
		double voltage = Math.pow(outputValue, -1.16);
		double coefficient = 10.298;
		double d = voltage * coefficient;
		return d;
    }
}


