/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TurnWithoutPID extends Command {
	private double goalAngle = 0.0;
	private boolean isDone = false;
	private double speed;
	private double tolerance = 3;
	//private double currentAngle;
	
    public TurnWithoutPID(double speed, double givenAngle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drivetrain);
    	goalAngle = givenAngle;
    	this.speed = speed;
    	isDone = false;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// Robot.drivetrain.resetAHRSGyro();
    	isDone = false;
 

    	//Robot.drivetrain.setAHRSAdjustment(80.0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// currentAngle = Robot.drivetrain.getAHRSGyroAngle();
    	// // SmartDashboard.putNumber("Gyro: ", currentAngle);
    	// if(Math.abs(goalAngle - currentAngle) < tolerance) {  //if within tolerance
    	// 	Robot.drivetrain.arcadeDrive(0, 0);
    	// 	isDone = true;
    	// } else if(currentAngle < goalAngle) {  //If left of target angle
    	// 	Robot.drivetrain.arcadeDrive(0, speed);  //turn clockwise
    	// } else if(currentAngle > goalAngle){  //If right of target angle
    	// 	Robot.drivetrain.arcadeDrive(0, -speed);  //turn counterclockwise
    	// }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		// return isDone;
		return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Robot.drivetrain.setAHRSAdjustment(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drivetrain.arcadeDrive(0, 0, false);
    	isDone = true;
    }
}
