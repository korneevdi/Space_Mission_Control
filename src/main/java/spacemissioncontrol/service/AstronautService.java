package spacemissioncontrol.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.AstronautDao;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Astronaut;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.util.HibernateConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AstronautService extends AbstractService<Astronaut> {

    private final AstronautDao astronautDao;
    private final MissionDao missionDao;

    public AstronautService() {
        this(new AstronautDao(), new MissionDao());
    }

    private AstronautService(AstronautDao astronautDao, MissionDao missionDao) {
        super(astronautDao);
        this.astronautDao = astronautDao;
        this.missionDao = missionDao;
    }

    public void showAllByMissionName(String missionName) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Astronaut> list = astronautDao.findAllByMissionName(session, missionName);

            if (list != null && !list.isEmpty()) {
                printList(list);
            } else {
                System.out.println("No data found for mission " + missionName);
            }
        }
    }

    public void addNew(String firstName, String lastName, String rank,
                       String birthDate, String country, String missionName) {

        if (missionName == null) {
            System.out.println("Required data missing. Mission name should be specified");
            return;
        }

        if (firstName == null || lastName == null || birthDate == null || country == null) {
            System.out.println("Missing data. First name, last name, birth date, and country should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean exists = astronautDao.existsByField(session, Map.of(
                    "firstName", firstName,
                    "lastName", lastName,
                    "birthDate", LocalDate.parse(birthDate),
                    "country", country
            ));
            if (exists) {
                System.out.println("This astronaut already exists");
                transaction.rollback();
                return;
            }

            Mission mission = missionDao.findAllByField(session, "name", missionName)
                    .stream()
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("Mission " + missionName + " not found")
                    );

            Astronaut astronaut = new Astronaut();
            astronaut.setFirstName(firstName);
            astronaut.setLastName(lastName);
            if (rank != null) {
                astronaut.setRank(rank);
            }
            astronaut.setBirthDate(LocalDate.parse(birthDate));
            astronaut.setCountry(country);

            mission.addAstronaut(astronaut);

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
}
