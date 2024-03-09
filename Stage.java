import java.time.LocalDateTime;
public class Stage {
    private static int id;
    private static String name;
    private static LocalDateTime startTime;
    private static double length;
    private static String description;
    private static StageType type;

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
}
