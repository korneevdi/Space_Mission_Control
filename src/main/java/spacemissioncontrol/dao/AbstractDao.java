package spacemissioncontrol.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T> {

    private final Class<T> entityClass;
    private final String entityName;

    protected AbstractDao(Class<T> entityClass, String entityName) {
        this.entityClass = entityClass;
        this.entityName = entityName;
    }

    public List<T> findAll(Session session) {
        return session.createQuery("FROM " + entityName, entityClass).list();
    }

    public List<T> findAllByField(Session session, String fieldName, Object value) {
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

    public void insert(Session session, T entity) {
        session.persist(entity);
    }

    public boolean existsByField(Session session, Map<String, Object> fieldsAndValues) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();

        for(Map.Entry<String, Object> entry : fieldsAndValues.entrySet()) {
            predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
        }

        criteriaQuery.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        Long count = session.createQuery(criteriaQuery).getSingleResult();

        return count > 0;
    }

    public void merge(Session session, T entity) {
        session.merge(entity);
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
