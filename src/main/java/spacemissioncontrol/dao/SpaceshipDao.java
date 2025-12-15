package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Spaceship;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class SpaceshipDao extends AbstractDao<Spaceship> {

    private final static String ENTITY_NAME = "Spaceship";

    public SpaceshipDao() {
        super(Spaceship.class, ENTITY_NAME);
    }
}
