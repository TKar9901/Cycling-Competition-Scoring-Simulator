package cycling;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team {
    private int id;
    private String name;
    private String description;
    private Map<Integer, Rider> riders;

    public Team() {

    }
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

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public Map<Integer, Rider> getRiders() {
        return this.riders;
    }
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
    public static Team findTeam(int teamId, Map<Integer, Team> teams) {
        return teams.get(teamId);
    } 
}
