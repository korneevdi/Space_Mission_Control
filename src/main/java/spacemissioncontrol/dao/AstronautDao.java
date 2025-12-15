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

    public List<Astronaut> findAllByMissionName(String missionName) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            """
                                    SELECT DISTINCT a
                                    FROM %s a
                                    JOIN a.missionList m
                                    WHERE m.name = :name
                                    """.formatted(ENTITY_NAME),
                            Astronaut.class
                    )
                    .setParameter("name", missionName)
                    .getResultList();
        }
    }
}
