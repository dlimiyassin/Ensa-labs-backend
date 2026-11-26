package com.ensa.labs.zBase.db;

import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.PublicationRepository;
import com.ensa.labs.zBase.db.seeder.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Value("${ENSA_ENABLE_DEMO_DATA:false}")
    private boolean demoDataEnabled;

    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;
    private final LabSeeder labSeeder;
    private final MemberSeeder memberSeeder;
    private final ResearchSeeder researchSeeder;
    private final CompetenceSeeder competenceSeeder;
    private final EquipmentSeeder equipmentSeeder;
    private final CollaborationSeeder collaborationSeeder;
    private final ProductionSeeder productionSeeder;
    private final EquipeSeeder equipeSeeder;
    private final RoleAssignmentSeeder roleAssignmentSeeder;
    private final LabRepository labRepository;
    private final PublicationRepository publicationRepository;

    /** Menu names of the seeded labs, filled once when a lab has none. */
    private static final Map<String, String> SHORT_TITLES = Map.of(
            "LRSTA", "Technologies Avancées",
            "LaRESI", "Ingénierie et Innovation"
    );

    public DatabaseSeeder(
            RoleSeeder roleSeeder,
            UserSeeder userSeeder,
            LabSeeder labSeeder,
            MemberSeeder memberSeeder,
            ResearchSeeder researchSeeder,
            CompetenceSeeder competenceSeeder,
            EquipmentSeeder equipmentSeeder,
            CollaborationSeeder collaborationSeeder,
            ProductionSeeder productionSeeder,
            EquipeSeeder equipeSeeder,
            RoleAssignmentSeeder roleAssignmentSeeder,
            LabRepository labRepository,
            PublicationRepository publicationRepository
    ) {
        this.roleSeeder = roleSeeder;
        this.userSeeder = userSeeder;
        this.labSeeder = labSeeder;
        this.memberSeeder = memberSeeder;
        this.researchSeeder = researchSeeder;
        this.competenceSeeder = competenceSeeder;
        this.equipmentSeeder = equipmentSeeder;
        this.collaborationSeeder = collaborationSeeder;
        this.productionSeeder = productionSeeder;
        this.equipeSeeder = equipeSeeder;
        this.roleAssignmentSeeder = roleAssignmentSeeder;
        this.labRepository = labRepository;
        this.publicationRepository = publicationRepository;
    }

    /**
     * Roles and the admin account are ensured on every start. The lab content (members, labs,
     * teams, resources, productions...) is seeded only into an empty database: once it exists it
     * is managed from the dashboard, and re-seeding would duplicate records or revert edits.
     */
    @Override
    @Transactional
    public void run(String... args) {

        var roles = roleSeeder.seed();
        userSeeder.seedAdmin(roles);

        List<Lab> labs;
        boolean initialCatalog = demoDataEnabled && labRepository.count() == 0;
        if (initialCatalog) {
            memberSeeder.seed(roles);
            labs = labSeeder.seed();
            for (var lab : labs) {
                researchSeeder.seed(lab);
                competenceSeeder.seed(lab);
                equipmentSeeder.seed(lab);
                collaborationSeeder.seed(lab);
                productionSeeder.seed(lab);
                equipeSeeder.seed(lab);
            }
        } else {
            labs = labRepository.findAll();
        }

        if (initialCatalog || roleSeeder.structureRolesCreated()) {
            roleAssignmentSeeder.seed(labs, roles);
        }

        for (Lab lab : labs) {
            if (lab.getShortTitle() == null && SHORT_TITLES.containsKey(lab.getAcronym())) {
                lab.setShortTitle(SHORT_TITLES.get(lab.getAcronym()));
                labRepository.save(lab);
            }
        }
        publicationRepository.validateLegacyPublications();
    }
}
