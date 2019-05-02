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
import frc.robot.commands.hatch.DeployHatch;
import frc.robot.commands.hatch.DriveHatchToLimit;
import frc.robot.commands.hatch.RetractHatch;
import frc.robot.commands.intake.DeployCargo;
import frc.robot.subsystems.Arm;

public class DriveAndScoreCargo extends CommandGroup {
  /**
   * Add your docs here.
   */
  public DriveAndScoreCargo() {
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
    addSequential(new DriveToTargetAuto()); // Use vision to align
    // addSequential(new PrintCommand("text test"));
    addParallel(new ArmSetPositionMM(Arm.ARM_POSITION_SCORING_CARGO)); //drive arm to scoring position
    addSequential(new DriveMM(32), 1.5);  // Finish remaining driving distance
    addSequential(new DeployCargo());  // Score hatch
    addSequential(new DriveMM(-10), 1.5);  // back off cargo ship
    addSequential(new ArmSetPositionMM(Arm.ARM_POSITION_ZERO)); //drive arm to zero position

  }
}
