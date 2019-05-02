package frc.robot.commands.autonomous;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCommandBuilder extends CommandGroup {

    public AutoCommandBuilder(ArrayList<CommandHolder> commandList) {
    	
		for( int i = 0; i < commandList.size(); i++) {
			   addSequential(commandList.get(i).getCommand());
			//    System.out.println("ACB Adding: " + commandList.get(i).getCommand());
		}

    }
    
}