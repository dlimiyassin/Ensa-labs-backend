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
        if (prodRepo.findByLabAcronym(lab.getAcronym()).isPresent()) return;

        Production production = new Production();
        production.setLab(lab);

        Set<Publication> pubs = new HashSet<>();
        for (PublicationData d : LabData.PUBLICATIONS) {
            if (!lab.getAcronym().equalsIgnoreCase(d.labAcronym())) continue;
            Publication p = new Publication();
            p.setLab(lab);
            p.setType(d.type() == null ? PublicationType.JOURNAL : d.type());
            p.setTitle(d.title());
            p.setPublicationYear(d.year());
            p.setAuthors(d.authors());
            p.setJournal(d.journal());
            p.setConference(d.conference());
            p.setDoi(d.doi());
            p.setPages(d.pages());
            pubs.add(pubRepo.save(p));
        }

        Set<Publication> communications = new HashSet<>();
        for (PublicationData d : LabData.COMMUNICATIONS) {
            if (!lab.getAcronym().equalsIgnoreCase(d.labAcronym())) continue;
            Publication p = new Publication();
            p.setLab(lab);
            p.setType(PublicationType.COMMUNICATION);
            p.setTitle(d.title());
            p.setPublicationYear(d.year());
            p.setAuthors(d.authors());
            p.setConference(d.conference());
            p.setJournal(d.journal());
            p.setDoi(d.doi());
            p.setPages(d.pages());
            communications.add(pubRepo.save(p));
        }

        Set<Thesis> theses = new HashSet<>();
        for (ThesisData d : LabData.THESES) {
            if (!lab.getAcronym().equalsIgnoreCase(d.labAcronym())) continue;
            Thesis t = new Thesis();
            t.setLab(lab);
            t.setAuthor(d.author());
            t.setTitle(d.title());
            t.setDefenseDate(d.defenseDate());
            t.setSupervisor(d.supervisor());
            theses.add(thesisRepo.save(t));
        }

        production.setPublications(pubs);
        production.setCommunications(communications);
        production.setTheses(theses);

        prodRepo.save(production);
    }
}
