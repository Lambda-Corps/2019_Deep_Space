/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DeployCargoManual extends Command {

    boolean done;
    int count;

    public DeployCargoManual() {
        requires(Robot.armIntake);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        count = 0;
        done = false;
        Robot.armIntake.deployCargo();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        // Sets the intake motor to zero if the intake has a ball
        if( ++count >= 50 ) { // 1 sec count
            done = true;
            Robot.armIntake.stopMotor();
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
        Robot.armIntake.stopMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
