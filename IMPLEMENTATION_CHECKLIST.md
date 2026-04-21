# Implementation Checklist - Backend Refactoring

## Foundation Phase (COMPLETED ✅)

### Entity Creation
- [x] Utilisateur entity created with French field names
- [x] Membre entity created (redundancy removed)
- [x] Equipe entity created
- [x] Laboratoire entity created
- [x] DomaineRecherche entity created
- [x] ElementRecherche entity created
- [x] These entity created
- [x] Equipement entity created
- [x] Competence_Renamed entity created
- [x] Departement entity created
- [x] Etiquette entity created
- [x] Production_Renamed entity created
- [x] Publication_Renamed entity created
- [x] Collaboration_Renamed entity created

### Repository Creation
- [x] UtilisateurDao created
- [x] MembreRepository created
- [x] EquipeRepository created
- [x] LaboratoireRepository created
- [x] DomaineRechercheRepository created
- [x] ElementRechercheRepository created
- [x] TheseRepository created
- [x] EquipementRepository created
- [x] CompetenceRenamedRepository created
- [x] DepartementRepository created
- [x] EtiquetteRepository created
- [x] PublicationRenamedRepository created
- [x] CollaborationRenamedRepository created
- [x] ProductionRenamedRepository created

### Documentation
- [x] REFACTORING_GUIDE.md created
- [x] FIELD_MAPPING_REFERENCE.md created
- [x] CODE_EXAMPLES.md created
- [x] REFACTORING_SUMMARY.md created
- [x] IMPLEMENTATION_CHECKLIST.md created

---

## Phase 4: DTO Creation (TODO)

### UtilisateurDTO
- [ ] Create UtilisateurDTO class
- [ ] Add fields: id, nomUtilisateur, prenom, nom, courrierElectronique, cin, numeroTelephone, actif, statut, derniereConnexion
- [ ] Add Set<RoleDTO> roles
- [ ] Add Javadoc comments
- [ ] Add @Data, @NoArgsConstructor, @AllArgsConstructor annotations
- [ ] Test DTO creation and serialization

### MembreDTO
- [ ] Create MembreDTO class
- [ ] Add fields: id, grade, specialite, etablissement, associe, roleDansLaboratoire, doctorantsEncadres
- [ ] Add foreign keys: laboratoireId, utilisateurId
- [ ] Add reference: UtilisateurDTO utilisateur
- [ ] Do NOT include firstName/lastName fields
- [ ] Add @Data, @NoArgsConstructor, @AllArgsConstructor annotations

### EquipeDTO
- [ ] Create EquipeDTO class
- [ ] Add fields: id, nom
- [ ] Add foreign keys: laboratoireId, domaineRechercheId
- [ ] Add collections: Set<String> membreIds
- [ ] Add references: LaboratoireDTO laboratoire, DomaineRechercheDTO domaineRecherche
- [ ] Add @Data, @NoArgsConstructor, @AllArgsConstructor annotations

### LaboratoireDTO
- [ ] Create LaboratoireDTO class
- [ ] Add fields: id, titreFr, titreEn, acronyme, universite, programme, dateDebutAccreditation, dateFinAccreditation, etablissement, telephone, courrierElectronique
- [ ] Add foreign keys: directeurId, directeurAdjointId, departementId
- [ ] Add relationships: Set<String> domainesRecherche, Set<String> membreIds, Set<String> equipeIds
- [ ] Add @Data, @NoArgsConstructor, @AllArgsConstructor annotations

### Supporting DTOs
- [ ] DomaineRechercheDTO
- [ ] ElementRechercheDTO
- [ ] TheseDTO
- [ ] EquipementDTO
- [ ] CompetenceDTO
- [ ] DepartementDTO
- [ ] EtiquetteDTO
- [ ] PublicationDTO
- [ ] CollaborationDTO
- [ ] ProductionDTO

### Validation
- [ ] All DTOs use standard Hibernate/Jakarta validation annotations
- [ ] DTOs include meaningful @NotNull, @NotBlank, @NotEmpty annotations
- [ ] DTOs tested for serialization/deserialization

---

## Phase 5: Mapper Implementation (TODO)

### Core Mappers
- [ ] Create UtilisateurMapper (MapStruct)
- [ ] Create MembreMapper (MapStruct)
  - [ ] Ensure utilisateur relationship properly mapped
  - [ ] Verify no firstName/lastName in mapping
- [ ] Create EquipeMapper (MapStruct)
- [ ] Create LaboratoireMapper (MapStruct)

### Supporting Mappers
- [ ] Create DomaineRechercheMapper
- [ ] Create ElementRechercheMapper
- [ ] Create TheseMapper
- [ ] Create EquipementMapper
- [ ] Create CompetenceMapper
- [ ] Create DepartementMapper
- [ ] Create EtiquetteMapper
- [ ] Create PublicationMapper
- [ ] Create CollaborationMapper
- [ ] Create ProductionMapper

