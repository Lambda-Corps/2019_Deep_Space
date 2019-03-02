/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.drivetrain.TurnMM;
import frc.robot.commands.vision.DriveAndScoreHatch;

public class P3toR_CB2 extends CommandGroup {
  /**
   * Rt2 is Right Bay 2
   * Adjust drive length measurements because of turn
   */
    
  
  public P3toR_CB2() {
      addSequential(new DriveMM(197.77));
      addSequential(new TurnMM(-90), 2);
      addSequential(new DriveAndScoreHatch());
  }
}
