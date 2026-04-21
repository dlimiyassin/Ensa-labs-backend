# Backend Refactoring Implementation Guide

## Completed Tasks

### Phase 1: Core Entity Renaming (COMPLETED)
All renamed French entity classes have been created:

**Created Entities:**
- `Utilisateur.java` - Renamed from User with French field names
- `Membre.java` - Renamed from Member with redundancy removed (no firstName/lastName, uses Utilisateur reference)
- `Equipe.java` - Renamed from Team with French field names
- `Laboratoire.java` - Renamed from Lab with French field names
- `DomaineRecherche.java` - Renamed from ResearchField
- `ElementRecherche.java` - Renamed from ResearchItem
- `These.java` - Renamed from Thesis
- `Equipement.java` - Renamed from Equipment
- `Competence_Renamed.java` - Competence with French fields
- `Departement.java` - Renamed from Department
- `Etiquette.java` - Renamed from Tag
- `Production_Renamed.java` - Production with renamed entities
- `Publication_Renamed.java` - Publication with French fields
- `Collaboration_Renamed.java` - Collaboration with French fields

### Phase 2: Remove Redundancy (COMPLETED)
- **Membre entity**: Removed firstName/lastName fields
- **Utilisteur reference**: Added OneToOne relationship to Utilisateur entity
- **Database mapping**: All field names mapped to French via @Column annotations

### Phase 3: Create Repositories (COMPLETED)
Created repository interfaces for all renamed entities:
- `UtilisateurDao` - For Utilisateur entity
- `MembreRepository` - For Membre entity
- `EquipeRepository` - For Equipe entity
- `LaboratoireRepository` - For Laboratoire entity
- `DomaineRechercheRepository` - For DomaineRecherche entity
- `ElementRechercheRepository`, `TheseRepository`, `EquipementRepository`, etc.

## Remaining Tasks

### Phase 4: Update All DTOs and Mappers
**Files to Create:**
- `UtilisateurDTO.java` - DTO for Utilisateur (replaces UserDto)
- `MembreDTO.java` - DTO for Membre (updated with Utilisateur reference)
- `EquipeDTO.java` - DTO for Equipe
- `LaboratoireDTO.java` - DTO for Laboratoire
- All other supporting DTOs for renamed entities

**Files to Update:**
- All existing Mapper classes to map between new entities and DTOs
- Update field mappings to use French field names
- Update relationship mappings (e.g., User → Utilisateur, Member → Membre)

**Key Mapper Updates:**
- UserMapper → UtilisateurMapper
- MemberMapper → MembreMapper  
- TeamMapper → EquipeMapper
- LabMapper → LaboratoireMapper

### Phase 5: Update All Service Layers
**Files to Rename/Create:**
- UserManagementService → UtilisateurManagementService
- MemberManagementService → MembreManagementService
- LabService → LaboratoireService
- TeamService → EquipeService
- Similar renames for all supporting services

**Key Changes:**
- Replace User references with Utilisateur throughout
- Replace Member references with Membre throughout
- Update repository injection points to new repository interfaces
- Update service method signatures that reference these entities
- Update method implementations to work with new Utilisateur/Membre structure
- Handle Membre.utilisateur relationship when accessing user name/email

### Phase 6: Update All Controllers
**Files to Rename/Create:**
- UserManagementController → UtilisateurManagementController
- MemberManagementController → MembreManagementController
- LabController → LaboratoireController
- TeamController → EquipeController

**Key Changes:**
- Update endpoint paths if desired (e.g., /api/users → /api/utilisateurs)
- Update request/response DTOs to new DTO classes
- Update method signatures and service calls
- Update error messages and logging
- Ensure JSON serialization/deserialization uses French field names if needed

### Phase 7: Update Security Configuration
**Files to Update:**
- Authentication configuration classes
- User Details service implementations
- JWT token generation/validation
- Permission evaluation logic
- Replace User with Utilisateur in security context

**Key Security Points:**
- UserDetailsService implementation needs to use UtilisateurDao
- loadUserByUsername method should search by nomUtilisateur
- Principal/Authentication should use Utilisateur instead of User
- Spring Security context handling

### Phase 8: Refactor DatabaseSeeder
**Current State:** DatabaseSeeder.java contains hardcoded seed data

