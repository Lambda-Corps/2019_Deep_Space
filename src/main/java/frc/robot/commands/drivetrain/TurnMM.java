/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;


public class TurnMM extends Command {

  double arcLength;
  int count_ok;

  //the number of times motion magic is on target before the command finishes
  final int STABLE_ITERATIONS_BEFORE_FINISHED = 5;

  public TurnMM() {  //TODO: make the parameter angle, not arc length?
    requires(Robot.drivetrain);

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    arcLength = SmartDashboard.getNumber("Turn angle", 90);

    /*
    512 encoder ticks per axle rotation * 360/120 * 64/20 (gearing) = 4915 encoder ticks per wheel rotation
    4915 ticks per rotation * x rotations per cm = 98 ticks/cm
    Convert input inches to cm, Multiply by ticks/cm
    */
    //(given targetPos in inches) * 98 ticks/cm * 2.54 cm/inch
    this.arcLength = arcLength*248.92*0.2443; //ticks/inch * inches/angle
    SmartDashboard.putNumber("arc", this.arcLength);


    count_ok = 0;

    Robot.drivetrain.resetLeftTalonEncoder();
    Robot.drivetrain.resetRightTalonEncoder();

    Robot.drivetrain.motionMagicTurn(arcLength);


    // System.out.println("DMM init");

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(Robot.drivetrain.motionMagicOnTargetTurn(arcLength)){
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
    // System.out.println("-----DRIVE MOTION MAGIC FINISHED-----");
    SmartDashboard.putNumber("LE value", Robot.drivetrain.readLeftEncoder());
    SmartDashboard.putNumber("RE value", Robot.drivetrain.readRightEncoder());
    Robot.drivetrain.arcadeDrive(0, 0, false);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
