// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot.subsystems.arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

import frc.robot.RobotMap;
import frc.robot.commands.arm.DefaultArm;
import frc.robot.commands.arm.MoveArm;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class Arm extends Subsystem {

    private static final double STANDARD_INTAKE_SPEED = .5;
    private static final double STANDARD_DEPLOY_SPEED = .5;
    //arm encoder positions
    public static final int ARM_POSITION_ZERO = 0;
    public static final int ARM_POSITION_PICKUP_CARGO = 6000;
    public static final int ARM_POSITION_SCORING_CARGO = 0;
    public static final int ARM_POSITION_CLIMB = 6000;


    private TalonSRX armMotor;
    private TalonSRX intakeMotor;
    private AnalogInput absoluteEncoder;
    private AnalogInput ballDetector;
    private AnalogInput ballDetector2;
    private DigitalInput limitSwitch;

    //softlimits
    private final int ARM_TALON_REVERSE_SOFT_LIMIT = 4615;
    private final int ARM_TALON_FORWARD_SOFT_LIMIT = 6000;
        //upper  4.3652
        //lower  0.91308
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Arm() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        //talonSRX4 = new WPI_TalonSRX(4);
       // talonSRX4.setInverted(true);
       limitSwitch = new DigitalInput(RobotMap.LIMIT_SWITCH); 


        armMotor = new TalonSRX(RobotMap.ARM_TALON);
        armMotor.configFactoryDefault();
        armMotor.setInverted(true);
        intakeMotor = new TalonSRX(RobotMap.INTAKE_TALON);
        intakeMotor.configFactoryDefault();
        ballDetector = new AnalogInput(RobotMap.INTAKE_BALL_DETECTOR);
        ballDetector2 = new AnalogInput(RobotMap.INTAKE_BALL_DETECTOR_2);

        
        

        absoluteEncoder = new AnalogInput(RobotMap.ARM_ABSOLUTE_ENCODER);
        addChild("AnalogInput0",absoluteEncoder);
        
        armMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
        armMotor.configForwardSoftLimitThreshold(ARM_TALON_FORWARD_SOFT_LIMIT);
        armMotor.configReverseSoftLimitThreshold(ARM_TALON_REVERSE_SOFT_LIMIT);
        armMotor.configForwardSoftLimitEnable(true);
        armMotor.configReverseSoftLimitEnable(true);
        
    
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new DefaultArm());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void setMotor(double speed){
      double encoderValue = getRelativeEncoder();

     /* if (speed > 0 ){
        if (encoderValue >= ARM_UPPER_SOFT_LIMIT){
            speed = 0;
            // System.out.println("stopped by upper limit");
        }
      } else if (speed < 0 ){
        if (encoderValue <= ARM_LOWER_SOFT_LIMIT){
           speed = 0;
        //    System.out.println("stopped by lower limit");
      }*/
    
     SmartDashboard.putNumber("speed", speed);
      if(limitSwitch.get()){
        armMotor.set(ControlMode.PercentOutput, 0); 
      } else {
        armMotor.set(ControlMode.PercentOutput, speed * 0.5);
      }
    }
      
        

        public double getRelativeEncoder(){
            return armMotor.getSelectedSensorPosition(0);
            
        }
        public double getAbsoluteEncoder(){
            return absoluteEncoder.getAverageVoltage();
        } 
        
    // for finding the distance from the sharp rangefinder to the ball if there is one - Quinten S.
    public double ballDetector() {
		double outputValue = ballDetector.getAverageVoltage();
		if (outputValue > 2.4 || outputValue < 0.4) { // code currently only accurate from 0.4-2.4 volts
			return -1;
		}
		double voltage = Math.pow(outputValue, -1.16);
		double coefficient = 10.298;
		double d = voltage * coefficient;
		return d;
    }

    public double ballDetector2(){
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
        double rangeFinderValue2 = 0;
        rangeFinderValue = ballDetector();
        rangeFinderValue2 = ballDetector2();
        if (rangeFinderValue == -1 && rangeFinderValue2 == -1){
            return false;
        }
        return true;
    }

    //Method to grab the cargo - Quinten S.
    public void grabCargo(){
        intakeMotor.set(ControlMode.PercentOutput, STANDARD_INTAKE_SPEED);
    }

    //Method to deploy the cargo - Quinten S.
    public void deployCargo(){
        intakeMotor.set(ControlMode.PercentOutput, STANDARD_DEPLOY_SPEED);
    }

    public void stopMotor(){
        intakeMotor.set(ControlMode.PercentOutput, 0);
    }
}

