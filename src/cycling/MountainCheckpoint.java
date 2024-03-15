package cycling;
import java.util.ArrayList;

public class MountainCheckpoint extends Checkpoint{
    private int[][] pointsTable;
    private int[][] timeLog;
    private double gradient;
    private static ArrayList<Integer> checkpointIds;
    private static ArrayList<Checkpoint> checkpoints;
    private double location;
    private CheckpointType type;
    private double length;
    private int id;

    
    public MountainCheckpoint(double location, CheckpointType type, double gradient, double length) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<MountainCheckpoint.getCheckpointIds().size(); i++) {
				if(MountainCheckpoint.getCheckpointIds().get(i) != id) {
					used = false;
				}
			}
		}

        this.location = location;
        this.type = type;
        this.gradient = gradient;
        this.length = length;

        checkpointIds.add(this.id);
        checkpoints.add(this);
    }

    public static ArrayList<Integer> getCheckpointIds() {
        return checkpointIds;
    }
    public static ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
    public int getId() {
        return this.id;
    }
}
