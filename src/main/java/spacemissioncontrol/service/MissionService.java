package spacemissioncontrol.service;

import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.entity.MissionDetails;

import java.util.List;

public class MissionService extends AbstractService<Mission> {

    private final MissionDao missionDao;

    public MissionService() {
        this(new MissionDao());
    }

    private MissionService(MissionDao missionDao) {
        super(missionDao);
        this.missionDao = missionDao;
    }

    public void showAllWithDetails() {
        List<Mission> missions = missionDao.findAllWithDetails();

        if(missions != null && !missions.isEmpty()) {
            printMissionsWithDetails(missions);
        } else {
            System.out.println("No data found");
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
