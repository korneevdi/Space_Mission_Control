package spacemissioncontrol.service;

import jakarta.validation.ConstraintViolation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Astronaut;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.entity.MissionDetails;
import spacemissioncontrol.util.EntityValidator;
import spacemissioncontrol.util.HibernateConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class MissionService extends AbstractService<Mission> {

    private final MissionDao missionDao;

    public MissionService() {
        this(new MissionDao());
    }

    private MissionService(MissionDao missionDao) {
        super(missionDao);
        this.missionDao = missionDao;
    }

    public void showAllWithDetails() {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Mission> missions = missionDao.findAllWithDetails(session);

            if(missions != null && !missions.isEmpty()) {
                printMissionsWithDetails(missions);
            } else {
                System.out.println("No data found");
            }
        }
    }

    public void showAllByNameLike(String nameLike) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<Mission> missions = missionDao.findAllByNameLike(session, nameLike);
            printNonEmptyList(missions, "No data found");
        }
    }

    public void addNew(String name, String launchDate, String status, String budgetMillionUSD,
                    Integer durationDays, String description) {

        if(name == null || status == null || description == null) {
            System.out.println("Required data missing. Mission name, status, and description should be specified");
            return;
        }

        LocalDate realLaunchDate;
        try {
            realLaunchDate = LocalDate.parse(launchDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Date must be in format yyyy-MM-dd");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(session, name);
            if(optId.isPresent()) {
                System.out.println("This mission already exists");
                transaction.rollback();
                return;
            }

            Mission mission = new Mission();
            mission.setName(name);
            mission.setLaunchDate(realLaunchDate);
            mission.setStatus(status);

            MissionDetails details = new MissionDetails();
            if(budgetMillionUSD != null) {
                details.setBudgetMillionUSD(new BigDecimal(budgetMillionUSD));
            }
            if(durationDays != null) {
                details.setDurationDays(durationDays);
            }
            details.setDescription(description);

            mission.setMissionDetails(details);
            details.setMission(mission);

            if(!isValidEntity(mission)) {
                transaction.rollback();
                return;
            }

            missionDao.insert(session, mission);

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

    public void updateName(String name, String newName) {
        if(newName == null) {
            System.out.println("New name should be specified");
            return;
        }
        if(name.equals(newName)) {
            System.out.println("Old name and new name are identical. Nothing to update");
            return;
        }
        updateMission(name,
                mission -> mission.setName(newName));
    }

    public void updateLaunchDate(String name, String newLaunchDate) {
        if(newLaunchDate == null) {
            System.out.println("New launch date should be specified");
            return;
        }
        LocalDate realNewLaunchDate;
        try {
            realNewLaunchDate = LocalDate.parse(newLaunchDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("New launch date must be in format yyyy-MM-dd");
            return;
        }
        updateMission(name,
                mission -> mission.setLaunchDate(realNewLaunchDate));
    }

    public void updateStatus(String name, String newStatus) {
        if(newStatus == null) {
            System.out.println("New status should be specified");
            return;
        }
        updateMission(name,
                mission -> mission.setStatus(newStatus));
    }

    public void updateBudget(String name, Double newBudgetMillionUSD) {
        if(newBudgetMillionUSD == null || newBudgetMillionUSD < 0) {
            System.out.println("New budget should be specified and be non-negative");
            return;
        }
        updateMission(name,
                mission -> mission.getMissionDetails().setBudgetMillionUSD(new BigDecimal(newBudgetMillionUSD)));
    }

    public void updateDuration(String name, Integer newDuration) {
        if(newDuration == null || newDuration < 0) {
            System.out.println("New duration should be specified and be non-negative");
            return;
        }
        updateMission(name,
                mission -> mission.getMissionDetails().setDurationDays(newDuration));
    }

    public void updateDescription(String name, String newDescription) {
        if(newDescription == null) {
            System.out.println("New description should be specified");
            return;
        }
        updateMission(name,
                mission -> mission.getMissionDetails().setDescription(newDescription));
    }

    private void updateMission(String name, Consumer<Mission> updater){

        if (name == null) {
            System.out.println("Missing data. Mission name should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try{
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Optional<Integer> optId = findId(
                    session,
                    name
            );

            if (optId.isEmpty()) {
                System.out.println("This mission does not exist");
                transaction.rollback();
                return;
            }

            Mission mission = dao.findById(session, optId.get())
                    .orElseThrow();

            updater.accept(mission);

            if(!isValidEntity(mission)) {
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

    private Optional<Integer> findId(Session session, String name) {
        return dao.findIdByFields(session, Map.of(
                "name", name
        ));
    }

    private void printMissionsWithDetails(List<Mission> list) {
        for(Mission m : list) {
            System.out.print(m);

            MissionDetails details = m.getMissionDetails();
            if(details != null) {
                System.out.println("    Budget: " + details.getBudgetMillionUSD() + " Mio. USD");
                System.out.println("    Duration: " + details.getDurationDays() + " days");
                System.out.println("    Description: " + details.getDescription() + "\n");
            }
        }
    }
}
