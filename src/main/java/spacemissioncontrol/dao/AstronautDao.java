package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Astronaut;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class AstronautDao {

    public List<Astronaut> findAll() {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Astronaut", Astronaut.class).list();
        }
    }
}
