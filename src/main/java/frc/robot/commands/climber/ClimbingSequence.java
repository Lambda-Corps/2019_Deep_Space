/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.arm.ArmSetPositionMM;
import frc.robot.commands.drivetrain.DrivetrainClimb;
import frc.robot.subsystems.Arm;

public class ClimbingSequence extends CommandGroup {
  /**
   * Add your docs here.
   */
  public ClimbingSequence() {

    /*
    Sequence: (arm??)
    Drive backward to level 2
    Extend back solenoids
    Drive backward to catch wheels on level 2
    Retract back solenoids, extend front solenoids
    Drive backward onto level 2
    Retract front solenoids
    Drive fully onto level 2
    */

    // Add Commands here:
    addSequential(new ArmSetPositionMM(Arm.ARM_POSITION_CLIMB));
    addSequential(new ExtendFrontAndBackSolenoids());
    addSequential(new DriveClimberMotor());
    addSequential(new DrivetrainClimb());
    addParallel(new RetractFrontSolenoid());
    // TODO -- This is not correct, position zero will make the robot 
    // too heavy on the front, needs an encoder count that is pointing
    // up in the air
    addSequential(new ArmSetPositionMM(Arm.ARM_POSITION_ZERO));
    addSequential(new RetractBackSolenoid());
    addSequential(new DrivetrainClimb());
  }
}
