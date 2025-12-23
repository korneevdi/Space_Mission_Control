package spacemissioncontrol.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.dao.SpaceshipDao;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.entity.Spaceship;
import spacemissioncontrol.util.HibernateConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class SpaceshipService extends AbstractService<Spaceship> {

    private final SpaceshipDao spaceshipDao;
    private final MissionDao missionDao;

    public SpaceshipService() {
        this(new SpaceshipDao(), new MissionDao());
    }

    private SpaceshipService(SpaceshipDao spaceshipDao, MissionDao missionDao) {
        super(spaceshipDao);
        this.spaceshipDao = spaceshipDao;
        this.missionDao = missionDao;
    }

    public void showAllByMissionName(String missionName) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Spaceship> spaceships = spaceshipDao.findAllByMissionName(session, missionName);
            printNonEmptyList(spaceships, "No data found for mission " + missionName);
        }
    }

    public void addNew(String model, String manufacturer, Integer capacity, String weightKg, String missionName) {

        if (missionName == null) {
            System.out.println("Required data missing. Mission name should be specified");
            return;
        }

        if (model == null) {
            System.out.println("Required data missing. Model should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(session, model, manufacturer);
            if (optId.isPresent()) {
                System.out.println("This spaceship already exists");
                transaction.rollback();
                return;
            }

            Mission mission = missionDao.findAllByFields(session, Map.of("name", missionName))
                    .stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("Mission " + missionName + " not found")
                    );

            Spaceship spaceship = new Spaceship();
            spaceship.setModel(model);
            if (manufacturer != null) {
                spaceship.setManufacturer(manufacturer);
            }
            if (capacity != null) {
                spaceship.setCapacity(capacity);
            }
            if (weightKg != null) {
                spaceship.setWeightKg(new BigDecimal(weightKg));
            }

            if (!isValidEntity(spaceship)) {
                transaction.rollback();
                return;
            }

            mission.addSpaceship(spaceship);

            missionDao.merge(session, mission);

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

    public void updateModel(String model, String manufacturer, String newModel) {
        if (newModel == null) {
            System.out.println("New model should be specified");
            return;
        }
        if (model.equals(newModel)) {
            System.out.println("Old model and new model are identical. Nothing to update");
            return;
        }
        updateSpaceship(model, manufacturer,
                spaceship -> spaceship.setModel(newModel));
    }

    public void updateManufacturer(String model, String manufacturer, String newManufacturer) {
        if (newManufacturer == null) {
            System.out.println("New manufacturer should be specified");
            return;
        }
        if (manufacturer.equals(newManufacturer)) {
            System.out.println("Old manufacturer and new manufacturer are identical. Nothing to update");
            return;
        }
        updateSpaceship(model, manufacturer,
                spaceship -> spaceship.setManufacturer(newManufacturer));
    }

    public void updateCapacity(String model, String manufacturer, Integer newCapacity) {
        if (newCapacity == null || newCapacity < 0) {
            System.out.println("New capacity should be specified and be non-negative");
            return;
        }
        updateSpaceship(model, manufacturer,
                spaceship -> spaceship.setCapacity(newCapacity));
    }

    public void updateWeight(String model, String manufacturer, Double newWeight) {
        if (newWeight == null || newWeight < 0) {
            System.out.println("New weight should be specified and be non-negative");
            return;
        }
        updateSpaceship(model, manufacturer,
                spaceship -> spaceship.setWeightKg(new BigDecimal(newWeight)));
    }

    private void updateSpaceship(String model, String manufacturer, Consumer<Spaceship> updater) {

        if (model == null || manufacturer == null) {
            System.out.println("Missing data. Model and manufacturer should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(
                    session,
                    model,
                    manufacturer
            );

            if (optId.isEmpty()) {
                System.out.println("This spaceship does not exist");
                transaction.rollback();
                return;
            }

            Spaceship spaceship = dao.findById(session, optId.get())
                    .orElseThrow();

            updater.accept(spaceship);

            if (!isValidEntity(spaceship)) {
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

    public void delete(String model, String manufacturer) {

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(
                    session,
                    model,
                    manufacturer
            );

            if (optId.isEmpty()) {
                System.out.println("This spaceship does not exist");
                transaction.rollback();
                return;
            }

            Spaceship spaceship = dao.findById(session, optId.get())
                    .orElseThrow(() -> new IllegalArgumentException("Not found"));

            for (Mission m : spaceship.getMissionList()) {
                m.getSpaceshipList().remove(spaceship);
            }

            spaceship.getMissionList().clear();

            dao.delete(session, spaceship);

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

    private Optional<Integer> findId(Session session, String model, String manufacturer) {
        return dao.findIdByFields(session, Map.of(
                "model", model,
                "manufacturer", manufacturer
        ));
    }
}
