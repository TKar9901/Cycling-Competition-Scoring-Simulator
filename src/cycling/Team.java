package cycling;
import java.util.ArrayList;

public class Team {
    private int id;
    private String name;
    private String description;
    private ArrayList<Integer> riders;
    private static ArrayList<Team> teams;
    private static ArrayList<Integer> teamIds;

    public Team() {

    }
    public Team(String name, String description) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<Team.getTeamIds().size(); i++) {
				if(Team.getTeamIds().get(i) != id) {
					used = false;
				}
			}
		}
        teamIds.add(this.id);
        
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static ArrayList<Integer> getTeamIds() {
        return teamIds;
    }
    public static ArrayList<Team> getTeams() {
        return teams;
    }
    public int getId() {
        return this.id;
    }
    public static Team findTeam(int teamId) {
        Team team = new Team();
		for(int i=0; i < Team.teamIds.size(); i++) {
			if(Team.teamIds.get(i) == teamId) {
				team = Team.teams.get(i);
			}
		}
		return team;
    }
    public ArrayList<Integer> getRiders() {
        return this.riders;
    }
}
