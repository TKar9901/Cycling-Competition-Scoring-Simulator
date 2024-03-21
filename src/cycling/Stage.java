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
    private ArrayList<Integer> riderPositions;
    private static ArrayList<Integer> usedCheckpointIds;
    
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
