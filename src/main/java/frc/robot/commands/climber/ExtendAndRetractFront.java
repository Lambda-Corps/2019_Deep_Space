/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ExtendAndRetractFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public ExtendAndRetractFront() {

    addSequential(new ExtendFrontSolenoid());
    addSequential(new WaitCommand("SolenoidPause", 0.5));
    addSequential(new ExtendBackSolenoid());

  }
}
