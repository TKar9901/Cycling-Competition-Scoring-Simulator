package cycling;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Rider stores information regard a rider such
 * as their properties and the races they belong to
 * 
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 */
public class Rider implements Serializable{
    /**
     * The unique id of a rider
     */
    private int id;
    @SuppressWarnings("unused")
    /**
     * The name of a rider
     */
    private String name;
    @SuppressWarnings("unused")
    /**
     * The year of birth of a rider
     */
    private int yearOfBirth;
    /**
     * The team a rider belongs to
     */
    private Team team;
    /**
     * A list of races the rider is apart of
     */
    private ArrayList<Integer> racesEnrolled;
    /**
     * Creates a new rider with the given
     * parameters
     * @param name The name of this rider
     * @param yearOfBirth The year this rider was born
     * @param id The unique id of this rider
     * @param team The team this rider belongs to
     */
    public Rider(String name, int yearOfBirth, int id, Team team) {
        this.id = id;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.team = team;
        this.racesEnrolled = new ArrayList<Integer>();
    }
    /**
     * Gets the unique id of a rider
     * @return The unique id of this rider
     */
    public int getId() {
        return this.id;
    }
    /**
     * Gets the list of races a rider is enrolled in
     * @return The list of races this rider is enrolled in
     */
    public ArrayList<Integer> getRacesEnrolled() {
        return this.racesEnrolled;
    }
    /**
     * Gets the team a rider belongs to
     * @return The team this rider belongs to
     */
    public Team getTeam() {
        return this.team;
    }
}