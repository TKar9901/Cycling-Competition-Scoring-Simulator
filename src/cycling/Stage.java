package cycling;
import java.time.LocalDateTime;

public class Stage {
    private int id;
    private String name;
    private LocalDateTime startTime;
    private double length;
    private String description;
    private StageType type;

    public Stage() {
        
    }

    public Stage(String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.id = 
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }
    public double getLength() {
        return this.length;
    }
}
