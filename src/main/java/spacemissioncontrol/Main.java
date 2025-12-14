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
            //missionService.showAllMissions();
            missionService.showAllMissionsWithDetails();

            //AstronautService astronautService = new AstronautService();
            //astronautService.showAllAstronauts();

            //SpaceshipService spaceshipService = new SpaceshipService();
            //spaceshipService.showAllSpaceships();

            //EquipmentService equipmentService = new EquipmentService();
            //equipmentService.showAllEquipment();
        }
    }
}