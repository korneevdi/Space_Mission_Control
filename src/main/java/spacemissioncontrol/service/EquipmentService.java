package spacemissioncontrol.service;

import spacemissioncontrol.dao.EquipmentDao;
import spacemissioncontrol.entity.Equipment;

import java.util.List;

public class EquipmentService {

    private final EquipmentDao equipmentDao = new EquipmentDao();

    public void showAllEquipment() {
        List<Equipment> equipment = equipmentDao.findAll();

        if(equipment != null && !equipment.isEmpty()) {
            printEquipment(equipment);
        } else {
            System.out.println("No data found");
        }
    }

    private void printEquipment(List<Equipment> list) {
        for(Equipment e : list) {
            System.out.println(e);
        }
    }
}
