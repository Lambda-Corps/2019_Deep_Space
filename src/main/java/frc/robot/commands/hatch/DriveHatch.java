/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Hatch;

public class DriveHatch extends Command {
  // Through testing found these values worked to turn the motor 90 degrees
  private static final int _90_DEGREE_LOOP_COUNT = 50;
  int count;
  
  public DriveHatch() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.hatch);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    count = _90_DEGREE_LOOP_COUNT;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    count -= 1; 
    Robot.hatch.driveMotor(Hatch.MOTOR_SPEED_UP);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return count == 0; 
  }

  // Called once after isFinished returns true
  @Override
  protected void end() { 
    Robot.hatch.driveMotor(0.0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
