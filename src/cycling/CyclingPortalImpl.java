package cycling;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.time.Duration;
import java.lang.Double;

/*TODO
 * UML Diagram - Tamanna -- done
 * Development Log - Jake
 * Writing more basic tests - Tamanna
 */

/**
 * CyclingPortalImpl is a compiling and functioning implementation
 * of the CyclingPortal interface.
 * 
 * @author Jake Klar
 * @author Tamanna Kar
 * @version 2.0
 *
 */
public class CyclingPortalImpl implements CyclingPortal {
	/**
	 * Map of races in this portal
	 */
	private Map<Integer, Race> races;
	/**
	 * Map of teams in this portal
	 */
	private Map<Integer, Team> teams;
	/**
	 * List of usedStageIds in this portal
	 */
	private ArrayList<Integer> usedStageIds;
	/**
	 * List of usedCheckpointIds in this portal
	 */ 
	private ArrayList<Integer> usedCheckpointIds;
	/**
	 * List of usedRiderIds in this portal
	 */
	private ArrayList<Integer> usedRiderIds;

	/**
	 * Initial empty consutrctor for new CyclingPortalImpl.
	 * For first time initalization.
	 * There are no races, stages, teams or riders
	 */
	public CyclingPortalImpl() {
		this.races = new HashMap<Integer, Race>();
		this.teams = new HashMap<Integer, Team>();
		this.usedStageIds = new ArrayList<Integer>();
		this.usedCheckpointIds = new ArrayList<Integer>();
		this.usedRiderIds = new ArrayList<Integer>();
	}

	// for testing
	@Override
	public String toString() {
		return "{" +
            "races='" + this.races + "'" +
            ", teams='" + this.teams + "'" +
            ", usedStageIds='" + this.usedStageIds + "'" +
            ", usedCheckpointIds='" + this.usedCheckpointIds + "'" +
            ", usedRiderIds='" + this.usedRiderIds + "'" +
            "}";
	}

	/**
	 * Get the races currently created in the platform.
	 * 
	 * @return An array of race IDs in the system or an empty array if none exists.
	 * 
	 */
	@Override
	public int[] getRaceIds() {
		if(races.size() < 1) {
			return new int[] {};
		} else {
			return races.keySet().stream().mapToInt(Integer::intValue).toArray();
		}
		
	}
	/**
	 * The method creates a staged race in the platform with the given name and
	 * description.
	 * 
	 *
	 * 
	 * @param name        Race's name.
	 * @param description Race's description (can be null).
	 * @throws IllegalNameException If the name already exists in the platform.
	 * @throws InvalidNameException If the name is null, empty, has more than 30
	 *                              characters, or has white spaces.
	 * @return the unique ID of the created race.
	 * 
	 */
	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		if(name==null || name=="" || name.length()>20 || name.contains(" ")){
			throw new InvalidNameException("You have entered an incorrectly formatted race name, ensure it is a string of characters with no spaces.");
		}
		for(Race r: races.values()) {
			if(r.getName() == name) {
				throw new IllegalNameException("You have entered a name that is already in use, ensure you are using unique name per race.");
			}
		}

