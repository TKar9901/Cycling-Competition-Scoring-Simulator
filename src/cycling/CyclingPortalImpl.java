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
		Race race = Race.getRaces().get(raceId);
		return race.toString();
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		Race.getRaces().remove(raceId);
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		return Race.getRaces().get(raceId).getStages().size();
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,StageType type) 
	throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
			return Race.getRaces().get(raceId).addStage(stageName, description, length, startTime, type);
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
			if(Race.getRaces().get(raceIds[i]).getStages().containsKey(stageId)) {
				length = Race.getRaces().get(raceIds[i]).getStages().get(stageId).getLength();
			}
		}
		return length;
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Race.findStage(stageId).getStages().remove(stageId);
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		return Race.findStage(stageId).getStages().get(stageId).addMountainCheckpoint(location, type, averageGradient, length);
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		return Race.findStage(stageId).getStages().get(stageId).addSprintCheckpoint(location);
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.findCheckpoint(checkpointId).getCheckpoints().remove(checkpointId);
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Race.findStage(stageId).getStages().get(stageId).setState();
	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		return Race.findStage(stageId)
		.getStages().get(stageId).getCheckpoints().keySet().stream().
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
		return Team.getTeams().get(teamId).getRiders().keySet()
		.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
			return Team.getTeams().get(teamID).addRider(name, yearOfBirth);
	}

	//Come back to final leaderboard issue to remove racers from that too
	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		for(int i=0; i<Team.findRider(riderId).getRiders().get(riderId).getRacesEnrolled().size(); i++) {
			int currRaceId = Team.findRider(riderId).getRiders().get(riderId).getRacesEnrolled().get(i);
			for(int j=0; j<Race.getRaces().get(currRaceId).getStages().size();j++) {
				int currStageId = Race.getRaces().get(currRaceId).getStageIds()[j];
				Race.findStage(currStageId).getStages().get(currStageId).getRiderTimes().remove(riderId);
			}
			
		}
		
		Team.findRider(riderId).getRiders().remove(riderId);
	}
	//JAKE
	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		LocalTime midnight = LocalTime.parse("00:00:00");
		Duration elapsedTime = Duration.between(checkpoints[0], checkpoints[checkpoints.length-1]);
		checkpoints[checkpoints.length-1] = midnight.plus(elapsedTime);
		
		Race.findStage(stageId).getStages().get(stageId).getRiderTimes().put(riderId, checkpoints);
		for(int i=0; i<Team.findRider(riderId).getRiders().get(riderId).getRacesEnrolled().size();i++) {
			if(Team.findRider(riderId).getRiders().get(riderId).getRacesEnrolled().get(i) != Race.findStage(stageId).getId()) {
				Team.findRider(riderId).getRiders().get(riderId).getRacesEnrolled().add(Race.findStage(stageId).getId());
			} 
		}

		for(int i=0; i<Race.findStage(stageId).getStages().get(stageId).getRiderPositions().size(); i++) {
			if(Race.findStage(stageId).getStages().get(stageId).getRiderPositions().size() == 0) {
				Race.findStage(stageId).getStages().get(stageId).getRiderPositions().add(riderId);
			}
			else if(Race.findStage(stageId).getStages().get(stageId).getRiderTimes()
			.get((Race.findStage(stageId).getStages().get(stageId).getRiderPositions().get(i)))[0].
			compareTo(checkpoints[checkpoints.length-1]) == 1 || Race.findStage(stageId).getStages().get(stageId).getRiderTimes()
			.get((Race.findStage(stageId).getStages().get(stageId).getRiderPositions().get(i)))[0].
			compareTo(checkpoints[checkpoints.length-1]) == 0) {
				Race.findStage(stageId).getStages().get(stageId).getRiderPositions().add(i-1, riderId);
			} 
			else if(Race.findStage(stageId).getStages().get(stageId).getRiderTimes()
			.get((Race.findStage(stageId).getStages().get(stageId).getRiderPositions().get(
				Race.findStage(stageId).getStages().get(stageId).getRiderPositions().size()-1)))[0].
			compareTo(checkpoints[checkpoints.length-1]) == 1 || 
			Race.findStage(stageId).getStages().get(stageId).getRiderTimes()
			.get((Race.findStage(stageId).getStages().get(stageId).getRiderPositions().get(
				Race.findStage(stageId).getStages().get(stageId).getRiderPositions().size()-1)))[0].
			compareTo(checkpoints[checkpoints.length-1]) == 0) {
				Race.findStage(stageId).getStages().get(stageId).getRiderPositions().add(riderId);
			}
		
		}
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return Race.findStage(stageId).getStages().get(stageId).getRiderTimes().get(riderId);
	}

	//Make sure to check functino description, it's wonky inpractice...
	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Race.findStage(stageId).getStages().get(stageId).getRiderTimes().remove(riderId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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
