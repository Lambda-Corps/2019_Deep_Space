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

public class TestGrabCargo extends Command {
  boolean done;
  double motorspeed;
  double endspeed;
  double veryendspeed;
  double ok_iterations;

  int ok_count;

  public TestGrabCargo() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.armIntake);
    done = false;
    motorspeed = 0.0;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    motorspeed = SmartDashboard.getNumber("motorspeed", 0.0);
    endspeed = SmartDashboard.getNumber("endspeed", 0.0);
    veryendspeed = SmartDashboard.getNumber("endspeed", 0.0);
    ok_iterations = SmartDashboard.getNumber("ok_iterations", 0.0);
    // Robot.testTabTable.getEntry("motorspeed").getDouble(0.0);
    done = false;
    ok_count = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
        //Sets the intake motor to zero if the intake has a ball
        if(Robot.armIntake.ballPresent() == true){
          ok_count++;
          Robot.armIntake.grabCargo(endspeed);
          if(ok_count>=25){
            done = true;
          }
        }
        else{
          Robot.armIntake.grabCargo(motorspeed);
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
    Robot.armIntake.grabCargo(veryendspeed);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
