package frc.robot.oi;
import edu.wpi.first.wpilibj.Joystick;

// Custom Gamepad class
public class F310 extends Joystick {

	private static final int numAxes = 6;
	private static final int numButtons = 10;
	
	// Constants to represent the raw button number with a human readable word
	public static final int A = 1;
	public static final int B = 2;
	public static final int X = 3;
	public static final int Y = 4;
	public static final int LB = 5; //top button
	public static final int RB = 6; //top button
	public static final int START = 8;
	public static final int BACK = 7;
	public static final int L_AXIS = 8;
	public static final int R_AXIS = 9;
	
	//Same as above for axis
	public static final int LX = 0; //0
	public static final int RX = 4;	//4
	public static final int LY = 1;	//1
	public static final int RY = 5;	//5
	public static final int LT = 2;	//2
	public static final int RT = 3;	//3
	
	private double[] scalar = new double[numAxes];
	
	private static double gain = 0.5;
	
	public F310(int port) {
		//needed to call super constructor in order for F310 to extend Joystick
		super(port);	
	}
	
	public static double getGain() {
		return gain;
	}
	
	public static void setGain(double g) {
		gain = g;
	}
	
	//call enum Buttons
	public boolean getButton(int buttons) {
		if(buttons < 0 || buttons >= numButtons) return false;
		return this.getRawButton(buttons);
	}
	
	//calls enum Axis
	public double getAxis(int axis) {
		if(axis < 0 || axis >= numAxes) return 0.0;
		double x = this.getRawAxis(axis);
		return gain * Math.pow(x, 3) + (1 - gain) * x;
	}
	
	//scales and axis by calling an axis and gives it a value
	public void setScalar(int axis, double value) {
		if(axis < 0 || axis >= numAxes) return;
		scalar[axis] = value;
	}
}
