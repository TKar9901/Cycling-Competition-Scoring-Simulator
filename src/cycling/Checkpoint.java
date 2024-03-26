package cycling;
import java.util.ArrayList;

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
    
}
