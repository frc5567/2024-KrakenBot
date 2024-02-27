package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class CopilotController {
    //private XboxController m_controller;
    private GamePad m_controller;

    CopilotController() {
        //m_controller = new XboxController(RobotMap.CopilotControllerConstants.XBOX_CONTROLLER_USB_PORT);
        m_controller = new GamePad(1);
    }

    /**
     * Method used to obtain pilot input for launching to the amp.
     * @return the state of the A button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getAmpLaunchButton() {
        boolean ampLauncherInput = m_controller.getRawButton(5);
        return ampLauncherInput;
    }

    /**
     * Method used to obtain pilot input for launching to the speaker.
     * @return the state of the B button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getSpeakerLaunchButton() {
        boolean speakerLauncherInput = m_controller.getRawButton(3);
        return speakerLauncherInput;
    }

    /**
     * Method used to obtain pilot input for intake.
     * @return the state of the X button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getIntakeButton() {
        boolean intakeInput = m_controller.getRawButton(12);
        return intakeInput;
    }

    /**
     * Method used to obtain pilot input for expelling.
     * @return the state of the Y button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getExpelButton() {
        boolean expelInput = m_controller.getRawButton(8);
        return expelInput;
    }

    /**
    //  * Method used to obtain copilot input for retracting the right climber.
    //  * @return the state of the A button as a boolean. True if pressed, false if not pressed.
    //  */
    // public boolean getRightClimbRetract() {
    //     boolean rightClimbRetract = m_controller.getAButton();
    //     return rightClimbRetract;
    // }

    // /**
    //  * Method used to obtain copilot input for retracting the left climber.
    //  * @return the state of the X button as a boolean. True if pressed, false if not pressed.
    //  */
    // public boolean getLeftClimbRetract() {
    //     boolean leftClimbRetract = m_controller.getXButton();
    //     return leftClimbRetract;
    // }

    // /**
    //  * Method used to obtain copilot input for extending the right climber.
    //  * @return the state of the B button as a boolean. True if pressed, false if not pressed.
    //  */
    // public boolean getRightClimbExtend() {
    //     boolean rightClimbExtend = m_controller.getBButton();
    //     return rightClimbExtend;
    // }

    // /**
    //  * Method used to obtain copilot input for extending the left climber.
    //  * @return the state of the Y button as a boolean. True if pressed, false if not pressed.
    //  */
    // public boolean getLeftClimbExtend() {
    //     boolean leftClimbExtend = m_controller.getYButton();
    //     return leftClimbExtend;
    // }
}
