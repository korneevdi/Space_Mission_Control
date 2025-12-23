package spacemissioncontrol.service;

import jakarta.validation.ConstraintViolation;
import org.hibernate.Session;
import spacemissioncontrol.dao.AbstractDao;
import spacemissioncontrol.util.EntityValidator;
import spacemissioncontrol.util.HibernateConfig;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractService<T> {

    protected final AbstractDao<T> dao;

    protected AbstractService(AbstractDao<T> dao) {
        this.dao = dao;
    }

    public void showAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<T> allItems = dao.findAll(session);
            printNonEmptyList(allItems, "No data found");
        }
    }

    public void showAllByField(String fieldName, String rawValue) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Object value = convertValue(fieldName, rawValue);
            List<T> allItems = dao.findAllByFields(session, Map.of(fieldName, value));
            printNonEmptyList(allItems, "No data found");
        }
    }

    private Object convertValue(String fieldName, String rawValue) {
        try {
            Field field = getEntityClass().getDeclaredField(fieldName);
            Class<?> fieldType = field.getType();

            if (fieldType.equals(LocalDate.class)) {
                return LocalDate.parse(rawValue);
            }

            if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                return Integer.parseInt(rawValue);
            }

            if (fieldType.equals(BigDecimal.class)) {
                return new BigDecimal(rawValue);
            }

            return rawValue;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field of unknown type: " + fieldName, e);
        }
    }

    protected boolean isValidEntity(T entity) {
        Set<ConstraintViolation<T>> violations =
                EntityValidator.validate(entity);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> e : violations) {
                System.out.println(
                        "Field '" + e.getPropertyPath() +
                                "': " + e.getMessage()
                );
            }
            return false;
        }
        return true;
    }

    protected void printNonEmptyList(List<T> list, String message) {
        if (list != null && !list.isEmpty()) {
            printList(list);
        } else {
            System.out.println(message);
        }
    }

    protected void printList(List<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }

    protected Class<T> getEntityClass() {
        return dao.getEntityClass();
    }
}
