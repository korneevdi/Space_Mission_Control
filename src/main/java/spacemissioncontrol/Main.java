package spacemissioncontrol;

import org.hibernate.SessionFactory;
import spacemissioncontrol.service.AstronautService;
import spacemissioncontrol.service.EquipmentService;
import spacemissioncontrol.service.MissionService;
import spacemissioncontrol.service.SpaceshipService;
import spacemissioncontrol.util.HibernateConfig;

public class Main {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateConfig.getSessionFactory()) {
            System.out.println("Hibernate started!");

            //MissionService missionService = new MissionService();
            //missionService.showAll();
            //missionService.showAllWithDetails();
            //missionService.showAllByField("launchDate", "2022-11-16");
            //missionService.add();
            //missionService.delete();
            //missionService.update();

            //AstronautService astronautService = new AstronautService();
            //astronautService.showAll();
            //astronautService.showAllByField("country", "USA");

            //SpaceshipService spaceshipService = new SpaceshipService();
            //spaceshipService.showAll();
            //spaceshipService.showAllByField("weightKg", "77700");

            //EquipmentService equipmentService = new EquipmentService();
            //equipmentService.showAll();
            //equipmentService.showAllByField("category", "Navigation");
        }
    }
}