package spacemissioncontrol.service;

import spacemissioncontrol.dao.AstronautDao;
import spacemissioncontrol.entity.Astronaut;

import java.util.List;

public class AstronautService extends AbstractService<Astronaut> {

    private final AstronautDao astronautDao;

    public AstronautService() {
        this(new AstronautDao());
    }

    private AstronautService(AstronautDao astronautDao) {
        super(astronautDao);
        this.astronautDao = astronautDao;
    }
}
