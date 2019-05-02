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

public class DriveHatchToLimit extends Command {
  boolean bDone;

  public DriveHatchToLimit() {
    requires(Robot.hatch);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    bDone = false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double motorSpeed = Hatch.MOTOR_SPEED_DOWN;
    if(Robot.hatch.hatchLimit()){
      motorSpeed = 0;
      bDone = true;
    }
    Robot.hatch.driveMotor(motorSpeed); //check direction
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return bDone; //get from limit switch
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
