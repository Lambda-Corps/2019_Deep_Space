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

public class Lvl1P1toLt2 extends CommandGroup {
  /**
   * Lt2 Left Bay 2
   * Adjust drive length measurements because of turn
   */
    
  
  public Lvl1P1toLt2() {
      addSequential(new DriveMM(197.77));
      addSequential(new TurnWithoutPID(0.5, 90));
      // from here use/activate vision
  }
}