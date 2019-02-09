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

public class Lvl1P2toCL0 extends CommandGroup {
  /**
   * CL0 is Center Left 0
   * Adjust drive length measurements because of turn
   * there might need to be an angle or distance change because of the two different bays and same starting point
   */
    
  
  public Lvl1P2toCL0() {
      addSequential(new DriveMM(100));
      // from here use/activate vision
      //put hatch on
      addSequential(new DriveMM(-24));//secondary goal
      addSequential(new TurnWithoutPID(0.5, 206)); 
      addSequential(new DriveMM(244.3));
  }
}
