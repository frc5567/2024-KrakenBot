package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This class encapsulates the indexing mechanism!
 */
public class Indexer {
    // The motorcontroller that controls the indexer motor.
    private WPI_TalonSRX m_indexMotor; 

    // The sensor that tells the indexer when to stop or go.    
    private DigitalInput m_indexSensor;

    /**
     * Constructor that instantiates the index motor and sensor objects.
     */
    public Indexer() {
        //TODO: Set the IDs for both
        m_indexMotor = new WPI_TalonSRX(RobotMap.IndexerConstants.INDEXER_CAN_ID);

        //DIO 0, not on the CAN bus
        m_indexSensor = new DigitalInput(RobotMap.IndexerConstants.SENSOR_PORT);
    }
         
    /**
     * Sets the speed of the index motor.
     * @param speed Input speed of the motor in percent. From -1 to 1.
     */
    private void setIndexSpeed(double speed) {
        m_indexMotor.set(speed);
    }

    /**
     * Sets the speed of the index motor to zero.
     */
    public void stop() {
        setIndexSpeed(0.0);
    }

    /**
     * Reads the index sensors current value (boolean).
     * @return true if we have a note, false if there is no note.
     */
    public boolean readIndexSensor() {
        boolean haveANote = false;

        haveANote = !m_indexSensor.get();

        //System.out.println("Have a note [" + Boolean.toString(haveANote) + "]");

        return haveANote;
    }

    /**
     * Helper method used to load a note from the intake.
     * @return true if there is a note loaded, false if the note is not loaded.
     */
    public boolean loadNote() {
        boolean noteLoaded = readIndexSensor();

        if(noteLoaded) {
            setIndexSpeed(0.0);
        }
        else {
            setIndexSpeed(RobotMap.IndexerConstants.LOAD_SPEED);
        }

        return noteLoaded;
    }

    /**
     * Helper method used to feed a note to the launcher for the amp.
     */
    public void feedNoteAmp() {

        setIndexSpeed(RobotMap.IndexerConstants.AMP_FEED_SPEED);
    }

    /**
     * Helper method used to feed a note to the launcher for the speaker.
     */
    public void feedNoteSpeaker() {

        setIndexSpeed(RobotMap.IndexerConstants.SPEAKER_FEED_SPEED);
    }

    /**
     * Helper method used to expel the note.
     * Sets the index motor to the negative value of LOAD_SPEED.
     */
    public void expelNote() {

        setIndexSpeed(-RobotMap.IndexerConstants.LOAD_SPEED);
    }

}
