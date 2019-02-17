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

public class TestDrive extends Command {

  int count;
  double speed;

  /*

  */
  public TestDrive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    Robot.drivetrain.resetLeftTalonEncoder();
    Robot.drivetrain.resetRightTalonEncoder();

    speed = SmartDashboard.getNumber("TestDrive Speed", 0);

    count = 0;

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    Robot.drivetrain.arcadeDrive(speed, 0, false);
    count++;

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return count>1000;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {

    Robot.drivetrain.arcadeDrive(0,0, false);

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
