package frc.robot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;


public class Camera  {
  public static UsbCamera driverCamera;
  public WalkOfShamePipeline pipeline;
  private final Object imgLock = new Object();
  private final double RESOLUTION_WIDTH = 320;
  private final double RESOLUTION_HEIGHT = 240;
  final double TARGET_WIDTH = (2 * Math.sin(1.318)) + (5.5 * Math.sin(0.2531));
  final double TARGET_HEIGHT = (2 * Math.cos(1.318)) + (5.5 * Math.cos(0.2531));
  private final double FISH_RESOLUTION_HEIGHT = RESOLUTION_HEIGHT * TARGET_HEIGHT; 
  private final double FISH_RESOLUTION_WIDTH = RESOLUTION_WIDTH * TARGET_WIDTH; 
  private final double WIDTH_FOV = .48;
  private final double HEIGHT_FOV = .353;

  private double perimeter = 0.0;	
  private double area = 0.0;
  private double valuex = 0.0;
  private double valuey = 0.0;
  private double midx = 0.0;
  private double midy = 0.0;
  private double fieldOfViewHeight = 0;
  private double fieldOfViewWidth = 0;
  private double distanceHeight = 0; 
  private double distanceWidth = 0; 

  public void init(){
    new Thread(() -> {
      UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
      camera.setResolution(320, 240);
      camera.setExposureManual(0);
      CvSink cvSink = CameraServer.getInstance().getVideo();
      CvSource outputStream = CameraServer.getInstance().putVideo("Grip Pipeline Video", 320, 240);
      
      Mat source = new Mat();
      Mat output = new Mat();
      
      pipeline = new WalkOfShamePipeline();

      while(!Thread.interrupted()) {
        long frameTime = cvSink.grabFrame(source);
        if(frameTime == 0){
          continue; 
        }
        
        pipeline.process(source);

        // Drawing contours on the SmartDashboard
        output = pipeline.maskOutput(); 
        ArrayList<MatOfPoint> contours = pipeline.goodBoiArray();
        // Always draw contours
        if (contours.size() > 0) {
          Imgproc.drawContours(output, contours, -1, new Scalar(0, 255, 0));
         
          // draws the contours of the rotated rectangle
          for(int i = 0; i < contours.size(); i++){
            MatOfPoint2f angleOneModified = new MatOfPoint2f(contours.get(0).toArray());
            RotatedRect rotatedRect = Imgproc.minAreaRect(angleOneModified);
            Point[] vertices = new Point[4];
            rotatedRect.points(vertices);
            List<MatOfPoint> boxContours = new ArrayList<>();
            boxContours.add(new MatOfPoint(vertices));
            Imgproc.drawContours(output, boxContours, 0, new Scalar(128, 128, 128), -1);
          }
        }
        outputStream.putFrame(output);
        ArrayList<MatOfPoint> greaterBoiArray = pipeline.goodBoiArray();
        if (greaterBoiArray.size() > 0) {
          compareAngles(greaterBoiArray);
          Rect r = Imgproc.boundingRect(greaterBoiArray.get(0));
          synchronized (imgLock) {
            perimeter = 2 * r.width + 2 * r.height;
            area = r.width * r.height;
            midx = r.x + r.width / 2;
            midy = r.y + r.height / 2;
            valuex = (midx - 320 / 2) / (320 / 2);
            valuey = (midy - 180 / 2) / (180 / 2);

            fieldOfViewHeight = FISH_RESOLUTION_HEIGHT / r.height; 
            distanceHeight = ( fieldOfViewHeight / ( 2 * Math.tan(HEIGHT_FOV) ) );

            fieldOfViewWidth = FISH_RESOLUTION_WIDTH / r.width; 
            distanceWidth = ( fieldOfViewWidth / ( 2 * Math.tan(WIDTH_FOV) ) );
          }
          //  System.out.println("perimeter: " + perimeter);
          //  System.out.println("area: " + area);
          //  System.out.println("X: " + valuex);
          //  System.out.println("Y: " + valuey);
          /* Distance calculating height is more accurate then using width. If it
          is changing between values then the smaller one is generally correct 
          */
          //System.out.println( "distance height: " + distanceHeight );
          SmartDashboard.putNumber("DistanceHeight:", distanceHeight);
          SmartDashboard.putBoolean("GoodBoiArray has values in it", greaterBoiArray.size() > 0);
          //System.out.println ("distance width: " + distanceWidth );
          //System.out.println("width: " + r.width);
          //System.out.println("height: " + r.height);
        }    
      }
    }).start();      
  }


  public class XAndAngle{
    public double XVal;
    public double AngleVal;

    public XAndAngle (double XVal, double AngleVal){
      this.XVal = XVal;
      this.AngleVal = AngleVal;
    }
  }

  public void compareAngles(ArrayList<MatOfPoint> contours){
    MatOfPoint angleOne;
    RotatedRect bounds;
    double boundingAngle;
    ArrayList<XAndAngle> angles_array = new ArrayList<>();
    Mat points = new Mat();
    for(int i = 0; i < contours.size(); i++){
      angleOne = contours.get(i);
      MatOfPoint2f angleOneModified = new MatOfPoint2f(angleOne.toArray());
      bounds = Imgproc.minAreaRect(angleOneModified);
      boundingAngle = adjustAngle(bounds);
      double centerx = bounds.center.x;
      angles_array.add(new XAndAngle(centerx, boundingAngle));
      if(i == 0){
        SmartDashboard.putNumber("Angle 0", boundingAngle);
      } else{
        SmartDashboard.putNumber("Angle 1", boundingAngle);
      }
    }
  }

  // puts the angle of the rectangle on a scale of 0 - 180
  public double adjustAngle(RotatedRect calculatedRect){
    if(calculatedRect.size.width < calculatedRect.size.height){
      return calculatedRect.angle + 180;
    }else{
      return calculatedRect.angle + 90;
    }
  } 
}

