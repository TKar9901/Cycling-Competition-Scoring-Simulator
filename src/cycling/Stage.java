package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public Stage() {
        
    }

    public Stage(String name, String description, double length, LocalDateTime startTime, StageType type) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<Stage.getStageIds().size(); i++) {
				if(Stage.getStageIds().get(i) != id) {
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
		Stage stage = new Stage();
		for(int i=0; i < Stage.stageIds.size(); i++) {
			if(Stage.stageIds.get(i) == stageId) {
				stage = Stage.stages.get(i);
			}
		}
		return stage;
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
}
