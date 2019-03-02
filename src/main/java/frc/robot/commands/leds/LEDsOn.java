/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.leds;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.LEDSignal;

public class LEDsOn extends Command {
  public LEDsOn() {
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
    // int pov = (int) SmartDashboard.getNumber("pov", 0);
    int pov = Robot.oi.partnerRemote.getPOV();
    switch(pov){
      case 0:
        Robot.ledSubsystem.setLEDMode(LEDSignal.mode.vision);
        break;
      case 90:
        Robot.ledSubsystem.setLEDMode(LEDSignal.mode.cargo);
        break;
      case 180:
        Robot.ledSubsystem.setLEDMode(LEDSignal.mode.hatch);
        break;
      default:
        Robot.ledSubsystem.setLEDMode(LEDSignal.mode.none);
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
