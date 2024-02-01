// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Drivetrain m_drivetrain; 
  private PilotController m_controller;
  private Launcher m_launcher;
  private Intake m_intake;
  private Indexer m_indexer;

  private boolean m_currentlyLaunching;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_drivetrain = new Drivetrain();
    m_controller = new PilotController();
    m_launcher = new Launcher();
    m_intake = new Intake();
    m_indexer = new Indexer();

    m_currentlyLaunching = false;

    m_drivetrain.initDrivetrain();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }

    double curSpeed = 0.0;
    double curTurn = 0.0;
    m_drivetrain.arcadeDrive(curSpeed, curTurn);
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double curSpeed = 0.0;
    double curTurn = 0.0;

    boolean ampLauncherOn = false;
    boolean speakerLauncherOn = false;
    boolean intakeOn = false;
    boolean haveNote = false;
    boolean expelOn = false;

    double leftLauncherAmpSpeed = 0.30;
    double rightLauncherAmpSpeed = 0.30;

    double leftLauncherSpeakerSpeed = 0.80;
    double rightLauncherSpeakerSpeed = 0.80;

    double intakeSpeed = 0.80;
    
    PilotController.DesiredDirection desiredDirection = PilotController.DesiredDirection.NoChange;

    curSpeed = m_controller.getDriverSpeed();
    curTurn = m_controller.getDriverTurn();
    ampLauncherOn = m_controller.getAmpLaunchButton();
    speakerLauncherOn = m_controller.getSpeakerLaunchButton();
    desiredDirection = m_controller.getPilotChangeControls();
    intakeOn = m_controller.getIntakeButton();
    haveNote = m_indexer.readIndexSensor();
    expelOn = m_controller.getExpelButton();

    m_drivetrain.setDesiredDirection(desiredDirection);

    m_drivetrain.arcadeDrive(curSpeed, curTurn);

    if (m_currentlyLaunching) {
      /**
       * If we are launching to the amp, set the speed of the launch motors to amp speed and feed the note from indexer.
       * Else if we are launching to the speaker, set the speed of the launch motors to speaker speed and feed note from indexer.
       * If we are not launching, set launcher and indexer speed to 0.
       */
      if (ampLauncherOn) {
        m_launcher.setSpeed(leftLauncherAmpSpeed, rightLauncherAmpSpeed);
        m_indexer.feedNote();
      }
      else if(speakerLauncherOn) {
        m_launcher.setSpeed(leftLauncherSpeakerSpeed, rightLauncherSpeakerSpeed);
        m_indexer.feedNote();
      }
      else {
        m_launcher.setSpeed(0.0, 0.0);
        m_currentlyLaunching = false;
        m_indexer.stop();
      }
    }
    else {
      if (haveNote) {
        /**
         * If we are launching to the amp, set the speed of the launch motors to amp speed and feed the note from indexer. m_currentlyLaunching is set to true.
         * Else if we are launching to the speaker, set the speed of the launch motors to speaker speed and feed note from indexer. m_currentlyLaunching is set to true.
         * Else if we are expelling, set launcher, index, and intake speeds to expel speeds.
         * If we are not launching or expelling, set launcher, indexer, and intake speed to 0. m_currentlyLaunching is set to false.
         */
        if (ampLauncherOn) {
          m_launcher.setSpeed(leftLauncherAmpSpeed, rightLauncherAmpSpeed);
          m_indexer.feedNote();
          m_intake.setSpeed(0.0);
          m_currentlyLaunching = true;
        }
        else if(speakerLauncherOn) {
          m_launcher.setSpeed(leftLauncherSpeakerSpeed, rightLauncherSpeakerSpeed);
          m_indexer.feedNote();
          m_intake.setSpeed(0.0);
          m_currentlyLaunching = true;
        }
        else if(expelOn) {
          m_launcher.setSpeed(-leftLauncherAmpSpeed, -rightLauncherAmpSpeed);
          m_indexer.expelNote();
          m_intake.setSpeed(-intakeSpeed);
        } 
        else {
          m_launcher.setSpeed(0.0, 0.0);
          m_indexer.stop();
          m_intake.setSpeed(0.0);
          m_currentlyLaunching = false;
        }
      }
      else{
        /**
         * If intakeOn is true, sets the speed of the intake, sets indexer to note loading speed, and sets launcher speed to 0.
         * Else if we are expelling, set launcher, index, and intake speeds to expel speeds.
         * If we are not intaking or expelling, sets the launcher, index, and intake speeds to 0.
         */
        if (intakeOn) {
          m_intake.setSpeed(intakeSpeed);
          m_launcher.setSpeed(0.0, 0.0);
          m_indexer.loadNote();
        }
        else if(expelOn) {
          m_launcher.setSpeed(-leftLauncherAmpSpeed, -rightLauncherAmpSpeed);
          m_indexer.expelNote();
          m_intake.setSpeed(-intakeSpeed);
        }
        else {
          m_intake.setSpeed(0.0);
          m_indexer.stop();
          m_launcher.setSpeed(0.0, 0.0);
        }
      }
    }
   
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    double curSpeed = 0.0;
    double curTurn = 0.0;
    m_drivetrain.arcadeDrive(curSpeed, curTurn);
    m_launcher.setSpeed(0.0, 0.0);
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    double curSpeed = 0.0;
    double curTurn = 0.0;
    m_drivetrain.arcadeDrive(curSpeed, curTurn);
    m_launcher.setSpeed(0.0, 0.0);
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
    double curSpeed = 0.0;
    double curTurn = 0.0;
    m_drivetrain.arcadeDrive(curSpeed, curTurn);
    m_launcher.setSpeed(0.0, 0.0);
  }
}
