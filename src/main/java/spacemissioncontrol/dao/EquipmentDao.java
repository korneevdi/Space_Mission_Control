package spacemissioncontrol.dao;

import org.hibernate.Session;
import spacemissioncontrol.entity.Equipment;
import spacemissioncontrol.util.HibernateConfig;

import java.util.List;

public class EquipmentDao extends AbstractDao<Equipment> {

    private final static String ENTITY_NAME = "Equipment";

    public EquipmentDao() {
        super(Equipment.class, ENTITY_NAME);
    }

    public List<Equipment> findAllByMissionName(String missionName) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT i FROM " + ENTITY_NAME + " i WHERE i.mission.name = :name",
                            Equipment.class
                    )
                    .setParameter("name", missionName)
                    .getResultList();
        }
    }
}
