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
    private static ArrayList<Integer> stageIds;
	private static ArrayList<Stage> stages;
    private static ArrayList<Checkpoint> checkpoints;
    private String state;
    //First column rider id second their times
    private Map<Integer, LocalTime[]> riderTimes;

    public Stage() {
        
    }

    public Stage(String name, String description, double length, LocalDateTime startTime, StageType type) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<getStageIds().size(); i++) {
				if(getStageIds().get(i) != id) {
					used = false;
				}
			}
		}
        stageIds.add(this.id);
        this.id = id;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        stages.add(this);
    }
    public static Stage findStage(int stageId) throws IDNotRecognisedException {
		int pos = 0;
		for(int i=0; i < stageIds.size(); i++) {
			if(stageIds.get(i) == stageId) {
				pos = i;
			}
		}
		return stages.get(pos);
	}
    public static ArrayList<Integer> getStageIds() {
        return stageIds;
    }
    public static ArrayList<Stage> getStages() {
        return stages;
    }
    public ArrayList<Checkpoint> getCheckpoints() {
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
}
