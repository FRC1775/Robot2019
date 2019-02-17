/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfPoint;

import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.vision.VisionRunner;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.ExampleSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static OI m_oi;
  public static UsbCamera driverCamera;
  private VisionThread visionThread;
	private double centerX = 0.0;	
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

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    RobotMap.init();
    m_oi = new OI();
    m_oi.init();
    // visionThread = new VisionThread(driverCamera, new WalkOfShamePipeline(), this::testFunction);
    // visionThread.start();
    // chooser.addOption("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);
   // initCamera(); 
    new Thread(() -> {
      UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
      camera.setResolution(320, 240);
      camera.setExposureManual(0);
      CvSink cvSink = CameraServer.getInstance().getVideo();
      CvSource outputStream = CameraServer.getInstance().putVideo("Grip Pipeline Video", 320, 240);
      
        Mat source = new Mat();
        Mat output = new Mat();
        
        

        WalkOfShamePipeline pipeline = new WalkOfShamePipeline();

        while(!Thread.interrupted()) {
            long frameTime = cvSink.grabFrame(source);
            if(frameTime == 0){
              continue; 
            }
          
           // System.out.println("huh " + source);
           
            // Imgproc.cvtColor(source, output, Imgproc.CV_BLUR);
            pipeline.process(source);
            output = pipeline.maskOutput(); 
            ArrayList<MatOfPoint> contours = pipeline.findContoursOutput();
            // Always draw contours
            if (contours.size() > 0) {
              Imgproc.drawContours(output, contours, -1, new Scalar(0, 255, 0));
            }
            outputStream.putFrame(output);
            
            if (pipeline.goodBoiArray().size() > 0) {
              Rect r = Imgproc.boundingRect(pipeline.goodBoiArray().get(0));
              synchronized (imgLock) {
                perimeter = 2 * r.width + 2 * r.height;
                area = r.width * r.height;
                midx = r.x + r.width / 2;
                midy = r.y + r.height / 2;
                valuex = (midx - 320 / 2) / (320 / 2);
                valuey = (midy - 180 / 2) / (180 / 2);
                // we need to be taking the smaller of the length or width in order to use this correctly
                fieldOfViewHeight = FISH_RESOLUTION_HEIGHT / r.height; 
                distanceHeight = ( fieldOfViewHeight / ( 2 * Math.tan(HEIGHT_FOV) ) );

                fieldOfViewWidth = FISH_RESOLUTION_WIDTH / r.width; 
                distanceWidth = ( fieldOfViewWidth / ( 2 * Math.tan(WIDTH_FOV) ) );
                //old angle = 0.357
                
              }
              //  System.out.println("perimeter: " + perimeter);
              //  System.out.println("area: " + area);
              //  System.out.println("X: " + valuex);
              //  System.out.println("Y: " + valuey);
              /* Distance calculating height is more accurate then using width. If it
              is changing between values then the smaller one is generally correct 
              */
              System.out.println( "distance height: " + distanceHeight );
              //System.out.println ("distance width: " + distanceWidth );
              //System.out.println("width: " + r.width);
              //System.out.println("height: " + r.height);
            }else{
              System.out.println("findContoursOutput is empty :(");
            }      

   }
  
}).start();


}
//Horizontal - 61 58.8991967
//Vertical - 34.3

// look into what the bounding box actually looks like / what the width is for an angled rectangle


  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
   
//we want to get the modified image onto the smart dashboard    
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }


 /* private void initCamera() {
    driverCamera = CameraServer.getInstance().startAutomaticCapture();
    //driverCamera.setResolution(320, 180);
    driverCamera.setFPS(30);
    //driverCamera.getProperty("focus_auto").set(1);
    driverCamera.setExposureManual(0);
    
  }*/
}
