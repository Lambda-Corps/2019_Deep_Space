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
import frc.robot.commands.drivetrain.TurnWithoutPID;

public class Lvl1RtoCB1 extends CommandGroup {
  /**
   * Add your docs here.
   */
  public Lvl1RtoCB1() {
    
    // addSequential(new DriveMM(1*6.25*3.14));  //1 rotation = 6.25*pi inches (~20)
    // addSequential(new DriveMM(5460));

    // addSequential(new TurnMM(21.205));

    // addSequential(new DriveMM(164.5));
    // addSequential(new TurnWithoutPID(0.5, -90.0));
    // addSequential(new DriveMM(6.0));

  }
}
