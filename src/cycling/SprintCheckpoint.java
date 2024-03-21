package cycling;
import java.util.ArrayList;

public class SprintCheckpoint extends Checkpoint{
    private int[] pointsTable;
    private int[][] timeLog;
    private double location;
    private int id;
    private CheckpointType type;

    public SprintCheckpoint(double location, int id) {
        this.id = id;
        this.location = location;
    }
    public int getId() {
        return this.id;
    }
    public CheckpointType getType() {
        return this.type;
    }
}
