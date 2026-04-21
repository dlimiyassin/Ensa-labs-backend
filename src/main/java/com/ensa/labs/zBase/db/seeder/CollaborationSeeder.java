package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Collaboration;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.enums.CollaborationScope;
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

        seedByScope(LabData.COLLAB_REGIONAL, lab, CollaborationScope.REGIONAL);
        seedByScope(LabData.COLLAB_NATIONAL, lab, CollaborationScope.NATIONAL);
        seedByScope(LabData.COLLAB_INT, lab, CollaborationScope.INTERNATIONAL);
    }

    private void seedByScope(List<CollabData> list, Lab lab, CollaborationScope scope) {
        for (CollabData d : list) {
            Collaboration c = new Collaboration();
            c.setOrganization(d.organization());
            c.setTheme(d.theme());
            c.setNature(d.nature());
            c.setScope(scope);
            c.setEstablishment(lab.getEstablishment());
            repo.save(c);
        }
    }
}
