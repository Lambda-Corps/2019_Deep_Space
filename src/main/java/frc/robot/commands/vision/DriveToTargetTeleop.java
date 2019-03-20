/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import frc.robot.Robot;
import frc.robot.oi.F310;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToTargetTeleop extends Command {
  private boolean isDone;
  private double size;
  private double speed;

  public DriveToTargetTeleop() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    SmartDashboard.putNumber("Vision Drive Speed", 0.25);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    // TODO -- Remove this once command is calibrated
    speed = SmartDashboard.getNumber("Vision Drive Speed", 0.25);
    isDone = false;
    size = 7;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
    // TODO -- Needs to have the area bounds < MaxSize, > MinSize
    // TODO -- needs to have a prelim vision.hasTarget() check so it doesn't 
    // run if we don't actually have a target.  This will prevent runaway robots.
    // SmartDashboard.putBoolean("driving dtta", true);

    final double STEER_K = 0.03; // how hard to turn toward the target
    final double DRIVE_K = 0.26; // how hard to drive fwd toward the target
    final double DESIRED_TARGET_AREA = 10.0; // area when we are close as possible to target while maintaining vision
    final double MAX_DRIVE = 0.7; // how hard to turn toward the target    

    if(Robot.vision.hasTarget()){ 

      Robot.drivetrain.curvatureDrive((DESIRED_TARGET_AREA-Robot.vision.getArea())*Robot.oi.driverRemote.getAxis(F310.LY), Robot.vision.getX()*Robot.oi.driverRemote.getAxis(F310.RX), false);

      // //I have to divide by the max X coordinate to normalize the range of the camera.
      // double yawSpeed = 1.4* Robot.vision.getX() / Robot.vision.maxXCordinatesDistance;
      //   Robot.drivetrain.curvatureDrive(speed, yawSpeed, false);
    }
    else{
      // Can't rely on the camera any more, return from this command
      Robot.drivetrain.curvatureDrive(0, 0, false);

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
    Robot.drivetrain.curvatureDrive(0, 0, false);

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
