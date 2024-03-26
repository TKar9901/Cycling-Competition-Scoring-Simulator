package cycling;
import java.util.ArrayList;
import java.time.LocalTime;;

/**
 * Checkpoint holds information regarding
 * a checkpoint, it is an abstract unimplemented
 * class
 */
public abstract class Checkpoint {
    @SuppressWarnings("unused")
    private int id;
    @SuppressWarnings("unused")
    private CheckpointType type;

    /**
     * Gets the id for a checkpoint
     * @return The unique id of the checkpoint
     */
    public abstract int getId();
    /**
     * Gets the type of a checkpoint: The category of the climb - {@link CheckpointType#C4},
	 *                        {@link CheckpointType#C3}, {@link CheckpointType#C2},
	 *                        {@link CheckpointType#C1}, {@link CheckpointType#HC} or
     *                        {@link CheckpointType#SPRINT}
     * @return The type of checkpoint
     */
    public abstract CheckpointType getType();
    /**
     * Adds a riders time of passing the checkpoint
     * @param riderId The unique id of the rider
     * @param time The time that they pased the checkpoint
     */
    public abstract void addResult(int riderId, LocalTime time);
    /**
     * Sorts the recorded results for the checkpoint
     */
    public abstract void sortResults();
    /**
     * Gets the number of points a rider should be rewarded for the checkpoint
     * @param riderId The unique id of the rider
     * @return The number of points
     */
    public abstract int getRiderPointReward(int riderId);
}
