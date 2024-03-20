package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.math.*;
import java.time.Duration;

/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Diogo Pacheco
 * @version 2.0
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
		Race newRace = new Race(name, description);
		return newRace.getId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		return Race.findRace(raceId).toString();
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		Race.getRaces().remove(raceId);
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		return Race.findRace(raceId).getStages().size();
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,StageType type) 
	throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
			return Race.findRace(raceId).addStage(stageName, description, length, startTime, type);
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		return Race.getRaces().get(raceId).getStages().keySet()
		.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
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
		Race.findStage(stageId).getRace().getStages().remove(stageId);
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		return Race.findStage(stageId).addMountainCheckpoint(location, type, averageGradient, length);
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		return Race.findStage(stageId).addSprintCheckpoint(location);
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.findCheckpoint(checkpointId).getCheckpoints().remove(checkpointId);
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Race.findStage(stageId).setState();
	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		return Race.findStage(stageId).getCheckpoints().keySet().stream().
		mapToInt(Integer:: intValue).toArray();
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		Team newTeam = new Team(name, description);
		return newTeam.getId();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		Team.getTeams().remove(teamId);
	}

	@Override
	public int[] getTeams() {
		return Team.getTeamIds();
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		return Team.findTeam(teamId).getRiders().keySet()
		.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
			return Team.findTeam(teamID).addRider(name, yearOfBirth);
	}

	//Come back to final leaderboard issue to remove racers from that too
	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		ArrayList<Integer> riderRaces = Team.findRider(riderId).getRacesEnrolled();
		for(int i=0; i<riderRaces.size(); i++) {
			for(int j=0; j<Race.findRace(riderRaces.get(i)).getStages().size();j++) {
				int currStageId = Race.findRace(riderRaces.get(i)).getStageIds()[j];
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

	//Make sure to check functino description, it's wonky inpractice...
	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		// TODO Auto-generated method stub

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
