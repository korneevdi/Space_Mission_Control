package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Spaceship;

import java.util.List;

public class SpaceshipDao extends AbstractDao<Spaceship> {

    private final static String ENTITY_NAME = "Spaceship";

    public SpaceshipDao() {
        super(Spaceship.class, ENTITY_NAME);
    }

    public List<Spaceship> findAllByMissionName(Session session, String missionName) {
        return session.createQuery(
                        """
                                SELECT DISTINCT a
                                FROM %s a
                                JOIN a.missionList m
                                WHERE lower(m.name) = :name
                                """.formatted(ENTITY_NAME),
                        Spaceship.class
                )
                .setParameter("name", missionName.toLowerCase())
                .getResultList();
    }
}
