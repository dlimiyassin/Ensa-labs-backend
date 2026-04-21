package com.ensa.labs.zBase.db;

import com.ensa.labs.zBase.db.seeder.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;
    private final LabSeeder labSeeder;
    private final MemberSeeder memberSeeder;
    private final ResearchSeeder researchSeeder;
    private final CompetenceSeeder competenceSeeder;
    private final EquipmentSeeder equipmentSeeder;
    private final CollaborationSeeder collaborationSeeder;
    private final ProductionSeeder productionSeeder;

    public DatabaseSeeder(
            RoleSeeder roleSeeder,
            UserSeeder userSeeder,
            LabSeeder labSeeder,
            MemberSeeder memberSeeder,
            ResearchSeeder researchSeeder,
            CompetenceSeeder competenceSeeder,
            EquipmentSeeder equipmentSeeder,
            CollaborationSeeder collaborationSeeder,
            ProductionSeeder productionSeeder
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
    }

    @Override
    @Transactional
    public void run(String... args) {

        var roles = roleSeeder.seed();
        var lab = labSeeder.seed();

        var admin = userSeeder.seedAdmin(roles);
        var members = memberSeeder.seed(lab, roles);

        researchSeeder.seed(lab);
        competenceSeeder.seed(lab);
        equipmentSeeder.seed(lab);
        collaborationSeeder.seed(lab);
        productionSeeder.seed(lab);
    }
}
