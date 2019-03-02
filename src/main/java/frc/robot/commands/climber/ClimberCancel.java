/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

/**
 * Command to cancel other commands running on the arm if needed.
 */
public class ClimberCancel extends InstantCommand {
  /**
   * Add your docs here.
   */
  public ClimberCancel() {
    super();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.climber);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.drivetrain.arcadeDrive(0, 0, false);
    Robot.climber.setMotor(0);
    Robot.climber.retractFrontSolenoid();
    Robot.climber.retractRearSolenoid();
  }

}
