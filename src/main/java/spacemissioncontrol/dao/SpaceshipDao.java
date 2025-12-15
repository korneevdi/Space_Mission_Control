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

    public List<Spaceship> findAllByMissionName(String missionName) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            """
                                    SELECT DISTINCT a
                                    FROM %s a
                                    JOIN a.missionList m
                                    WHERE m.name = :name
                                    """.formatted(ENTITY_NAME),
                            Spaceship.class
                    )
                    .setParameter("name", missionName)
                    .getResultList();
        }
    }
}