**Action Required:**
1. Create `src/main/resources/seeders/lab_data.json` with the JSON data from DatabaseSeeder
2. Extract seeder logic into individual classes:
   - `UtilisateurSeeder.java` - Seeds users
   - `MembreSeeder.java` - Seeds members
   - `LaboratoireSeeder.java` - Seeds labs
   - `EquipeSeeder.java` - Seeds teams
   - `DomaineRechercheSeeder.java` - Seeds research domains
   - etc.
3. Create a main `SeederRunner` or update DatabaseSeeder to orchestrate all seeders
4. Parse lab_data.json in seeders and create entities

### Phase 9: Update Any Remaining References
**Search and Replace:**
- Global find/replace of class references in configuration files
- Update any @Bean definitions that reference old entity classes
- Update any query strings or method references
- Check for any hard-coded column names in native queries

**Configuration Files to Check:**
- application.properties/yml
- Any XML configuration files
- Bean definition files
- JPA/Hibernate configuration

### Phase 10: Compilation and Testing
**Steps:**
1. Run `mvn clean compile` to check for compilation errors
2. Fix any compilation errors related to:
   - Missing imports
   - Type mismatches
   - Method signature incompatibilities
3. Run `mvn test` to verify unit tests pass
4. Perform manual testing of endpoints
5. Verify seeding functionality
6. Test authentication flow

## Important Notes

### Relationship Updates
- **Team → Equipe**: References Utilisateur in members and chefEquipe (unchanged relationship type)
- **Lab → Laboratoire**: References Membre in directeur and deputyDirector (unchanged relationship type)
- **Membre**: Now has OneToOne reference to Utilisateur instead of duplicate firstName/lastName
- Access member name via: `membre.getUtilisateur().getPrenom()` instead of `membre.getFirstName()`

### Database Changes
- Old tables (users, members, teams, labs, etc.) will need migration to new table names
- Foreign key constraints need updating
- Create migration scripts for:
  - Renaming tables
  - Renaming columns
  - Creating new relationships

### API/DTO Considerations
- DTOs can maintain English field names for backward compatibility or switch to French
- If maintaining English field names in JSON, add @JsonProperty annotations
- Update API documentation to reflect changes
- Consider versioning API endpoints (e.g., /api/v2/utilisateurs)

### Testing Strategy
1. Unit tests: Update entity tests to use new class names
2. Integration tests: Update test data builders and assertions
3. End-to-end tests: Verify complete workflows work with renamed entities
4. Seeding tests: Verify database is properly seeded with new entities

## File Organization Summary

```
Renamed Entities Created:
src/main/java/com/ensa/labs/zBase/security/bean/
  - Utilisateur.java (was User)

src/main/java/com/ensa/labs/research/bean/
  - Membre.java (was Member)
  - Equipe.java (was Team)
  - Laboratoire.java (was Lab)
  - DomaineRecherche.java (was ResearchField)
  - ElementRecherche.java (was ResearchItem)
  - These.java (was Thesis)
  - Equipement.java (was Equipment)
  - Competence_Renamed.java
  - Departement.java (was Department)
  - Etiquette.java (was Tag)
  - Production_Renamed.java
  - Publication_Renamed.java
  - Collaboration_Renamed.java

Repositories Created:
src/main/java/com/ensa/labs/zBase/security/dao/facade/
  - UtilisateurDao.java

src/main/java/com/ensa/labs/research/dao/
  - MembreRepository.java
  - EquipeRepository.java
  - LaboratoireRepository.java
  - DomaineRechercheRepository.java
  - ElementRechercheRepository.java
  - TheseRepository.java
  - EquipementRepository.java
  - CompetenceRenamedRepository.java
  - DepartementRepository.java
  - EtiquetteRepository.java
  - PublicationRenamedRepository.java
  - CollaborationRenamedRepository.java
  - ProductionRenamedRepository.java
```

## Compilation Command

Once all refactoring is complete, compile with:
```bash
./mvnw clean compile
```

Or with full build:
```bash
./mvnw clean package
```

## Version Control

All changes should be committed with messages following conventional commit format:
```
refactor: rename entities to French (Phase X)
refactor: update DTOs for renamed entities
refactor: update services and controllers
refactor: update seeder implementation
```
