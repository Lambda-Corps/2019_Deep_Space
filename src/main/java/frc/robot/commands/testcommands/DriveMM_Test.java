/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.testcommands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

/*
  A test version of the Drive Motion Magic command: gets target value from 
  SmartDashboard
*/
public class DriveMM_Test extends Command {

  double targetPos;

  //counter for motion magic being on target
  int count_ok;

  //the number of times motion magic must be on target before the command finishes
  final int STABLE_ITERATIONS_BEFORE_FINISHED = 5;

  public DriveMM_Test() {
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    targetPos = SmartDashboard.getNumber("DriveMM_Test Goal", 0);

    /*
    512 encoder ticks per axle rotation * 360/120 * 64/20 (gearing) = 4915 encoder ticks per wheel rotation
    4915 ticks per rotation * x rotations per cm = 98 ticks/cm
    Convert input inches to cm, Multiply by ticks/cm
    */
    //(given targetPos in inches) * 98 ticks/cm * 2.54 cm/inch
    this.targetPos = targetPos*248.92;
    SmartDashboard.putNumber("target", this.targetPos);

    count_ok = 0;

    Robot.drivetrain.resetLeftTalonEncoder();
    Robot.drivetrain.resetRightTalonEncoder();
    Robot.drivetrain.motionMagicStartConfig_Drive();

    Robot.drivetrain.motionMagicDrive(targetPos);

    // System.out.println("DMM init");

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(Robot.drivetrain.motionMagicOnTargetDrive(targetPos)){
      count_ok++;
    } else {
      count_ok = 0;
    }

  }

  //  TO DO
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // return false;
    return count_ok >= STABLE_ITERATIONS_BEFORE_FINISHED;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drivetrain.arcadeDrive(0, 0, false);
    Robot.drivetrain.motionMagicEndConfig_Drive();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
