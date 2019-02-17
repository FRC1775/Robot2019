package frc.robot;

import java.util.ArrayList;
import java.util.List;


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

      // set up the camera and the smart dashboard videos
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
        SmartDashboard.putNumber("contours size", contours.size());
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

        if (pipeline.goodBoiArray().size() > 0) {
          // find the index in goodBoiArray of the rightmost strip of tape 
          // for our target hatch. This is the tape we will use to preform distance calculations.
          int indexOfRightTarget = compareAngles(pipeline.goodBoiArray());
          SmartDashboard.putNumber("index of right target", indexOfRightTarget);
          Rect r = Imgproc.boundingRect(pipeline.goodBoiArray().get(0));
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
          SmartDashboard.putBoolean("GoodBoiArray has values in it", pipeline.goodBoiArray().size() > 0);
          //System.out.println ("distance width: " + distanceWidth );
          //System.out.println("width: " + r.width);
          //System.out.println("height: " + r.height);
        }    
      }
    }).start();      
  }

  // class created to allow us to store the x value of the center of
  // a rotated rectangle, the angle at which that rectangle is rotated,
  // and its index in goodBoiArray.
  public class XAndAngle{
    public double xVal;
    public double angleVal;
    public int originalIndex;

    public XAndAngle (double xVal, double angleVal, int originalIndex){
      this.xVal = xVal;
      this.angleVal = angleVal;
      this.originalIndex = originalIndex;
    }
  }

  
  public int compareAngles(ArrayList<MatOfPoint> contours){
    // Finds the rotated rectangle for each of the strips of tape we see, 
    // then stores them in angles_array as (center x value, angle of rotation, index in goodBoiArray)
    ArrayList<XAndAngle> angles_array = findRotatedRectangle(contours);

    // If there are at least two pieces of tape being seen, determine which is to the right of a target.
    if(angles_array.size() >= 2){
      return findTarget(angles_array);
    }else{
      // There is only one element in the array. We assume for now that it's the right target, though
      // we need to write code that handles if it's the left target instead. 
      return -1;
    }
  }

  // for each of the contours, generates a rotated rectangle and puts its 
  // center X coordinae and angle of rotation in an array
  public ArrayList<XAndAngle> findRotatedRectangle(ArrayList<MatOfPoint> contours){
    MatOfPoint originalContours;
    RotatedRect bounds;
    double boundingAngle;
    ArrayList<XAndAngle> angles_array = new ArrayList<>();
    double centerx;
    int index;
    // looping through goodBoiArray
    for(int i = 0; i < contours.size(); i++){
      originalContours = contours.get(i);
      MatOfPoint2f angleOneModified = new MatOfPoint2f(originalContours.toArray());
      // get the rotated rectangle
      bounds = Imgproc.minAreaRect(angleOneModified);
      // before, the angle is from -90 to 0; we want it from 0 to 180
      boundingAngle = adjustAngle(bounds);
      centerx = bounds.center.x;
      // Orders the array from smallest x value to largest.
      // We're ordering as we put values in the array as an attempt to not 
      // have to iterate through the whole thing.
      index = sortArray(angles_array, centerx);
      angles_array.add(index, new XAndAngle(centerx, boundingAngle, i));
      // this was for when we had two strips of tape in the field of view. 
      // It can be modified to suit any number of strips.
      if(i == 0){
        SmartDashboard.putNumber("Angle 0", boundingAngle);
        SmartDashboard.putNumber("CenterX 0", centerx);
        SmartDashboard.putNumber("original index 0", i);
      } else if(i == 1) {
        SmartDashboard.putNumber("Angle 1", boundingAngle);
        SmartDashboard.putNumber("CenterX 1", centerx);
        SmartDashboard.putNumber("original index 1", i);
      }else if(i == 2){
        SmartDashboard.putNumber("Angle 2", boundingAngle);
        SmartDashboard.putNumber("CenterX 2", centerx);
        SmartDashboard.putNumber("original index 2", i);
      }
    }
    for(int i = 0; i < angles_array.size(); i++){
      System.out.println("original index: " + angles_array.get(i).originalIndex + "  new index: " + i);
    }
    // returns a list of (centerX, rotated angle, index in goodBoiArray)
    return angles_array;
  }

  // puts the angle of the rectangle on a scale of 0 - 180
  public double adjustAngle(RotatedRect calculatedRect){
    if(calculatedRect.size.width < calculatedRect.size.height){
      return calculatedRect.angle + 180;
    }else{
      return calculatedRect.angle + 90;
    }
  } 

  // Returns the index where the centerX value should be put in the array 
  // to make the array ordered from least to greatest centerX values
  public int sortArray(ArrayList<XAndAngle> array, double centerX){
    double currentX;
    // If the array is empty, center x is our smallest value. Put it first
    if(array.isEmpty()){
      return 0;
    }

    // Compare the x value to be sorted to those already in the array.
    // Values in the array increase as we iterate through it. The first 
    // value in the array to be greater than the one we want to place tells
    // us where to place x. 
    for(int i = 0; i < array.size(); i++){
      currentX = array.get(i).xVal;
      if(centerX < currentX){
        return i;
      }
    }

    // Center x is greater than every other value in the array. Put it last
    return array.size();
  }

  public int findTarget(ArrayList<XAndAngle> array){
    double angleSum;
    for(int i = 0; i < array.size() - 1; i++){
      // Check that the angles are close to supplementary as an extra 
      // filter to verify that we have the correct contours
      angleSum = array.get(i).angleVal + array.get(i + 1).angleVal;
      if(//160 <= angleSum && angleSum <= 200 &&
         array.get(i).angleVal < 90 && array.get(i + 1).angleVal > 90){
        // If both are angled inwards, the target is in between them.
        // We want to always return the rightmost strip of tape
        return array.get(i + 1).originalIndex;
      }
    }

    /* In the case where there are at least two strips of tape, but none are 
     * facing each other, the tape must be positioned like this: \  /
     * This means that the leftmost strip of tape is actually the rightmost
     * piece of tape for a target, and the other piece is out of the field of view.
     * To be consistent and always return the rightmost tape of a pair, we will
     * return the first (index 0) element of the array of tape strips. 
     */
    return array.get(0).originalIndex;
  }
}

