package frc.robot;



import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Drivetrain {
    private TalonFX m_leftLeader;
    private TalonFX m_rightLeader;
    private TalonFX m_leftFollower;
    private TalonFX m_rightFollower;

    private Pigeon2 m_pigeon;

    private MotionMagicVoltage m_mmVoltage;

    private TalonFXConfiguration m_leftConfig = new TalonFXConfiguration();
    private TalonFXConfiguration m_rightConfig = new TalonFXConfiguration();

    //private DifferentialDrive m_drive;

    /**
     * The variable that keeps track of current drivetrain direction.
     * True is initial direction (forward). False is reversed control.
     */
    private boolean m_isDrivetrainForward;

    /**
     * Constructor for the drivetrain class. Instantiates the drivetrain motors and pigeon.
     */
    public Drivetrain(Pigeon2 pigeon) {
        // Sets the corresponding CAN IDs to each of the drivetrain motors.
        m_leftLeader = new TalonFX(RobotMap.DrivetrainConstants.LEFT_LEADER_CAN_ID);
        m_rightLeader = new TalonFX(RobotMap.DrivetrainConstants.RIGHT_LEADER_CAN_ID);
        m_leftFollower = new TalonFX(RobotMap.DrivetrainConstants.LEFT_FOLLOWER_CAN_ID);
        m_rightFollower = new TalonFX(RobotMap.DrivetrainConstants.RIGHT_FOLLOWER_CAN_ID);

        m_pigeon = pigeon;

        m_mmVoltage = new MotionMagicVoltage(0.0);

        m_isDrivetrainForward = true;
    }

    /**
     * Init function to intialize the Drivetrain in its entirety
     */
    public void initDrivetrain() {

        Pigeon2Configuration pigeonConfiguration = new Pigeon2Configuration();

        // Sets brake mode to the left and right configurations.
        m_leftConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        m_rightConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        // Counter clockwise is not inverted, clockwise is inverted.
        m_leftConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        m_rightConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        // Sets the Followers to follow the Leaders.
        m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
        m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));

        // Enables safety mode which requires input to the motors every cycle or it will set them to 0.
        m_leftLeader.setSafetyEnabled(true);
        m_rightLeader.setSafetyEnabled(true);

        m_isDrivetrainForward = true;

        m_rightConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = RobotMap.DrivetrainConstants.OPEN_RAMPS;
        m_leftConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = RobotMap.DrivetrainConstants.OPEN_RAMPS;

        this.configPID();

        m_pigeon.getYaw().setUpdateFrequency(RobotMap.DrivetrainConstants.UPDATE_FREQUENCY);
        m_rightLeader.getPosition().setUpdateFrequency(RobotMap.DrivetrainConstants.UPDATE_FREQUENCY);
        m_leftLeader.getPosition().setUpdateFrequency(RobotMap.DrivetrainConstants.UPDATE_FREQUENCY);

        // Applies the corresponding configurations to each drivetrain motor and the pigeon.
        m_leftLeader.getConfigurator().apply(m_leftConfig);
        m_leftFollower.getConfigurator().apply(m_leftConfig);
        m_rightLeader.getConfigurator().apply(m_rightConfig);
        m_rightFollower.getConfigurator().apply(m_rightConfig);
        m_pigeon.getConfigurator().apply(pigeonConfiguration);

        this.zeroSensors();
    }

    /**
     * Sets coast mode on the drivetrain motors.
     */
    public void coastMode() {
        m_leftConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        m_rightConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        
        m_leftLeader.getConfigurator().apply(m_leftConfig);
        m_leftFollower.getConfigurator().apply(m_leftConfig);
        m_rightLeader.getConfigurator().apply(m_rightConfig);
        m_rightFollower.getConfigurator().apply(m_rightConfig);
    }

    /**
     * Sets brake mode on the drivetrain motors.
     */
    public void brakeMode() {
        m_leftConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        m_rightConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        
        m_leftLeader.getConfigurator().apply(m_leftConfig);
        m_leftFollower.getConfigurator().apply(m_leftConfig);
        m_rightLeader.getConfigurator().apply(m_rightConfig);
        m_rightFollower.getConfigurator().apply(m_rightConfig);
    }

    /**
     * Zeros out the encoder positions and the Pigeon.
     */
    public void zeroSensors() {
        this.zeroDistance();
        m_pigeon.setYaw(0.0);
    }

    /**
     * Used to zero integrated sensors position.
     */
    public void zeroDistance() {
        m_leftLeader.setPosition(0.0);
        m_rightLeader.setPosition(0.0);
    }

    // /**
    //  * Method that makes the drivetrain move forward/backwards and turn.
    //  * @param speed Value between -1 and 1 for robot speed.
    //  * @param turn Value between -1 and 1 for turning
    //  */
    // public void arcadeDrive(double speed, double turn) {
    //     if(m_isDrivetrainForward == true) {
    //         m_drive.arcadeDrive(speed, turn);
    //     }
    //     else {
    //         m_drive.arcadeDrive(-speed, turn);
    //     }
    // }
    /**
     * Method that makes the drivetrain move forward/backwards and turn.
     * @param speed Value between -1 and 1 for robot speed.
     * @param turn  Value between -1 and 1 for turning.
     */
    public void arcadeDrive(double speed, double turn) {
        DutyCycleOut leftOut = new DutyCycleOut(0); // Initialize with 0% output
        DutyCycleOut rightOut = new DutyCycleOut(0); // Initialize with 0% output

        if (m_isDrivetrainForward) {
            leftOut.Output = speed - turn;
            rightOut.Output = speed + turn;
            m_leftLeader.setControl(leftOut);
            m_rightLeader.setControl(rightOut);
        } else {
            leftOut.Output = -speed - turn;
            rightOut.Output = -speed + turn;
            m_leftLeader.setControl(leftOut);
            m_rightLeader.setControl(rightOut);
        }
    }

    /**
     * Method for controlling the drivetrain by using a stick for each side.
     * @param ls speed for the left side between -1.0 and 1.0.
     * @param rs speed for the right side between -1.0 and 1.0.
     */
    public void tankDrive(double ls, double rs) {
        DutyCycleOut leftOut = new DutyCycleOut(0); // Initialize with 0% output
        DutyCycleOut rightOut = new DutyCycleOut(0); // Initialize with 0% output

        leftOut.Output = ls;
        rightOut.Output = rs;
        m_leftLeader.setControl(leftOut);
        m_rightLeader.setControl(rightOut);
    }

    /**
     * Method used to set motor directions while driving.
     * 
     * @param desiredDirection true represents the initial direction, false represents the reversed controls.
     */
    public void setDesiredDirection(PilotController.DesiredDirection desiredDirection) {

        if(desiredDirection == PilotController.DesiredDirection.Initial) {
            m_isDrivetrainForward = true;
        }
        else if(desiredDirection == PilotController.DesiredDirection.Reversed) {
            m_isDrivetrainForward = false;
        }
       
    }

    /**
     * Drive straight forward (or backward for negative distance) a set number of inches.
     * @param distance a double passed for setting what magnitude of distance the robot must travel in inches.
     * @return a boolean designating whether the target has been reached.
     */
    public boolean driveStraight(double distance) {

        double rotations = distance / 18.85;

        boolean reachedTarget = false;

        m_rightLeader.setControl(m_mmVoltage.withPosition(rotations));
        m_leftLeader.setControl(m_mmVoltage.withPosition(rotations));

        m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
        m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));

        double currentRots = m_rightLeader.getPosition().getValueAsDouble();
        double currentDistance = currentRots * 18.85;
        if ((Math.abs(currentDistance - distance) < 0.5)) {
            reachedTarget = true;
        }

        System.out.println("Target distance[" + distance + "] current?[" + currentDistance + "]");

        return reachedTarget;
    }

    /**
     * Method for turning to a target angle relative to robot starting position.
     * @param angle Target angle to turn to - 0 is straight ahead at start, left is positive, right is negative.
     * @return a boolean designating whether the target has been reached.
     */
    public boolean turnToAngle(double angle) {
        boolean reachedTarget = false;
        StatusSignal yaw = m_pigeon.getYaw();
        double currentYaw = yaw.getValueAsDouble();
        double target_turn = angle;
        System.out.println("current yaw [" + currentYaw + "]");

        // Checks if current yaw (angle) is within a certain deadband and adjusts turn speed accordingly.
        if (target_turn < (currentYaw - (RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND * 10))) {
            m_rightLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.LARGEST_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.LARGEST_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
            System.out.println("furthest deadband");
        } 
        else if (target_turn < (currentYaw - (RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND * 5))) {
            m_rightLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.LARGER_MIDDLE_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.LARGER_MIDDLE_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
            System.out.println("second deadband");
        }
        else if (target_turn < (currentYaw - (RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND * 3))) {
            m_rightLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.SMALLER_MIDDLE_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.SMALLER_MIDDLE_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
        }
        else if (target_turn < (currentYaw - (RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND))) {
            m_rightLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.SMALLEST_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.SMALLEST_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
        }
        else if (target_turn > (currentYaw + (RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND * 10))) {
            m_rightLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.LARGEST_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.LARGEST_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
        }
        else if (target_turn > (currentYaw + RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND * 5)) {
            m_rightLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.LARGER_MIDDLE_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.LARGER_MIDDLE_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
        }
        else if (target_turn > (currentYaw + RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND * 3)) {
            m_rightLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.SMALLER_MIDDLE_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.SMALLER_MIDDLE_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
        }
        else if (target_turn > (currentYaw + RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND)) {
            m_rightLeader.setControl(new DutyCycleOut(RobotMap.DrivetrainConstants.SMALLEST_ANGLE_SPEED));
            m_leftLeader.setControl(new DutyCycleOut(-RobotMap.DrivetrainConstants.SMALLEST_ANGLE_SPEED));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
        }
        else {
            m_rightLeader.setControl(new DutyCycleOut(0.0));
            m_leftLeader.setControl(new DutyCycleOut(0.0));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
        }

        if ((Math.abs(m_pigeon.getYaw().getValueAsDouble() - angle) < (RobotMap.DrivetrainConstants.DRIVE_ANGLE_DEADBAND * 2))) {
            System.out.println("TurnToAngle completed!!!!!!!!");
            m_rightLeader.setControl(new DutyCycleOut(0.0));
            m_leftLeader.setControl(new DutyCycleOut(0.0));
            m_leftFollower.setControl(new Follower(m_leftLeader.getDeviceID(), false));
            m_rightFollower.setControl(new Follower(m_rightLeader.getDeviceID(), false));
            reachedTarget = true;
        }

        return reachedTarget;
    }



    /**
     * Sets up the PID configuration for drive straight (or turn)
     */
    public void configPID() {

        /* FPID for Distance */
        Slot0Configs slot0 = m_rightConfig.Slot0;
        slot0.kV = RobotMap.DrivetrainConstants.DISTANCE_GAINS.kV;
        slot0.kP = RobotMap.DrivetrainConstants.DISTANCE_GAINS.kP;
        slot0.kI = RobotMap.DrivetrainConstants.DISTANCE_GAINS.kI;
        slot0.kD = RobotMap.DrivetrainConstants.DISTANCE_GAINS.kD;
        slot0.kS = 0.0;

        m_leftConfig.Slot0 = slot0;

        Slot1Configs slot1 = m_rightConfig.Slot1;
        slot1.kV = RobotMap.DrivetrainConstants.TURNING_GAINS.kV;
        slot1.kP = RobotMap.DrivetrainConstants.TURNING_GAINS.kP;
        slot1.kI = RobotMap.DrivetrainConstants.TURNING_GAINS.kI;
        slot1.kD = RobotMap.DrivetrainConstants.TURNING_GAINS.kD;
        slot1.kS = 0.0;

        m_leftConfig.Slot1 = slot1;

        MotionMagicConfigs mm = m_rightConfig.MotionMagic;

        mm.MotionMagicCruiseVelocity = 10.0;
        mm.MotionMagicAcceleration = 20.0;
        mm.MotionMagicJerk = 25.0;

        m_leftConfig.MotionMagic = mm;

        FeedbackConfigs fdb = m_rightConfig.Feedback;
        fdb.SensorToMechanismRatio = 9.82;

        m_leftConfig.Feedback = fdb;

    }
}
