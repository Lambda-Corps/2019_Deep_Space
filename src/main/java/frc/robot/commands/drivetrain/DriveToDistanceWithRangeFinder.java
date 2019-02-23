/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DriveToDistanceWithRangeFinder extends Command {

  private double goalDistance;

  public DriveToDistanceWithRangeFinder(double distance) {
    requires(Robot.drivetrain);
    requires(Robot.hatch);
    goalDistance = distance;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(goalDistance >= Robot.hatch.getHatchDistance()){//Tells the robot to stop if it is at the distance
      Robot.drivetrain.arcadeDrive(0, 0, true);
    }
    else{//Tells the robot to drive to the goal distance if it isn't there already
      Robot.drivetrain.arcadeDrive(1, 0, true);
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
