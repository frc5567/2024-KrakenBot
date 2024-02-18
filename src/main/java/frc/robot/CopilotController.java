package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class CopilotController {
    private XboxController m_controller;

    CopilotController() {
        m_controller = new XboxController(RobotMap.CopilotControllerConstants.XBOX_CONTROLLER_USB_PORT);

    }

    /**
     * Method used to obtain copilot input for retracting the right climber.
     * @return the state of the A button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getRightClimbRetract() {
        boolean rightClimbRetract = m_controller.getAButton();
        return rightClimbRetract;
    }

    /**
     * Method used to obtain copilot input for retracting the left climber.
     * @return the state of the X button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getLeftClimbRetract() {
        boolean leftClimbRetract = m_controller.getXButton();
        return leftClimbRetract;
    }

    /**
     * Method used to obtain copilot input for extending the right climber.
     * @return the state of the B button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getRightClimbExtend() {
        boolean rightClimbExtend = m_controller.getBButton();
        return rightClimbExtend;
    }

    /**
     * Method used to obtain copilot input for extending the left climber.
     * @return the state of the Y button as a boolean. True if pressed, false if not pressed.
     */
    public boolean getLeftClimbExtend() {
        boolean leftClimbExtend = m_controller.getYButton();
        return leftClimbExtend;
    }
}
