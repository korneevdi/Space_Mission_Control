package spacemissioncontrol.service;

import jakarta.validation.ConstraintViolation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.EquipmentDao;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Equipment;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.util.EntityValidator;
import spacemissioncontrol.util.HibernateConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EquipmentService extends AbstractService<Equipment> {

    private final EquipmentDao equipmentDao;
    private final MissionDao missionDao;

    public EquipmentService() {
        this(new EquipmentDao(), new MissionDao());
    }

    private EquipmentService(EquipmentDao equipmentDao, MissionDao missionDao) {
        super(equipmentDao);
        this.equipmentDao = equipmentDao;
        this.missionDao = missionDao;
    }

    public void showAllByMissionName(String missionName) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Equipment> list = equipmentDao.findAllByMissionName(session, missionName);

            if(list != null && !list.isEmpty()) {
                printList(list);
            } else {
                System.out.println("No data found for mission " + missionName);
            }
        }
    }

    public void showAllByNameLike(String nameLike) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Equipment> list = equipmentDao.findAllByNameLike(session, nameLike);

            if(list != null && !list.isEmpty()) {
                printList(list);
            } else {
                System.out.println("No data found");
            }
        }
    }

    public void addNew(String name, String category, String weightKg, String missionName) {

        if(missionName == null) {
            System.out.println("Required data missing. Mission name should be specified");
            return;
        }

        if(name == null) {
            System.out.println("Required data missing. Equipment name should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean exists = equipmentDao.existsByField(session, Map.of(
                    "name", name,
                    "category", category
            ));
            if(exists) {
                System.out.println("This equipment unit already exists");
                transaction.rollback();
                return;
            }

            Mission mission = missionDao.findAllByField(session, "name", missionName)
                    .stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("Mission " + missionName + " not found")
                    );

            Equipment equipment = new Equipment();
            equipment.setName(name);
            if(category != null) {
                equipment.setCategory(category);
            }
            if(weightKg != null) {
                equipment.setWeightKg(new BigDecimal(weightKg));
            }
            equipment.setMission(mission);

            Set<ConstraintViolation<Equipment>> violations =
                    EntityValidator.validate(equipment);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<Equipment> e : violations) {
                    System.out.println(
                            "Field '" + e.getPropertyPath() +
                                    "': " + e.getMessage()
                    );
                }
                return;
            }

            equipmentDao.insert(session, equipment);

            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }
}
