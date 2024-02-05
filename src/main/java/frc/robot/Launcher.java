package frc.robot;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

public class Launcher {
    TalonFX m_launcherLeft;
    TalonFX m_launcherRight;

    final DutyCycleOut m_motorOutput = new DutyCycleOut(0);

    Launcher() {
        m_launcherLeft = new TalonFX(RobotMap.LauncherConstants.LEFT_LAUNCHER_CAN_ID);
        m_launcherLeft.setInverted(true);

        m_launcherRight = new TalonFX(RobotMap.LauncherConstants.RIGHT_LAUNCHER_CAN_ID);
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

        m_launcherLeft.setControl(m_motorOutput.withOutput(leftSpeed));
        m_launcherRight.setControl(m_motorOutput.withOutput(rightSpeed));
    }
}
