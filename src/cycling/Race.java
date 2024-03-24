package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Race {
    private int id;
    private ArrayList<Rider> riders;
    private int[] generalClassification;
    private int[] sprinterClassification;
    private int[] mountainClassification;
    private String name;
    private String description;
    private double length;
    private Map<Integer, Stage> stages = new HashMap<Integer, Stage>();
    
    private static ArrayList<Integer> usedStageIds;

    public Race() {
        
    }
    public Race(String name, String description, Map<Integer, Race> races) {
        int id = 0;
		boolean used = true;
		while(used) {
            id = (int)(Math.random() * 9000) + 1000;
            if(!races.containsKey(id)) {
                used = false;
            }
        }

        this.name = name;
        this.description = description;
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public int addStage(String name, String description, double Length, LocalDateTime starTime, StageType type) {
        int id = 0;
		boolean used = true;
		while(used) {
            id = (int)(Math.random() * 9000) + 1000;
            if(!stages.containsKey(id)) {
                used = false;
            }
        }
        this.stages.put(id, new Stage(name, description, length, starTime, type, id, this));
        usedStageIds.add(id);
        return id;
    }
    public ArrayList<Rider> getRiders() {
        return this.riders;
    }
    public Map<Integer, Stage> getStages() {
        return this.stages;
    }
    public static Stage findStage(int stageId, Map<Integer, Race> races) {
        int correctId = 0;
        int[] raceIds = races.keySet().stream().mapToInt(Integer::intValue).toArray();
        for(int i=0; i<races.size(); i++) {
            if(races.get(raceIds[i]).getStages().containsKey(stageId)) {
                correctId = raceIds[i];
            }
        }
        return races.get(correctId).getStages().get(stageId);
    }
    public static Race findStagesRace(int stageId, Map<Integer, Race> races) {
        int correctId = 0;
        int[] raceIds = races.keySet().stream().mapToInt(Integer::intValue).toArray();
        for(int i=0; i<races.size(); i++) {
            if(races.get(raceIds[i]).getStages().containsKey(stageId)) {
                correctId = raceIds[i];
            }
        }
        return races.get(correctId);
    }

    //Any formatted string containing the race ID, name, description, the number of stages, and the total length (i.e., the sum of all stages' length).
    @Override
    public String toString() {
        return "{" +
            ", id='" + this.id + "'" +
            ", name='" + this.name + "'" +
            ", description='" + this.description + "'" +
            ", length=' " + this.length + "'" +
            "}";
    }

}

