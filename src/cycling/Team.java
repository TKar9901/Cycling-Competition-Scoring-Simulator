package cycling;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team {
    private int id;
    private String name;
    private String description;
    private static Map<Integer, Team> teams = new HashMap<Integer, Team>();
    private Map<Integer, Rider> riders;
    private static ArrayList<Integer> usedRiderIds;

    public Team() {

    }
    public Team(String name, String description) {
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
        teams.put(this.id, this);
    }

    public static int[] getTeamIds() {
        return teams.keySet().stream().mapToInt(Integer::intValue).toArray();
    }
    public static int[] getRiderIds() {
        return usedRiderIds.stream().mapToInt(Integer::intValue).toArray();
    }
    public static Map<Integer, Team> getTeams() {
        return teams;
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
        usedRiderIds.add(id);
        return id;
    }
    public static Rider findRider(int riderId) {
        Team team = new Team();
        int[] teamIds = getTeamIds();
		for(int i=0; i<teamIds.length; i++) {
			if(teams.get(teamIds[i]).getRiders().containsKey(riderId)) {
                team = teams.get(teamIds[i]);
            }
        }
        return team.riders.get(riderId);
    }
    public static Team findTeam(int teamId) {
        return teams.get(teamId);
    } 
}
