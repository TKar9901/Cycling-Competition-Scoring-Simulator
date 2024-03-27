import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import cycling.CheckpointType;
import cycling.CyclingPortalImpl;
import cycling.DuplicatedResultException;
import cycling.IDNotRecognisedException;
import cycling.IllegalNameException;
import cycling.InvalidCheckpointTimesException;
import cycling.InvalidLengthException;
import cycling.InvalidLocationException;
import cycling.InvalidNameException;
import cycling.InvalidStageStateException;
import cycling.InvalidStageTypeException;
import cycling.NameNotRecognisedException;
import cycling.StageType;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortal interface -- note you
 * will want to increase these checks, and run it on your CyclingPortalImpl class
 * (not the BadCyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 2.0
 */
public class CyclingPortalTestApp {

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		CyclingPortalImpl portal1 = new CyclingPortalImpl();
		assert (portal1.toString() != "{races='{}', teams='{}', usedStageIds='[]', usedCheckpointIds='[]', usedRiderIds='[]'}")
			: "cycling portal constructor faulty";
		assert (portal1.getRaceIds().length == 0)
			: "race ids not handled correctly upon portal initialisation";
		try {
			portal1.createRace("raceName1", "raceDesc1");
			// portal1.createRace("raceName 1", "raceDesc1"); -> InvalidNameException
			// portal1.createRace("", "raceDesc2"); -> InvalidNameException
			// portal1.createRace("dejfruhwforw9ejrfwhurigogtrhrsefa", "raceDesc3"); -> InvalidNameException
			// portal1.createRace("raceName1", "raceDesc1"); -> IllegalNameException
			portal1.createRace("raceName2", "raceDesc2");
		} catch(InvalidNameException e) {
			e.printStackTrace();
		} catch(IllegalNameException e) {
			e.printStackTrace();
		} 
		System.out.println("races created.");
		assert (portal1.getRaceIds().length == 2)
			: "race ids not handled correctly upon race creation";
		
		int raceId0 = portal1.getRaceIds()[0];
		int raceId1 = portal1.getRaceIds()[1];

		try {
			System.out.println("viewing race0 details:");
			// portal1.viewRaceDetails(0); -> IDNotRecognisedException
			String details = portal1.viewRaceDetails(raceId0);
			System.out.println(details);
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("deleting race0:");
			// portal1.removeRaceById(0); -> IDNotRecognisedException
			portal1.removeRaceById(raceId0);
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		assert (portal1.getRaceIds().length == 1)
			: "race ids not handled correctly upon race deletion";

		System.out.println("adding stages to remaining race1:");
		try {
			// portal1.addStageToRace(raceId0, "stageName1", "stageDesc1", 25.0, LocalDateTime.now(), StageType.FLAT); -> IDNotRecognisedException
			// portal1.addStageToRace(raceId1, "stageName1", "stageDesc1", 25.0, LocalDateTime.now(), StageType.FLAT); -> IllegalNameException
			// portal1.addStageToRace(raceId1, "stageName1", "stageDesc1", 3.0, LocalDateTime.now(), StageType.FLAT); -> InvalidLengthException
			portal1.addStageToRace(raceId1, "stageName1", "stageDesc1", 25.0, LocalDateTime.now(), StageType.FLAT);
			// portal1.addStageToRace(raceId1, "stageName1", "stageDesc2", 25.0, LocalDateTime.now(), StageType.FLAT); -> IllegalNameException
			portal1.addStageToRace(raceId1, "stageName2", "stageDesc2", 25.0, LocalDateTime.now().plusHours(2), StageType.TT);
			// portal1.addStageToRace(raceId1, " ", "stageDesc3", 25.0, LocalDateTime.now(), StageType.FLAT); -> InvalidNameException
			// portal1.addStageToRace(raceId1, "", "stageDesc3", 25.0, LocalDateTime.now(), StageType.FLAT); -> InvalidNameException
			// portal1.addStageToRace(raceId1, "stageName 3", "stageDesc3", 25.0, LocalDateTime.now(), StageType.FLAT); -> InvalidNameException
			portal1.addStageToRace(raceId1, "stageName3", "stageDesc3", 25, LocalDateTime.now().plusHours(5), StageType.FLAT); //TODO: int makes no difference to length?
			portal1.addStageToRace(raceId1, "stageName4", "stageDesc4", 25.0, LocalDateTime.now().plusHours(6), StageType.FLAT);
			portal1.addStageToRace(raceId1, "stageName5", "stageDesc5", 25.0, LocalDateTime.now().plusHours(7), StageType.HIGH_MOUNTAIN);
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		} catch(InvalidNameException e) {
			e.printStackTrace();
		} catch(IllegalNameException e) {
			e.printStackTrace();
		} catch(InvalidLengthException e) {
			e.printStackTrace();
		}

		try {
			// int stagesNo = portal1.getNumberOfStages(raceId0); -> IDNotRecognisedException
			int stagesNo = portal1.getNumberOfStages(raceId1);
			assert (stagesNo == 5)
			: "stages per race not calculated/ stored correctly";
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("stageIds of race1 in order as stored in object:");
			// System.out.println(portal1.getRaceStages(raceId0)); -> IDNotRecognisedException
			int[] stages = portal1.getRaceStages(raceId1);
			for(int id: stages) {
				System.out.println(id);
			}
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("compared with race details:");
			// portal1.viewRaceDetails(raceId0); -> IDNotRecognisedException
			String details = portal1.viewRaceDetails(raceId1);
			System.out.println(details);
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("removing last stage and comparing with race details:");
			// portal1.removeStageById(raceId0); -> IDNotRecognisedException
			int stageId = portal1.getRaceStages(raceId1)[3];
			System.out.println(portal1.toString());
			portal1.removeStageById(stageId);
			String details = portal1.viewRaceDetails(raceId1);
			System.out.println(details);
			System.out.println("comparing with portal data:");
			System.out.println(portal1.toString()); //TODO: problem with usedStageIds - FIXED
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}
		
		//TODO: something isn't working with getRaceStages() potentially not ordered correctly? so id references are jumbled - causes error sometimes..
		try {
			int stageId = portal1.getRaceStages(raceId1)[0];
			// portal1.addCategorizedClimbToStage(stageId, -1.0, CheckpointType.C1, 5.0, 2.0); -> InvalidLocationException
			// portal1.addCategorizedClimbToStage(stageId, 27.0, CheckpointType.C1, 5.0, 2.0); -> InvalidLocationException
			// portal1.addCategorizedClimbToStage(raceId0, 14.0, CheckpointType.C1, 5.0, 2.0); -> IDNotRecognisedException
			portal1.addCategorizedClimbToStage(stageId, 14.0, CheckpointType.C1, 5.0, 2.0);
			portal1.addCategorizedClimbToStage(stageId, 15.0, CheckpointType.C1, 5.0, 2.0);
			int stageId2 = portal1.getRaceStages(raceId1)[1];
			// portal1.addCategorizedClimbToStage(stageId2, 14.0, CheckpointType.C1, 5.0, 2.0); -> InvalidStageTypeException
			portal1.concludeStagePreparation(stageId2);
			// portal1.addCategorizedClimbToStage(stageId2, 11.0, CheckpointType.C1, 5.0, 2.0); ->InvalidStageStateException
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		} catch(InvalidLocationException e) {
			e.printStackTrace();
		} catch(InvalidStageTypeException e) {
			e.printStackTrace();
		} catch(InvalidStageStateException e) {
			e.printStackTrace();
		}
		
		try {
			int stageId = portal1.getRaceStages(raceId1)[0];
			// portal1.addIntermediateSprintToStage(stageId, -1.0); -> InvalidLocationException
			// portal1.addIntermediateSprintToStage(stageId, 27.0); -> InvalidLocationException
			// portal1.addIntermediateSprintToStage(raceId0, 19.0); -> IDNotRecognisedException
			portal1.addIntermediateSprintToStage(stageId, 19.0);
			int stageId2 = portal1.getRaceStages(raceId1)[2];
			// portal1.addIntermediateSprintToStage(stageId2, 6.0); -> InvalidStageTypeException
			portal1.concludeStagePreparation(stageId2);	
			// portal1.addIntermediateSprintToStage(stageId2, 11.0); ->InvalidStageStateException
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		} catch(InvalidLocationException e) {
			e.printStackTrace();
		} catch(InvalidStageTypeException e) {
			e.printStackTrace();
		} catch(InvalidStageStateException e) {
			e.printStackTrace();
		}	
	
		System.out.println("added mountain and intermediate checkpoints to stage0");

		try {
			// int[] checkpoints = portal1.getStageCheckpoints(raceId0); -> IDNotRecognisedException
			int stageId = portal1.getRaceStages(raceId1)[0];
			// int[] checkpoints = portal1.getStageCheckpoints(raceId0); -> IDNotRecognisedException
			int[] checkpoints = portal1.getStageCheckpoints(stageId);
			assert (checkpoints.length == 3)
				: "checkpoints added incorrectly";
			System.out.println("checkpointIds in stage0 object compared with portal data:");
			for(int id: checkpoints) {
				System.out.println(id);
			}
			System.out.println(portal1.toString());
			// portal1.removeCheckpoint(raceId0); -> IDNotRecognisedException
			System.out.println("removing checkpoint0 and comparing with portal data:");
			portal1.removeCheckpoint(checkpoints[0]); //TODO: ArrayIndexOutOfBoundsException on Stage.findCheckpointsStage - FIXED
			System.out.println(portal1.toString());
			// portal1.removeCheckpoint(checkpoints[0]); -> IDNotRecognisedException
			portal1.concludeStagePreparation(stageId);
			// portal1.removeCheckpoint(checkpoints[1]); -> InvalidStageStateException
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		} catch(InvalidStageStateException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("comparing race details with checkpoints in stages in objects in the race:");
			System.out.println(portal1.viewRaceDetails(raceId1)); //TODO: yes ordering is weird look at this
			int[] stages = portal1.getRaceStages(raceId1);
			for (int id: stages) {
				System.out.println(id);
				int[] checkpoints = portal1.getStageCheckpoints(id);
				for(int cpid: checkpoints) {
					System.out.println(cpid);
				}
				System.out.println();
			}
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		try {
			portal1.createTeam("teamName1", "teamDesc1");
			// portal1.createTeam("teamName 1", "teamDesc1"); -> InvalidNameException
			// portal1.createTeam("", "teamDesc1"); -> InvalidNameException
			// portal1.createTeam("dejfruhwforw9ejrfwhurigogtrhrsefa", "teamDesc1"); -> InvalidNameException
			// portal1.createTeam("teamName1", "raceDesc1"); -> IllegalNameException
			portal1.createTeam("teamName2", "teamDesc2");
		} catch(InvalidNameException e) {
			e.printStackTrace();
		} catch(IllegalNameException e) {
			e.printStackTrace();
		}
		System.out.println("created teams.");
		
		int teamId0 = portal1.getTeams()[0];
		int teamId1 = portal1.getTeams()[1];

		try {
			System.out.println("removing team0: ");
			System.out.println(portal1.toString());
			// portal1.removeTeam(raceId0); -> IDNotRecognisedException
			portal1.removeTeam(teamId0);
			System.out.println(portal1.toString());
		}catch(IDNotRecognisedException e){
			e.printStackTrace();
		}

		try {
			// portal1.createRider(teamId0, "rider1", 2000); -> IDNotRecognisedException
			portal1.createRider(teamId1, "rider1", 2000);
			portal1.createRider(teamId1, "rider2", 2000);
			portal1.createRider(teamId1, "rider3", 2000);
			portal1.createRider(teamId1, "rider4", 2000);
			// System.out.println(portal1.getTeamRiders(teamId0)); -> IDNotRecognisedException
			int[] riders = portal1.getTeamRiders(teamId1);
			for(int r: riders) {
				System.out.println(r);
			}
			portal1.removeRider(riders[0]); //TODO: NullPointerException in removeRider() - FIXED
			System.out.println(portal1.toString());
			int[] ridersAfter = portal1.getTeamRiders(teamId1);
			for(int r: ridersAfter) {
				System.out.println(r);
			}
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		System.out.println("adding rider results to stages: ");
		try {
			int[] riders = portal1.getTeamRiders(teamId1);
			int[] stages = portal1.getRaceStages(raceId1);
			portal1.concludeStagePreparation(stages[3]);
			int x = 1;
			for(int r: riders) {
				// System.out.println(r + "\n");
				for(int i=0; i<stages.length; i++) {
					// System.out.println(stages[i]);
					if(i==0){
						// LocalTime[] times = {LocalTime.now(), LocalTime.now().plusMinutes(1+x), LocalTime.now().plusMinutes(2+x)}; -> InvalidCheckpointTimesException
						LocalTime[] times = {LocalTime.now(), LocalTime.now().plusMinutes(1+x), LocalTime.now().plusMinutes(2+x), LocalTime.now().plusMinutes(3+x)};
						// portal1.registerRiderResultsInStage(stages[3], r, times); -> InvalidStageStateException
						// portal1.registerRiderResultsInStage(stages[0], raceId0, times); -> IDNotRecognisedException
						// portal1.registerRiderResultsInStage(raceId0, r, times); -> IDNotRecognisedException
						portal1.registerRiderResultsInStage(stages[i], r, times); //TODO: does removeRider() actually remove from usedRiderIds()
						// portal1.registerRiderResultsInStage(s, r, times); -> DuplicateResultException
					}else if(i==1) {
						LocalTime[] times = {LocalTime.now(), LocalTime.now().plusMinutes(1+x)};
						portal1.registerRiderResultsInStage(stages[i], r, times);
					}else if(i==2) {
						LocalTime[] times = {LocalTime.now(), LocalTime.now().plusMinutes(1+x)};
						portal1.registerRiderResultsInStage(stages[i], r, times);
					}else if(i==3) {
						LocalTime[] times = {LocalTime.now(), LocalTime.now().plusMinutes(1+x)};
						portal1.registerRiderResultsInStage(stages[i], r, times);
					}
				}
				// System.out.println("\n");
				x++;
			}
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		} catch(InvalidStageStateException e) {
			e.printStackTrace();
		} catch(DuplicatedResultException e) {
			e.printStackTrace();
		} catch(InvalidCheckpointTimesException e) {
			e.printStackTrace();
		}

		try {
			int[] riders = portal1.getTeamRiders(teamId1);
			int[] stages = portal1.getRaceStages(raceId1);
			for(int r: riders) {
				System.out.println(r);
				for(int s: stages) {
					System.out.println(s);
					LocalTime[] times = portal1.getRiderResultsInStage(s, r);
					for(LocalTime t: times) {
						System.out.println(t);
					}
					System.out.println();
				}
			}
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("before removing rider:");
			System.out.println(portal1.toString());
			int[] riders = portal1.getTeamRiders(teamId1);
			portal1.removeRider(riders[0]);
			System.out.println("after removing rider: ");
			System.out.println(portal1.toString());
			int[] ridersAfter = portal1.getTeamRiders(teamId1);
			int[] stages = portal1.getRaceStages(raceId1);
			for(int r: ridersAfter) {
				System.out.println(r);
				for(int s: stages) {
					System.out.println(s);
					LocalTime[] times = portal1.getRiderResultsInStage(s, r);
					for(LocalTime t: times) {
						System.out.println(t);
					}
					System.out.println();
				}
			}
		} catch(IDNotRecognisedException e) {
			e.printStackTrace();
		}
		
	}
}
