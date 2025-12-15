package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Astronaut;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class AstronautDao extends AbstractDao<Astronaut> {

    private final static String ENTITY_NAME = "Astronaut";

    public AstronautDao() {
        super(Astronaut.class, ENTITY_NAME);
    }
}
