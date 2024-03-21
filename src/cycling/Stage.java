package cycling;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stage {
    private int id;
    private String name;
    private LocalDateTime startTime;
    private double length;
    private Race race;
    private String description;
    private StageType type;
    private Map<Integer, Checkpoint> checkpoints = new HashMap<Integer, Checkpoint>();
    private String state;
    private Map<Integer, LocalTime[]> riderTimes = new HashMap<Integer, LocalTime[]>();
    private Map<Integer, LocalTime> adjustedTimes = new HashMap<Integer, LocalTime>();
    private Map<Integer, Integer> sprinterClassification = new HashMap<Integer, Integer>();
    private ArrayList<Integer> riderPositions = new ArrayList<Integer>();
    //Integer will be the position in checkpoints it is at
    private ArrayList<Integer> sprintCheckpointPositions = new ArrayList<Integer>();
    private static ArrayList<Integer> usedCheckpointIds = new ArrayList<Integer>();
    private static final int[] FLATSTAGEPOINTS = {50,30,20,18,16,14,12,10,8,7,6,5,4,3,2};
    private static final int[] MEDIUMSTAGEPOINTS = {30,25,22,19,17,15,13,11,9,7,6,5,4,3,2};
    private static final int[] HIGHMOUNTAINPOINTS = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
    private static final int[] TIMETRIALPOINTS = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};

    
    public Stage() {
        
    }

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
        //Points for any checkpoints that are intermediate sprints, need to look at all racers times for this checkpoint

    }

    //Recursively determine times
    public Map<Integer, LocalTime> adjustTimes(int index) {
        LocalTime currTime = riderTimes.get(riderPositions.get(index))[riderTimes.size()-1];
        LocalTime nextTime = riderTimes.get(riderPositions.get(index+1))[riderTimes.size()-1];
        if(adjustedTimes.isEmpty()) {
            adjustedTimes.put(riderPositions.get(riderPositions.get(0)), currTime);
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
    public static int[] getCheckpointIds() {
        return usedCheckpointIds.stream().mapToInt(Integer::intValue).toArray();
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
        this.sprintCheckpointPositions.add(usedCheckpointIds.size()-1);
        return id;
    }
    public static Stage findCheckpointsStage(int checkpointId) {
        Stage stage = new Stage();
        int[] raceIds = Race.getRaceIds();
        int[] stageIds = Race.getStageIds();
        for(int i=0; i<raceIds.length; i++) {
            for(int j=0; j<Race.getRaces().get(raceIds[i]).getStages().size(); i++) {
                if(Race.getRaces().get(raceIds[i]).getStages()
                .get(stageIds[j]).getCheckpoints().containsKey(checkpointId)) {
                    stage = Race.getRaces().get(raceIds[i]).getStages().get(stageIds[j]);
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
