package spacemissioncontrol.service;

import jakarta.validation.ConstraintViolation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.dao.SpaceshipDao;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.entity.Spaceship;
import spacemissioncontrol.util.EntityValidator;
import spacemissioncontrol.util.HibernateConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Spaceship> spaceships = spaceshipDao.findAllByMissionName(session, missionName);
            printNonEmptyList(spaceships, "No data found for mission " + missionName);
        }
    }

    public void addNew(String model, String manufacturer, Integer capacity, String weightKg, String missionName) {

        if(missionName == null) {
            System.out.println("Required data missing. Mission name should be specified");
            return;
        }

        if(model == null) {
            System.out.println("Required data missing. Model should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean exists = spaceshipDao.existsByField(session, Map.of(
                    "model", model,
                    "manufacturer", manufacturer
            ));
            if(exists) {
                System.out.println("This spaceship already exists");
                transaction.rollback();
                return;
            }

            Mission mission = missionDao.findAllByField(session, "name", missionName)
                    .stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("Mission " + missionName + " not found")
                    );

            Spaceship spaceship = new Spaceship();
            spaceship.setModel(model);
            if(manufacturer != null) {
                spaceship.setManufacturer(manufacturer);
            }
            if(capacity != null) {
                spaceship.setCapacity(capacity);
            }
            if(weightKg != null) {
                spaceship.setWeightKg(new BigDecimal(weightKg));
            }

            Set<ConstraintViolation<Spaceship>> violations =
                    EntityValidator.validate(spaceship);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<Spaceship> s : violations) {
                    System.out.println(
                            "Field '" + s.getPropertyPath() +
                                    "': " + s.getMessage()
                    );
                }
                return;
            }

            mission.addSpaceship(spaceship);

            missionDao.merge(session, mission);

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
