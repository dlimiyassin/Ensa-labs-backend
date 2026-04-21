package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Equipment;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.enums.EquipmentCategory;
import com.ensa.labs.recherche.dao.EquipmentRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquipmentSeeder {

    private final EquipmentRepository repo;

    public EquipmentSeeder(EquipmentRepository repo) {
        this.repo = repo;
    }

    public void seed(Lab lab) {

        seedByCategory(lab, LabData.EQUIP_LAB, EquipmentCategory.LAB);
        seedByCategory(lab, LabData.EQUIP_UNIV, EquipmentCategory.UNIVERSITY);
        seedByCategory(lab, LabData.EQUIP_SHARED, EquipmentCategory.SHARED);
        seedByCategory(lab, LabData.EQUIP_IT, EquipmentCategory.IT);
    }

    private void seedByCategory(Lab lab, List<String> list, EquipmentCategory cat) {
        for (String value : list) {
            Equipment e = new Equipment();
            e.setLab(lab);
            e.setCategory(cat);
            e.setName(value);
            repo.save(e);
        }
    }
}