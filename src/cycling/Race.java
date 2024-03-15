package cycling;
import java.time.LocalDateTime;

public class Race {
    private int[][] point_leaderboard;
    private int id;
    private static int[][] mountainCheckpointValues;
    private Stage[] stages;
    private Rider[] riders;
    private int generalClassification;
    private int sprinterClassification;
    private int mountainClassification;
    private String name;
    private String description;
    private double length;

    public Race() {
        
    }
    public Race(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    public int stageNumber() {
        return this.stages.length;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public double getLength() {
        double length = 0;
        for(int i = 0; i<stages.length - 1; i++) {
            length = length + stages[i].getLength();
        }
        return length;
    }
    public Stage[] getStages() {
        return this.stages;
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

