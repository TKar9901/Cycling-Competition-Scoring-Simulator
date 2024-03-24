package cycling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.math.*;
import java.time.Duration;
import java.lang.Double;
import java.io.Serializable;;

/*TODO
 * Replace all functions with new map storage system - Jake
 * UML Diagram - Tamanna
 * Java Doc Comments - Jake
 * Development Log - Jake
 * Serilization functions - Wait
 * General Classification Functions - Wait
 * Writing more basic tests - Tamanna
 * If time doing the mountain points and sprint points - Wait
 */

/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Jake Klar, Tamanna Kar
 * @version 3.0
 *
 */
public class CyclingPortalImpl implements CyclingPortal {

	private Map<Integer, Race> races = new HashMap<Integer, Race>();
	private Map<Integer, Team> teams = new HashMap<Integer, Team>();
	private ArrayList<Integer> usedStageIds = new ArrayList<Integer>();
	private ArrayList<Integer> usedCheckpointIds = new ArrayList<Integer>();
	private ArrayList<Integer> usedRiderIds = new ArrayList<Integer>();

	//Empty constructor
	public CyclingPortalImpl() {

	}

	//Loading in constructor
	public CyclingPortalImpl(Map races, Map teams, ArrayList usedStageIds, ArrayList usedCheckpointIds) {

	}

	@Override
	public int[] getRaceIds() {
		if(races.size() < 1) {
			return new int[] {};
		} else {
			return races.keySet().stream().mapToInt(Integer::intValue).toArray();
		}
		
	}

	//Changed to randomized id system, same functinality but our old system would become more complicated once the length of the array it was based on changed
	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		if(name==null || name=="" || name.length()>20 || name.contains(" ")){ //TODO: FIND CHARACTER LIMIT FOR RACE NAME??
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
		races.remove(raceId);
	}

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
		if(stageName==null || stageName=="" || stageName.length()>20 || stageName.contains(" ")){ //TODO: FIND CHARACTER LIMIT FOR STAGE NAME??
			throw new InvalidNameException("You have entered an incorrectly formatted stage name, ensure it is a string of characters with no spaces.");
		}
		if(Double.valueOf(length) == null || length<5) {
			throw new InvalidLengthException("You have entered an incorrectly formatted stage length, ensure it is a decimal greater than 5km.");
		}

		int id = races.get(raceId).addStage(stageName, description, length, startTime, type);
		usedStageIds.add(id);
		return id;
	}

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
		
		return races.get(raceId).getStages().keySet()
		.stream().mapToInt(Integer::intValue).toArray();
	}

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

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedStageIds) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}

		Race.findStagesRace(stageId, races).getStages().remove(stageId);
	}

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
		
		return Race.findStage(stageId, races).addMountainCheckpoint(location, type, averageGradient, length);
	}

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
		int id = Race.findStage(stageId, races).addSprintCheckpoint(location);
		usedCheckpointIds.add(id);
		return id;
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		boolean found = false;
		for(int id: usedCheckpointIds) {
			if(checkpointId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined checkpoint.");
		}
		if(Stage.findCheckpointsStage(checkpointId, races, usedStageIds).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer change the details of this stage as preparation phase has already been concluded.");
		}
		
		Stage.findCheckpointsStage(checkpointId, races, usedStageIds).getCheckpoints().remove(checkpointId);
	}

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

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		for(Team t: teams.values()) {
			if(t.getName() == name) {
				throw new IllegalNameException("You have entered a name that is already in use, ensure you are using unique name per team.");
			}
		}
		
		if(name==null || name.equals("") || name.length()>20 || name.contains(" ")){ //TODO: FIND CHARACTER LIMIT FOR TEAM NAME??
			throw new InvalidNameException("You have entered an incorrectly formatted team name, ensure it is a string of characters with no spaces.");
		}
		
		Team newTeam = new Team(name, description, teams);
		int id = newTeam.getId();
		teams.put(id, newTeam);
		return id;
	}

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

	@Override
	public int[] getTeams() {
		return teams.keySet().stream().mapToInt(Integer::intValue).toArray();
	}

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
		
		return teams.get(teamId).getRiders().keySet()
		.stream().mapToInt(Integer::intValue).toArray();
	}

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

	//TODO: Come back to final leaderboard issue to remove racers from that too
	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: usedRiderIds) {
			if(riderId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined rider.");
		}
		
		//Removes all of a riders scores in the stages they are inside
		ArrayList<Integer> riderRaces = Team.findRider(riderId, teams).getRacesEnrolled();
		for(int i=0; i<riderRaces.size(); i++) {
			for(int j=0; j<races.get((riderRaces.get(i))).getStages().size();j++) {
				int[] currentRaceStages = races.get((riderRaces.get(i))).getStages().keySet().
				stream().mapToInt(Integer::intValue).toArray();
				int currStageId = races.get((riderRaces.get(i))).getStages().get(currentRaceStages[j]).getId();
				Race.findStage(currStageId, races).getRiderTimes().remove(riderId);
			}
			
		}
		
		Team.findRider(riderId, teams).getTeam().getRiders().remove(riderId);
	}
	//JAKE
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
		for(int i=0; i<checkpoints.length; ++i) {
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
	
	// should i bother adding other checks if he didnt declare them so is it fair to assume he won't test for them?.. applies to all functions onwards
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

	//I really am not sure if this works properly lol
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
		
		Map<Integer, LocalTime> adjustedTimes = Race.findStage(stageId, races).adjustTimes(0);
		return adjustedTimes.get(riderId);
	}

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
		
		return Race.findStage(stageId, races).getRiderPositions().stream().
		mapToInt(Integer::intValue).toArray();
	}

	//Again may or may not work
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
		
		Map<Integer, LocalTime> adjustedTimes = Race.findStage(stageId, races).adjustTimes(0);
		return adjustedTimes.values().toArray(new LocalTime[0]);
	}

	//May need help with this one not sure how to go about it
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
		
		// TODO Auto-generated method stub
		return null;
	}

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
		
		// TODO Auto-generated method stub
		return null;
	}

	//Unless I'm dense there isn't anything stored in this so I don't get what it's meant to do
	@Override
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename)); 
		out.writeObject(this);
		out.close();
	}

	//Have 0 clue to do what this is asking
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
		ArrayList<Race> raceList = new ArrayList<Race>(races.values());
        for(int i=0; i<raceList.size(); i++) {
            if(raceList.get(i).getName().equals(name)) {
                races.remove(raceList.get(i).getId());
            }
        }
	}

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

		// TODO Auto-generated method stub
		return null;
	}

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

		// TODO Auto-generated method stub
		return null;
	}

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

		// TODO Auto-generated method stub
		return null;
	}

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

		// TODO Auto-generated method stub
		return null;
	}

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

		// TODO Auto-generated method stub
		return null;
	}

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

		// TODO Auto-generated method stub
		return null;
	}

}
