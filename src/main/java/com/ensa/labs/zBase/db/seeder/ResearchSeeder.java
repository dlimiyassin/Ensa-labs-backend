package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.AxeRecherche;
import com.ensa.labs.recherche.bean.DomaineRecherche;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.AxeRechercheRepository;
import com.ensa.labs.recherche.dao.DomaineRechercheRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

@Component
public class ResearchSeeder {

    private final DomaineRechercheRepository domaineRepo;
    private final AxeRechercheRepository axeRepo;

    public ResearchSeeder(DomaineRechercheRepository domaineRepo,
                          AxeRechercheRepository axeRepo) {
        this.domaineRepo = domaineRepo;
        this.axeRepo = axeRepo;
    }

    public void seed(Lab lab) {

        for (String d : LabData.DOMAINES) {
            DomaineRecherche domaine = domaineRepo.findByName(d)
                    .orElseGet(() -> {
                        DomaineRecherche newD = new DomaineRecherche();
                        newD.setName(d);
                        return domaineRepo.save(newD);
                    });

            lab.getDomainesRecherche().add(domaine);
        }

        for (String a : LabData.AXES) {
            AxeRecherche axe = new AxeRecherche();
            axe.setLab(lab);
            axe.setTitle(a);
            axeRepo.save(axe);
        }
    }
}
