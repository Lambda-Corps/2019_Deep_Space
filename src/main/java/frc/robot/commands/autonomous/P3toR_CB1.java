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
import frc.robot.commands.vision.DriveToTargetAuto;
import frc.robot.commands.vision.DriveToTargetGroup;

public class P3toR_CB1 extends CommandGroup {
  /**
   * Add your docs here.
   * Rt1 is Right Bay 1
   * Adjust drive length measurements because of turn
   */
  public P3toR_CB1() {
        addSequential(new DriveMM(176.02));
        addSequential(new TurnWithoutPID(0.5, -90));
        addSequential(new DriveToTargetGroup());
        // addSequential(new DriveMM(-24));//secondary goal
        // addSequential(new TurnWithoutPID(0.5, -111.4));
        // addSequential(new DriveMM(290.25));
    
    // addSequential(new DriveMM(5*50));
    // addSequential(new DriveMM(5460));



    // addSequential(new DriveMM(164.5));
    // addSequential(new TurnWithoutPID(0.5, -90.0));
    // addSequential(new DriveMM(6.0));

  }
}
