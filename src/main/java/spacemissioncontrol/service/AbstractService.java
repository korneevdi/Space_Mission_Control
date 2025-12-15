package spacemissioncontrol.service;

import spacemissioncontrol.dao.AbstractDao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractService<T> {

    protected final AbstractDao<T> dao;

    protected AbstractService(AbstractDao<T> dao) {
        this.dao = dao;
    }

    public void showAll() {
        List<T> allItems = dao.findAll();

        if(allItems != null && !allItems.isEmpty()) {
            printList(allItems);
        } else {
            System.out.println("No data found");
        }
    }

    public void showAllByField(String fieldName, String rawValue) {
        Object value = convertValue(fieldName, rawValue);

        List<T> allItems = dao.findAllByField(fieldName, value);

        if(allItems != null && !allItems.isEmpty()) {
            printList(allItems);
        } else {
            System.out.println("No data found");
        }
    }

    private Object convertValue(String fieldName, String rawValue) {
        try{
            Field field = getEntityClass().getDeclaredField(fieldName);
            Class<?> fieldType = field.getType();

            if(fieldType.equals(LocalDate.class)) {
                return LocalDate.parse(rawValue);
            }

            if(fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                return Integer.parseInt(rawValue);
            }

            if(fieldType.equals(BigDecimal.class)) {
                return new BigDecimal(rawValue);
            }

            return rawValue;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field of unknown type: " + fieldName, e);
        }
    }

    protected void printList(List<T> list) {
        for(T m : list) {
            System.out.println(m);
        }
    }

    protected Class<T> getEntityClass() {
        return dao.getEntityClass();
    }
}
