# French Field Mapping Reference

## Utilisateur (formerly User)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| username | nom d'utilisateur | nomUtilisateur | nom_utilisateur |
| email | courrier électronique | courrierElectronique | courrier_electronique |
| cin | CIN | cin | cin |
| firstName | prénom | prenom | prenom |
| lastName | nom | nom | nom |
| phoneNumber | numéro de téléphone | numeroTelephone | numero_telephone |
| password | mot de passe | motDePasse | mot_de_passe |
| enabled | actif | actif | actif |
| status | statut | statut | statut |
| lastLogin | dernière connexion | derniereConnexion | derniere_connexion |
| roles | rôles | roles | (join table) |
| teams | équipes | equipes | (join table) |

## Membre (formerly Member)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| ~~firstName~~ | ~~prénom~~ | REMOVED (use utilisateur.prenom) | - |
| ~~lastName~~ | ~~nom~~ | REMOVED (use utilisateur.nom) | - |
| grade | grade | grade | grade |
| speciality | spécialité | specialite | specialite |
| establishment | établissement | etablissement | etablissement |
| associated | associé | associe | associe |
| roleInLab | rôle dans le laboratoire | roleDansLaboratoire | role_dans_laboratoire |
| phdStudents | doctorants encadrés | doctorantsEncadres | doctorant_encadre |
| lab | laboratoire | laboratoire | laboratoire_id |
| user | utilisateur | utilisateur | utilisateur_id |

## Equipe (formerly Team)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| name | nom | nom | nom |
| lab | laboratoire | laboratoire | laboratoire_id |
| researchField | domaine de recherche | domaineRecherche | domaine_recherche_id |
| members | membres | membres | (join table: equipe_membres) |
| leader | chef d'équipe | chefEquipe | chef_equipe_id |

## Laboratoire (formerly Lab)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| titleFr | titre en français | titreFr | titre_fr |
| titleEn | titre en anglais | titreEn | titre_en |
| acronym | acronyme | acronyme | acronyme |
| university | université | universite | universite |
| program | programme | programme | programme |
| accreditationStart | date de début d'accréditation | dateDebutAccreditation | date_debut_accreditation |
| accreditationEnd | date de fin d'accréditation | dateFinAccreditation | date_fin_accreditation |
| establishment | établissement | etablissement | etablissement |
| phone | téléphone | telephone | telephone |
| email | courrier électronique | courrierElectronique | courrier_electronique |
| director | directeur | directeur | directeur_id |
| deputyDirector | directeur adjoint | directeurAdjoint | directeur_adjoint_id |
| members | membres | membres | (one-to-many) |
| researchFields | domaines de recherche | domainesRecherche | (join table) |
| researchItems | éléments de recherche | elementsRecherche | (one-to-many) |
| equipments | équipements | equipements | (one-to-many) |
| competences | compétences | competences | (one-to-many) |
| production | production | production | (one-to-one) |
| comiteGestion | comité de gestion | comiteGestion | (element collection) |
| department | département | departement | departement_id |
| teams | équipes | equipes | (one-to-many) |
| tags | étiquettes | etiquettes | (join table) |

## DomaineRecherche (formerly ResearchField)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| name | nom | nom | nom |
| labs | laboratoires | laboratoires | (join table) |
| teams | équipes | equipes | (one-to-many) |

## ElementRecherche (formerly ResearchItem)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| title | titre | titre | titre |
| lab | laboratoire | laboratoire | laboratoire_id |

## These (formerly Thesis)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| author | auteur | auteur | auteur |
| title | titre | titre | titre |
| defenseDate | date de soutenance | dateSoutenance | date_soutenance |
| supervisor | directeur de thèse | directeurThese | directeur_these |
| lab | laboratoire | laboratoire | laboratoire_id |

## Equipement (formerly Equipment)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| name | nom | nom | nom |
| category | catégorie | categorie | categorie |
| lab | laboratoire | laboratoire | laboratoire_id |

## Departement (formerly Department)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| name | nom | nom | nom |
| labs | laboratoires | laboratoires | (one-to-many) |

## Etiquette (formerly Tag)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| name | nom | nom | nom |
| labs | laboratoires | laboratoires | (join table) |

## Publication_Renamed (formerly Publication)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| title | titre | titre | titre |
| type | type | type | type |
| publicationYear | année de publication | anneePublication | annee_publication |
| authors | auteurs | auteurs | (element collection) |
| journal | journal | journal | journal |
| conference | conférence | conference | conference |
| doi | DOI | doi | doi |
| pages | pages | pages | pages |
| lab | laboratoire | laboratoire | laboratoire_id |
| team | équipe | equipe | equipe_id |

## Collaboration_Renamed (formerly Collaboration)
| English Field | French Field | Java Field Name | Column Name |
|---|---|---|---|
| organization | organisation | organisation | organisation |
| establishment | établissement | etablissement | etablissement |
| theme | thème | theme | theme |
| nature | nature | nature | nature |
| scope | portée | portee | portee |

## DTO Mapping Examples

### UtilisateurDTO
```java
public class UtilisateurDTO {
    private String id;
    private String nomUtilisateur;        // was username
    private String prenom;                // was firstName
    private String nom;                   // was lastName
    private String courrierElectronique;  // was email
    private String cin;
    private String numeroTelephone;       // was phoneNumber
    private boolean actif;                // was enabled
    private UserStatus statut;            // was status
    private Instant derniereConnexion;    // was lastLogin
    private Set<RoleDTO> roles;
}
```

### MembreDTO
```java
public class MembreDTO {
    private String id;
    private String grade;
    private String specialite;
    private String etablissement;
    private boolean associe;
    private MemberRoleInLab roleDansLaboratoire;
    private List<String> doctorantsEncadres;
    private String laboratoireId;
    private UtilisateurDTO utilisateur;  // Reference to user, not duplicate names
}
```

## Key Points for Implementation

1. **Membre no longer has firstName/lastName** - Always access via `membre.getUtilisateur().getPrenom()` and `membre.getUtilisateur().getNom()`

2. **Table Name Changes** - Remember that class names changed, which typically means table names also changed:
   - `users` → `utilisateurs`
   - `members` → `membres`
   - `teams` → `equipes`
   - `labs` → `laboratoires`
   - `research_fields` → `domaines_recherche`
   - etc.

3. **Foreign Key Updates** - Any joins using the old column names need updating:
   - `user_id` → `utilisateur_id`
   - `member_id` → `membre_id`
   - `team_id` → `equipe_id`
   - `lab_id` → `laboratoire_id`

4. **Service Method Updates** - When updating services, remember:
   - `UserService.findByUsername()` → `UtilisateurService.findByNomUtilisateur()`
   - `UserService.findByEmail()` → `UtilisateurService.findByCourrierElectronique()`
   - Query methods must use new field names

5. **Controller Endpoints** - Consider whether to update endpoints:
   - Option 1: Keep `/api/users` but process Utilisateur internally
   - Option 2: Change to `/api/utilisateurs` for consistency
   - Option 3: Support both with deprecation notice on old endpoints
