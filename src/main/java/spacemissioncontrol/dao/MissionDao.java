package spacemissioncontrol.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
                    "SELECT m FROM Mission m " +
                    "LEFT JOIN FETCH m.missionDetails",
                    Mission.class
            ).list();
        }
    }
}
