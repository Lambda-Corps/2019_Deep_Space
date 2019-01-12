package frc.robot;

import frc.robot.oi.F310;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/** 
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

/**
 * Changelog:
 * 
 * 
 *
 */

public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	
	// Create the joysticks here. Until the specialized classes are implemented, 
	// use the Joystick class from wpilib.
	public Joystick  leftArcadeJoystick;
	public Joystick rightArcadeJoystick;
	
	public F310 gamepad;
	public F310 gamepad2;
	
	
	public OI() {
		leftArcadeJoystick = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightArcadeJoystick = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		gamepad = new F310(RobotMap.GAMEPAD_PORT);
		gamepad2 = new F310(RobotMap.GAMEPAD2_PORT);
		
	}
	
	public double getGainOI() {
		return F310.getGain();
	}
}
