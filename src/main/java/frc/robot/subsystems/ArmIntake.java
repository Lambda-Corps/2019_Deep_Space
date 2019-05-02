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
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class ArmIntake extends Subsystem{


    private static final double STANDARD_INTAKE_SPEED = -0.55;
    private static final double STANDARD_DEPLOY_SPEED = 1.0;
    private static final double STANDARD_NO_BOUNCE = -.25;
    private static final double STANDARD_HOLD_CARGO = -.1;

    private TalonSRX intakeMotor;
    private AnalogInput ballDetector;
    //private AnalogInput ballDetector2;


    public ArmIntake(){
        intakeMotor = new TalonSRX(RobotMap.INTAKE_TALON);
        intakeMotor.configFactoryDefault();
        ballDetector = new AnalogInput(RobotMap.INTAKE_BALL_DETECTOR);
       // ballDetector2 = new AnalogInput(RobotMap.INTAKE_BALL_DETECTOR_2);
    }
    
    @Override
    public void initDefaultCommand() {
    
       // setDefaultCommand(new DefaultArm());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }
    // for finding the distance from the sharp rangefinder to the ball if there is one - Quinten S.
    public double getBallDistance() {
		double outputValue = ballDetector.getAverageVoltage();
		if (outputValue > 2.4 || outputValue < 0.4) { // code currently only accurate from 0.4-2.4 volts
			return -1;
		}
		double voltage = Math.pow(outputValue, -1.16);
		double coefficient = 10.298;
		double d = voltage * coefficient;
		return d;
    }

    // used to tell if a ball is in the intake or not - Quinten S.
    public boolean ballPresent(){
        double rangeFinderValue = 0;
        // double rangeFinderValue2 = 0;
        rangeFinderValue = ballDetector.getAverageVoltage();
        // rangeFinderValue2 = ballDetector2();
        // if (rangeFinderValue == -1 || rangeFinderValue2 == -1){
        if(rangeFinderValue>0.8){ //rangeFinderValue<3 && 
            return true;
        }
        return false;
    }

    //Method to grab the cargo - Quinten S.
    public void grabCargoFullSpeed(){
        intakeMotor.set(ControlMode.PercentOutput, STANDARD_INTAKE_SPEED);
    }

    public void grabCargoNoBounceBack(){
        intakeMotor.set(ControlMode.PercentOutput, STANDARD_NO_BOUNCE);
    }

    public void holdCargo(){
        intakeMotor.set(ControlMode.PercentOutput, STANDARD_HOLD_CARGO);
    }

    //Method to deploy the cargo - Quinten S.
    public void deployCargo(){
        intakeMotor.set(ControlMode.PercentOutput, STANDARD_DEPLOY_SPEED);
    }

    public void deployCargo(double speed){
        intakeMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopMotor(){
        intakeMotor.set(ControlMode.PercentOutput, 0);
    }

    public double getMotorCurrent(){
        return intakeMotor.getOutputCurrent();
    }
}
