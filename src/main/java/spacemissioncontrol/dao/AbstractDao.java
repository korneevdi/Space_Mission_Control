package spacemissioncontrol.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public abstract class AbstractDao<T> {

    private final Class<T> entityClass;
    private final String entityName;

    protected AbstractDao(Class<T> entityClass, String entityName) {
        this.entityClass = entityClass;
        this.entityName = entityName;
    }

    public List<T> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityName, entityClass).list();
        }
    }

    public List<T> findAllByField(String fieldName, Object value) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);

            Predicate predicate;

            if(value instanceof String str) {
                predicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get(fieldName)), str.toLowerCase());
            } else {
                predicate = criteriaBuilder.equal(root.get(fieldName), value);
            }

            criteriaQuery.select(root)
                    .where(predicate);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
