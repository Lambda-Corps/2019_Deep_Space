/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.arm.ArmSetPositionMM;
import frc.robot.subsystems.Arm;

public class GrabCargoGroup extends CommandGroup {
  /**
   * Add your docs here.
   */
  public GrabCargoGroup() {
    // Add Commands here:
    addSequential(new GrabCargo());
    addSequential(new ArmSetPositionMM(Arm.ARM_POSITION_ZERO));
  }
}
