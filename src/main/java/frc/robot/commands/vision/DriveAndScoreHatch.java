/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import frc.robot.commands.arm.ArmSetPositionMM;
import frc.robot.commands.drivetrain.DriveMM;
import frc.robot.commands.drivetrain.DrivetrainCancel;
import frc.robot.commands.hatch.DeployHatch;
import frc.robot.commands.hatch.DriveHatchToLimit;
import frc.robot.commands.hatch.RetractHatch;
import frc.robot.subsystems.Arm;

public class DriveAndScoreHatch extends CommandGroup {
  /**
   * Add your docs here.
   */
  public DriveAndScoreHatch() {

    // addSequential(new PrintCommand("Drive to Target Auto:"));
    addSequential(new DriveToTargetAuto()); // Use vision to align
    // addSequential(new PrintCommand("text test"));
    // addSequential(new PrintCommand("Drive Hatch to Limit:"));
    addParallel(new DriveHatchToLimit());  // Drop hatch 
    // addSequential(new PrintCommand("Arm Scoring Position:"));
    //vv why??
    addParallel(new ArmSetPositionMM(Arm.ARM_POSITION_SCORING_CARGO)); //drive arm to scoring position
    // addSequential(new PrintCommand("Drive MM:"));
    addSequential(new DriveMM(37), 1.5);  // Finish remaining driving distance
    // addSequential(new PrintCommand("Drivetrain Cancel:"));
    addSequential(new DrivetrainCancel());
    // addSequential(new PrintCommand("Deploy Hatch:"));
    addSequential(new DeployHatch());  // Score hatch
    // addSequential(new DriveMM(-10), 1.5);  // back off cargo ship
    // addSequential(new RetractHatch()); // retract hatch while driving
    // addSequential(new ArmSetPositionMM(Arm.ARM_POSITION_ZERO)); //drive arm to zero position

  }
}

