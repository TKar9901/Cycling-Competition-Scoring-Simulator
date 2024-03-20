package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rider {
    private int id;
    private String name;
    private int yearOfBirth;
    private Team team;
    private ArrayList<Integer> racesEnrolled;

    public Rider(String name, int yearOfBirth, int id, Team team) {
        this.id = id;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.team = team;
    }

    public int getId() {
        return this.id;
    }
    public ArrayList<Integer> getRacesEnrolled() {
        return this.racesEnrolled;
    }
    public Team getTeam() {
        return this.team;
    }
}