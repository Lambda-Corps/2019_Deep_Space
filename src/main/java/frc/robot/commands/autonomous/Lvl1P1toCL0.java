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

public class Lvl1P1toCL0 extends CommandGroup {
  /**
   * CL0 is Center Left 0
   * Adjust drive length measurements because of turn
   */
    
  
  public Lvl1P1toCL0() {
        addSequential(new TurnWithoutPID(0.5, 15)); //figure out this angle
        addSequential(new DriveMM(100)); //might be slightly higher because of angle
      // from here use/activate vision/use light to align
  }
}
