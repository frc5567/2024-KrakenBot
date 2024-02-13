package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Launcher {
    TalonFX m_launcherLeft;
    TalonFX m_launcherRight;

    final DutyCycleOut m_motorOutput = new DutyCycleOut(0);

    Launcher() {
        m_launcherLeft = new TalonFX(RobotMap.LauncherConstants.LEFT_LAUNCHER_CAN_ID);
        //m_launcherLeft.setInverted(true);

        m_launcherRight = new TalonFX(RobotMap.LauncherConstants.RIGHT_LAUNCHER_CAN_ID);
       // m_launcherRight.setInverted(false);
        

        TalonFXConfiguration leftConfiguration = new TalonFXConfiguration();
        TalonFXConfiguration rightConfiguration = new TalonFXConfiguration();

        //leftConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        leftConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        rightConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        leftConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        rightConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        //m_leftLeader.setInverted(false);
        //m_rightLeader.setInverted(true);

        m_launcherLeft.getConfigurator().apply(leftConfiguration);
        m_launcherRight.getConfigurator().apply(rightConfiguration);

        m_launcherLeft.setSafetyEnabled(true);
        m_launcherRight.setSafetyEnabled(true);

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
