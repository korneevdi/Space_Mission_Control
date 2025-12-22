package spacemissioncontrol.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.EquipmentDao;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Equipment;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.util.HibernateConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
            List<Equipment> equipment = equipmentDao.findAllByMissionName(session, missionName);
            printNonEmptyList(equipment, "No data found for mission " + missionName);
        }
    }

    public void showAllByNameLike(String nameLike) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Equipment> equipment = equipmentDao.findAllByNameLike(session, nameLike);
            printNonEmptyList(equipment, "No data found");
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

            Optional<Integer> optId = findId(session, name, category);
            if(optId.isPresent()) {
                System.out.println("This equipment already exists");
                transaction.rollback();
                return;
            }

            Mission mission = missionDao.findAllByFields(session, Map.of("name", missionName))
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

            if(!isValidEntity(equipment)) {
                transaction.rollback();
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

    public void updateName(String name, String category, String newName) {
        if(newName == null) {
            System.out.println("New name should be specified");
            return;
        }
        if(name.equals(newName)) {
            System.out.println("Old name and new name are identical. Nothing to update");
            return;
        }
        updateEquipment(name, category,
                equipment -> equipment.setName(newName));
    }

    public void updateCategory(String name, String category, String newCategory) {
        if(newCategory == null) {
            System.out.println("New category should be specified");
            return;
        }
        if(category.equals(newCategory)) {
            System.out.println("Old category and new category are identical. Nothing to update");
            return;
        }
        updateEquipment(name, category,
                equipment -> equipment.setCategory(newCategory));
    }

    public void updateWeight(String name, String category, Double newWeight) {
        if(newWeight == null || newWeight < 0) {
            System.out.println("New weight should be specified and be non-negative");
            return;
        }
        updateEquipment(name, category,
                equipment -> equipment.setWeightKg(new BigDecimal(newWeight)));
    }

    private void updateEquipment(String name, String category, Consumer<Equipment> updater){

        if (name == null || category == null) {
            System.out.println("Missing data. Name and category should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try{
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(
                    session,
                    name,
                    category
            );

            if (optId.isEmpty()) {
                System.out.println("This equipment does not exist");
                transaction.rollback();
                return;
            }

            Equipment equipment = dao.findById(session, optId.get())
                    .orElseThrow();

            updater.accept(equipment);

            if(!isValidEntity(equipment)) {
                transaction.rollback();
                return;
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private Optional<Integer> findId(Session session, String name, String category) {
        return dao.findIdByFields(session, Map.of(
                "name", name,
                "category", category
        ));
    }
}
