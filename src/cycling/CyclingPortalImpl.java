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

/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Jake Klar, Tamanna Kar
 * @version 3.0
 *
 */
public class CyclingPortalImpl implements CyclingPortal {

	@Override
	public int[] getRaceIds() {
		if(Race.getRaces().size() < 1) {
			return new int[] {};
		} else {
			return Race.getRaceIds();
		}
		
	}

	//Changed to randomized id system, same functinality but our old system would become more complicated once the length of the array it was based on changed
	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		if(name==null || name=="" || name.length()>20 || name.contains(" ")){ //TODO: FIND CHARACTER LIMIT FOR RACE NAME??
			throw new InvalidNameException("You have entered an incorrectly formatted race name, ensure it is a string of characters with no spaces.");
		}
		for(Race r: Race.getRaces().values()) {
			if(r.getName() == name) {
				throw new IllegalNameException("You have entered a name that is already in use, ensure you are using unique name per race.");
			}
		}

		Race newRace = new Race(name, description);
		return newRace.getId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Race.getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}

		return Race.findRace(raceId).toString();
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Race.getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}

		Race.getRaces().remove(raceId);
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Race.getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		
		return Race.findRace(raceId).getStages().size();
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type) 
	throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		boolean found = false;
		for(Race r: Race.getRaces().values()) {
			if(raceId == r.getId()) {
				found = true;
			}
			for(Stage s: r.getStages().values()) {
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

		return Race.findRace(raceId).addStage(stageName, description, length, startTime, type);
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Race.getRaceIds()) {
			if(raceId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined race.");
		}
		
		return Race.getRaces().get(raceId).getStages().keySet()
		.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Race.getStageIds()) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}

		int[] raceIds = getRaceIds();
		double length = 0;
		for(int i=0; i<raceIds.length; i++) {
			if(Race.findStagesRace(stageId).getStages().containsKey(stageId)) {
				length = Race.findStage(stageId).getLength();
			}
		}
		return length;
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Race.getStageIds()) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}

		Race.findStage(stageId).getRace().getStages().remove(stageId);
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		boolean found = false;
		for(int id: Race.getStageIds()) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(location<0 || location>Race.findStage(stageId).getLength()) {
			throw new InvalidLocationException("You have entered an incorrectly formatted location, ensure it is between 0 and length of the chosen stage.");
		}
		if(Race.findStage(stageId).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer add details to this stage as preparation phase has already been concluded.");
		}
		if(Race.findStage(stageId).getType() == StageType.TT) {
			throw new InvalidStageTypeException("You cannot add this checkpoint type to a time trial stage, ensure you have entered the intended stage ID.");
		}
		
		return Race.findStage(stageId).addMountainCheckpoint(location, type, averageGradient, length);
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		boolean found = false;
		for(int id: Race.getStageIds()) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(location<0 || location>Race.findStage(stageId).getLength()) {
			throw new InvalidLocationException("You have entered an incorrectly formatted location, ensure it is between 0 and length of the chosen stage.");
		}
		if(Race.findStage(stageId).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer change the details of this stage as preparation phase has already been concluded.");
		}
		if(Race.findStage(stageId).getType() == StageType.TT) {
			throw new InvalidStageTypeException("You cannot add this checkpoint type to a time trial stage, ensure you have entered the intended stage ID.");
		}
		
		return Race.findStage(stageId).addSprintCheckpoint(location);
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		boolean found = false;
		for(int id: Stage.getCheckpointIds()) {
			if(checkpointId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined checkpoint.");
		}
		if(Stage.findCheckpointsStage(checkpointId).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer change the details of this stage as preparation phase has already been concluded.");
		}
		
		Stage.findCheckpointsStage(checkpointId).getCheckpoints().remove(checkpointId);
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		boolean found = false;
		for(int id: Race.getStageIds()) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		if(Race.findStage(stageId).getState() != "in preparation") {
			throw new InvalidStageStateException("You can no longer change the details of this stage as preparation phase has already been concluded.");
		}
		
		Race.findStage(stageId).setState();
	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Race.getStageIds()) {
			if(stageId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined stage.");
		}
		
		return Race.findStage(stageId).getCheckpoints().keySet().stream().
		mapToInt(Integer:: intValue).toArray();
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		for(Team t: Team.getTeams().values()) {
			if(t.getName() == name) {
				throw new IllegalNameException("You have entered a name that is already in use, ensure you are using unique name per team.");
			}
		}
		
		if(name==null || name.equals("") || name.length()>20 || name.contains(" ")){ //TODO: FIND CHARACTER LIMIT FOR TEAM NAME??
			throw new InvalidNameException("You have entered an incorrectly formatted team name, ensure it is a string of characters with no spaces.");
		}
		
		Team newTeam = new Team(name, description);
		return newTeam.getId();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Team.getTeamIds()) {
			if(teamId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined team.");
		}
		
		Team.getTeams().remove(teamId);
	}

	@Override
	public int[] getTeams() {
		return Team.getTeamIds();
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Team.getTeamIds()) {
			if(teamId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined team.");
		}
		
		return Team.findTeam(teamId).getRiders().keySet()
		.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int createRider(int teamId, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		boolean found = false;
		for(int id: Team.getTeamIds()) {
			if(teamId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined team.");
		}

		return Team.findTeam(teamId).addRider(name, yearOfBirth);
	}

	//TODO: Come back to final leaderboard issue to remove racers from that too
	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		boolean found = false;
		for(int id: Team.getRiderIds()) {
			if(riderId == id) {
				found = true;
			}
		}
		if(found == false) {
			throw new IDNotRecognisedException("You have entered an unrecognisable ID, ensure the ID requested matches a previously defined rider.");
		}
		
		ArrayList<Integer> riderRaces = Team.findRider(riderId).getRacesEnrolled();
		for(int i=0; i<riderRaces.size(); i++) {
			for(int j=0; j<Race.findRace(riderRaces.get(i)).getStages().size();j++) {
				int currStageId = Race.getStageIds()[j];
				Race.findStage(currStageId).getRiderTimes().remove(riderId);
			}
			
		}
		
		Team.findRider(riderId).getTeam().getRiders().remove(riderId);
	}
	//JAKE
	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		//Calculate the elapsed time for that stage and convert it into the format wanted and add it to the stage's results
		LocalTime midnight = LocalTime.parse("00:00:00");
		Duration elapsedTime = Duration.between(checkpoints[0], checkpoints[checkpoints.length-1]);
		checkpoints[checkpoints.length-1] = midnight.plus(elapsedTime);
		Race.findStage(stageId).getRiderTimes().put(riderId, checkpoints);

		//If the race hasnt already been added to a rider's races, add it
		ArrayList<Integer> riderRaces = Team.findRider(riderId).getRacesEnrolled();
		for(int i=0; i<riderRaces.size();i++) {
			if(riderRaces.get(i) == Race.findStage(stageId).getRace().getId()) {
				break;
			}
			if(i==riderRaces.size()-1) {
				Team.findRider(riderId).getRacesEnrolled().add(Race.findStage(stageId).getRace().getId());
			}
		}

		//Insert the rider id at the appropriate position on the stages leaderboard
		int[] stagePositions = Race.findStage(stageId).getRiderPositions().stream().mapToInt(Integer::intValue).toArray();
		Map<Integer, LocalTime[]> riderTimes = Race.findStage(stageId).getRiderTimes();
		for(int i=0; i<stagePositions.length; i++) {
			LocalTime currentRacersFinish = riderTimes.get(stagePositions[i])[riderTimes.get(stagePositions[i]).length-1];
			//In the case it is the first result place it in
			if(stagePositions.length == 0) {
				Race.findStage(stageId).getRiderPositions().add(riderId);
				break;
			}
			//In the case it is faster than an index or the same speed place it before
			else if(checkpoints[checkpoints.length-1].compareTo(currentRacersFinish)
			==-1 || checkpoints[checkpoints.length-1].compareTo(currentRacersFinish)
			==0) {
				Race.findStage(stageId).getRiderPositions().add(i-1, riderId);
				break;
			}
			//In the case it is the slowest time yet
			else if(checkpoints[checkpoints.length-1].compareTo(riderTimes.get(stagePositions[stagePositions.length-1])[checkpoints.length-1])
			==1) {
				Race.findStage(stageId).getRiderPositions().add(riderId);
			}
				
		}
		
	}
	

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return Race.findStage(stageId).getRiderTimes().get(riderId);
	}

	//I really am not sure if this works properly lol
	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Map<Integer, LocalTime> adjustedTimes = Race.findStage(stageId).adjustTimes(0);
		return adjustedTimes.get(riderId);
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Race.findStage(stageId).getRiderTimes().remove(riderId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		return Race.findStage(stageId).getRiderPositions().stream().
		mapToInt(Integer::intValue).toArray();
	}

	//Again may or may not work
	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		Map<Integer, LocalTime> adjustedTimes = Race.findStage(stageId).adjustTimes(0);
		return adjustedTimes.values().toArray(new LocalTime[0]);
	}

	//May need help with this one not sure how to go about it
	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
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
		CyclingPortalImpl portalImpl = (CyclingPortalImpl) in.readObject();
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		Race.removeName(name);
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

}
