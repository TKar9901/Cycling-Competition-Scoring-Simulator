package cycling;
import java.util.ArrayList;

/**
 * SprintCheckpoint extends the abstract
 * class checkpoint and stores information
 * specifically for: {@link CheckpointType#SPRINT}
 * checkpoints
 */
public class SprintCheckpoint extends Checkpoint{
    @SuppressWarnings("unused")
    private double location;
    private int id;
    private CheckpointType type;

    /**
     * Creates a new SprintCheckpoint with the given parameters
     * @param location Where the checkpoint takes place in the stage
     * @param id The unique id of the checkpoint
     */
    public SprintCheckpoint(double location, int id) {
        this.id = id;
        this.location = location;
    }
    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public CheckpointType getType() {
        return this.type;
    }
}
