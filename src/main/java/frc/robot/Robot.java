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
  private PilotController m_pilotController;
  private CopilotController m_copilotController;
  private Launcher m_launcher;
  private Intake m_intake;
  private Indexer m_indexer;
  private Climber m_climber;

  private boolean m_currentlyLaunching;

  private int m_launchCounter = 0;


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
    m_pilotController = new PilotController();
    m_copilotController = new CopilotController();
    m_launcher = new Launcher();
    m_intake = new Intake();
    m_indexer = new Indexer();
    m_climber = new Climber();

    m_currentlyLaunching = false;

    m_drivetrain.initDrivetrain();
    //System.out.print("Robot Init");
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
    //System.out.println("Teleop Periodic");

    // set to false for arcade drive, true for tank drive
    boolean isTank = false;

    double curSpeed = 0.0;
    double curTurn = 0.0;

    double curLT = 0.0;
    double curRT = 0.0;

    boolean ampLauncherOn = false;
    boolean speakerLauncherOn = false;
    boolean intakeOn = false;
    boolean haveNote = false;
    boolean expelOn = false;
    boolean leftClimberExtending = false;
    boolean rightClimberExtending = false;
    boolean leftClimberRetracting = false;
    boolean rightClimberRetracting = false;

    double leftLauncherAmpSpeed = 0.40;
    double rightLauncherAmpSpeed = 0.40;

    //launcher speeds are offset for more predictable flight pattern
    double leftLauncherSpeakerSpeed = 0.95;
    double rightLauncherSpeakerSpeed = 0.95;

    double intakeSpeed = 0.50;

    double leftClimberSpeed = RobotMap.ClimberConstants.LEFT_SPEED;
    double rightClimberSpeed = RobotMap.ClimberConstants.RIGHT_SPEED;
    
    PilotController.DesiredDirection desiredDirection = PilotController.DesiredDirection.NoChange;

    curSpeed = m_pilotController.getDriverSpeed();
    curTurn = m_pilotController.getDriverTurn();

    curLT = -m_pilotController.getDriverLeftTank();
    curRT = -m_pilotController.getDriverRightTank();

    ampLauncherOn = m_pilotController.getAmpLaunchButton();
    speakerLauncherOn = m_pilotController.getSpeakerLaunchButton();
    desiredDirection = m_pilotController.getPilotChangeControls();
    intakeOn = m_pilotController.getIntakeButton();
    haveNote = m_indexer.readIndexSensor();
    expelOn = m_pilotController.getExpelButton();
    leftClimberExtending = m_copilotController.getLeftClimbExtend();
    rightClimberExtending = m_copilotController.getRightClimbExtend();
    leftClimberRetracting = m_copilotController.getLeftClimbRetract();
    rightClimberRetracting = m_copilotController.getRightClimbRetract();

    m_drivetrain.setDesiredDirection(desiredDirection);
    if (isTank) {
      m_drivetrain.tankDrive(curLT, curRT);
    }
    else {
      m_drivetrain.arcadeDrive(curSpeed, curTurn);
    }

    if (leftClimberExtending) {
      m_climber.setLeftSpeed(leftClimberSpeed);
    }
    else if (leftClimberRetracting) {
      m_climber.setLeftSpeed(-leftClimberSpeed);
    }
    else {
      m_climber.setLeftSpeed(0.0);
    }

    if (rightClimberExtending) {
      m_climber.setRightSpeed(rightClimberSpeed);
    }
    else if (rightClimberRetracting) { 
      m_climber.setRightSpeed(-rightClimberSpeed);
    }
    else {
      m_climber.setRightSpeed(0.0);
    }

    if (m_currentlyLaunching) {
      //System.out.println("currently launching");

      /**
       * If we are launching to the amp, set the speed of the launch motors to amp speed and feed the note from indexer.
       * Else if we are launching to the speaker, set the speed of the launch motors to speaker speed and feed note from indexer.
       * If we are not launching, set launcher and indexer speed to 0.
       */
      if (ampLauncherOn) {
        m_launcher.setSpeed(leftLauncherAmpSpeed, rightLauncherAmpSpeed);
        m_indexer.feedNoteAmp();
      }
      else if(speakerLauncherOn) {
        if (++m_launchCounter > 50) {
          m_indexer.feedNoteSpeaker();
        }
        else {
          m_indexer.stop();
        }
        m_launcher.setSpeed(leftLauncherSpeakerSpeed, rightLauncherSpeakerSpeed);

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
          m_indexer.feedNoteAmp();
          m_intake.setSpeed(0.0);
          m_currentlyLaunching = true;
        }
        else if(speakerLauncherOn) {
          if (++m_launchCounter > 50) {
            m_indexer.feedNoteSpeaker();
          }
          else {
            m_indexer.stop();
          }
          m_launcher.setSpeed(leftLauncherSpeakerSpeed, rightLauncherSpeakerSpeed);
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
          //System.out.println("Not launching\n");

        /**
         * If intakeOn is true, sets the speed of the intake, sets indexer to note loading speed, and sets launcher speed to 0.
         * Else if we are expelling, set launcher, index, and intake speeds to expel speeds.
         * If we are not intaking or expelling, sets the launcher, index, and intake speeds to 0.
         */
        if (intakeOn) {
          m_intake.setSpeed(intakeSpeed);
          m_launcher.setSpeed(0.0, 0.0);
          m_indexer.loadNote();
          
          //System.out.println("Currently intaking");
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
      m_launchCounter = 0;
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
