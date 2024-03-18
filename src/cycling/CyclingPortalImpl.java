package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.math.*;

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
			return Race.getRaceIds().stream().mapToInt(i -> i).toArray();
		}
		
	}

	//Changed to randomized id system, same functinality but our old system would become more complicated once the length of the array it was based on changed
	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		int id = 0;
		boolean used = true;
		while(used) {
			id = (int)Math.floor(Math.random() *(1000 - 1000 + 1) + 1000);
			for(int i=0; i<Race.getRaceIds().size(); i++) {
				if(Race.getRaceIds().get(i) != id) {
					used = false;
				}
			}
		}
		Race newRace = new Race(name, description, id);
		return newRace.getId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		Race race = Race.findRace(raceId);
		String details = race.toString();
		return details;
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		for(int i=0; i < Race.getRaceIds().size(); i++) {
			if(Race.getRaceIds().get(i) == raceId) {
				Race.getRaceIds().remove(i);
				Race.getRaces().remove(i);
			}
		}

	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		Race race = Race.findRace(raceId);
		return race.getStages().length;
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,StageType type) 
	throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
			Race race = Race.findRace(raceId);
			Stage newStage = new Stage(stageName, description, length, startTime, type);
			int stageId = newStage.getId();
			race.addStage(newStage);
			return stageId;
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		return Stage.getStageIds().stream().mapToInt(i -> i).toArray();
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		return Stage.findStage(stageId).getLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		for(int i=0; i < Stage.getStageIds().size(); i++) {
			if(Stage.getStageIds().get(i) == stageId) {
				Stage.getStageIds().remove(i);
				Stage.getStages().remove(i);
			}
		}
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		MountainCheckpoint checkpoint = new MountainCheckpoint(location, type, averageGradient, length);
		Stage.findStage(stageId).getCheckpoints().add(checkpoint);
		return checkpoint.getId();
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		SprintCheckpoint checkpoint = new SprintCheckpoint(location);
		Stage.findStage(stageId).getCheckpoints().add(checkpoint);
		return checkpoint.getId();
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		for(int i=0; i<Stage.getStages().size()-1; i++) {
			for(int j=0; j<Stage.getStages().get(i).getCheckpoints().size()-1; j++) {
				if(Stage.getStages().get(i).getCheckpoints().get(j).getId() == checkpointId) {
					Stage.getStages().get(i).getCheckpoints().remove(j);
				}
			}
		}

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.findStage(stageId).setState();
	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		Checkpoint [] checkpoints = (Checkpoint[]) Stage.findStage(stageId).getCheckpoints().toArray();
		int[] checkpointIds = new int[checkpoints.length];
		for(int i=0; i<checkpoints.length-1; i++) {
			checkpointIds[i] = checkpoints[i].getId();
		}
		return checkpointIds;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		Team newTeam = new Team(name, description);
		return newTeam.getId();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		for(int i=0; i < Team.getTeamIds().size()-1; i++) {
			if(Team.getTeamIds().get(i) == teamId) {
				Team.getTeamIds().remove(i);
				Team.getTeams().remove(i);
			}
		}
	}

	@Override
	public int[] getTeams() {
		return Team.getTeamIds().stream().mapToInt(i -> i).toArray();
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		return Team.findTeam(teamId).getRiders().stream().mapToInt(i -> i).toArray();
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		Rider newRider = new Rider(teamID, name, yearOfBirth);
		return newRider.getId();
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		for(int i=0; i<Race.getRaces().size()-1; i++) {
			for(int j=0; j<Race.getRaces().get(i).getRiders().size(); i++) {
				if(Race.getRaces().get(i).getRiders().get(j).getId() == riderId) {
					Race.getRaces().get(i).getRiders().remove(j);
				}
			}
		}
		for(int i=0; i<Rider.getRiderIds().size()-1; i++) {
			if(Rider.getRiderIds().get(i) == riderId) {
				Rider.getRiderIds().remove(i);
				Rider.getRiders().remove(i);
			}
		}
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		Stage.findStage(stageId).addResults(riderId, checkpoints);


	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return Stage.findStage(stageId).getResults(riderId);
	}

	//Make sure to check functino description, it's wonky inpractice...
	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

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
