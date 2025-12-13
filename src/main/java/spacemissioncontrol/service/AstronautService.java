package spacemissioncontrol.service;

import spacemissioncontrol.dao.AstronautDao;
import spacemissioncontrol.entity.Astronaut;

import java.util.List;

public class AstronautService {

    private final AstronautDao astronautDao = new AstronautDao();

    public void showAllAstronauts() {
        List<Astronaut> astronauts = astronautDao.findAll();

        if(astronauts != null && !astronauts.isEmpty()) {
            printAstronauts(astronauts);
        } else {
            System.out.println("No data found");
        }
    }

    private void printAstronauts(List<Astronaut> list) {
        for(Astronaut a : list) {
            System.out.println(a);
        }
    }
}
