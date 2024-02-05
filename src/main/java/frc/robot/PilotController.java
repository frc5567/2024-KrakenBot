package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Encapsulates the pilot controller inputs
 */
public class PilotController {
    private XboxController m_controller;

    private SlewRateLimiter m_accelFilter;

    private SlewRateLimiter m_accelLTFilter;
    private SlewRateLimiter m_accelRTFilter;

    /**
     * The possible values for DesiredDirection input.
     */
    public enum DesiredDirection {
        Initial, 
        Reversed,
        NoChange
    };

    /**
     * Constructor for the pilot controller. Instantiates the xbox controller.
     */
    public PilotController() {
        m_controller = new XboxController(RobotMap.PilotControllerConstants.XBOX_CONTROLLER_USB_PORT);
        m_accelFilter = new SlewRateLimiter(RobotMap.PilotControllerConstants.ACCEL_SLEW_RATE);

        m_accelLTFilter = new SlewRateLimiter(RobotMap.PilotControllerConstants.ACCEL_SLEW_RATE);
        m_accelRTFilter = new SlewRateLimiter(RobotMap.PilotControllerConstants.ACCEL_SLEW_RATE);
    }

    /**
     * Sets the robot speed to the difference of the left and right triggers. 
     * Applies a slew rate limiter. This caps the max rate of change.
     * @return The speed adjusted for deadband.
     */
    public double getDriverSpeed() {
        double speed = m_controller.getRightTriggerAxis() - m_controller.getLeftTriggerAxis();
        speed = m_accelFilter.calculate(speed);
        return adjustForDeadband(speed);
    }

    public double getDriverLeftTank() {
        double speed = m_controller.getLeftY();
        double squaredInput = speed * speed;
        squaredInput = Math.copySign(squaredInput, speed);
        speed = m_accelLTFilter.calculate(squaredInput);
        return adjustForDeadband(speed);
    }

    public double getDriverRightTank() {
        double speed = m_controller.getRightY();
        double squaredInput = speed * speed;
        squaredInput = Math.copySign(squaredInput, speed);        
        speed = m_accelRTFilter.calculate(squaredInput);
        return adjustForDeadband(speed);
    }
    
    /**
     * Sets the turn speed using the left joystick.
     * Squares the turn input. This allows for more control by making large changes in input less impactful.
     * Also applies a turn scaler. (currently 0.7) Also allows for more control.
     * @return the turn speed adjusted for deadband.
     */
    public double getDriverTurn() {
        //Adjusting for a deadband to compensate for controller stick drift.
        double turnInput = -m_controller.getLeftX();
        double squaredTurnInput = turnInput * turnInput;
        squaredTurnInput = Math.copySign(squaredTurnInput, turnInput);

        double scaledTurnInput = (squaredTurnInput * RobotMap.PilotControllerConstants.TURN_SCALER);

        return adjustForDeadband(scaledTurnInput);
    }

    /**
     * Method used to obtain pilot input for launching to the amp.
     * @return the state of the A button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getAmpLaunchButton() {
        boolean ampLauncherInput = m_controller.getAButton();
        return ampLauncherInput;
    }

    /**
     * Method used to obtain pilot input for launching to the speaker.
     * @return the state of the B button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getSpeakerLaunchButton() {
        boolean speakerLauncherInput = m_controller.getBButton();
        return speakerLauncherInput;
    }

    /**
     * Method used to obtain pilot input for intake.
     * @return the state of the X button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getIntakeButton() {
        boolean intakeInput = m_controller.getXButton();
        return intakeInput;
    }

    /**
     * Method used to obtain pilot input for expelling.
     * @return the state of the Y button as a boolean. True if pressed, falsed if not pressed.
     */
    public boolean getExpelButton() {
        boolean expelInput = m_controller.getYButton();
        return expelInput;
    }

    /**
     * Sets the desired direction of the drivetrain. The desired direction defaults to NoChange. 
     * When the right bumper is pressed the desired direction is set to the initial direction. 
     * When the left bumper is pressed the desired direction is set to the opposite direction. 
     *  
     * @return desiredDirection set by the pilot based on input.
     */
    public DesiredDirection getPilotChangeControls() {
        DesiredDirection desiredDirection = DesiredDirection.NoChange;
        
        if(m_controller.getRightBumper()) {
            desiredDirection = DesiredDirection.Initial;
        }
        else if (m_controller.getLeftBumper()){
            desiredDirection = DesiredDirection.Reversed;
        }

       return desiredDirection;
    }
    
    /**
     * Deadband method for stick.
     * @param stickInput takes a value from -1 to 1.
     * @return input value adjusted for deadband. It is a double with a value between -1 and 1.
     */
    private double adjustForDeadband(double stickInput) {
        double retVal = 0;
        double deadband = RobotMap.PilotControllerConstants.STICK_DEADBAND;
    
        //take absolute value for simplification.
        double absStickInput = Math.abs(stickInput);

        //if the stick input is outside the deadband, adjust.
        if (!(absStickInput < deadband)) {
            retVal = (absStickInput / (1.0 - deadband));
            retVal = Math.copySign(retVal, stickInput);
        }

        return retVal;
    }    
}
