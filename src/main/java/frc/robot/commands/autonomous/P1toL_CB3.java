/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.drivetrain.TurnWithoutPID;
import frc.robot.commands.vision.DriveToTarget;
import frc.robot.commands.vision.DriveToTargetAuto;

public class P1toL_CB3 extends CommandGroup {
  /**
   * Lt3 is Left Bay 3
   * Adjust drive length measurements because of turn
   */
    
  
  public P1toL_CB3() {
      addSequential(new DriveMM(219.52));
      addSequential(new TurnWithoutPID(0.5, 90));
      addSequential(new DriveToTargetAuto());
  }
}
