package spacemissioncontrol.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class MissionDao {

    public List<Mission> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Mission", Mission.class).list();
        }
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

    public List<Mission> findAllByField(String fieldName, Object value) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Mission> criteriaQuery = criteriaBuilder.createQuery(Mission.class);
            Root<Mission> root = criteriaQuery.from(Mission.class);

            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get(fieldName), value));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
