package cycling;
import java.util.ArrayList;

/**
 * SprintCheckpoint extends the abstract
 * class checkpoint and stores information
 * specifically for: {@link CheckpointType#C4},
	 *                        {@link CheckpointType#C3}, {@link CheckpointType#C2},
	 *                        {@link CheckpointType#C1}, {@link CheckpointType#HC}
 * checkpoints
 */
public class MountainCheckpoint extends Checkpoint{
    @SuppressWarnings("unused")
    private double gradient;
    @SuppressWarnings("unused")
    private double location;
    private CheckpointType type;
    @SuppressWarnings("unused")
    private double length;
    private int id;

    /**
     * Creates a new MountainCheckpoint with the following parameters
     * @param location Where in the stage the checkpoint is
     * @param type The type: {@link CheckpointType#C4},
	 *                        {@link CheckpointType#C3}, {@link CheckpointType#C2},
	 *                        {@link CheckpointType#C1} or {@link CheckpointType#HC}
     * of MountainCheckpoint it is
     * @param gradient The average gradient for the checkpoint
     * @param length The length of the climb
     * @param id The unique id for the checkpoint
     */
    public MountainCheckpoint(double location, CheckpointType type, double gradient, double length, int id) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.gradient = gradient;
        this.length = length;
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
