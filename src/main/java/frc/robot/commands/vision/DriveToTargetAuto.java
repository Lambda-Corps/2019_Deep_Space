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

public class DriveToTargetAuto extends Command {
  private boolean isDone;
  private double size;
  private double speed;

  public DriveToTargetAuto() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    SmartDashboard.putNumber("Vision Drive Speed", 0.25);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    speed = SmartDashboard.getNumber("Vision Drive Speed", 0);
    isDone = false;
    size = 7;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
    if(Robot.vision.getArea() < size){//TODO

      //I have to divide by the max X coordinate to normalize the range of the camera.
      // SmartDashboard.putBoolean("key", true);
        Robot.drivetrain.arcadeDrive(speed, 
          (Robot.vision.getX())/Robot.vision.maxXCordinatesDistance,false);
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
