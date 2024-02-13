package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Climber {
    TalonSRX m_leftLauncher;
    TalonSRX m_rightLauncher;

    /**
     * Constructor that instantiates and sets the inversion on the left and right climber motors.
     */
    Climber() {
        m_leftLauncher = new TalonSRX(RobotMap.ClimberConstants.LEFT_CLIMBER_CAN_ID);
        m_rightLauncher = new TalonSRX(RobotMap.ClimberConstants.RIGHT_CLIMBER_CAN_ID);
        //TODO: CHECK INVERSION WHEN TESTING !!
        m_leftLauncher.setInverted(false);
        m_rightLauncher.setInverted(true);
    }

    /**
     * Sets the speed of the left motor.
     */
    public void setLeftSpeed(double speed) {
        m_leftLauncher.set(TalonSRXControlMode.PercentOutput, speed);
    }

    /**
     * Sets the speed of the right motor.
     */
    public void setRightSpeed(double speed) {
        m_rightLauncher.set(TalonSRXControlMode.PercentOutput, speed);
    }

}
