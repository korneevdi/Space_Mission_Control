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
            //missionService.updateName("Artemis I", "Artemis II");
            //missionService.updateStatus("Artemis II", "coming up");
            //missionService.updateLaunchDate("Another mission", "2026-04-04");
            //missionService.updateBudget("Another mission", 12.0);
            //missionService.updateDuration("New mission", 122);
            //missionService.updateDescription("Another mission", "Orbital lunar crewed flight");
            //missionService.delete("New mission");

            AstronautService astronautService = new AstronautService();
            //astronautService.showAll();
            //astronautService.showAllByField("country", "uae");
            //astronautService.showAllByMissionName("juice");
            //astronautService.addNew("Sara", "Mohn", "engineer", "1995-07-01", "Canada", "JUICE");
            //astronautService.updateFirstName("Andrey", "Fedyaev", "1981-02-26", "Russia", "Andrei");
            //astronautService.updateLastName("Andrey", "Fedyaev", "1981-02-26", "Russia", "Fedyaev");
            //astronautService.updateRank("Andrey", "Fedyaev", "1981-02-26", "Russia", "Spec");
            //astronautService.updateBirthDate("Andrey", "Fedyaev", "1981-02-28", "Russia", "1981-02-26");
            //astronautService.updateCountry("Andrey", "Fedyaev", "1981-02-26", "Russia", "Serbia");
            //astronautService.delete("Stephen", "Bowen", "1964-02-13", "USA");

            SpaceshipService spaceshipService = new SpaceshipService();
            //spaceshipService.showAll();
            //spaceshipService.showAllByField("weightKg", "77700");
            //spaceshipService.showAllByMissionName("Artemis i");
            //spaceshipService.addNew("Lana I", "ESA", 82, "8500", "Artemis I");
            //spaceshipService.updateModel("Ariane 5 ECA", "Arianespace", "Ariane 5 ESA");
            //spaceshipService.updateManufacturer("Ariane 5 ESA", "Arianespace", "ArianespaceX");
            //spaceshipService.updateCapacity("Ariane 5 ESA", "ArianespaceX", 45);
            //spaceshipService.updateWeight("Ariane 5 ESA", "ArianespaceX", 15500.0);
            //spaceshipService.delete("Ariane 5 ESA", "ArianespaceX");

            EquipmentService equipmentService = new EquipmentService();
            //equipmentService.showAll();
            //equipmentService.showAllByField("category", "Navigation");
            //equipmentService.showAllByMissionName("juice");
            //equipmentService.showAllByNameLike("camera");
            //equipmentService.addNew("Telescope mirror", "science", "120.4", "JUICE");
            //equipmentService.updateName("Telescope mirror", "science", "Engine");
            //equipmentService.updateCategory("Engine", "science", "sport");
            //equipmentService.updateWeight("Engine", "sport", 11820.0);
            //equipmentService.delete("Engine", "sport");
        }
    }
}