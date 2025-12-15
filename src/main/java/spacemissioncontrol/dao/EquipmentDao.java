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
}
