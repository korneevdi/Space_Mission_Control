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
            //missionService.showAllByField("name", "artemis i");

            //missionService.showByNameLike(); // Ignore case?
            //missionService.add();
            //missionService.delete();
            //missionService.update();

            //AstronautService astronautService = new AstronautService();
            //astronautService.showAll();
            //astronautService.showAllByField("country", "uae");
            //astronautService.showAllByMissionName("Crew-6");

            //SpaceshipService spaceshipService = new SpaceshipService();
            //spaceshipService.showAll();
            //spaceshipService.showAllByField("weightKg", "77700");
            //spaceshipService.showAllByMissionName("Artemis I");

            //EquipmentService equipmentService = new EquipmentService();
            //equipmentService.showAll();
            //equipmentService.showAllByField("category", "Navigation");
            //equipmentService.showAllByMissionName("artemis i");

            //equipmentService.showByNameLike();
        }
    }
}