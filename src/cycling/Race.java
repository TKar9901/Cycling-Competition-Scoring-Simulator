package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Race stores information regarding a race
 * such as stages and the properties associated with it
 * 
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 *
 */
public class Race {
    private int id;
    private ArrayList<Rider> riders;
    private String name;
    private String description;
    private Map<Integer, Stage> stages = new HashMap<Integer, Stage>();

    /**
     * Constructs a new race with the provided parameters. It will
     * also generate a unique random ID for the race.
     * @param name The name of the race
     * @param description The description of the race
     * @param races All of the races currently in CyclingPortalImpl
     */
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
    /**
     * Gets the id of a race
     * @return The id of the race.
     */
    public int getId() {
        return this.id;
    }
    /**
     * Gets the name of a race
     * @return The name of the race.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Gets the description of a race
     * @return The description of the race
     */
    public String getDescription() {
        return this.description;
    }
    /**
     * Creates a new stage with the given parameters.
     * Also generates a unique id for the stage
     * @param name The name of the stage
     * @param description The description of the stage
     * @param Length The distance of the stage
     * @param startTime The time the stage starts
     * @param type The type of race {@link StageType#FLAT},
     * {@link StageType#MEDIUM_MOUNTAIN}, {@link StageType#HIGH_MOUNTAIN} or
     * {@link StageType#TT}
     * @return The unique ID of the created stage
     */
    public int addStage(String name, String description, double length, LocalDateTime startTime, StageType type) {
        int id = 0;
		boolean used = true;
		while(used) {
            id = (int)(Math.random() * 9000) + 1000;
            if(!stages.containsKey(id)) {
                used = false;
            }
        }
        this.stages.put(id, new Stage(name, description, length, startTime, type, id, this));
        return id;
    }
    /**
     * Gets the riders in a race
     * @return An ArrayList of rider ids of those competing in the race
     */
    public ArrayList<Rider> getRiders() {
        return this.riders;
    }
    /**
     * Gets the stages in a race
     * @return A map containing the stage ids and stage objects of those in the race
     */
    public Map<Integer, Stage> getStages() {
        return this.stages;
    }
    /**
     * Finds a stage using a stageId
     * @param stageId The unique id representing the stage being searched for
     * @param races The list of all races in CyclingPortalImpl
     * @return The stage that stageId belongs to
     */
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
    /**
     * Finds the race a stage is apart of from the stage's id
     * @param stageId The unique id representing the stage inside the race being searched for
     * @param races The list of all races in CyclingPortalImpl
     * @return The race that the stage represented by stageId belongs to
     */
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
    /**
     * Calculates the length of a race from the length of all
     * its stages
     * @return The length of the race
     */
    public double getLength() {
        double length = 0;
        for(int i = 0; i<stages.size(); i++) {
            length = length + stages.get(i).getLength();
        }
        return length;
    }
    /**
     * Writes a formatted string containing information about a race
     * @return A formatted string containing the id,
     * name, description, number of stages and 
     * length of a race
     */
    @Override
    public String toString() {
        return "{" +
            ", id='" + this.id + "'" +
            ", name='" + this.name + "'" +
            ", description='" + this.description + "'" +
            ", stages='" + this.stages.size() + "'" +
            ", length=' " + this.getLength() + "'" +
            "}";
    }

}

