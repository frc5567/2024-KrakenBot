package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class Launcher {
    WPI_TalonFX m_launcherLeft;
    WPI_TalonFX m_launcherRight;

    Launcher() {
        //TODO: Can ID for left launcher is currently 17. WILL NEED TO UPDATE.
        m_launcherLeft = new WPI_TalonFX(RobotMap.LauncherConstants.LEFT_LAUNCHER_CAN_ID);
        m_launcherLeft.setInverted(true);

        //TODO: Can ID for right launcher is currently 18. WILL NEED TO UPDATE.
        m_launcherRight = new WPI_TalonFX(RobotMap.LauncherConstants.RIGHT_LAUNCHER_CAN_ID);
        m_launcherRight.setInverted(false);
        
    }

    /**
     * Method that tells the motors how fast to spin using percentages.
     * 
     * @param leftSpeed percent power to spin the left motor.
     * @param rightSpeed percent power to spin the right motor.
     * 
     * @return void 
     */
    public void setSpeed(double leftSpeed, double rightSpeed) {

        m_launcherLeft.set(TalonFXControlMode.PercentOutput, leftSpeed);
        m_launcherRight.set(TalonFXControlMode.PercentOutput, rightSpeed);
    }
}
