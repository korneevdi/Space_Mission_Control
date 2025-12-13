package spacemissioncontrol;

import org.hibernate.SessionFactory;
import spacemissioncontrol.service.MissionService;
import spacemissioncontrol.util.HibernateConfig;

public class Main {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateConfig.getSessionFactory()) {
            System.out.println("Hibernate started!");

            MissionService missionService = new MissionService();
            missionService.showAllMissions();
        }
    }
}