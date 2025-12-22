package spacemissioncontrol.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.AstronautDao;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Astronaut;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.util.HibernateConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
            List<Astronaut> astronauts = astronautDao.findAllByMissionName(session, missionName);
            printNonEmptyList(astronauts, "No data found for mission " + missionName);
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

        LocalDate realBirthDate;
        try {
            realBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Date must be in format yyyy-MM-dd");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(session, firstName, lastName, realBirthDate, country);
            if(optId.isPresent()) {
                System.out.println("This astronaut already exists");
                transaction.rollback();
                return;
            }

            Mission mission = missionDao.findAllByFields(session, Map.of("name", missionName))
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
            astronaut.setBirthDate(realBirthDate);
            astronaut.setCountry(country);

            if(!isValidEntity(astronaut)) {
                transaction.rollback();
                return;
            }

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

    public void updateFirstName(String firstName, String lastName, String birthDate,
                                String country, String newFirstName) {
        if(newFirstName == null) {
            System.out.println("New first name should be specified");
            return;
        }
        if(firstName.equals(newFirstName)) {
            System.out.println("Old first name and new first name are identical. Nothing to update");
            return;
        }
        updateAstronaut(firstName, lastName, birthDate, country,
                astronaut -> astronaut.setFirstName(newFirstName));
    }

    public void updateLastName(String firstName, String lastName, String birthDate,
                                String country, String newLastName) {
        if(newLastName == null) {
            System.out.println("New last name should be specified");
            return;
        }
        if(lastName.equals(newLastName)) {
            System.out.println("Old last name and new last name are identical. Nothing to update");
            return;
        }
        updateAstronaut(firstName, lastName, birthDate, country,
                astronaut -> astronaut.setLastName(newLastName));
    }

    public void updateBirthDate(String firstName, String lastName, String birthDate,
                               String country, String newBirthDate) {
        if(newBirthDate == null) {
            System.out.println("New birth date should be specified");
            return;
        }
        if(birthDate.equals(newBirthDate)) {
            System.out.println("Old birth date and new birth date are identical. Nothing to update");
            return;
        }
        LocalDate realNewBirthDate;
        try {
            realNewBirthDate = LocalDate.parse(newBirthDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("New date must be in format yyyy-MM-dd");
            return;
        }
        updateAstronaut(firstName, lastName, birthDate, country,
                astronaut -> astronaut.setBirthDate(realNewBirthDate));
    }

    public void updateRank(String firstName, String lastName, String birthDate,
                               String country, String newRank) {
        if(newRank == null) {
            System.out.println("New rank should be specified");
            return;
        }
        updateAstronaut(firstName, lastName, birthDate, country,
                astronaut -> astronaut.setRank(newRank));
    }

    public void updateCountry(String firstName, String lastName, String birthDate,
                               String country, String newCountry) {
        if(newCountry == null) {
            System.out.println("New country should be specified");
            return;
        }
        if(country.equals(newCountry)) {
            System.out.println("Old country and new country are identical. Nothing to update");
            return;
        }
        updateAstronaut(firstName, lastName, birthDate, country,
                astronaut -> astronaut.setCountry(newCountry));
    }

    private void updateAstronaut(String firstName, String lastName, String birthDate,
                                 String country, Consumer<Astronaut> updater){

        if (firstName == null || lastName == null || birthDate == null || country == null) {
            System.out.println("Missing data. First name, last name, birth date, and country should be specified");
            return;
        }

        LocalDate realBirthDate;
        try {
            realBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Date must be in format yyyy-MM-dd");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try{
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(
                    session,
                    firstName,
                    lastName,
                    realBirthDate,
                    country
            );

            if (optId.isEmpty()) {
                System.out.println("This astronaut does not exist");
                transaction.rollback();
                return;
            }

            Astronaut astronaut = dao.findById(session, optId.get())
                    .orElseThrow();

            updater.accept(astronaut);

            if(!isValidEntity(astronaut)) {
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

    private Optional<Integer> findId(Session session, String firstName, String lastName,
                                     LocalDate birthDate, String country) {
        return dao.findIdByFields(session, Map.of(
                "firstName", firstName,
                "lastName", lastName,
                "birthDate", birthDate,
                "country", country
        ));
    }
}
