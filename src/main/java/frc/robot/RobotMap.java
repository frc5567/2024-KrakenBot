package frc.robot;

public class RobotMap {
    
    /**
     * All drivetrain constants
     */
    public static class DrivetrainConstants {

        /**
         * CAN ID of the left leader motor controller
         */
        public static final int LEFT_LEADER_CAN_ID = 5;

        /**
         * CAN ID of the right leader motor controller
         */
        public static final int RIGHT_LEADER_CAN_ID = 4;

        /**
         * CAN ID of the left follower motor controller
         */
        public static final int LEFT_FOLLOWER_CAN_ID = 7;

        /**
         * CAN ID of the left follower motor controller
         */
        public static final int RIGHT_FOLLOWER_CAN_ID = 6;

    }

    /**
     * All pilot controller constants
     */
    public static class PilotControllerConstants {

        /**
         * USB port number of the pilot x-box controller
         */
        public static final int XBOX_CONTROLLER_USB_PORT = 0;

        /**
         * Absolute value of the deadband range for stick input
         */
        public static final double STICK_DEADBAND = 0.09;

        /**
         * adjust output of turns to tone down the final output.
         */
        public static final double TURN_SCALER = 0.7;

        /*
         * Rate limit for acceleration to prevent brownouts. 
         * Initial value matches 2022 robot value of 2.25, but can be adjusted as needed
         */
        public static final double ACCEL_SLEW_RATE = 1.6;
    }

    /**
     * All copilot controller constants
     */
    public static class CopilotControllerConstants {

        /**
         * USB port number of the copilot x-box controller
         */
        public static final int XBOX_CONTROLLER_USB_PORT = 1;
    }

    /**
     * All Indexer constants
     */
    public static class IndexerConstants {

        /**
         * CAN ID of the index motor
         */
        public static final int INDEXER_CAN_ID = 10;

        /**
         * DIO port of the index sensor
         */
        public static final int SENSOR_PORT = 0;

        /**
         * Speed set to index motor for loading note from intake
         */
        public static final double LOAD_SPEED = 0.5;

        /**
         * Speed set to index motor for feeding note into launcher for amp.
         */
        public static final double AMP_FEED_SPEED = 0.3;

        /**
         * Speed set to index motor for feeding note into launcher for speaker.
         */
        public static final double SPEAKER_FEED_SPEED = 1.0;
    }

    /**
     * All Intake constants
     */
    public static class IntakeConstants {

        /**
         * CAN ID of the intake motor
         */
        public static final int INTAKE_CAN_ID = 19;

    }

    /**
     * All Launcher constants
     */
    public static class LauncherConstants {

        /**
         * CAN ID of the left launcher motor
         */
        public static final int LEFT_LAUNCHER_CAN_ID = 17;

        /**
         * CAN ID of the right launcher motor
         */
        public static final int RIGHT_LAUNCHER_CAN_ID = 18;
    }

    /**
     * All Climber constants.
     */
    public static class ClimberConstants {

        /**
         * CAN ID of the left climber motor
         */
        public static final int LEFT_CLIMBER_CAN_ID = 8;

        /**
         * CAN ID of the right climber motor
         */
        public static final int RIGHT_CLIMBER_CAN_ID = 9;

        /**
         * Speed set to left climber motor
         */
        public static final double LEFT_SPEED = 0.5;

        /**
         * Speed set to the right climber motor
         */
        public static final double RIGHT_SPEED = 0.5;
    }
}
