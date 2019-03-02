/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.leds.LEDsOn;

/**
 * Add your docs here.
 */
public class LEDSignal extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  DigitalOutput[] ledArray;

  boolean Value0;
  boolean Value1;
  boolean Value2;
  boolean Value3;

  public mode trackingModeGlobal;

  public enum ledNum {
    l1, l2, l3, l4;
  }

  // public enum remote{
  //   driverRemote, partnerRemote;
  // }

  public static enum mode{
    cargo, hatch, vision, none;
  }

  public LEDSignal() {
    ledArray = new DigitalOutput[4];
    ledArray[0] = new DigitalOutput(RobotMap.LED_0);
    ledArray[1] = new DigitalOutput(RobotMap.LED_1);
    ledArray[2] = new DigitalOutput(RobotMap.LED_2);
    ledArray[3] = new DigitalOutput(RobotMap.LED_3);

    SmartDashboard.putNumber("color", 0);

    Value0 = false;
    Value1 = false;
    Value2 = false;
    Value3 = false;
    
    trackingModeGlobal = mode.none;

  }

  public void setLED(int value0, int value1, int value2, int value3) {
    Value0 = (value0 == 1)? true: false;
    Value1 = (value1 == 1)? true: false;
    Value2 = (value2 == 1)? true: false;
    Value3 = (value3 == 1)? true: false;
    ledArray[0].set(Value0);
    ledArray[1].set(Value1);
    ledArray[2].set(Value2);
    ledArray[3].set(Value3);
  }

  public void setColor(int color) {
    switch (color) {
    case 0:
      setLED(0, 0, 0, 0);
      break;
    case 1:
      setLED(0, 0, 0, 1);
      break;
    case 2:
      setLED(0, 0, 1, 0);
      break;
    case 3:
      setLED(0, 0, 1, 1);
      break;
    case 4:
      setLED(0, 1, 0, 0);
      break;
    case 5:
      setLED(0, 1, 0, 1);
      break;
    case 6:
      setLED(0, 1, 1, 0);
      break;
    case 7:
      setLED(0, 1, 1, 1);
      break;
    case 8:
      setLED(1, 0, 0, 0);
      break;
    case 9:
      setLED(1, 0, 0, 1);
      break;
    case 10:
      setLED(1, 0, 1, 0);
      break;
    case 11:
      setLED(1, 0, 1, 1);
      break;
    case 12:
      setLED(1, 1, 0, 0);
      break;
    case 13:
      setLED(1, 1, 0, 1);
      break;
    case 14:
      setLED(1, 1, 1, 0);
      break;
    case 15:
      setLED(1, 1, 1, 1);
      break;

    }
  }

  @Override
  public void periodic(){
    // setColor(0);
    // Double d = SmartDashboard.getNumber("color", 0);
    // setColor((d.intValue()));
    // SmartDashboard.putNumber("input", d);
    // SmartDashboard.putBoolean("led 0 ", ledArray[0].get());
    // SmartDashboard.putBoolean("led 1 ", ledArray[1].get());
    // SmartDashboard.putBoolean("led 2 ", ledArray[2].get());
    // SmartDashboard.putBoolean("led 3 ", ledArray[3].get());
    // SmartDashboard.putBoolean("led 0 V", Value0);
    // SmartDashboard.putBoolean("led 1 V", Value1);
    // SmartDashboard.putBoolean("led 2 V", Value2);
    // SmartDashboard.putBoolean("led 3 V", Value3);
    

    // if(Robot.armIntake.ballPresent()){
    //   setLED(1, 1, 1, 1); //ball present - BLUE 
    // } else {
    //   setLED(0, 0, 1, 0); //ball not present - RED
    // }

        if(Robot.hatch.hatchLimit()){
          setLED(1, 1, 1, 1); //limit hit - BLUE 
        } else {
          setLED(0, 1, 0, 0); //limit not hit - RED
        }

    // switch(trackingModeGlobal){
      // case vision:
      //   SmartDashboard.putString("Tracking Mode", "Vision");
      //   if(Robot.armIntake.ballPresent()){
      //     setLED(0, 0, 0, 1); //in range - GREEN 
      //   } else {
      //     setLED(0, 0, 1, 0); //not in range - RED
      //   }
      //   break;
    //   case cargo:
    //     SmartDashboard.putString("Tracking Mode", "Cargo");
    //     if(Robot.armIntake.ballPresent()){
    //       setLED(1, 1, 1, 1); //ball present - BLUE 
    //     } else {
    //       setLED(0, 0, 1, 0); //ball not present - RED
    //     }
    //     break;
    //   case hatch:
    //     SmartDashboard.putString("Tracking Mode", "Hatch");
    //     if(Robot.hatch.hatchLimit()){
    //       setLED(1, 1, 1, 1); //limit hit - BLUE 
    //     } else {
    //       setLED(0, 1, 0, 0); //limit not hit - RED
    //     }
    //     break;
    //   case none:
    //     SmartDashboard.putString("Tracking Mode", "None");
    //     setLED(0, 0, 0, 0); //no info to output - WHITE
    //     break;
      
    // }

  }

  public void setLEDMode(mode trackingMode){
    trackingModeGlobal = trackingMode;
  }

  public mode getLEDMode() {
    return trackingModeGlobal;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new LEDsOn());
  }
}
