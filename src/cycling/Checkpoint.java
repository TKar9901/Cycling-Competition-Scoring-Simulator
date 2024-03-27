package cycling;
import java.util.ArrayList;
import java.util.Map;
import java.time.LocalTime;;

/**
 * Checkpoint holds information regarding
 * a checkpoint, it is an abstract unimplemented
 * class
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 */
public abstract class Checkpoint {
    @SuppressWarnings("unused")
    private int id;
    @SuppressWarnings("unused")
    private double location;
    @SuppressWarnings("unused")
    private CheckpointType type;
    @SuppressWarnings("unused")
    private Map<LocalTime, Integer> riderTimes;
    @SuppressWarnings("unused")
    private ArrayList<LocalTime> sortedTimes;

    /**
     * Creates a new checkpoint with the given parameters
     * @param location Where in the stage this checkpoint is
     * @param id The unique id of this checkpoint
     */
    public Checkpoint(double location, int id) {
        this.location = location;
        this.id = id;
    }
    /**
     * Gets the id for a checkpoint
     * @return The unique id of this checkpoint
     */
    public abstract int getId();
    /**
     * Gets the type of a checkpoint: The category of the climb - {@link CheckpointType#C4},
	 *                        {@link CheckpointType#C3}, {@link CheckpointType#C2},
	 *                        {@link CheckpointType#C1}, {@link CheckpointType#HC} or
     *                        {@link CheckpointType#SPRINT}
     * @return The type of this checkpoint
     */
    public abstract CheckpointType getType();
    /**
     * Adds a riders time of passing the checkpoint
     * @param riderId The unique id of the rider
     * @param time The time that they pased this checkpoint
     */
    public abstract void addResult(int riderId, LocalTime time);
    /**
     * Sorts the recorded results for this checkpoint
     */
    public abstract void sortResults();
    /**
     * Gets the number of points a rider should be rewarded for this checkpoint
     * @param riderId The unique id of the rider
     * @return The number of points
     */
    public abstract int getRiderPointReward(int riderId);
}
