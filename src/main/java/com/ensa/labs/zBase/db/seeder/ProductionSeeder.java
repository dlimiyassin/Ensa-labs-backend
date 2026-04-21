package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Production;
import com.ensa.labs.recherche.bean.Publication;
import com.ensa.labs.recherche.bean.Thesis;
import com.ensa.labs.recherche.bean.enums.PublicationType;
import com.ensa.labs.recherche.dao.ProductionRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.recherche.dao.ThesisRepository;
import com.ensa.labs.zBase.db.data.LabData;
import com.ensa.labs.zBase.db.data.PublicationData;
import com.ensa.labs.zBase.db.data.ThesisData;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductionSeeder {

    private final PublicationRepository pubRepo;
    private final ThesisRepository thesisRepo;
    private final ProductionRepository prodRepo;

    public ProductionSeeder(PublicationRepository pubRepo,
                            ThesisRepository thesisRepo,
                            ProductionRepository prodRepo) {
        this.pubRepo = pubRepo;
        this.thesisRepo = thesisRepo;
        this.prodRepo = prodRepo;
    }

    public void seed(Lab lab) {
        if (prodRepo.count() > 0) {
            return; // already seeded
        }

        Production production = new Production();
        production.setLab(lab);

        Set<Publication> pubs = new HashSet<>();
        for (PublicationData d : LabData.PUBLICATIONS) {
            Publication p = new Publication();
            p.setLab(lab);
            p.setType(PublicationType.JOURNAL);
            p.setTitle(d.title());
            p.setPublicationYear(d.year());
            p.setAuthors(d.authors());
            p.setJournal(d.journal());
            p.setDoi(d.doi());
            pubs.add(pubRepo.save(p));
        }

        Set<Thesis> theses = new HashSet<>();
        for (ThesisData d : LabData.THESES) {
            Thesis t = new Thesis();
            t.setLab(lab);
            t.setAuthor(d.author());
            t.setTitle(d.title());
            t.setSupervisor(d.supervisor());
            theses.add(thesisRepo.save(t));
        }

        production.setPublications(pubs);
        production.setTheses(theses);

        prodRepo.save(production);
    }
}