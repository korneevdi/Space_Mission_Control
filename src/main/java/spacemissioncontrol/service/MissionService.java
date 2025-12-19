package spacemissioncontrol.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import spacemissioncontrol.dao.MissionDao;
import spacemissioncontrol.entity.Mission;
import spacemissioncontrol.entity.MissionDetails;
import spacemissioncontrol.util.HibernateConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

            if(missions != null && !missions.isEmpty()) {
                printList(missions);
            } else {
                System.out.println("No data found");
            }
        }
    }

    public void addNew(String name, String launchDate, String status, String budgetMillionUSD,
                    Integer durationDays, String description) {

        if(name == null || status == null || description == null) {
            System.out.println("Required data missing. Mission name, status, and description should be specified");
            return;
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            boolean exists = missionDao.existsByField(session, Map.of("name", name));
            if(exists) {
                System.out.println("Mission with name '" + name + "' already exists");
                transaction.rollback();
                return;
            }

            Mission mission = new Mission();
            mission.setName(name);
            if(launchDate != null) {
                mission.setLaunchDate(LocalDate.parse(launchDate));
            }
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
