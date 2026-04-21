package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LabSeeder {

    private final LabRepository labRepository;

    public LabSeeder(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public Lab seed() {

        return labRepository.findByAcronym(LabData.ACRONYM)
                .orElseGet(() -> {
                    Lab lab = new Lab();
                    lab.setTitleFr(LabData.TITLE_FR);
                    lab.setTitleEn(LabData.TITLE_EN);
                    lab.setAcronym(LabData.ACRONYM);
                    lab.setUniversity("Université Sultan Moulay Slimane");
                    lab.setProgram("Programme de Structuration de la Recherche Scientifique");
                    lab.setEstablishment("ENSA BM");
                    lab.setPhone("212661487420");
                    lab.setEmail("r.allaoui@usms.ma");
                    lab.setAccreditationStart(LocalDate.of(2026, 1, 1));
                    lab.setAccreditationEnd(LocalDate.of(2029, 12, 31));

                    return labRepository.save(lab);
                });
    }
}