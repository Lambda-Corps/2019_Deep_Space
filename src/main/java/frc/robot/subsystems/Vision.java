package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem {
    public static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    
    //Pipelines//
    public final int CargoShipPipeLine = 0;
    public final int OrangeBallPipeline = 1;

    //Field Variables//
    public final double maxArea = 100;//TO-DO .. 3.8
    public final double minDistance = 36;//TO-DO in in feet
    //public final double maxXCordinatesDistance = 23.0; dx
    public final double maxYCordinatesDistance = 16.0; //dy
    //Limelight Terms//
    public final int processorMode = 0;
    public final int streamMode = 1;

    public Vision(){
    }
    
    // private void getTarget(){
    //     NetworkTableEntry tx = table.getEntry("tx");
    //     NetworkTableEntry ty = table.getEntry("ty");
    //     NetworkTableEntry ta = table.getEntry("ta");
    //     NetworkTableEntry pipe = table.getEntry("getpipe");
        
    //     //read values periodically
    //     double x = tx.getDouble(0.0);
    //     double y = ty.getDouble(0.0);
    //     double area = ta.getDouble(0.0);
    //     double pip = pipe.getDouble(0.0);

    //     //post to smart dashboard periodically
    //     SmartDashboard.putNumber("LimelightX", x);
    //     SmartDashboard.putNumber("LimelightY", y);
    //     SmartDashboard.putNumber("LimelightArea", area);
    //     SmartDashboard.putNumber("Pipeline", pip);
    // }
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

	@Override
	protected void initDefaultCommand() {
        
	}
}