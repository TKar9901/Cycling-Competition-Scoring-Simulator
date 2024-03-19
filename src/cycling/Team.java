package cycling;
import java.util.ArrayList;
import java.util.Map;

public class Team {
    private int id;
    private String name;
    private String description;
    private static Map<Integer, Team> teams;
    private Map<Integer, Rider> riders;
    private static ArrayList<Integer> usedRiderIds;

    public Team() {

    }
    public Team(String name, String description) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<teams.size(); i++) {
				if(teams.containsKey(id) == false) {
					used = false;
				}
			}
		}
        this.id = id;
        this.name = name;
        this.description = description;
        teams.put(this.id, this);
    }

    public static int[] getTeamIds() {
        return teams.keySet().stream().mapToInt(Integer::intValue).toArray();
    }
    public static Map<Integer, Team> getTeams() {
        return teams;
    }
    public int getId() {
        return this.id;
    }
    public Map<Integer, Rider> getRiders() {
        return this.riders;
    }
    public int addRider(String name, int yearOfBirth) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<riders.size()-1; i++) {
				if(riders.containsKey(id) == false) {
					used = false;
				}
			}
		}
        this.riders.put(id, new Rider(name, yearOfBirth, id));
        return id;
    }
}
