package spacemissioncontrol.service;

import spacemissioncontrol.dao.SpaceshipDao;
import spacemissioncontrol.entity.Spaceship;

import java.util.List;

public class SpaceshipService extends AbstractService<Spaceship> {

    private final SpaceshipDao spaceshipDao;

    public SpaceshipService() {
        this(new SpaceshipDao());
    }

    private SpaceshipService(SpaceshipDao spaceshipDao) {
        super(spaceshipDao);
        this.spaceshipDao = spaceshipDao;
    }
}
