package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.AxeRecherche;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.AxeRechercheRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

@Component
public class ResearchSeeder {

    private final AxeRechercheRepository axeRepo;

    public ResearchSeeder(AxeRechercheRepository axeRepo) {
        this.axeRepo = axeRepo;
    }

    public void seed(Lab lab) {
        LabData.LABS.stream()
                .filter(seed -> seed.acronym().equalsIgnoreCase(lab.getAcronym()))
                .findFirst()
                .ifPresent(seed -> seed.axesRecherche().forEach(title -> {
                    AxeRecherche axe = new AxeRecherche();
                    axe.setLab(lab);
                    axe.setTitle(title);
                    axeRepo.save(axe);
                }));
    }
}
