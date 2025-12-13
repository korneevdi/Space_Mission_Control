package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class MissionDao {

    public List<Mission> findAll(){
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("FROM Mission", Mission.class).list();
        }
    }
}
