/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import java.lang.module.ModuleDescriptor.Requires;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.drivetrain.TurnWithoutPID;

public class Lvl1P1toLt1 extends CommandGroup {
  /**
   * Lt1 is Left Bay 1
   * Adjust drive length measurements because of turn
   */
    
  
  public Lvl1P1toLt1() {
      addSequential(new DriveMM(176.02));
      addSequential(new TurnWithoutPID(0.5, 90));
      // from here use/activate vision
      addSequential(new DriveMM(-24));//secondary goal
      addSequential(new TurnWithoutPID(0.5, 111.4));
      addSequential(new DriveMM(290.25));
  
  }
}
