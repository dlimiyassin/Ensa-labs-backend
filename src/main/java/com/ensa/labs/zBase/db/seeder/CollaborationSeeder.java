package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Collaboration;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.CollaborationRepository;
import com.ensa.labs.zBase.db.data.CollabData;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollaborationSeeder {

    private final CollaborationRepository repo;

    public CollaborationSeeder(CollaborationRepository repo) {
        this.repo = repo;
    }

    public void seed(Lab lab) {
        List<CollabData> collaborations = switch (lab.getAcronym()) {
            case "LRSTA" -> LabData.COLLAB_LRSTA;
            case "LaRESI" -> LabData.COLLAB_LARESI;
            default -> List.of();
        };
        seedForLab(collaborations, lab);
    }

    private void seedForLab(List<CollabData> list, Lab lab) {
        for (CollabData d : list) {
            Collaboration c = new Collaboration();
            c.setOrganization(d.organization());
            c.setEstablishment(d.establishment());
            c.setTheme(d.theme());
            c.setNature(d.nature());
            c.setScope(d.scope());
            c.setLab(lab);
            repo.save(c);
        }
    }
}
