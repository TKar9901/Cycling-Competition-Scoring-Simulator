package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rider {
    private int id;
    private String name;
    private int yearOfBirth;
    private ArrayList<Race> racesEnrolled;

    public Rider(String name, int yearOfBirth, int id) {
        this.id = id;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public int getId() {
        return this.id;
    }
}