		Race newRace = new Race(name, description, races);
		int id = newRace.getId();
		races.put(id, newRace);
		return id;
	}

	/**
	 * Get the details from a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return A formatted string containing the race ID, name, description,
	 * number of stages and total length 
	 * @throws IDNotRecognisedException If the ID does not match to any race in the
	 *                                  system.
	 */
	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}

		return races.get(raceId).toString();
	}

	/**
	 * The method removes the race and all its related information, i.e., stages,
	 * checkpoints, and results.
	 * 
	 * @param raceId The ID of the race to be removed.
	 * @throws IDNotRecognisedException If the ID does not match to any race in the
	 *                                  system.
	 */
	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		Race race = races.get(raceId);
		for(int i=0; i<race.getStages().size(); i++) {
			for(int j=0; j<race.getStages().get(i).getCheckpoints().size(); j++) {
				usedCheckpointIds.remove(usedCheckpointIds.indexOf
				(race.getStages().get(i).getCheckpoints().get(j).getId()));
			}
			usedStageIds.remove(usedStageIds.indexOf(race.getStages().get(i).getId()));
		}
		races.remove(raceId);
	}
	/**
	 * The method queries the number of stages created for a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return The number of stages created for the race.
	 * @throws IDNotRecognisedException If the ID does not match to any race in the
	 *                                  system.
	 */
	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		
		return races.get(raceId).getStages().size();
	}
	/**
	 * Creates a new stage and adds it to the race.
	 * 
	 * @param raceId      The race which the stage will be added to.
	 * @param stageName   An identifier name for the stage.
	 * @param description A descriptive text for the stage.
	 * @param length      Stage length in kilometres.
	 * @param startTime   The date and time in which the stage will be raced. It
	 *                    cannot be null.
	 * @param type        The type of the stage. This is used to determine the
	 *                    amount of points given to the winner.
	 * @return the unique ID of the stage.
	 * @throws IDNotRecognisedException If the ID does not match to any race in the
	 *                                  system.
	 * @throws IllegalNameException     If the name already exists in the platform.
	 * @throws InvalidNameException     If the name is null, empty, has more than 30
	 *                              	characters, or has white spaces.
	 * @throws InvalidLengthException   If the length is less than 5km.
	 */
	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type) 
	throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		boolean found = false;
		for(int r: getRaceIds()) {
			if(raceId == r) {
				found = true;
			}
			for(Stage s: races.get(r).getStages().values()) {
				if(s.getName() == stageName) {
					throw new IllegalNameException("You have entered a name that is already in use, ensure you are using unique name per stage.");
				}
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		if(stageName==null || stageName=="" || stageName.length()>20 || stageName.contains(" ")){
			throw new InvalidNameException("You have entered an incorrectly formatted stage name, ensure it is a string of characters with no spaces.");
		}
		if(Double.valueOf(length) == null || length<5) {
			throw new InvalidLengthException("You have entered an incorrectly formatted stage length, ensure it is a decimal greater than 5km.");
		}

		int id = races.get(raceId).addStage(stageName, description, length, startTime, type);
		usedStageIds.add(id);
		return id;
	}
	/**
	 * Retrieves the list of stage IDs of a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return An array of stage IDs ordered (from first to last) by their sequence in the
	 *         race or an empty array if none exists.
	 * @throws IDNotRecognisedException If the ID does not match to any race in the
	 *                                  system.
	 */
	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		if(races.get(raceId).getStages().size() < 1) {
			return new int[] {};
		}
		return races.get(raceId).getOrderedStageIds().stream().mapToInt(
			Integer::intValue).toArray();
	}
	/**
	 * Gets the length of a stage in a race, in kilometres.
	 * 
	 * @param stageId The ID of the stage being queried.
	 * @return The stage's length.
	 * @throws IDNotRecognisedException If the ID does not match to any stage in the
	 *                                  system.
	 */
	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		double length = Race.findStage(stageId, races).getLength();
		return length;
	}
	/**
	 * Removes a stage and all its related data, i.e., checkpoints and results.
	 * 
	 * @param stageId The ID of the stage being removed.
	 * @throws IDNotRecognisedException If the ID does not match to any stage in the
	 *                                  system.
	 */
	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		int index = 0;
		for(int id: usedStageIds) {
			if(stageId == id) {
				index = usedStageIds.indexOf(id);
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		usedStageIds.remove(index);
		for(int i=0; i<Race.findStagesRace(stageId, races).getOrderedStageIds().size(); i++) {
			if(Race.findStagesRace(stageId, races).getOrderedStageIds().get(i)==stageId) {
				Race.findStagesRace(stageId, races).getOrderedStageIds().remove(i);
			}
		}
		Race.findStagesRace(stageId, races).getStages().remove(stageId);
		
	}
	/**
	 * Adds a climb checkpoint to a stage.
	 * 
	 * @param stageId         The ID of the stage to which the climb checkpoint is
	 *                        being added.
	 * @param location        The kilometre location where the climb finishes within
	 *                        the stage.
	 * @param type            The category of the climb - {@link CheckpointType#C4},
	 *                        {@link CheckpointType#C3}, {@link CheckpointType#C2},
	 *                        {@link CheckpointType#C1}, or {@link CheckpointType#HC}.
	 * @param averageGradient The average gradient for the climb.
	 * @param length          The length of the climb in kilometre.
	 * @return The ID of the checkpoint created.
	 * @throws IDNotRecognisedException   If the ID does not match to any stage in
	 *                                    the system.
	 * @throws InvalidLocationException   If the location is out of bounds of the
	 *                                    stage length.
	 * @throws InvalidStageStateException If the stage is "waiting for results".
	 * @throws InvalidStageTypeException  Time-trial stages cannot contain any
	 *                                    checkpoint.
	 */
	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(location<0 || location>Race.findStage(stageId, races).getLength()) {
			throw new InvalidLocationException("You have entered an incorrectly formatted location, ensure it is between 0 and length of the chosen stage.");
		}
		if(Race.findStage(stageId, races).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer add details to this stage as preparation phase has already been concluded.");
		}
		if(Race.findStage(stageId, races).getType() == StageType.TT) {
			throw new InvalidStageTypeException("You cannot add this checkpoint type to a time trial stage, ensure you have entered the intended stage ID.");
		}
		int cpId = Race.findStage(stageId, races).addMountainCheckpoint(location, type, averageGradient, length);
		usedCheckpointIds.add(cpId);
		return cpId;
	}
	/**
	 * Adds an intermediate sprint to a stage.
	 * 
	 * @param stageId  The ID of the stage to which the intermediate sprint checkpoint
	 *                 is being added.
	 * @param location The kilometre location where the intermediate sprint finishes
	 *                 within the stage.
	 * @return The ID of the checkpoint created.
	 * @throws IDNotRecognisedException   If the ID does not match to any stage in
	 *                                    the system.
	 * @throws InvalidLocationException   If the location is out of bounds of the
	 *                                    stage length.
	 * @throws InvalidStageStateException If the stage is "waiting for results".
	 * @throws InvalidStageTypeException  Time-trial stages cannot contain any
	 *                                    checkpoint.
	 */
	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(location<0 || location>Race.findStage(stageId, races).getLength()) {
			throw new InvalidLocationException("You have entered an incorrectly formatted location, ensure it is between 0 and length of the chosen stage.");
		}
		if(Race.findStage(stageId, races).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer change the details of this stage as preparation phase has already been concluded.");
		}
		if(Race.findStage(stageId, races).getType() == StageType.TT) {
			throw new InvalidStageTypeException("You cannot add this checkpoint type to a time trial stage, ensure you have entered the intended stage ID.");
		}
		int cpId = Race.findStage(stageId, races).addSprintCheckpoint(location);
		usedCheckpointIds.add(cpId);
		return cpId;
	}
	/**
	 * Removes a checkpoint from a stage.
	 * 
	 * @param checkpointId The ID of the checkpoint to be removed.
	 * @throws IDNotRecognisedException   If the ID does not match to any checkpoint in
	 *                                    the system.
	 * @throws InvalidStageStateException If the stage is "waiting for results".
	 */
	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		boolean found = false;
		int index = 0;
		for(int id: usedCheckpointIds) {
			if(checkpointId == id) {
				index = usedCheckpointIds.indexOf(id);
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined checkpoint.");
		}
		if(Stage.findCheckpointsStage(checkpointId, races, usedStageIds).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer change the details of this stage as preparation phase has already been concluded.");
		}
		usedCheckpointIds.remove(index);
		Stage.findCheckpointsStage(checkpointId, races, usedStageIds).getCheckpoints().remove(checkpointId);
	}
	/**
	 * Concludes the preparation of a stage. After conclusion, the stage's state
	 * will be "waiting for results".
	 * 
	 * @param stageId The ID of the stage to be concluded.
	 * @throws IDNotRecognisedException   If the ID does not match to any stage in
	 *                                    the system.
	 * @throws InvalidStageStateException If the stage is "waiting for results".
	 */
	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(Race.findStage(stageId, races).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer change the details of this stage as preparation phase has already been concluded.");
		}
		
		Race.findStage(stageId, races).setState();
	}
	/**
	 * Retrieves the list of checkpoint (mountains and sprints) IDs of a stage.
	 * 
	 * @param stageId The ID of the stage being queried.
	 * @return The list of checkpoint IDs ordered (from first to last) by their location in the
	 *         stage.
	 * @throws IDNotRecognisedException If the ID does not match to any stage in the
	 *                                  system.
	 */
	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		
		return Race.findStage(stageId, races).getCheckpoints().keySet().stream().
		mapToInt(Integer:: intValue).toArray();
	}
	/**
	 * Creates a team with name and description.
	 * 
	 * @param name        The identifier name of the team.
	 * @param description A description of the team.
	 * @return The ID of the created team.
	 * @throws IllegalNameException If the name already exists in the platform.
	 * @throws InvalidNameException If the name is null, empty, has more than 30
	 *                              characters, or has white spaces.
	 */
	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		for(Team t: teams.values()) {
			if(t.getName() == name) {
				throw new IllegalNameException("You have entered a name that is already in use, ensure you are using unique name per team.");
			}
		}
		
		if(name==null || name.equals("") || name.length()>20 || name.contains(" ")){ 
			throw new InvalidNameException("You have entered an incorrectly formatted team name, ensure it is a string of characters with no spaces.");
		}
		
		Team newTeam = new Team(name, description, teams);
		int id = newTeam.getId();
		teams.put(id, newTeam);
		return id;
	}
	/**
	 * Removes a team from the system.
	 * 
	 * @param teamId The ID of the team to be removed.
	 * @throws IDNotRecognisedException If the ID does not match to any team in the
	 *                                  system.
	 */
	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getTeams()) {
			if(teamId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined team.");
		}
		
		teams.remove(teamId);
	}
	/**
	 * Get the list of teams' IDs in the system.
	 * 
	 * @return The list of IDs from the teams in the system. An empty list if there
	 *         are no teams in the system.
	 * 
	 */
	@Override
	public int[] getTeams() {
		return teams.keySet().stream().mapToInt(Integer::intValue).toArray();
	}
	/**
	 * Get the riders of a team.
	 * 
	 * @param teamId The ID of the team being queried.
	 * @return A list with riders' ID.
	 * @throws IDNotRecognisedException If the ID does not match to any team in the
	 *                                  system.
	 */
	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getTeams()) {
			if(teamId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined team.");
		}
		
		return teams.get(teamId).getRiders()
		.stream().mapToInt(Integer::intValue).toArray();
	}
	/**
	 * Creates a rider.
	 * 
	 * @param teamId The ID rider's team.
	 * @param name The name of the rider.
	 * @param yearOfBirth The year of birth of the rider.
	 * @return The ID of the rider in the system.
	 * @throws IDNotRecognisedException If the ID does not match to any team in the
	 *                                  system.
	 * @throws IllegalArgumentException If the name of the rider is null or empty,
	 *                                  or the year of birth is less than 1900.
	 */
	@Override
	public int createRider(int teamId, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		boolean found = false;
		for(int id: getTeams()) {
			if(teamId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined team.");
		}
		int id = teams.get(teamId).addRider(name, yearOfBirth);
		usedRiderIds.add(id);
		return id;
	}

	/**
	 * Removes a rider from the system. When a rider is removed from the platform,
	 * all of its results are also removed. Race results are updated to reflect this.
	 * 
	 * @param riderId The ID of the rider to be removed.
	 * @throws IDNotRecognisedException If the ID does not match to any rider in the
	 *                                  system.
	 */
	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		boolean found = false;
		int index = 0;
		for(int id: usedRiderIds) {
			if(riderId == id) {
				index = usedRiderIds.indexOf(id);
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined rider.");
		}
		
		//Removes all of a riders scores in the stages they are inside
		ArrayList<Integer> riderRaces = Team.findRider(riderId, teams).getRacesEnrolled();
		if(riderRaces != null) {
			for(int i=0; i<riderRaces.size(); i++) {
				for(int j=0; j<races.get((riderRaces.get(i))).getStages().size();j++) {
					int[] currentRaceStages = races.get((riderRaces.get(i))).getStages().keySet().
					stream().mapToInt(Integer::intValue).toArray();
					int currStageId = races.get((riderRaces.get(i))).getStages().get(currentRaceStages[j]).getId();
					Race.findStage(currStageId, races).getRiderTimes().remove(riderId);
					Race.findStage(currStageId, races).getRiderPositions().remove(riderId);
					Race.findStage(currStageId, races).getAdjustedTimes().remove(riderId);
				}
				//Removes the rider from the races final elapsed times if it has been calculated
				races.get((riderRaces.get(i))).getAdjustedTimes().values().remove(riderId);
				
				
			}
			//Removes the rider from the team they were in
			
		}
		Team.findRider(riderId, teams).getTeam().getRiders().remove(riderId);
		usedRiderIds.remove(index);

	}
	/**
	 * Record the times of a rider in a stage.
	 * 
	 * @param stageId     The ID of the stage the result refers to.
	 * @param riderId     The ID of the rider.
	 * @param checkpoints An array of times at which the rider reached each of the
	 *                    checkpoints of the stage, including the start time and the
	 *                    finish line.
	 * @throws IDNotRecognisedException    If the ID does not match to any rider or
	 *                                     stage in the system.
	 * @throws DuplicatedResultException   Thrown if the rider has already a result
	 *                                     for the stage. Each rider can have only
	 *                                     one result per stage.
	 * @throws InvalidCheckpointTimesException Thrown if the length of checkpointTimes is
	 *                                     not equal to n+2, where n is the number
	 *                                     of checkpoints in the stage; +2 represents
	 *                                     the start time and the finish time of the
	 *                                     stage.
	 * @throws InvalidStageStateException  Thrown if the stage is not "waiting for
	 *                                     results". Results can only be added to a
	 *                                     stage while it is "waiting for results".
	 */
	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		boolean stageFound = false;
		boolean riderFound = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				stageFound = true;
			}
		}
		if(stageFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		for(int id: usedRiderIds) {
			if(riderId == id) {
				riderFound = true;
			}
		}
		if(riderFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined rider.");
		}
		if(Race.findStage(stageId, races).getState() == "in preparation") {
			throw new InvalidStageStateException("You cannot add results to this stage as preparation phase has not yet been concluded.");
		}
		if(Race.findStage(stageId, races).getRiderTimes().containsKey(riderId)) {
			throw new DuplicatedResultException("You have entered a riderID for which results have already been entered in this stage, ensure you are entering the correct stageID and riderID.");
		}
		boolean inOrder = true;
		for(int i=0; i<checkpoints.length-1; ++i) {
			if(checkpoints[i].compareTo(checkpoints[i+1])>0) {
				inOrder = false;
			}
		}
		if(inOrder == false || checkpoints.length != Race.findStage(stageId, races).getCheckpoints().size()+2) {
			throw new InvalidCheckpointTimesException("You have entered an incorrectly formatted checkpoints list, ensure it contains the rider's times in order for each checkpoint as well as the start and finish time of the given stage.");
		}
		
		//Calculate the elapsed time for that stage and convert it into the format wanted and add it to the stage's results
		LocalTime midnight = LocalTime.parse("00:00:00");
		Duration elapsedTime = Duration.between(checkpoints[0], checkpoints[checkpoints.length-1]);
		checkpoints[checkpoints.length-1] = midnight.plus(elapsedTime);
		Race.findStage(stageId, races).getRiderTimes().put(riderId, checkpoints);

		//If the race hasnt already been added to a rider's races, add it
		ArrayList<Integer> riderRaces = Team.findRider(riderId, teams).getRacesEnrolled();
		for(int i=0; i<riderRaces.size();i++) {
			if(riderRaces.get(i) == Race.findStage(stageId, races).getRace().getId()) {
				break;
			}
			if(i==riderRaces.size()-1) {
				Team.findRider(riderId, teams).getRacesEnrolled().add(Race.findStage(stageId, races).getRace().getId());
			}
		}
		//If the rider hasn't been added to the race for the stage, add it
		ArrayList<Rider> raceRiders = Race.findStagesRace(stageId, races).getRiders();
		if(raceRiders != null && !raceRiders.contains(Team.findRider(riderId, teams))) {
			Race.findStagesRace(stageId, races).getRiders().add(Team.findRider(riderId, teams));
		}
		

		//Insert the rider id at the appropriate position on the stages leaderboard
		int[] stagePositions = Race.findStage(stageId, races).getRiderPositions().stream().mapToInt(Integer::intValue).toArray();
		Map<Integer, LocalTime[]> riderTimes = Race.findStage(stageId, races).getRiderTimes();
		for(int i=0; i<stagePositions.length; i++) {
			LocalTime currentRacersFinish = riderTimes.get(stagePositions[i])[riderTimes.get(stagePositions[i]).length-1];
			//In the case it is the first result place it in
			if(stagePositions.length == 0) {
				Race.findStage(stageId, races).getRiderPositions().add(riderId);
				break;
			}
			//In the case it is faster than an index or the same speed place it before
			else if(checkpoints[checkpoints.length-1].compareTo(currentRacersFinish)
			==-1 || checkpoints[checkpoints.length-1].compareTo(currentRacersFinish)
			==0) {
				Race.findStage(stageId, races).getRiderPositions().add(i-1, riderId);
				break;
			}
			//In the case it is the slowest time yet
			else if(checkpoints[checkpoints.length-1].compareTo(riderTimes.get(stagePositions[stagePositions.length-1])[checkpoints.length-1])
			==1) {
				Race.findStage(stageId, races).getRiderPositions().add(riderId);
			}
				
		}
		
	}
	
	/**
	 * Get the times of a rider in a stage.
	 * 
	 * @param stageId The ID of the stage the result refers to.
	 * @param riderId The ID of the rider.
	 * @return The array of times at which the rider reached each of the checkpoints
	 *         of the stage and the total elapsed time. The elapsed time is the
	 *         difference between the finish time and the start time. Returns an
	 *         empty array if there is no result registered for the rider in the
	 *         stage.
	 * @throws IDNotRecognisedException If the ID does not match to any rider or
	 *                                  stage in the system.
	 */
	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		boolean stageFound = false;
		boolean riderFound = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				stageFound = true;
			}
		}
		if(stageFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		for(int id: usedRiderIds) {
			if(riderId == id) {
				riderFound = true;
			}
		}
		if(riderFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined rider.");
		}

		return Race.findStage(stageId, races).getRiderTimes().get(riderId);
	}

	/**
	 * Gets the adjusted elapsed times for the rider in the stage
	 * 
	 * @param stageId The ID of the stage the result refers to.
	 * @param riderId The ID of the rider.
	 * @return The adjusted elapsed time for the rider in the stage. Return null if 
	 * 		  there is no result registered for the rider in the stage.
	 * @throws IDNotRecognisedException   If the ID does not match to any rider or
	 *                                    stage in the system.
	 */
	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		boolean stageFound = false;
		boolean riderFound = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				stageFound = true;
			}
		}
		if(stageFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		for(int id: usedRiderIds) {
			if(riderId == id) {
				riderFound = true;
			}
		}
		if(riderFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined rider.");
		}
		if(!Race.findStage(stageId, races).getRiderTimes().containsKey(riderId)) {
			return null;
		}
		
		Map<Integer, LocalTime> adjustedTimes = Race.findStage(stageId, races).adjustTimes(0);
		return adjustedTimes.get(riderId);
	}
	/**
	 * Removes the stage results from the rider.
	 * 
	 * @param stageId The ID of the stage the result refers to.
	 * @param riderId The ID of the rider.
	 * @throws IDNotRecognisedException If the ID does not match to any rider or
	 *                                  stage in the system.
	 */
	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		boolean stageFound = false;
		boolean riderFound = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				stageFound = true;
			}
		}
		if(stageFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		for(int id: usedRiderIds) {
			if(riderId == id) {
				riderFound = true;
			}
		}
		if(riderFound == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined rider.");
		}
		
		Race.findStage(stageId, races).getRiderTimes().remove(riderId);
	}
	/**
	 * Get the riders finished position in a a stage.
	 * 
	 * @param stageId The ID of the stage being queried.
	 * @return A list of riders ID sorted by their elapsed time. An empty list if
	 *         there is no result for the stage.
	 * @throws IDNotRecognisedException If the ID does not match any stage in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(Race.findStage(stageId, races).getRiderTimes().size()<1) {
			return new int[] {};
		}
		
		return Race.findStage(stageId, races).getRiderPositions().stream().
		mapToInt(Integer::intValue).toArray();
	}
	/**
	 * Get the adjusted elapsed times of riders in a stage.
	 * 
	 * @param stageId The ID of the stage being queried.
	 * @return The ranked list of adjusted elapsed times sorted by their finish
	 *         time. An empty list if there is no result for the stage.
	 * @throws IDNotRecognisedException If the ID does not match any stage in the
	 *                                  system.
	 */
	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(Race.findStage(stageId, races).getRiderTimes().size() < 1) {
			return new LocalTime[] {};
		}
		
		Map<Integer, LocalTime> adjustedTimes = Race.findStage(stageId, races).adjustTimes(0);
		return adjustedTimes.values().toArray(new LocalTime[0]);
	}

	/**
	 * Get the number of points obtained by each rider in a stage.
	 * 
	 * @param stageId The ID of the stage being queried.
	 * @return The ranked list of points each riders received in the stage, sorted
	 *         by their elapsed time. An empty list if there is no result for the
	 *         stage.
	 * @throws IDNotRecognisedException If the ID does not match any stage in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(Race.findStage(stageId, races).getRiderPositions().size() < 1) {
			return new int[] {};
		}

		return Race.findStage(stageId, races).
		getSprinterPoints().values().stream().mapToInt(Integer::intValue).toArray();
	}
	/**
	 * Get the number of mountain points obtained by each rider in a stage.
	 * 
	 * @param stageId The ID of the stage being queried.
	 * @return The ranked list of mountain points each riders received in the stage,
	 *         sorted by their finish time. An empty list if there is no result for
	 *         the stage.
	 * @throws IDNotRecognisedException If the ID does not match any stage in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(Race.findStage(stageId, races).getRiderTimes().size()<1) {
			return new int[] {};
		}
		return Race.findStage(stageId, races).
		getMountainPoints().values().stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * Method empties this CyclingPortalImpl of its contents and resets all
	 * internal counters.
	 */
	@Override
	public void eraseCyclingPortal() {
		this.races = new HashMap<Integer, Race>();
		this.teams = new HashMap<Integer, Team>();
		this.usedCheckpointIds = new ArrayList<Integer>();
		this.usedStageIds = new ArrayList<Integer>();
		this.usedRiderIds = new ArrayList<Integer>();
	}
	/**
	 * Method saves this CyclingPortalImpl contents into a serialised file,
	 * with the filename given in the argument.
	 *
	 * @param filename Location of the file to be saved.
	 * @throws IOException If there is a problem experienced when trying to save the
	 *                     store contents to the file.
	 */
	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename)); 
		out.writeObject(this);
		out.close();
	}
	/**
	 * Method loads and replaces this CyclingPortalImpl contents with the
	 * serialised contents stored in the file given in the argument.
	 *
	 * @param filename Location of the file to be loaded.
	 * @throws IOException            If there is a problem experienced when trying
	 *                                to load the store contents from the file.
	 * @throws ClassNotFoundException If required class files cannot be found when
	 *                                loading.
	 */
	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
		CyclingPortalImpl loadedImpl = (CyclingPortalImpl) in.readObject();
		this.races = loadedImpl.races;
		this.teams = loadedImpl.teams;
		this.usedCheckpointIds = loadedImpl.usedCheckpointIds;
		this.usedRiderIds = loadedImpl.usedRiderIds;
		this.usedStageIds = loadedImpl.usedStageIds;
		in.close();
	}
	/**
	 * The method removes the race and all its related information, i.e., stages,
	 * checkpoints, and results.
	 * 
	 * @param name The name of the race to be removed.
	 * @throws NameNotRecognisedException If the name does not match to any race in
	 *                                    the system.
	 */
	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		boolean found = false;
		for(Race r: races.values()) {
			if(name == r.getName()) {
				found = true;
			}
		}
		if(found == false) {
			throw new NameNotRecognisedException("You have entered an unrecognisable name, ensure the name requested matches a previously defined race.");
		}
		
		Race race = new Race();
		ArrayList<Race> raceList = new ArrayList<Race>(races.values());
        for(int i=0; i<raceList.size(); i++) {
            if(raceList.get(i).getName().equals(name)) {
				race = races.get(raceList.get(i).getId());
            }
        }
		for(int i=0; i<race.getStages().size(); i++) {
			for(int j=0; j<race.getStages().get(i).getCheckpoints().size(); j++) {
				usedCheckpointIds.remove(usedCheckpointIds.indexOf
				(race.getStages().get(i).getCheckpoints().get(j).getId()));
			}
			usedStageIds.remove(usedStageIds.indexOf(race.getStages().get(i).getId()));
		}
		races.remove(race.getId());
		
	}
	/**
	 * Get the general classification times of riders in a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return A list of riders' times sorted by the sum of their adjusted elapsed
	 *         times in all stages of the race. An empty list if there is no result
	 *         for any stage in the race.
	 * @throws IDNotRecognisedException If the ID does not match any race in the
	 *                                  system.
	 */
	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}

		Race race = races.get(raceId);
		//Testing for the case where no stages have any results
		for(int i=0; i<race.getStages().size(); i++) {
			if(race.getStages().get(usedStageIds.get(i)).getRiderPositions().size() > 0) {
				break;
			}
			else if(i==race.getStages().size()-1){
				return new LocalTime[] {};
			}
		}
		LocalTime[] generalClassificationTimes = new LocalTime[race.getRiders().size()];
		int[] allStages = race.getStages().keySet().stream().mapToInt(Integer::intValue).toArray();
		LocalTime currentRiderTime;
		for(int i=0; i<race.getRiders().size(); i++) {
			currentRiderTime = LocalTime.parse("00:00:00");
			for(int j=0; j<allStages.length; j++) {
				Duration dur = Duration.between(currentRiderTime, getRiderAdjustedElapsedTimeInStage
				(allStages[j], race.getRiders().get(i).getId()));
				currentRiderTime.plus(dur);
			}
			race.addAdjustedTime(currentRiderTime, race.getRiders().get(i).getId());
			generalClassificationTimes[i] = currentRiderTime;
		}
		Arrays.sort(generalClassificationTimes);
		return generalClassificationTimes;
	}
	/**
	 * Get the overall points of riders in a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return An array of riders' points (i.e., the sum of their points in all stages
	 *         of the race), sorted by the total adjusted elapsed time. An empty array if
	 *         there is no result for any stage in the race.
	 * @throws IDNotRecognisedException If the ID does not match any race in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		Race race = races.get(raceId);
		for(int i=0; i<race.getStages().size(); i++) {
			if(race.getStages().get(usedStageIds.get(i)).getRiderPositions().size() > 0) {
				break;
			}
			else if(i==race.getStages().size()-1){
				return new int[] {};
			}
		}
		int[] generalClassificationRanks = getRidersGeneralClassificationRank(raceId);
		Map<Integer, Integer> sprinterPoints = new HashMap<>(races.get(raceId).
		getSprinterPoints());
		int[] sortedPoints = new int[generalClassificationRanks.length];
		for(int i=0; i<sortedPoints.length; i++) {
			sortedPoints[i] = sprinterPoints.get(generalClassificationRanks[i]);
		}
		return sortedPoints;
	}
	/**
	 * Get the overall mountain points of riders in a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return An array of riders' mountain points (i.e., the sum of their mountain
	 *         points in all stages of the race), sorted by the total adjusted elapsed time.
	 *         An empty array if there is no result for any stage in the race.
	 *  
	 * @throws IDNotRecognisedException If the ID does not match any race in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		int[] generalClassificationRanks = getRidersGeneralClassificationRank(raceId);
		Map<Integer, Integer> mountainPoints = new HashMap<>(races.get(raceId).
		getMountainPoints());
		int[] sortedPoints = new int[generalClassificationRanks.length];
		for(int i=0; i<sortedPoints.length; i++) {
			sortedPoints[i] = mountainPoints.get(generalClassificationRanks[i]);
		}
		return sortedPoints;
	}

	/**
	 * Get the general classification rank of riders in a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return A ranked list of riders' IDs sorted ascending by the sum of their
	 *         adjusted elapsed times in all stages of the race. That is, the first
	 *         in this list is the winner (least time). An empty list if there is no
	 *         result for any stage in the race.
	 * @throws IDNotRecognisedException If the ID does not match any race in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		Race race = races.get(raceId);
		//Testing for the case where no stages have any results
		for(int i=0; i<race.getStages().size(); i++) {
			if(race.getStages().get(usedStageIds.get(i)).getRiderPositions().size() > 0) {
				break;
			}
			else if(i==race.getStages().size()-1) {
				return new int[] {};
			}
		}
		LocalTime[] keys = getGeneralClassificationTimesInRace(raceId);
		int[] generalClassificationRanks = new int[keys.length];
		Map<LocalTime, Integer> times = races.get(raceId).getAdjustedTimes();
		for(int i=0; i<keys.length; i++) {
			generalClassificationRanks[i] = times.get(keys[i]);
		}
		return generalClassificationRanks;
		
	}
	/**
	 * Get the ranked list of riders based on the points classification in a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return A ranked list of riders' IDs sorted descending by the sum of their
	 *         points in all stages of the race. That is, the first in this list is
	 *         the winner (more points). An empty list if there is no result for any
	 *         stage in the race.
	 * @throws IDNotRecognisedException If the ID does not match any race in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		Race race = races.get(raceId);
		//Testing for the case where no stages have any results
		for(int i=0; i<race.getStages().size(); i++) {
			if(race.getStages().get(usedStageIds.get(i)).getRiderPositions().size() > 0) {
				break;
			}
			else if(i==race.getStages().size()-1) {
				return new int[] {};
			}
		}

		ArrayList<Integer> sortedPoints = races.get(raceId).sprinterPointsSorted();
		Map<Integer, Integer> sprinterMap = races.get(raceId).getSprinterClassification();
		int[] sprinterClassification = new int[sortedPoints.size()];
		for(int i=0; i<sortedPoints.size(); i++) {
			sprinterClassification[i] = sprinterMap.get(sortedPoints.get(i));
		}
		return sprinterClassification;
	}
	/**
	 * Get the ranked list of riders based on the mountain classification in a race.
	 * 
	 * @param raceId The ID of the race being queried.
	 * @return A ranked list of riders' IDs sorted descending by the sum of their
	 *         mountain points in all stages of the race. That is, the first in this
	 *         list is the winner (more points). An empty list if there is no result
	 *         for any stage in the race.
	 * @throws IDNotRecognisedException If the ID does not match any race in the
	 *                                  system.
	 */
	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		Race race = races.get(raceId);
		//Testing for the case where no stages have any results
		for(int i=0; i<race.getStages().size(); i++) {
			if(race.getStages().get(usedStageIds.get(i)).getRiderPositions().size() > 0) {
				break;
			}
			else if(i==race.getStages().size()-1) {
				return new int[] {};
			}
		}
		ArrayList<Integer> sortedPoints = races.get(raceId).mountainPointsSorted();
		Map<Integer, Integer> mountainMap = races.get(raceId).getMountainClassification();
		int[] mountainClassification = new int[sortedPoints.size()];
		for(int i=0; i<sortedPoints.size(); i++) {
			mountainClassification[i] = mountainMap.get(sortedPoints.get(i));
		}
		return mountainClassification;
	}

}
