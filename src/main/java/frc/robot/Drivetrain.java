package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drivetrain {
    private WPI_TalonFX m_leftLeader;
    private WPI_TalonFX m_rightLeader;
    private WPI_TalonFX m_leftFollower;
    private WPI_TalonFX m_rightFollower;



    private DifferentialDrive m_drive;

    /**
     * The variable that keeps track of current drivetrain direction.
     * True is initial direction (forward). False is reversed control.
     */
    private boolean m_isDrivetrainForward;

    /**
     * Main constructor for the drivetrain class
     * @param pidgey IMU pigeon instance
     */
    public Drivetrain() {
        m_leftLeader = new WPI_TalonFX(RobotMap.DrivetrainConstants.LEFT_LEADER_CAN_ID);
        m_rightLeader = new WPI_TalonFX(RobotMap.DrivetrainConstants.RIGHT_LEADER_CAN_ID);
        m_leftFollower = new WPI_TalonFX(RobotMap.DrivetrainConstants.LEFT_FOLLOWER_CAN_ID);
        m_rightFollower = new WPI_TalonFX(RobotMap.DrivetrainConstants.RIGHT_FOLLOWER_CAN_ID);
        
        m_leftFollower.follow(m_leftLeader);
        m_rightFollower.follow(m_rightLeader);
        
        m_drive = new DifferentialDrive(m_leftLeader, m_rightLeader);

        m_isDrivetrainForward = true;
    }

    /**
     * Init function to intialize the Drivetrain in its entirety
     */
    public void initDrivetrain() {
 
        m_leftLeader.setInverted(false);
        m_leftFollower.setInverted(false);
        
        m_rightLeader.setInverted(true);
        m_rightFollower.setInverted(true);

        m_isDrivetrainForward = true;
    }

    /**
     * Method that makes the drivetrain move forward/backwards and turn.
     * @param speed Value between -1 and 1 for robot speed.
     * @param turn Value between -1 and 1 for turning
     */
    public void arcadeDrive(double speed, double turn) {
        if(m_isDrivetrainForward == true) {
            m_drive.arcadeDrive(speed, turn);
        }
        else {
            m_drive.arcadeDrive(-speed, turn);
        }
    }

    /**
     * Method for controlling the drivetrain by using a stick for each side.
     * @param ls speed for the left side between -1.0 and 1.0.
     * @param rs speed for the right side between -1.0 and 1.0.
     */
    public void tankDrive(double ls, double rs) {
        m_drive.tankDrive(ls, rs);
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
}
