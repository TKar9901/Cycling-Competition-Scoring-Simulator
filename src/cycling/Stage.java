package cycling;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Stage stores information regarding a stage
 * such as checkpoints and the properties associated with it
 * 
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 *
 */
public class Stage {
    private int id;
    private String name;
    @SuppressWarnings("unused")
    private LocalDateTime startTime;
    private double length;
    private Race race;
    @SuppressWarnings("unused")
    private String description;
    private StageType type;
    private Map<Integer, Checkpoint> checkpoints = new HashMap<Integer, Checkpoint>();
    private String state;
    private Map<Integer, LocalTime[]> riderTimes = new HashMap<Integer, LocalTime[]>();
    private Map<Integer, LocalTime> adjustedTimes = new HashMap<Integer, LocalTime>();
    private Map<Integer, Integer> sprinterClassification = new HashMap<Integer, Integer>();
    private ArrayList<Integer> riderPositions = new ArrayList<Integer>();
    private ArrayList<Integer> sprintCheckpointPositions = new ArrayList<Integer>();
    private static final int[] FLATSTAGEPOINTS = {50,30,20,18,16,14,12,10,8,7,6,5,4,3,2};
    private static final int[] MEDIUMSTAGEPOINTS = {30,25,22,19,17,15,13,11,9,7,6,5,4,3,2};
    private static final int[] HIGHMOUNTAINPOINTS = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
    private static final int[] TIMETRIALPOINTS = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};

    /**
     * An empty constructor used
     * to make a temporarily empty stage
     */
    public Stage() {
        
    }
    /**
     * Creates a new stage with the provided parameters
     * @param name The name of the stage
     * @param description The description of the stage
     * @param length The distance of the stage
     * @param startTime The time the stage begins
     * @param type The type of stage {@link StageType#FLAT},
     * {@link StageType#MEDIUM_MOUNTAIN}, {@link StageType#HIGH_MOUNTAIN} or
     * {@link StageType#TT}
     * @param id The unique id of the stage
     * @param race The list of all races in CyclingPortalImpl
     */
    public Stage(String name, String description, double length, LocalDateTime startTime, StageType type, int id, Race race) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.race = race;
        this.state = "in preparation";
    }

    //Calculate each riders points for the stage for sprinter classification, intermediate sprint checkpoints + stage finish time
    public Map<Integer, Integer> getSprinterPoints() {
        //Points for the stage itself
        for(int i=0; i<riderPositions.size(); i++) {
            switch(this.type) {
                case StageType.FLAT:
                    sprinterClassification.put(riderPositions.get(i), FLATSTAGEPOINTS[i]);
                    break;
                case StageType.MEDIUM_MOUNTAIN:
                    sprinterClassification.put(riderPositions.get(i), MEDIUMSTAGEPOINTS[i]);
                    break;
                case StageType.HIGH_MOUNTAIN:
                    sprinterClassification.put(riderPositions.get(i), HIGHMOUNTAINPOINTS[i]);
                    break;
                //This one will need to look at all racers time for this type, current implementation doesn't work
                case StageType.TT:
                    sprinterClassification.put(riderPositions.get(i), TIMETRIALPOINTS[i]);
            }
            
        }
        //TODO: Points for any checkpoints that are intermediate sprints, need to look at all racers times for this checkpoint
        return null;
    }

    /**
     * Adjusts the times of riders who finish within 1 second
     * of each other to both have the adjusted time that was
     * the fastest between the two
     * @param index What position to begin sorting from
     * @return The Map of riderIds and their newly adjusted times
     */
    public Map<Integer, LocalTime> adjustTimes(int index) {
        LocalTime currTime = riderTimes.get(riderPositions.get(index))[riderTimes.size()-1];
        LocalTime nextTime = riderTimes.get(riderPositions.get(index+1))[riderTimes.size()-1];
        if(adjustedTimes.isEmpty()) {
            adjustedTimes.put(riderPositions.get(riderPositions.get(0)), currTime);
            return adjustTimes(index+1);
        }
        else if(currTime.plusSeconds(1).isAfter(nextTime)) {
            adjustedTimes.put(riderPositions.get(riderPositions.get(index+1)), currTime);
            return adjustTimes(index+1);
        }
        else if(index+1 < riderPositions.size()){
            adjustedTimes.put(riderPositions.get(riderPositions.get(index+1)), nextTime);
            return adjustTimes(index+1);
        }
        return adjustedTimes;
    }
    
    public Map<Integer, Checkpoint> getCheckpoints() {
        return checkpoints;
    }
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public double getLength() {
        return this.length;
    }
    public String getState() {
        return this.state;
    }
    public void setState() {
        this.state = "waiting for results";
    }
    public StageType getType() {
        return this.type;
    }
    public void addResults(int id, LocalTime[] times) {
        this.riderTimes.put(id, times);
    }
    public LocalTime[] getResults(int id) {
        return this.riderTimes.get(id);
    }
    public int addMountainCheckpoint(double location, CheckpointType type, double gradient, double length) {
        int id = 0;
		boolean used = true;
		while(used) {
            id = (int)(Math.random() * 9000) + 1000;
            if(!checkpoints.containsKey(id)) {
                used = false;
            }
        }
        this.checkpoints.put(id, new MountainCheckpoint(location, type, gradient, length, id));
        return id;
    }
    public int addSprintCheckpoint(double location) {
        int id = 0;
		boolean used = true;
		while(used) {
            id = (int)(Math.random() * 9000) + 1000;
            if(!checkpoints.containsKey(id)) {
                used = false;
            }
        }
        this.checkpoints.put(id, new SprintCheckpoint(location, id));
        this.sprintCheckpointPositions.add(this.getCheckpoints().size()-1);
        return id;
    }
    public static Stage findCheckpointsStage(int checkpointId, Map<Integer, Race> races, ArrayList<Integer> stageIds) {
        Stage stage = new Stage();
        int[] raceIds = races.keySet().stream().mapToInt(Integer::intValue).toArray();
        for(int i=0; i<races.size(); i++) {
            for(int j=0; j<races.get(raceIds[i]).getStages().size(); i++) {
                if(races.get(raceIds[i]).getStages()
                .get(stageIds.get(j)).getCheckpoints().containsKey(checkpointId)) {
                    stage = races.get(raceIds[i]).getStages().get(stageIds.get(j));
                }
            }
        }
        return stage;
    }

    public Map<Integer, LocalTime[]> getRiderTimes() {
        return this.riderTimes;
    }
    public ArrayList<Integer> getRiderPositions() {
        return this.riderPositions;
    }
    public Race getRace() {
        return this.race;
    }
}
