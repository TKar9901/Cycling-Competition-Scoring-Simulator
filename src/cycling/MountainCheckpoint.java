package cycling;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private Map<LocalTime, Integer> riderTimes;
    private ArrayList<LocalTime> sortedTimes;
    private static final int[] CATFOURPOINTS = {1};
    private static final int[] CATTHREEPOINTS = {2,1};
    private static final int[] CATTWOPOINTS = {5,3,2,1};
    private static final int[] CATONEPOINTS = {10,8,6,4,2,1};
    private static final int[] CATHORSPOINTS = {20,15,12,10,8,6,4,2};

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
        switch(this.type) {
            case C4:
                if(riderTimes.get(sortedTimes.get(0)) == riderId) {
                    points = CATFOURPOINTS[0];
                }
            case C3:
                for(int i=0; i<sortedTimes.size(); i++) {
                    if(i<1) {
                        if(riderTimes.get(sortedTimes.get(i)) == riderId) {
                            points = CATTHREEPOINTS[i];
                        }
                    }
                }
            case C2:
                for(int i=0; i<sortedTimes.size(); i++) {
                    if(i<3) {
                        if(riderTimes.get(sortedTimes.get(i)) == riderId) {
                            points = CATTWOPOINTS[i];
                        }
                    }
                }
            case C1:
                for(int i=0; i<sortedTimes.size(); i++) {
                    if(i<5) {
                        if(riderTimes.get(sortedTimes.get(i)) == riderId) {
                            points = CATONEPOINTS[i];
                        }
                    }
                }
            case HC:
                for(int i=0; i<sortedTimes.size(); i++) {
                    if(i<8) {
                        if(riderTimes.get(sortedTimes.get(i)) == riderId) {
                            points = CATHORSPOINTS[i];
                        }
                    } 
                }
            case SPRINT:
                break;
            default:
                break;
        }
        
        return points;
    }
}
