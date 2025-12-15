package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class MissionDao extends AbstractDao<Mission> {

    private final static String ENTITY_NAME = "Mission";

    public MissionDao() {
        super(Mission.class, ENTITY_NAME);
    }

    public List<Mission> findAllWithDetails() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                    """
                            SELECT m FROM %s m
                            LEFT JOIN FETCH m.missionDetails
                            """.formatted(ENTITY_NAME),
                    Mission.class
            ).list();
        }
    }

    public List<Mission> findAllByNameLike(String missionNameLike) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            """
                                    SELECT m
                                    FROM %s m
                                    WHERE lower(m.name) LIKE :name
                                    """.formatted(ENTITY_NAME),
                            Mission.class
                    )
                    .setParameter("name", "%" + missionNameLike.toLowerCase() + "%")
                    .getResultList();
        }
    }
}
