package com.ensa.labs.zBase.db;

import com.ensa.labs.research.bean.*;
import com.ensa.labs.research.bean.enums.*;
import com.ensa.labs.research.dao.*;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleDao roleDao;
    private final UserDao userDao;
    private final LabRepository labRepository;
    private final MemberRepository memberRepository;
    private final ResearchFieldRepository researchFieldRepository;
    private final ResearchItemRepository researchItemRepository;
    private final CompetenceRepository competenceRepository;
    private final EquipmentRepository equipmentRepository;
    private final CollaborationRepository collaborationRepository;
    private final PublicationRepository publicationRepository;
    private final ThesisRepository thesisRepository;
    private final ProductionRepository productionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public DatabaseSeeder(RoleDao roleDao, UserDao userDao, LabRepository labRepository, MemberRepository memberRepository, ResearchFieldRepository researchFieldRepository, ResearchItemRepository researchItemRepository, CompetenceRepository competenceRepository, EquipmentRepository equipmentRepository, CollaborationRepository collaborationRepository, PublicationRepository publicationRepository, ThesisRepository thesisRepository, ProductionRepository productionRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.labRepository = labRepository;
        this.memberRepository = memberRepository;
        this.researchFieldRepository = researchFieldRepository;
        this.researchItemRepository = researchItemRepository;
        this.competenceRepository = competenceRepository;
        this.equipmentRepository = equipmentRepository;
        this.collaborationRepository = collaborationRepository;
        this.publicationRepository = publicationRepository;
        this.thesisRepository = thesisRepository;
        this.productionRepository = productionRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        Role admin = createRoleIfMissing("ADMIN");
        Role researcher = createRoleIfMissing("RESEARCHER");
        seedUser("admin", "AA111", "admin@ensa.ma", "System", "Admin", Set.of(admin));

        JsonNode root = objectMapper.readTree(DATA_JSON);
        Lab lab = createLab(root.path("laboratoire"));

        Map<String, Member> members = seedMembers(root.path("membres_principaux"), lab, false, researcher);
        members.putAll(seedMembers(root.path("membres_associes"), lab, true, researcher));
        assignDirection(root.path("direction"), members, lab);
        seedComiteGestion(root.path("comite_de_gestion"), lab);

        seedThematiques(root.path("thematiques_recherche"), lab);
        seedAxes(root.path("axes_recherche"), lab);
        seedCompetences(root.path("competences"), lab);
        seedEquipements(root.path("equipements"), lab);
        seedCollaborations(root.path("collaborations"), text(root.path("laboratoire"), "etablissement_domiciliation"));
        seedProduction(root.path("production_scientifique_2021_2024"), lab);

        labRepository.save(lab);
    }

    private Lab createLab(JsonNode node) {
        Lab lab = labRepository.findByAcronym(text(node, "acronyme")).orElseGet(Lab::new);
        lab.setTitleFr(text(node, "intitule_fr"));
        lab.setTitleEn(text(node, "intitule_en"));
        lab.setAcronym(text(node, "acronyme"));
        lab.setUniversity(text(node, "universite"));
        lab.setProgram(text(node, "programme"));
        lab.setEstablishment(text(node, "etablissement_domiciliation"));
        lab.setPhone(text(node, "telephone"));
        lab.setEmail(text(node, "email"));
        lab.setAccreditationStart(LocalDate.of(2026, 1, 1));
        lab.setAccreditationEnd(LocalDate.of(2029, 12, 31));
        return labRepository.save(lab);
    }

    private Map<String, Member> seedMembers(JsonNode array, Lab lab, boolean associated, Role researcherRole) {
        Map<String, Member> map = new HashMap<>();
        if (!array.isArray()) return map;

        for (JsonNode node : array) {
            String fullName = text(node, "nom");
            NameParts nameParts = splitName(fullName);
            Member member = memberRepository.findByLabIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(lab.getId(), nameParts.firstName(), nameParts.lastName()).orElseGet(Member::new);

            member.setLab(lab);
            member.setFirstName(nameParts.firstName());
            member.setLastName(nameParts.lastName());
            member.setGrade(toGrade(text(node, "grade")));
            member.setSpeciality(text(node, "specialite"));
            member.setEstablishment(text(node, "etablissement"));
            member.setAssociated(associated);
            member.setRoleInLab(MemberRoleInLab.MEMBER);
            member.setPhdStudents(toList(node.path("etudiants_doctorat")));

            User linkedUser = seedUser(username(nameParts), defaultCin(nameParts), null, nameParts.firstName(), nameParts.lastName(), Set.of(researcherRole));
            member.setUser(linkedUser);

            Member saved = memberRepository.save(member);
            map.put(normalizeName(fullName), saved);
        }

        return map;
    }

    private void assignDirection(JsonNode node, Map<String, Member> members, Lab lab) {
        Member director = getOrCreateDirectionMember(node.path("directeur"), members, lab, MemberRoleInLab.DIRECTOR);
        Member deputy = getOrCreateDirectionMember(node.path("directeur_adjoint"), members, lab, MemberRoleInLab.DEPUTY_DIRECTOR);
        lab.setDirector(director);
        lab.setDeputyDirector(deputy);
    }

    private Member getOrCreateDirectionMember(JsonNode node, Map<String, Member> members, Lab lab, MemberRoleInLab role) {
        String key = normalizeName(text(node, "nom"));
        Member member = members.get(key);
        if (member == null) {
            NameParts nameParts = splitName(text(node, "nom"));
            member = new Member();
            member.setLab(lab);
            member.setFirstName(nameParts.firstName());
            member.setLastName(nameParts.lastName());
            member.setGrade(toGrade(text(node, "grade")));
        }
        member.setRoleInLab(role);
        return memberRepository.save(member);
    }

    private void seedComiteGestion(JsonNode array, Lab lab) {
        if (!array.isArray()) return;
        List<ComiteGestionMembre> comite = new ArrayList<>();
        for (JsonNode node : array) {
            ComiteGestionMembre membre = new ComiteGestionMembre();
            membre.setNomEnseignant(text(node, "nom"));
            membre.setRoleComite(text(node, "role"));
            comite.add(membre);
        }
        lab.setComiteGestion(comite);
    }

    private void seedThematiques(JsonNode array, Lab lab) {
        if (!array.isArray()) return;
        for (JsonNode n : array) {
            String value = n.asText().trim();
            if (value.isEmpty()) continue;
            ResearchField field = researchFieldRepository.findByName(value).orElseGet(ResearchField::new);
            field.setName(value);
            researchFieldRepository.save(field);
            lab.getResearchFields().add(field);
        }
    }

    private void seedAxes(JsonNode array, Lab lab) {
        if (!array.isArray()) return;
        for (JsonNode n : array) {
            String value = n.asText().trim();
            if (value.isEmpty()) continue;
            ResearchItem item = new ResearchItem();
            item.setLab(lab);
            item.setTitle(value);
            researchItemRepository.save(item);
        }
    }

    private void seedCompetences(JsonNode node, Lab lab) {
        seedCompetenceByType(node.path("scientifiques_et_methodologiques"), lab, CompetenceType.SCIENTIFIC);
        seedCompetenceByType(node.path("savoir_faire_technologique"), lab, CompetenceType.TECHNOLOGICAL);
        seedCompetenceByType(node.path("sectorielles"), lab, CompetenceType.SECTORIAL);
        seedCompetenceByType(node.path("innovation_et_transfert"), lab, CompetenceType.INNOVATION);
    }

    private void seedCompetenceByType(JsonNode array, Lab lab, CompetenceType type) {
        if (!array.isArray()) return;
        for (JsonNode n : array) {
            Competence c = new Competence();
            c.setLab(lab);
            c.setType(type);
            c.setDescription(n.asText());
            competenceRepository.save(c);
        }
    }

    private void seedEquipements(JsonNode node, Lab lab) {
        seedEquipementByCategorie(node.path("scientifiques_laboratoire"), lab, EquipmentCategory.LAB);
        seedEquipementByCategorie(node.path("dotations_universite"), lab, EquipmentCategory.UNIVERSITY);
        seedEquipementByCategorie(node.path("etablissement_et_universite"), lab, EquipmentCategory.SHARED);
        seedEquipementByCategorie(node.path("informatiques_laboratoire"), lab, EquipmentCategory.IT);
    }

    private void seedEquipementByCategorie(JsonNode array, Lab lab, EquipmentCategory category) {
        if (!array.isArray()) return;
        for (JsonNode n : array) {
            Equipment e = new Equipment();
            e.setLab(lab);
            e.setCategory(category);
            e.setName(n.asText());
            equipmentRepository.save(e);
        }
    }

    private void seedCollaborations(JsonNode node, String etablissement) {
        seedCollaborationsByScope(node.path("regionales"), etablissement, CollaborationScope.REGIONAL);
        seedCollaborationsByScope(node.path("nationales"), etablissement, CollaborationScope.NATIONAL);
        seedCollaborationsByScope(node.path("etrangeres"), etablissement, CollaborationScope.INTERNATIONAL);
    }

    private void seedCollaborationsByScope(JsonNode array, String etablissement, CollaborationScope scope) {
        if (!array.isArray()) return;
        for (JsonNode n : array) {
            Collaboration c = new Collaboration();
            c.setOrganization(text(n, "organisme"));
            c.setEstablishment(etablissement);
            c.setTheme(text(n, "thematique"));
            c.setNature(text(n, "nature"));
            c.setScope(scope);
            collaborationRepository.save(c);
        }
    }

    private void seedProduction(JsonNode node, Lab lab) {
        Production production = new Production();
        production.setLab(lab);

        Set<Publication> publications = new HashSet<>();
        for (JsonNode n : node.path("publications")) {
            publications.add(savePublication(n, PublicationType.JOURNAL, lab));
        }

        Set<Publication> communications = new HashSet<>();
        for (JsonNode n : node.path("communications")) {
            communications.add(savePublication(n, PublicationType.COMMUNICATION, lab));
        }

        Set<Thesis> theses = new HashSet<>();
        for (JsonNode n : node.path("theses_soutenues")) {
            Thesis t = new Thesis();
            t.setLab(lab);
            t.setAuthor(text(n, "auteur"));
            t.setTitle(text(n, "titre"));
            t.setSupervisor(text(n, "directeur"));
            theses.add(thesisRepository.save(t));
        }

        production.setPublications(publications);
        production.setCommunications(communications);
        production.setTheses(theses);
        productionRepository.save(production);
    }

    private Publication savePublication(JsonNode n, PublicationType type, Lab lab) {
        Publication p = new Publication();
        p.setLab(lab);
        p.setType(type);
        p.setTitle(text(n, "titre"));
        p.setPublicationYear(n.path("annee").isInt() ? n.path("annee").asInt() : null);
        p.setAuthors(toList(n.path("auteurs")));
        p.setJournal(text(n, "revue"));
        p.setConference(text(n, "conference"));
        p.setDoi(text(n, "doi"));
        p.setPages(text(n, "pages"));
        return publicationRepository.save(p);
    }

    private Role createRoleIfMissing(String roleName) {
        return roleDao.findByName(roleName).orElseGet(() -> roleDao.save(new Role(roleName)));
    }

    private User seedUser(String username, String cin, String email, String firstName, String lastName, Set<Role> roles) {
        return userDao.findByUsername(username).orElseGet(() -> {
            User user = new User();
            user.setUsername(username);
            user.setCin(cin);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setStatus(UserStatus.ACTIF);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(cin));
            return userDao.save(user);
        });
    }

    private MemberGrade toGrade(String grade) {
        if (grade == null) return MemberGrade.OTHER;
        try {
            return MemberGrade.valueOf(grade.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return MemberGrade.OTHER;
        }
    }

    private String username(NameParts p) {
        return (p.firstName() + "." + p.lastName()).toLowerCase(Locale.ROOT).replace(" ", "");
    }

    private String defaultCin(NameParts p) {
        String base = (p.firstName() + p.lastName()).replace(" ", "").toUpperCase(Locale.ROOT);
        return base.length() > 8 ? base.substring(0, 8) : (base + "00000000").substring(0, 8);
    }

    private NameParts splitName(String fullName) {
        if (fullName == null || fullName.isBlank()) return new NameParts("unknown", "unknown");
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) return new NameParts(parts[0], parts[0]);
        return new NameParts(String.join(" ", Arrays.copyOf(parts, parts.length - 1)), parts[parts.length - 1]);
    }

    private String normalizeName(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private List<String> toList(JsonNode array) {
        if (!array.isArray()) return new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (JsonNode item : array) values.add(item.asText());
        return values;
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asText();
    }

    private record NameParts(String firstName, String lastName) {}

    // Keep JSON inline for easy edit/replacement later.
    private static final String DATA_JSON = """
            {
              "laboratoire": {
                "intitule_fr": "Laboratoire de Recherche en Sciences de l'Ingénieur et Innovation (LaRSII)",
                "intitule_en": "Laboratory of Research in Engineering Sciences and Innovation (LaRESI)",
                "acronyme": "LaRESI",
                "universite": "Université Sultan Moulay Slimane",
                "programme": "Programme de Structuration de la Recherche Scientifique",
                "etablissement_domiciliation": "Ecole Nationale des Sciences Appliquées de Beni Mellal (ENSA BM)",
                "telephone": "212661 48 74 20",
                "email": "r.allaoui@usms.ma"
              },
              "direction": {
                "directeur": {"nom": "Allaoui Rabha", "grade": "PES"},
                "directeur_adjoint": {"nom": "El Alaoui Mohamed", "grade": "MCA"}
              },
              "comite_de_gestion": [
                {"nom": "Allaoui Rabha", "grade": "PES", "role": "Président du conseil du Laboratoire"},
                {"nom": "El Alaoui Mohamed", "grade": "MCA", "role": "Responsable documentation et RH"}
              ],
              "membres_principaux": [
                {"nom":"Hidki Rachid","grade":"MCA","specialite":"Mécanique et énergétique","etablissement":"ENSA BM","etudiants_doctorat":[]},
                {"nom":"Hassoune Abdelilah","grade":"MCA","specialite":"Génie Electrique","etablissement":"ENSA BM","etudiants_doctorat":[]},
                {"nom":"Allaoui Rabha","grade":"PES","specialite":"Informatique","etablissement":"ENSA BM","etudiants_doctorat":["Bourzik Abdelaati"]},
                {"nom":"El Alaoui Mohamed","grade":"MCA","specialite":"Génie Industriel","etablissement":"ENSA BM","etudiants_doctorat":[]}
              ],
              "membres_associes": [
                {"nom":"Ouanan Hamid","grade":"MCH","specialite":"Génie informatique","etablissement":"ENSABM","etudiants_doctorat":[]}
              ],
              "thematiques_recherche": ["Mathématiques – Informatique et Applications", "Physique et Applications"],
              "axes_recherche": ["Énergie et efficacité énergétique", "Intelligence artificielle et science des données"],
              "competences": {
                "scientifiques_et_methodologiques": ["Modélisation mathématique", "Simulation numérique"],
                "savoir_faire_technologique": ["Développement de solutions smart cities"],
                "sectorielles": ["Agriculture numérique"],
                "innovation_et_transfert": ["Conception de projets collaboratifs R&D"]
              },
              "equipements": {
                "scientifiques_laboratoire": [],
                "dotations_universite": ["Centre d'analyse de l'université"],
                "etablissement_et_universite": ["Chromatographie en phase gazeuse", "Imprimante 3D technologie FDM"],
                "informatiques_laboratoire": ["Matériel de conception MOOCs"]
              },
              "collaborations": {
                "regionales": [{"organisme":"COSUMAR Oulad Ayad Beni Mellal","thematique":"Agro-alimentaire","nature":"Stages de recherche"}],
                "nationales": [{"organisme":"FST-Marrakech","thematique":"Mécanique, énergétique","nature":"Recherche scientifique"}],
                "etrangeres": [{"organisme":"Université de Nantes","thematique":"Informatique","nature":"Recherche scientifique"}]
              },
              "production_scientifique_2021_2024": {
                "publications": [
                  {"auteurs":["Y. Elaouzy","A. El Fadar"],"titre":"Building-integrated passive and renewable solar technologies","revue":"Sustainable Energy Technologies and Assessments","annee":2024},
                  {"auteurs":["Elbaghazaoui, Bahaa Eddine"],"titre":"Predicting the next word using the Markov chain model","revue":"The Journal of Supercomputing","annee":2023,"doi":"10.1007/s11227-023-05125-2"}
                ],
                "communications": [
                  {"auteurs":["A. Esswidi"],"titre":"Traffic congestion multilevel classification using deep learning","conference":"ICRAMCS 2023"}
                ],
                "theses_soutenues": [
                  {"auteur":"Ouassam Elhoucine","titre":"Méthodes Heuristiques et Intelligence Artificielle","directeur":"Pr. Belaid Bouikhalene"}
                ]
              }
            }
            """;
}
