package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;

public class GamePad extends GenericHID {

	/**
	 * Constructor, used for calling super constructor
	 * @param port Port the gamepad is connected to
	 */
	public GamePad(final int port) {
		super(port);
	}

    // this enum defines the buttons and what they do when active
	private enum GamePadControls {
		//Port values for the different gamepad buttons
		Intake(4),
		AmpLaunch(3),
		SpeakerLaunch(2),
		Expel(1),
		RightClimbExtend(5),
		LeftClimbExtend(6),
		RightClimbRetract(7),
		LeftClimbRetract(8),
		ClimbLock(9),
        ClimbUnlock(10);
        
		//Variable used to get port values 
		public final int portNum;

		//Enum constructor that allows for the different port values to be called upon
		GamePadControls(int newPortNum) {
			this.portNum = newPortNum;
		}
	}
    
}
