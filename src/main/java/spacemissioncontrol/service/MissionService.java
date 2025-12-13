package spacemissioncontrol.service;

import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Mission;

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

    private void printMissions(List<Mission> list) {
        for(Mission m : list) {
            System.out.println(m);
        }
    }
}
