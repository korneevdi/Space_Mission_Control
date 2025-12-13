package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Equipment;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class EquipmentDao {

    public List<Equipment> findAll() {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Equipment", Equipment.class).list();
        }
    }
}
