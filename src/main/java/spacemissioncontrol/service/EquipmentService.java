package spacemissioncontrol.service;

import spacemissioncontrol.dao.EquipmentDao;
import spacemissioncontrol.entity.Equipment;

import java.util.List;

public class EquipmentService extends AbstractService<Equipment> {

    private final EquipmentDao equipmentDao;

    public EquipmentService() {
        this(new EquipmentDao());
    }

    private EquipmentService(EquipmentDao equipmentDao) {
        super(equipmentDao);
        this.equipmentDao = equipmentDao;
    }

    public void showAllByMissionName(String missionName) {
        List<Equipment> list = equipmentDao.findAllByMissionName(missionName);

        if(list != null && !list.isEmpty()) {
            printList(list);
        } else {
            System.out.println("No data found for mission " + missionName);
        }
    }
}
