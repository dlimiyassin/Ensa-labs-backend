package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Competence;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.enums.CompetenceType;
import com.ensa.labs.recherche.dao.CompetenceRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompetenceSeeder {

    private final CompetenceRepository repo;

    public CompetenceSeeder(CompetenceRepository repo) {
        this.repo = repo;
    }

    public void seed(Lab lab) {

        seedByType(lab, LabData.COMPETENCES_SCIENTIFIC, CompetenceType.SCIENTIFIC);
        seedByType(lab, LabData.COMPETENCES_TECH, CompetenceType.TECHNOLOGICAL);
        seedByType(lab, LabData.COMPETENCES_SECTOR, CompetenceType.SECTORIAL);
        seedByType(lab, LabData.COMPETENCES_INNOVATION, CompetenceType.INNOVATION);
    }

    private void seedByType(Lab lab, List<String> list, CompetenceType type) {
        for (String value : list) {
            Competence c = new Competence();
            c.setLab(lab);
            c.setType(type);
            c.setDescription(value);
            repo.save(c);
        }
    }
}