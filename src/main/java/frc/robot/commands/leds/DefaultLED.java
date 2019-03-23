/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.LEDSignal;

public class DefaultLED extends Command {
  public DefaultLED() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.ledSubsystem);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    int pov = Robot.oi.partnerRemote.getPOV();
    // Shuffleboard.getTab("Testing").add(pov).withWidget(BuiltInWidgets.kTextView);
    // SmartDashboard.putNumber("pov", pov);
    if(pov!=-1){
      switch(pov){
        //up = vision alignment
        case 0:
          Robot.ledSubsystem.setLEDMode(LEDSignal.mode.vision);
          break;
        //right = cargo posession
        // case 90:
        //   Robot.ledSubsystem.setLEDMode(LEDSignal.mode.cargo);
        //   break;
        //down = hatch flipper position
        case 180:
          Robot.ledSubsystem.setLEDMode(LEDSignal.mode.hatch);
          break;
        //other = none
        default:
          Robot.ledSubsystem.setLEDMode(LEDSignal.mode.vision);
          break;
      }
    }

    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
