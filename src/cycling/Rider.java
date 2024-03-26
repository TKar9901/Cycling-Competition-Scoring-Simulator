package cycling;
import java.util.ArrayList;

/**
 * Rider stores information regard a rider such
 * as their properties and the races they belong to
 * 
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 */
public class Rider {
    private int id;
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private int yearOfBirth;
    private Team team;
    private ArrayList<Integer> racesEnrolled;

    /**
     * Creates a new rider with the given
     * parameters
     * @param name The name of the rider
     * @param yearOfBirth The year the rider was born
     * @param id The unique id of the rider
     * @param team The team the rider belongs to
     */
    public Rider(String name, int yearOfBirth, int id, Team team) {
        this.id = id;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.team = team;
    }
    /**
     * Gets the unique id of a rider
     * @return The unique id of a rider
     */
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