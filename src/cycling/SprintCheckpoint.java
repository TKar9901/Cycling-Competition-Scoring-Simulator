package cycling;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;

/**
 * SprintCheckpoint extends the abstract
 * class checkpoint and stores information
 * specifically for: {@link CheckpointType#SPRINT}
 * checkpoints
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 */
public class SprintCheckpoint extends Checkpoint{
    @SuppressWarnings("unused")
    private double location;
    private int id;
    private CheckpointType type;
    private Map<LocalTime, Integer> riderTimes;
    private ArrayList<LocalTime> sortedTimes;
    private static final int[] POINTS = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};

    /**
     * Creates a new SprintCheckpoint with the given parameters
     * @param location Where the checkpoint takes place in this stage
     * @param id The unique id of this checkpoint
     */
    public SprintCheckpoint(double location, int id) {
        super(location, id);
        this.riderTimes = new HashMap<LocalTime, Integer>();
        this.sortedTimes = new ArrayList<LocalTime>();
    }
    @Override
    public int getId() {
        return this.id;
    }
    @Override
    public CheckpointType getType() {
        return this.type;
    }
    @Override
    public void addResult(int riderId, LocalTime time) {
        riderTimes.put(time, riderId);
        sortedTimes.add(time);
    }
    @Override
    public void sortResults() {
        Collections.sort(sortedTimes);
    }
    @Override
    public int getRiderPointReward(int riderId) {
        int points = 0;
        for(int i=0; i<sortedTimes.size(); i++) {
            if(i < 15) {
                if(riderTimes.get(sortedTimes.get(i)) == riderId) {
                    points = POINTS[i];
                }
            }
        }
        return points;
    }
}
