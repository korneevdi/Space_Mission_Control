package spacemissioncontrol.service;

import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.entity.MissionDetails;

import java.util.List;

public class MissionService {

    private final MissionDao missionDao = new MissionDao();

    public void showAllMissions() {
        List<Mission> missions = missionDao.findAll();

        if(missions != null && !missions.isEmpty()) {
            printMissions(missions);
        } else {
            System.out.println("No data found");
        }
    }

    public void showAllMissionsWithDetails() {
        List<Mission> missions = missionDao.findAllWithDetails();

        if(missions != null && !missions.isEmpty()) {
            printMissionsWithDetails(missions);
        } else {
            System.out.println("No data found");
        }
    }

    private void printMissions(List<Mission> list) {
        for(Mission m : list) {
            System.out.println(m);
        }
    }

    private void printMissionsWithDetails(List<Mission> list) {
        for(Mission m : list) {
            System.out.print(m);

            MissionDetails details = m.getMissionDetails();
            if(details != null) {
                System.out.println("    Budget: " + details.getBudgetMillionUSD() + " Mio. USD");
                System.out.println("    Duration: " + details.getDurationDays() + " days");
                System.out.println("    Description: " + details.getDescription() + "\n");
            }
        }
    }
}
