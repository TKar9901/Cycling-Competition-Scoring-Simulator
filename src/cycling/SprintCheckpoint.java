package cycling;
import java.util.ArrayList;

public class SprintCheckpoint extends Checkpoint{
    private int[] pointsTable;
    private int[][] timeLog;
    private double location;
    private static ArrayList<Integer> checkpointIds;
    private static ArrayList<Checkpoint> checkpoints;
    private int id;

    public SprintCheckpoint(double location) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<getCheckpointIds().size(); i++) {
				if(getCheckpointIds().get(i) != id) {
					used = false;
				}
			}
		}
        this.id = id;
        this.location = location;

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
