# Code Examples for Refactored Entities

## Service Layer Examples

### UtilisateurManagementService
```java
@Service
public class UtilisateurManagementService {
    
    @Autowired
    private UtilisateurDao utilisateurDao;
    
    public Utilisateur createUtilisateur(UtilisateurDTO dto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur(dto.getNomUtilisateur());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setNom(dto.getNom());
        utilisateur.setCourrierElectronique(dto.getCourrierElectronique());
        utilisateur.setCin(dto.getCin());
        utilisateur.setNumeroTelephone(dto.getNumeroTelephone());
        utilisateur.setMotDePasse(encodePassword(dto.getMotDePasse()));
        utilisateur.setActif(dto.isActif());
        utilisateur.setStatut(UserStatus.EN_ATTENTE);
        return utilisateurDao.save(utilisateur);
    }
    
    public Utilisateur findByNomUtilisateur(String nomUtilisateur) {
        return utilisateurDao.findByNomUtilisateur(nomUtilisateur)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur not found"));
    }
    
    public Utilisateur findByCourrierElectronique(String email) {
        return utilisateurDao.findByCourrierElectronique(email)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur not found"));
    }
}
```

### MembreManagementService
```java
@Service
public class MembreManagementService {
    
    @Autowired
    private MembreRepository membreRepository;
    
    @Autowired
    private LaboratoireRepository laboratoireRepository;
    
    @Autowired
    private UtilisateurDao utilisateurDao;
    
    public Membre createMembre(MembreDTO dto, String laboratoireId, String utilisateurId) {
        Laboratoire laboratoire = laboratoireRepository.findById(laboratoireId)
            .orElseThrow(() -> new EntityNotFoundException("Laboratoire not found"));
        
        Utilisateur utilisateur = utilisateurDao.findById(utilisateurId)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur not found"));
        
        Membre membre = new Membre();
        membre.setUtilisateur(utilisateur);
        membre.setGrade(dto.getGrade());
        membre.setSpecialite(dto.getSpecialite());
        membre.setEtablissement(dto.getEtablissement());
        membre.setAsssocie(dto.isAsssocie());
        membre.setRoleDansLaboratoire(dto.getRoleDansLaboratoire());
        membre.setDoctorantsEncadres(dto.getDoctorantsEncadres());
        membre.setLaboratoire(laboratoire);
        
        return membreRepository.save(membre);
    }
    
    public Membre getMembre(String id) {
        return membreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Membre not found"));
    }
    
    // Important: Access member name through utilisateur reference
    public String getMembreFullName(Membre membre) {
        return membre.getUtilisateur().getPrenom() + " " + 
               membre.getUtilisateur().getNom();
    }
    
    public String getMemberemail(Membre membre) {
        return membre.getUtilisateur().getCourrierElectronique();
    }
}
```

### LaboratoireService
```java
@Service
public class LaboratoireService {
    
    @Autowired
    private LaboratoireRepository laboratoireRepository;
    
    @Autowired
    private MembreRepository membreRepository;
    
    public Laboratoire createLaboratoire(LaboratoireDTO dto) {
        Laboratoire laboratoire = new Laboratoire();
        laboratoire.setTitreFr(dto.getTitreFr());
        laboratoire.setTitreEn(dto.getTitreEn());
        laboratoire.setAcronyme(dto.getAcronyme());
        laboratoire.setUniversite(dto.getUniversite());
        laboratoire.setProgramme(dto.getProgramme());
        laboratoire.setDateDebutAccreditation(dto.getDateDebutAccreditation());
        laboratoire.setDateFinAccreditation(dto.getDateFinAccreditation());
        laboratoire.setEtablissement(dto.getEtablissement());
        laboratoire.setTelephone(dto.getTelephone());
        laboratoire.setCourrierElectronique(dto.getCourrierElectronique());
        
        return laboratoireRepository.save(laboratoire);
    }
    
    public Laboratoire findByAcronyme(String acronyme) {
        return laboratoireRepository.findByAcronyme(acronyme)
            .orElseThrow(() -> new EntityNotFoundException("Laboratoire not found"));
    }
    
    public List<Membre> getLaboratoireMembers(String laboratoireId) {
        Laboratoire laboratoire = laboratoireRepository.findById(laboratoireId)
            .orElseThrow(() -> new EntityNotFoundException("Laboratoire not found"));
        return new ArrayList<>(laboratoire.getMembres());
    }
}
```

### EquipeService
```java
@Service
public class EquipeService {
    
    @Autowired
    private EquipeRepository equipeRepository;
    
    @Autowired
    private LaboratoireRepository laboratoireRepository;
    
    @Autowired
    private DomaineRechercheRepository domaineRechercheRepository;
    
    public Equipe createEquipe(EquipeDTO dto) {
        Laboratoire laboratoire = laboratoireRepository.findById(dto.getLaboratoireId())
            .orElseThrow(() -> new EntityNotFoundException("Laboratoire not found"));
        
        DomaineRecherche domaine = domaineRechercheRepository.findById(dto.getDomaineRechercheId())
            .orElseThrow(() -> new EntityNotFoundException("Domaine de recherche not found"));
        
        Equipe equipe = new Equipe();
        equipe.setNom(dto.getNom());
        equipe.setLaboratoire(laboratoire);
        equipe.setDomaineRecherche(domaine);
        
        return equipeRepository.save(equipe);
    }
    
    public Equipe findByNom(String nom) {
        return equipeRepository.findByNom(nom)
            .orElseThrow(() -> new EntityNotFoundException("Equipe not found"));
    }
}
```

