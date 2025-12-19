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

            MissionService missionService = new MissionService();
            //missionService.showAll();
            //missionService.showAllWithDetails();
            //missionService.showAllByField("name", "artemis i");
            //missionService.showAllByNameLike("crew");
            //missionService.addNew("Another mission", null, "in planing", null, null, "description");

            //missionService.delete();
            //missionService.update();

            AstronautService astronautService = new AstronautService();
            //astronautService.showAll();
            //astronautService.showAllByField("country", "uae");
            //astronautService.showAllByMissionName("juice");
            //astronautService.addNew("Sara", "Mohn", "engineer", "1995-07-01", "Canada", "JUICE");

            //astronautService.delete();
            //astronautService.update();

            SpaceshipService spaceshipService = new SpaceshipService();
            //spaceshipService.showAll();
            //spaceshipService.showAllByField("weightKg", "77700");
            //spaceshipService.showAllByMissionName("Artemis i");
            //spaceshipService.addNew("Lana I", "ESA", 82, "8500", "Artemis I");

            //spaceshipService.delete();
            //spaceshipService.update();

            EquipmentService equipmentService = new EquipmentService();
            //equipmentService.showAll();
            //equipmentService.showAllByField("category", "Navigation");
            //equipmentService.showAllByMissionName("juice");
            //equipmentService.showAllByNameLike("camera");
            //equipmentService.addNew("Telescope mirror", "science", "120.4", "JUICE");

            //equipmentService.delete();
            //equipmentService.update();
        }
    }
}