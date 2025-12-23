package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Astronaut;

import java.util.List;

public class AstronautDao extends AbstractDao<Astronaut> {

    private final static String ENTITY_NAME = "Astronaut";

    public AstronautDao() {
        super(Astronaut.class, ENTITY_NAME);
    }

    public List<Astronaut> findAllByMissionName(Session session, String missionName) {
        return session.createQuery(
                        """
                                SELECT DISTINCT a
                                FROM %s a
                                JOIN a.missionList m
                                WHERE lower(m.name) = :name
                                """.formatted(ENTITY_NAME),
                        Astronaut.class
                )
                .setParameter("name", missionName.toLowerCase())
                .getResultList();
    }
}