### Mapper Validation
- [ ] All mappers use @Mapper(componentModel = "spring")
- [ ] Circular references handled correctly
- [ ] Complex relationships properly mapped
- [ ] Test mapper functionality

---

## Phase 6: Service Layer Updates (TODO)

### Core Services
- [ ] Rename UserManagementService → UtilisateurManagementService
  - [ ] Update all method signatures
  - [ ] Update repository references to UtilisateurDao
  - [ ] Update field names to French
  - [ ] Update any queries
  
- [ ] Rename MemberManagementService → MembreManagementService
  - [ ] Update Member references to Membre
  - [ ] Handle utilisateur relationship (not firstName/lastName)
  - [ ] Update repository references to MembreRepository
  
- [ ] Rename LabService → LaboratoireService
  - [ ] Update Lab references to Laboratoire
  - [ ] Update field names to French
  
- [ ] Rename TeamService → EquipeService
  - [ ] Update Team references to Equipe
  - [ ] Update field names to French

### Supporting Services
- [ ] Rename/update all supporting service classes
- [ ] Update service interfaces if any exist
- [ ] Update any transaction management
- [ ] Update validation logic

### Service Testing
- [ ] All services compile without errors
- [ ] Service methods properly inject repositories
- [ ] Service methods use new entity/repository names
- [ ] Test core service functionality

---

## Phase 7: Controller Implementation (TODO)

### Core Controllers
- [ ] Update UserManagementController → UtilisateurManagementController
  - [ ] Decide on endpoint paths (/api/users vs /api/utilisateurs)
  - [ ] Update all method signatures
  - [ ] Update DTO references
  - [ ] Update service injections
  
- [ ] Update MemberManagementController → MembreManagementController
  - [ ] Update endpoint paths
  - [ ] Update DTO references
  - [ ] Update service calls
  
- [ ] Update LabController → LaboratoireController
- [ ] Update TeamController → EquipeController

### Supporting Controllers
- [ ] Update all supporting controller classes
- [ ] Update @RequestMapping paths
- [ ] Update @PostMapping/@GetMapping/@PutMapping/@DeleteMapping decorators
- [ ] Update request/response DTOs

### Error Handling
- [ ] Update @ExceptionHandler if present
- [ ] Update error response messages
- [ ] Update HTTP status codes

### Controller Testing
- [ ] All controllers compile without errors
- [ ] Test all CRUD endpoints
- [ ] Test relationship endpoints
- [ ] Test error scenarios

---

## Phase 8: Security Configuration (TODO)

### Spring Security Updates
- [ ] Update UserDetailsService implementation
  - [ ] Replace User with Utilisateur
  - [ ] Update loadUserByUsername() to use findByNomUtilisateur()
  - [ ] Test authentication
  
- [ ] Update SecurityConfig class
  - [ ] Update bean definitions
  - [ ] Update authentication provider references
  
- [ ] Update JWT token handling (if applicable)
  - [ ] Update JWT claim generation
  - [ ] Update principal parsing
  
- [ ] Update authentication controllers
  - [ ] Update login endpoint
  - [ ] Update registration endpoint
  - [ ] Update token refresh endpoint

### Security Testing
- [ ] User login works with new entity
- [ ] JWT tokens generated correctly
- [ ] Authentication endpoints return correct DTOs
- [ ] Authorization still works properly

---

## Phase 9: Seeder Refactoring (TODO)

### Data Extraction
- [ ] Extract seed data from DatabaseSeeder.java
- [ ] Create src/main/resources/seeders/lab_data.json
- [ ] Validate JSON structure
- [ ] Ensure all required fields present

### Seeder Classes
- [ ] Create UtilisateurSeeder
- [ ] Create MembreSeeder
- [ ] Create LaboratoireSeeder
- [ ] Create EquipeSeeder
- [ ] Create DomaineRechercheSeeder
- [ ] Create ElementRechercheSeeder
- [ ] Create TheseSeeder
- [ ] Create EquipementSeeder
- [ ] Create CompetenceSeeder
- [ ] Create DepartementSeeder
- [ ] Create EtiquetteSeeder
- [ ] Create PublicationSeeder
- [ ] Create CollaborationSeeder
- [ ] Create ProductionSeeder

### Seeder Orchestration
- [ ] Create SeederRunner or update DatabaseSeeder
- [ ] Implement execution order (dependencies first)
- [ ] Add transaction handling
- [ ] Add error handling and logging
- [ ] Test seeder execution

### Data Validation
- [ ] All seed data properly creates entities
- [ ] Foreign key relationships properly established
- [ ] Verify seed count matches expectations
- [ ] Test idempotency (can run seeder multiple times safely)

---

## Phase 10: Compilation & Testing (TODO)

