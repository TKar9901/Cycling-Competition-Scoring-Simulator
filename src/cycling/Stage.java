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
    private ArrayList<Integer> sprintCheckpointIds = new ArrayList<Integer>();
    private ArrayList<Integer> sprintCheckpointRef = new ArrayList<Integer>();
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
            if(i < 15) {
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
                    case StageType.TT:
                        sprinterClassification.put(riderPositions.get(i), TIMETRIALPOINTS[i]);
                } 
            }
            //If placing over 15th place, riders are given 0 points for stage finish
            else {
                sprinterClassification.put(riderPositions.get(i), 0);
            }
        }
        //Congregating all rider times for the all the sprint checkpoints in the stage
        for(int i=0; i < sprintCheckpointIds.size(); i++) {
            for(int j=0; j<riderPositions.size(); j++) {
                int currRider = riderPositions.get(j);
                checkpoints.get(sprintCheckpointIds.get(i)).addResult(currRider,
                riderTimes.get(currRider)[sprintCheckpointRef.get(i)]);
            }
            checkpoints.get(sprintCheckpointIds.get(i)).sortResults();
            
            //Calculates the amount of points to be rewarded for this checkpoint
            for(int k=0; k<riderPositions.size(); k++) {
                int points = checkpoints.get(sprintCheckpointIds.get(i))
                .getRiderPointReward(riderPositions.get(k));
                sprinterClassification.put(riderPositions.get(k), 
                riderPositions.get(k) + points);
            }
        }
        return sprinterClassification;
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
    /**
     * Gets the adjusted times of a race
     * @return The adjusted times of the race
     */
    public Map<Integer, LocalTime> getAdjustedTimes() {
        return this.adjustedTimes;
    }
    /**
     * Gets the checkpoints within a stage
     * @return A map of the checkpoint objects and ids within a stage
     */
    public Map<Integer, Checkpoint> getCheckpoints() {
        return checkpoints;
    }
    /**
     * Gets the id of a stage
     * @return The unique id of the stage
     */
    public int getId() {
        return this.id;
    }
    /**
     * Gets the name of a stage
     * @return The name of the stage
     */
    public String getName() {
        return this.name;
    }
    /**
     * Gets the length of a stage
     * @return The length of the stage
     */
    public double getLength() {
        return this.length;
    }
    /**
     * Gets the state of a tage
     * @return The state of the stage
     */
    public String getState() {
        return this.state;
    }
    /**
     * Sets the current state of a stage to "waiting for results".
     * Applies after the stage has finished being configured
     */
    public void setState() {
        this.state = "waiting for results";
    }
    /**
     * Gets the type of a stage
     * @return The type of stage: {@link StageType#FLAT},
     * {@link StageType#MEDIUM_MOUNTAIN}, {@link StageType#HIGH_MOUNTAIN} or
     * {@link StageType#TT} it is
     */
    public StageType getType() {
        return this.type;
    }
    /**
     * Adds the results of a stage for a specific rider
     * @param id The unique id of the rider these times belongs to
     * @param times The array of times each checkpoint was passed
     * inside the stage for that rider
     */
    public void addResults(int id, LocalTime[] times) {
        this.riderTimes.put(id, times);
    }
    /**
     * Gets the results for a rider in a stage
     * @param id The unique id of the rider whose results are being returned
     * @return The array of times each checkpoint was passed
     * inside the stage for the rider
     */
    public LocalTime[] getResults(int id) {
        return this.riderTimes.get(id);
    }
    /**
     * Adds a new mountain checkpoint to a stage
     * @param location Where in the stage this checkpoint is
     * @param type The category of the climb - {@link CheckpointType#C4},
	 *                        {@link CheckpointType#C3}, {@link CheckpointType#C2},
	 *                        {@link CheckpointType#C1}, or {@link CheckpointType#HC}
     * @param gradient The average gradient of the climb
     * @param length The length (in km) of the climb
     * @return The unique id of the checkpoint created
     */
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
    /**
     * Adds an intermediate sprint checkpoint to a stage
     * @param location Where in the stage this checkpoint is
     * @return The unique id of the checkpoint created
     */
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
        this.sprintCheckpointIds.add(id);
        this.sprintCheckpointRef.add(checkpoints.size()-1);
        return id;
    }
    /**
     * Finds the stage a checkpoint is in
     * @param checkpointId The unique id of the checkpoint being referenced
     * @param races The list of races in CyclingPortalImpl
     * @param stageIds The list of all stage ids in CyclingPortalImpl
     * @return The stage that the checkpoint that checkpointId belongs to is in
     */
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
    /**
     * Gets all of the times for every rider in a stage
     * @return A map containing all of the rider ids and their
     * times for the stage
     */
    public Map<Integer, LocalTime[]> getRiderTimes() {
        return this.riderTimes;
    }
    /**
     * Gets the finishing positions of every rider in a stage
     * @return An ordered list of the rider ids corresponding
     * to where they finished for this stage
     */
    public ArrayList<Integer> getRiderPositions() {
        return this.riderPositions;
    }
    /**
     * Gets the race that a stage belongs to
     * @return The race that the stage belongs to
     */
    public Race getRace() {
        return this.race;
    }
}
