/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.vision.DriveAndScoreHatch;

public class P2toL_CB0 extends CommandGroup {
  /**
   * CL0 is Center Left 0
   * Adjust drive length measurements because of turn
   * there might need to be an angle or distance change because of the two different bays and same starting point
   */
    
  
  public P2toL_CB0() {
      addSequential(new DriveMM(60));
      addSequential(new DriveAndScoreHatch());
      //addSequential(new DriveMM(50)); STEAMWORKS
      //addSequential(new TurnMM(67)); STEAMWORKS
      //addSequential(new DriveMM(50)); STEAMWORKS

      // addSequential(new DriveMM(-24));//secondary goal
      // addSequential(new TurnWithoutPID(0.5, 206)); 
      // addSequential(new DriveMM(244.3));
  }
}
