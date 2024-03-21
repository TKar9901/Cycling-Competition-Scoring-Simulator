package cycling;
import java.util.ArrayList;

public class MountainCheckpoint extends Checkpoint{
    private int[][] pointsTable;
    private int[][] timeLog;
    private double gradient;
    private double location;
    private CheckpointType type;
    private double length;
    private int id;

    
    public MountainCheckpoint(double location, CheckpointType type, double gradient, double length, int id) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.gradient = gradient;
        this.length = length;
    }
    public int getId() {
        return this.id;
    }
}
