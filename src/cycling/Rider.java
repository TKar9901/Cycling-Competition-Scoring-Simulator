package cycling;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rider {
    private int id;
    private String name;
    private int yearOfBirth;
    private int teamId;
    private ArrayList<Race> racesEnrolled;
    private static ArrayList<Rider> riders;
    private static ArrayList<Integer> riderIds;

    public Rider(int teamId, String name, int yearOfBirth) {
        int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<getRiderIds().size(); i++) {
				if(getRiderIds().get(i) != id) {
					used = false;
				}
			}
		}
        riderIds.add(id);

        this.id = id;
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public static ArrayList<Integer> getRiderIds() {
        return riderIds;
    }
    public static ArrayList<Rider> getRiders() {
        return riders;
    }
    public int getId() {
        return this.id;
    }
    public static Rider findRider(int id) {
        int pos = 0;
        for(int i=0; i<riderIds.size();i++) {
            if(riderIds.get(i) == id) {
                pos = i;
            }
        }
        return riders.get(pos);
    }
}