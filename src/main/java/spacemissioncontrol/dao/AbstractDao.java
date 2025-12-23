package spacemissioncontrol.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDao<T> {

    private final Class<T> entityClass;
    private final String entityName;

    protected AbstractDao(Class<T> entityClass, String entityName) {
        this.entityClass = entityClass;
        this.entityName = entityName;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public List<T> findAll(Session session) {
        return session.createQuery("FROM " + entityName, entityClass).list();
    }

    public List<T> findAllByFields(Session session, Map<String, Object> fields) {

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

            Predicate predicate;

            if (value instanceof String str) {
                predicate = cb.equal(
                        cb.lower(root.get(fieldName)),
                        str.toLowerCase()
                );
            } else {
                predicate = cb.equal(root.get(fieldName), value);
            }

            predicates.add(predicate);
        }

        cq.select(root)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        return session.createQuery(cq).getResultList();
    }

    public void insert(Session session, T entity) {
        session.persist(entity);
    }

    public void merge(Session session, T entity) {
        session.merge(entity);
    }

    public void delete(Session session, T entity) {
        session.remove(entity);
    }

    public Optional<Integer> findIdByFields(Session session, Map<String, Object> fieldsAndValues) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<T> root = criteriaQuery.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, Object> entry : fieldsAndValues.entrySet()) {
            predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
        }

        criteriaQuery.select(root.get("id")).
                where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        List<Integer> result = session.createQuery(criteriaQuery).getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    public Optional<T> findById(Session session, Integer id) {
        return Optional.ofNullable(session.get(entityClass, id));
    }
}
