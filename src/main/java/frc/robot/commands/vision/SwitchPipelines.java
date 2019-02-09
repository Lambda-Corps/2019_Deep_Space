/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.vision.Vision;

public class SwitchPipelines extends Command {
  private int pipeline;

  public SwitchPipelines(int num) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    pipeline = num;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    // SmartDashboard.putBoolean("end?", false);
    // int num = (int) Robot.vision.getPipeline() + 1;
    // if((Robot.vision.getPipeline() + 1.0 ) == 2.0){
    //   num = 0;
    // }
    Robot.vision.setPipeline(pipeline);
    Robot.vision.setCamMode(Robot.vision.processorMode);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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
