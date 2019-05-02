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
import frc.robot.commands.vision.DriveToTargetAuto;

public class P1toL_CB1 extends CommandGroup {
  /**
   * Lt1 is Left Bay 1
   * Adjust drive length measurements because of turn
   */
    
  
  public P1toL_CB1() {
      // addSequential(new PrintCommand("the one you want is running!"));
      addSequential(new DriveMM(176.02));
      addSequential(new TurnMM(90), 2);
      // align with vision targets
      addSequential(new DriveToTargetAuto());

      // addSequential(new DriveMM(-24));//secondary goal
      // addSequential(new TurnWithoutPID(0.5, 111.4));
      // addSequential(new DriveMM(290.25));
  
  }
}
