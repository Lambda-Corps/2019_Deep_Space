package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;

// This class is used to build up the auto sequences.  
// The whole purpose is to allow us to declare our commands
// that we build for auto as either Parallel or Sequential
// when we build up the CommandGroup
public class CommandHolder {
	
	public static final int SEQUENTIAL_COMMAND = 0;
	public static final int PARALLEL_COMMAND = 1;
	
	int m_CommandType;
	Command m_Command;
	
	public CommandHolder(int commandType, Command command) {
		m_CommandType = commandType;
		m_Command = command;
	}

	public boolean isSequentialCommand() {
		return m_CommandType == SEQUENTIAL_COMMAND;
	}

	public Command getCommand() {
		return m_Command;
	}
}