/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;


public class DriveMM extends Command {

  double targetPos;
  int count_ok;

  //the number of times motion magic is on target before the command finishes
  final int STABLE_ITERATIONS_BEFORE_FINISHED = 5;

  public DriveMM(double targetPos) {
    requires(Robot.drivetrain);

    this.targetPos = targetPos;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    count_ok = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.drivetrain.motionMagicDrive(targetPos);
    if(Robot.drivetrain.motionMagicOnTarget()){
      count_ok++;
    } else {
      count_ok = 0;
    }
  }

  //  TO DO
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return count_ok >= STABLE_ITERATIONS_BEFORE_FINISHED;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("-----DRIVE MOTION MAGIC FINISHED-----");
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
