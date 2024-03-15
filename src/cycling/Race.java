package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Race {
    private int[][] point_leaderboard;
    private int id;
    private static int[][] mountainCheckpointValues;
    private ArrayList<Stage> stages;
    private Rider[] riders;
    private int generalClassification;
    private int sprinterClassification;
    private int mountainClassification;
    private String name;
    private String description;
    private double length;
    private static ArrayList<Integer> raceIds;
	private static ArrayList<Race> races;

    public Race() {
        
    }
    public Race(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;

        raceIds.add(this.id);
        races.add(this);
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
        for(int i = 0; i<stages.size() - 1; i++) {
            length = length + stages.get(i).getLength();
        }
        return length;
    }
    public Stage[] getStages() {
        Stage[] arr = new Stage[stages.size()];
        return this.stages.toArray(arr);
    }
    public static ArrayList<Integer> getRaceIds() {
        return raceIds;
    }
    public static ArrayList<Race> getRaces() {
        return races;
    }
    public static Race findRace(int raceId) {
		Race race = new Race();
		for(int i=0; i < raceIds.size(); i++) {
			if(raceIds.get(i) == raceId) {
				race = races.get(i);
			}
		}
		return race;
	}
    public void addStage(Stage stage) {
        this.stages.add(stage);
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

