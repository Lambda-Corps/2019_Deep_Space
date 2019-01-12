package frc.robot.oi;

import edu.wpi.first.wpilibj.Joystick;

public class ArcadeJoystick extends Joystick {
	
	//represents the number of buttons and axes used by the joystick
	private static final int numButtons = 12;
	private static final int numAxes = 3;
	
	// Constants to represent the raw axis number with a human readable word
	private static final int X = 0;
	private static final int Y = 1;
	private static final int THROTTLE = 2;
	
	//lets you scale the value of the joystick to increase or decrease its maximum range
	private double[] scalar = new double[numAxes];
	
	private double gain = 0.0;
	
	//constructor -- takes the joystick's port value as an argument
	public ArcadeJoystick(int port) {
		//super class needed in order for ArcadeJoystick to extend Joystick
		super(port);
		for(double i : scalar) { 		//for the length of scalar, to each i, set = 1.0
			i = 1.0;
		}
	}
	
	public double getAxis(int axis) {
		double x = this.getRawAxis(axis); //gets axis value of a certain axis
		double value = gain  * Math.pow(x, 3) * (1 - gain) * x;
		return value;
	}
	
	//returns whether a button is pressed or not
	public boolean getButton(int button) {
		//if the button isn't within range, we aren't using it so no buttons are being pressed
		if(button < 0 || button >= numButtons) return false;
		
		//if the button asked for is a button we have
		return this.getRawButton(button);
		
	}
	
	//scales an axis by calling an axis and giving it a value
	public void setScalar(int axis, double value) {
		if(axis < 0 || axis >= numAxes) return;
		scalar[axis] = value;
	}
}
