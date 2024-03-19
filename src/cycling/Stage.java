package cycling;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class Stage {
    private int id;
    private String name;
    private LocalDateTime startTime;
    private double length;
    private String description;
    private StageType type;
    private Map<Integer, Checkpoint> checkpoints;
    private String state;
    private Map<Integer, LocalTime[]> riderTimes;

    public Stage() {
        
    }

    public Stage(String name, String description, double length, LocalDateTime startTime, StageType type, int id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
    }
    public Map<Integer, Checkpoint> getCheckpoints() {
        return checkpoints;
    }
    public int getId() {
        return this.id;
    }
    public double getLength() {
        return this.length;
    }
    public void setState() {
        this.state = "waiting for results";
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
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<checkpoints.size()-1; i++) {
				if(checkpoints.containsKey(id) == false) {
					used = false;
				}
			}
		}
        this.checkpoints.put(id, new MountainCheckpoint(location, type, gradient, length));
        return id;
    }
    public int addSprintCheckpoint(double location) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<checkpoints.size()-1; i++) {
				if(checkpoints.containsKey(id) == false) {
					used = false;
				}
			}
		}
        this.checkpoints.put(id, new SprintCheckpoint(location));
        return id;
    }
    public static Stage findCheckpoint(int checkpointId) {
        Stage stage = new Stage();
        int[] raceIds = Race.getRaceIds();
        int[] stageIds = Race.getStageIds();
        for(int i=0; i<raceIds.length-1; i++) {
            for(int j=0; j<Race.getRaces().get(raceIds[i]).getStages().size()-1; i++) {
                if(Race.getRaces().get(raceIds[i]).getStages()
                .get(stageIds[j]).getCheckpoints().containsKey(checkpointId)) {
                    stage = Race.getRaces().get(raceIds[i]).getStages().get(stageIds[j]);
                }
            }
        }
        return stage;
    }
}