## Controller Examples

### UtilisateurManagementController
```java
@RestController
@RequestMapping("/api/utilisateurs")  // Changed from /api/users
@CrossOrigin(origins = "*")
public class UtilisateurManagementController {
    
    @Autowired
    private UtilisateurManagementService utilisateurService;
    
    @Autowired
    private UtilisateurMapper utilisateurMapper;
    
    @PostMapping
    public ResponseEntity<UtilisateurDTO> createUtilisateur(@RequestBody UtilisateurDTO dto) {
        Utilisateur utilisateur = utilisateurService.createUtilisateur(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(utilisateurMapper.toDTO(utilisateur));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateur(@PathVariable String id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateur(id);
        return ResponseEntity.ok(utilisateurMapper.toDTO(utilisateur));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(
            @PathVariable String id,
            @RequestBody UtilisateurDTO dto) {
        Utilisateur utilisateur = utilisateurService.updateUtilisateur(id, dto);
        return ResponseEntity.ok(utilisateurMapper.toDTO(utilisateur));
    }
}
```

### MembreManagementController
```java
@RestController
@RequestMapping("/api/membres")  // Changed from /api/members
@CrossOrigin(origins = "*")
public class MembreManagementController {
    
    @Autowired
    private MembreManagementService membreService;
    
    @Autowired
    private MembreMapper membreMapper;
    
    @PostMapping
    public ResponseEntity<MembreDTO> createMembre(@RequestBody MembreDTO dto) {
        Membre membre = membreService.createMembre(dto, dto.getLaboratoireId(), dto.getUtilisateurId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(membreMapper.toDTO(membre));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MembreDTO> getMembre(@PathVariable String id) {
        Membre membre = membreService.getMembre(id);
        return ResponseEntity.ok(membreMapper.toDTO(membre));
    }
}
```

## Mapper Examples

### UtilisateurMapper
```java
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {
    
    UtilisateurDTO toDTO(Utilisateur utilisateur);
    
    Utilisateur toEntity(UtilisateurDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(UtilisateurDTO dto, @MappingTarget Utilisateur utilisateur);
}
```

### MembreMapper
```java
@Mapper(componentModel = "spring", uses = UtilisateurMapper.class)
public interface MembreMapper {
    
    MembreDTO toDTO(Membre membre);
    
    Membre toEntity(MembreDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "utilisateur", ignore = true)  // Don't map utilisateur directly
    void updateEntityFromDTO(MembreDTO dto, @MappingTarget Membre membre);
}
```

### LaboratoireMapper
```java
@Mapper(componentModel = "spring", uses = {MembreMapper.class})
public interface LaboratoireMapper {
    
    LaboratoireDTO toDTO(Laboratoire laboratoire);
    
    Laboratoire toEntity(LaboratoireDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(LaboratoireDTO dto, @MappingTarget Laboratoire laboratoire);
}
```

## DTO Examples

### UtilisateurDTO
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {
    private String id;
    private String nomUtilisateur;
    private String prenom;
    private String nom;
    private String courrierElectronique;
    private String cin;
    private String numeroTelephone;
    private String motDePasse;  // Only in creation, not in response
    private boolean actif;
    private UserStatus statut;
    private Instant derniereConnexion;
    private Set<RoleDTO> roles;
}
```

### MembreDTO
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembreDTO {
    private String id;
    private String grade;
    private String specialite;
    private String etablissement;
    private boolean associe;
    private MemberRoleInLab roleDansLaboratoire;
    private List<String> doctorantsEncadres;
    
    // Foreign keys
    private String laboratoireId;
    private String utilisateurId;
    
    // Related DTOs
    private UtilisateurDTO utilisateur;
}
```

### LaboratoireDTO
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoireDTO {
    private String id;
    private String titreFr;
    private String titreEn;
    private String acronyme;
    private String universite;
    private String programme;
    private LocalDate dateDebutAccreditation;
    private LocalDate dateFinAccreditation;
    private String etablissement;
    private String telephone;
    private String courrierElectronique;
    
    // Foreign keys
    private String directeurId;
    private String directeurAdjointId;
    private String departementId;
    
    // Relationships
    private Set<String> domainesRecherche;
    private Set<String> membres;
    private Set<String> equipes;
}
```

## Security Configuration Example

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UtilisateurDao utilisateurDao;
    
    @Bean
    public UserDetailsService userDetailsService() {
        return nomUtilisateur -> {
            Utilisateur utilisateur = utilisateurDao.findByNomUtilisateur(nomUtilisateur)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur not found"));
            
            return User.builder()
                .username(utilisateur.getNomUtilisateur())
                .password(utilisateur.getMotDePasse())
                .authorities(utilisateur.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNom()))
                    .collect(Collectors.toList()))
                .accountLocked(!utilisateur.isActif())
                .build();
        };
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) 
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
```

## Query Method Patterns

### Finding by Natural Keys
```java
// Old pattern
User user = userDao.findByUsername("john.doe");

// New pattern
Utilisateur utilisateur = utilisateurDao.findByNomUtilisateur("john.doe");
```

### Handling Relationships
```java
// Old: Direct access to member name
String name = member.getFirstName() + " " + member.getLastName();

// New: Must go through utilisateur
String name = membre.getUtilisateur().getPrenom() + " " + 
              membre.getUtilisateur().getNom();
```

### Complex Queries
```java
// Finding members in a lab (old)
List<Member> members = memberRepository.findByLabId(labId);

// Finding members in a lab (new)
List<Membre> membres = membreRepository.findByLaboratoireId(laboratoireId);
```
