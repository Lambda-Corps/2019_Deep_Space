package frc.robot.commands.vision;

import frc.robot.Robot;
import frc.robot.oi.F310;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class DriveToTarget extends Command{
    public final double reduceSpeed = 2;

    public DriveToTarget(){
        requires(Robot.drivetrain);
    }

	@Override
	protected void initialize() {
        // SmartDashboard.putBoolean("key", false);
        Robot.vision.setPipeline(Robot.vision.CargoShipPipeLine);
        Robot.vision.setCamMode(0);
        Robot.vision.setLED(3);//set force on
	}

    @Override
	protected void execute() {
        //calls vision code to constantly update the fx and fy value.
        if(Robot.hatch.getHatchRFRaw() > 0.38 ){
            //I have to divide by the max X coordinate to normalize the range of the camera.
            // SmartDashboard.putBoolean("key", true);
            Robot.drivetrain.arcadeDrive(-Robot.oi.driverRemote.getAxis(F310.LY)/reduceSpeed, 
                (-1*Robot.vision.getY())/Robot.vision.maxYCordinatesDistance,false);
        }
        else{
            //Drive Straight
            Robot.drivetrain.arcadeDrive(-Robot.oi.driverRemote.getAxis(F310.LY)/reduceSpeed, 
                0,false);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
        //once we have rangefinder code I will set this to true and alert
        //the driver to let them know they have reached the target.
    }

    // Called once after isFinished returns true
    @Override
	protected void end() {
        Robot.drivetrain.arcadeDrive(0, 0, false);
	
    }
    
    // Called when another command which requires one or more of the same
	// subsystems is scheduled to run.
	@Override
	protected void interrupted() {
	}

}