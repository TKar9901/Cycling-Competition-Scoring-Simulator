package cycling;

import java.util.ArrayList;

public abstract class Checkpoint {
    private int id;
    private CheckpointType type;

    public abstract int getId();
    public abstract CheckpointType getType();
    
}
