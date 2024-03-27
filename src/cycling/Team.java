package cycling;
import java.util.HashMap;
import java.util.Map;
/**
 * Team stores information regarding a team
 * such as riders and the properties associated with it
 * 
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 *
 */
public class Team {
    private int id;
    private String name;
    @SuppressWarnings("unused")
    private String description;
    private Map<Integer, Rider> riders = new HashMap<Integer, Rider>();

    /**
     * An empty constructor used to make
     * a temporarily empty team
     */
    public Team() {

    }
    /**
     * Creates a new team with the provided parameters.
     * Also generates a unique random ID for the team.
     * @param name The name of this team.
     * @param description The description of this team.
     * @param teams The list of all teams in CyclingPortalImpl
     */
    public Team(String name, String description, Map<Integer, Team> teams) {
        int id = 0;
		boolean used = true;
		while(used) {
            id = (int)(Math.random() * 9000) + 1000;
            if(!teams.containsKey(id)) {
                used = false;
            }
        }
        this.id = id;
        this.name = name;
        this.description = description;
    }
    /**
     * Gets the id of a team
     * @return The unique ID of this team
     */
    public int getId() {
        return this.id;
    }
    /**
     * Gets the the name of a team
     * @return The name of this team
     */
    public String getName() {
        return this.name;
    }
    /**
     * Gets the riders inside a team
     * @return A map containing the rider ids and
     * rider objects for this team
     */
    public Map<Integer, Rider> getRiders() {
        return this.riders;
    }
    /**
     * Adds a rider to a team
     * @param name The name of the rider
     * @param yearOfBirth The year the rider was born
     * @return The unique ID of the rider added to this team
     */
    public int addRider(String name, int yearOfBirth) {
        int id = 0;
		boolean used = true;
		while(used) {
            id = (int)(Math.random() * 9000) + 1000;
            if(!riders.containsKey(id)) {
                used = false;
            }
        }
        this.riders.put(id, new Rider(name, yearOfBirth, id, this));
        return id;
    }
    /**
     * Finds a rider using their id
     * @param riderId The unique ID of the rider being searched for
     * @param teams The list of all teams in CyclingPortalImpl
     * @return The rider that riderId belongs to
     */
    public static Rider findRider(int riderId, Map<Integer, Team> teams) {
        Team team = new Team();
        int[] teamIds = teams.keySet().stream().mapToInt(Integer::intValue).toArray();
		for(int i=0; i<teams.size(); i++) {
			if(teams.get(teamIds[i]).getRiders().containsKey(riderId)) {
                team = teams.get(teamIds[i]);
            }
        }
        return team.riders.get(riderId);
    }
    /**
     * Finds a team using their team id
     * @param teamId The unique ID of the team being searched for
     * @param teams The list of all teams in CyclingPortalImpl
     * @return The team that teamId belongs to
     */
    public static Team findTeam(int teamId, Map<Integer, Team> teams) {
        return teams.get(teamId);
    } 
}
