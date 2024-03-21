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
    private static Map<Integer, Race> races = new HashMap<Integer, Race>();
    private Map<Integer, Stage> stages = new HashMap<Integer, Stage>();
    private static ArrayList<Integer> usedStageIds;

    public Race() {
        
    }
    public Race(String name, String description) {
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
        races.put(this.id, this);
    }
    public int getId() {
        return this.id;
    }
    public int stageNumber() {
        return this.stages.size();
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public double getLength() {
        double length = 0;
        for(int i = 0; i<stages.size(); i++) {
            length = length + stages.get(i).getLength();
        }
        return length;
    }
    public static int[] getRaceIds() {
        return races.keySet().stream().mapToInt(Integer::intValue).toArray();
    }
    public static int[] getStageIds() {
        return usedStageIds.stream().mapToInt(Integer::intValue).toArray();
    }
    public static Map<Integer, Race> getRaces() {
        return races;
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
    public static Stage findStage(int stageId) {
        Race race = new Race();
        int[] raceIds = getRaceIds();
		for(int i=0; i<raceIds.length; i++) {
			if(races.get(raceIds[i]).getStages().containsKey(stageId)) {
                race = races.get(raceIds[i]);
            }
        }
        return race.getStages().get(stageId);
    }
    public static Race findStagesRace(int stageId) {
        return Race.findStage(stageId).getRace();
    }
    public static Race findRace(int raceId) {
        return races.get(raceId);
    }
    public static void removeName(String name) {
        ArrayList<Race> raceList = new ArrayList<Race>(getRaces().values());
        for(int i=0; i<raceList.size(); i++) {
            if(raceList.get(i).getName().equals(name)) {
                races.remove(raceList.get(i).getId());
            }
        }
        
    }

    //Any formatted string containing the race ID, name, description, the number of stages, and the total length (i.e., the sum of all stages' length).
    @Override
    public String toString() {
        return "{" +
            ", id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", length=' " + getLength() + "'" +
            "}";
    }

}

