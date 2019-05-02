/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class GrabCargo extends Command {
  boolean done;
  int ok_count;

  public GrabCargo() {
    requires(Robot.arm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    ok_count = 0;
    done = false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // Sets the intake motor to zero if the intake has a ball
    if (Robot.armIntake.ballPresent() == true) {
      if (++ok_count >= 18) {
        done = true;
        Robot.armIntake.holdCargo();
      }
      else{
        Robot.armIntake.grabCargoNoBounceBack();
      }
      
    } else {
      Robot.armIntake.grabCargoFullSpeed();
      ok_count = 0;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return done;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.armIntake.holdCargo();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.armIntake.stopMotor();
  }
}
