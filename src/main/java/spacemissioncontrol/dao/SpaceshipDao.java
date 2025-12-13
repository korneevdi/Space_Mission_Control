package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Spaceship;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class SpaceshipDao {

    public List<Spaceship> findAll() {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Spaceship", Spaceship.class).list();
        }
    }
}
