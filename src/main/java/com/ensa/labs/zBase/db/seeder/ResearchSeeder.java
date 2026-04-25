package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.AxeRecherche;
import com.ensa.labs.recherche.bean.DomaineRecherche;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.AxeRechercheRepository;
import com.ensa.labs.recherche.dao.DomaineRechercheRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ResearchSeeder {

    private final AxeRechercheRepository axeRepo;
    private final DomaineRechercheRepository domaineRepo;

    public ResearchSeeder(AxeRechercheRepository axeRepo, DomaineRechercheRepository domaineRepo) {
        this.axeRepo = axeRepo;
        this.domaineRepo = domaineRepo;
    }

    public void seed(Lab lab) {
        LabData.LABS.stream()
                .filter(seed -> seed.acronym().equalsIgnoreCase(lab.getAcronym()))
                .findFirst()
                .ifPresent(seed -> {

                    // Save Axes
                    seed.axesRecherche().forEach(title -> {
                        AxeRecherche axe = new AxeRecherche();
                        axe.setLab(lab);
                        axe.setTitle(title);
                        axeRepo.save(axe);
                    });

                    // Save Domaines (with deduplication)
                    seed.demainesRecherche().forEach(name -> {

                        DomaineRecherche domaine = domaineRepo
                                .findByName(name)
                                .orElseGet(() -> {
                                    DomaineRecherche newDomaine = new DomaineRecherche();
                                    newDomaine.setName(name);
                                    return domaineRepo.save(newDomaine);
                                });

                        // 🔗 Link Lab <-> Domaine
                        domaine.getLabs().add(lab);
                        lab.getDomainesRecherche().add(domaine);
                    });
                });
    }

}