### Compilation
- [ ] Run `mvn clean compile`
- [ ] Fix all compilation errors
- [ ] Verify no warnings
- [ ] Successful compilation

### Unit Tests
- [ ] Run `mvn test`
- [ ] Fix failing tests
- [ ] Update test fixtures to use new entities
- [ ] Verify all tests pass

### Integration Tests
- [ ] Test entity creation and persistence
- [ ] Test entity relationships
- [ ] Test service layer functionality
- [ ] Test controller endpoints

### API Testing
- [ ] Test all HTTP endpoints
- [ ] Verify request/response formats
- [ ] Test error scenarios
- [ ] Verify status codes

### Database Testing
- [ ] Verify seeder creates all entities
- [ ] Verify foreign keys are correct
- [ ] Verify data integrity
- [ ] Test data migrations

### Authentication Testing
- [ ] Test user login with Utilisateur
- [ ] Test JWT token generation
- [ ] Test token validation
- [ ] Test permission checks

### End-to-End Testing
- [ ] Complete workflow: Create user → Create member → Assign to lab
- [ ] Complete workflow: Create lab → Add team → Add members
- [ ] Test all major features
- [ ] Verify no regressions

---

## Database Migration (TODO)

### Pre-Migration
- [ ] Backup existing database
- [ ] Document existing schema
- [ ] Plan migration strategy
- [ ] Create rollback plan

### Migration Scripts
- [ ] Create table rename migration
- [ ] Create column rename migration
- [ ] Create foreign key migration
- [ ] Create join table migration
- [ ] Create data transformation scripts

### Post-Migration
- [ ] Verify all data migrated correctly
- [ ] Verify foreign keys intact
- [ ] Verify constraints satisfied
- [ ] Test application with migrated data

---

## Documentation Updates (TODO)

### API Documentation
- [ ] Update API documentation (Swagger/OpenAPI)
- [ ] Document new endpoint paths
- [ ] Document DTO structures
- [ ] Document required fields

### Code Documentation
- [ ] Add Javadoc to all new classes
- [ ] Update existing Javadoc references
- [ ] Document complex logic
- [ ] Add usage examples

### README Updates
- [ ] Update setup instructions if needed
- [ ] Document new entity names
- [ ] Update database schema documentation
- [ ] Add migration instructions

---

## Final Verification (TODO)

### Code Quality
- [ ] Run code formatter
- [ ] Run linting tools
- [ ] Check for code smells
- [ ] Verify naming conventions

### Performance
- [ ] Check for N+1 query problems
- [ ] Verify lazy/eager loading strategies
- [ ] Profile database queries
- [ ] Optimize if needed

### Security
- [ ] Verify no hardcoded passwords/secrets
- [ ] Verify input validation
- [ ] Verify SQL injection protection
- [ ] Verify authorization checks

### Standards Compliance
- [ ] Verify REST conventions
- [ ] Verify HTTP status codes
- [ ] Verify error response formats
- [ ] Verify content type handling

---

## Deployment (TODO)

### Pre-Deployment
- [ ] Create feature branch pull request
- [ ] Code review and approval
- [ ] Resolve all review comments
- [ ] Final testing in staging

### Deployment Steps
- [ ] Merge to main branch
- [ ] Run final compilation
- [ ] Run full test suite
- [ ] Deploy to staging environment
- [ ] Test in staging
- [ ] Deploy to production

### Post-Deployment
- [ ] Verify application running
- [ ] Monitor logs for errors
- [ ] Test critical workflows
- [ ] Monitor performance metrics

---

## Rollback Plan (TODO)

- [ ] Document rollback procedures
- [ ] Prepare rollback scripts
- [ ] Test rollback in staging
- [ ] Document rollback steps
- [ ] Keep backup copies ready

---

## Sign-Off Checklist

- [ ] All entities created and tested
- [ ] All repositories created and tested
- [ ] All DTOs created and tested
- [ ] All mappers created and tested
- [ ] All services updated and tested
- [ ] All controllers updated and tested
- [ ] Security configuration updated and tested
- [ ] Seeder refactored and tested
- [ ] Database migrations created and tested
- [ ] Compilation successful (mvn clean compile)
- [ ] All tests passing (mvn test)
- [ ] Code review completed
- [ ] Documentation updated
- [ ] Performance verified
- [ ] Security verified
- [ ] Staging environment tested
- [ ] Production deployment ready

---

**Notes for Implementation Team:**
- Follow implementation order: DTOs → Mappers → Services → Controllers → Security → Seeder → Testing
- Refer to CODE_EXAMPLES.md for implementation patterns
- Refer to FIELD_MAPPING_REFERENCE.md for field names
- Test frequently to catch issues early
- Commit regularly with meaningful commit messages
- Keep communication open about blockers or questions

**Estimated Total Effort:** 8-10 days of development work
