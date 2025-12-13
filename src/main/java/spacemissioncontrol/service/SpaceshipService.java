package spacemissioncontrol.service;

import spacemissioncontrol.dao.SpaceshipDao;
import spacemissioncontrol.entity.Spaceship;

import java.util.List;

public class SpaceshipService {

    private final SpaceshipDao spaceshipDao = new SpaceshipDao();

    public void showAllSpaceships() {
        List<Spaceship> spaceships = spaceshipDao.findAll();

        if(spaceships != null && !spaceships.isEmpty()) {
            printSpaceships(spaceships);
        } else {
            System.out.println("No data found");
        }
    }

    private void printSpaceships(List<Spaceship> list) {
        for(Spaceship s : list) {
            System.out.println(s);
        }
    }
}
