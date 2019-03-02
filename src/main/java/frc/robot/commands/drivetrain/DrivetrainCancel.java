/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

/**
 * Command to cancel other commands running on the arm if needed.
 */
public class DrivetrainCancel extends InstantCommand {
  /**
   * Add your docs here.
   */
  public DrivetrainCancel() {
    super();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.drivetrain);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.drivetrain.arcadeDrive(0, 0, false);
  }

}
