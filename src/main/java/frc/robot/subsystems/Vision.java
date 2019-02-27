package frc.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.UsbCameraInfo;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem {
    public static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    
    //Pipelines//
    public final int CargoShipPipeLine = 0;
    public final int OrangeBallPipeline = 1;

    //Field Variables//
    public static final double maxArea = 5;
    public static final double minArea = 0.3; // TODO - ? needs calibration
    public final double minDistance = 36;
    public final double maxXCordinatesDistance = 23.0; //dx
    public final double maxYCordinatesDistance = 16.0; //dy
    //Limelight Terms//
    public final int processorMode = 0; 
    public final int streamMode = 1;

    UsbCamera ballCamera;

    public Vision(){
        ballCamera = CameraServer.getInstance().startAutomaticCapture("Rear Facing Camera", 0);
    }
    
    public double getX(){
        NetworkTableEntry tx = table.getEntry("tx");
        double x = tx.getDouble(0.0);
        return x;
    }
    public double getY(){
        NetworkTableEntry ty = table.getEntry("ty");
        double y = ty.getDouble(0.0);
        return y;
    }
    public double getArea(){
        NetworkTableEntry ta = table.getEntry("ta");
        double area = ta.getDouble(0.0);
        return area;
    }
    public double getPipeline(){
        return table.getEntry("getpipe").getDouble(0.0);
    }


    public void setCamMode(int mode){
        table.getEntry("camMode").setNumber(mode);
    }
    public void setLED(int mode){
        table.getEntry("ledMode").setNumber(mode);
    }

    public void setPipeline(int num){
        table.getEntry("pipeline").setNumber(num);
    }
    //TODO
    public double getDistance(){
        double ratio = 1.0;//unknown
        return (getArea()*ratio);
    }

    // TODO -- Finish this implementation with calibration of min
    // and max ta calculations
    public boolean hasTargetAcquired(){
        double targetArea = getArea();

        return (targetArea > minArea) && (targetArea < maxArea);
    }

	@Override
	protected void initDefaultCommand() {
        
	}
}