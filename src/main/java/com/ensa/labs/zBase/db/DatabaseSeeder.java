package com.ensa.labs.zBase.db;

import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.zBase.db.seeder.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private final EquipeSeeder equipeSeeder;
    private final RoleAssignmentSeeder roleAssignmentSeeder;
    private final LabRepository labRepository;

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
            LabRepository labRepository
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
        if (labRepository.count() == 0) {
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

        if (roleSeeder.structureRolesCreated()) {
            roleAssignmentSeeder.seed(labs, roles);
        }
    }
}
