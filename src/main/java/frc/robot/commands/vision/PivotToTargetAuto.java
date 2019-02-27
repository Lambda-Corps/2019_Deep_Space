/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PivotToTargetAuto extends Command {
  private boolean isDone;
  private double speed;

  public PivotToTargetAuto() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    SmartDashboard.putNumber("Pivot Speed: ", 0.25);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    speed = SmartDashboard.getNumber("Pivot Speed: ", 0.25);
    isDone = false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(Robot.vision.getX() > 0.5 ){
      //I have to divide by the max X coordinate to normalize the range of the camera.
      // SmartDashboard.putBoolean("key", true);
        Robot.drivetrain.arcadeDrive(0, speed, false);
    }
    else if(Robot.vision.getX() < -0.5){
      Robot.drivetrain.arcadeDrive(0, -1*speed, false);
    }
    else{
      //Drive Straight
      isDone = true;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return isDone;
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
