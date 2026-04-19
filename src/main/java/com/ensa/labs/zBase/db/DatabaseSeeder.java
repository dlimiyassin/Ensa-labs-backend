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

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleDao roleDao;
    private final UserDao userDao;
    private final LabRepository labRepository;
    private final MemberRepository memberRepository;
    private final ResearchItemRepository researchItemRepository;
    private final CompetenceRepository competenceRepository;
    private final EquipmentRepository equipmentRepository;
    private final CollaborationRepository collaborationRepository;
    private final PublicationRepository publicationRepository;
    private final ThesisRepository thesisRepository;
    private final ProductionRepository productionRepository;
    private final RegulationRepository regulationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public DatabaseSeeder(RoleDao roleDao, UserDao userDao, LabRepository labRepository, MemberRepository memberRepository, ResearchItemRepository researchItemRepository, CompetenceRepository competenceRepository, EquipmentRepository equipmentRepository, CollaborationRepository collaborationRepository, PublicationRepository publicationRepository, ThesisRepository thesisRepository, ProductionRepository productionRepository, RegulationRepository regulationRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.labRepository = labRepository;
        this.memberRepository = memberRepository;
        this.researchItemRepository = researchItemRepository;
        this.competenceRepository = competenceRepository;
        this.equipmentRepository = equipmentRepository;
        this.collaborationRepository = collaborationRepository;
        this.publicationRepository = publicationRepository;
        this.thesisRepository = thesisRepository;
        this.productionRepository = productionRepository;
        this.regulationRepository = regulationRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        Role admin = createRoleIfMissing("ADMIN");
        Role researcher = createRoleIfMissing("RESEARCHER");
        seedUser("admin", "AA111", "admin@ensa.ma", "System", "Admin", Set.of(admin));

        JsonNode root = objectMapper.readTree(DATA_JSON);
        Lab lab = seedLab(root.path("laboratoire"));
        Map<String, Member> membersByNormalizedName = seedMembers(root, lab, researcher);
        seedLeadership(root.path("direction"), lab, membersByNormalizedName);
        seedResearchItems(root.path("thematiques_recherche"), ResearchItemType.THEME, lab);
        seedResearchItems(root.path("axes_recherche"), ResearchItemType.AXIS, lab);
        seedCompetences(root.path("competences"), lab);
        seedEquipments(root.path("equipements"), lab);
        seedCollaborations(root.path("collaborations"), lab);
        seedProduction(root.path("production_scientifique_2021_2024"), lab);
        seedRegulations(root.path("reglement_interieur"), lab);
        labRepository.save(lab);
    }

    private Lab seedLab(JsonNode node) {
        String acronym = text(node, "acronyme");
        Lab lab = labRepository.findByAcronym(acronym).orElseGet(Lab::new);
        lab.setAcronym(acronym);
        lab.setTitleFr(text(node, "intitule_fr"));
        lab.setTitleEn(text(node, "intitule_en"));
        lab.setUniversity(text(node, "universite"));
        lab.setProgram(text(node, "programme"));
        lab.setEstablishment(text(node, "etablissement_domiciliation"));
        lab.setPhone(text(node, "telephone"));
        lab.setEmail(text(node, "email"));
        lab.setAccreditationStart(LocalDate.of(2026, 1, 1));
        lab.setAccreditationEnd(LocalDate.of(2029, 12, 31));
        return labRepository.save(lab);
    }

    private Map<String, Member> seedMembers(JsonNode root, Lab lab, Role researcherRole) {
        Map<String, Member> map = new HashMap<>();
        seedMemberArray(root.path("membres_principaux"), lab, false, researcherRole, map);
        seedMemberArray(root.path("membres_associes"), lab, true, researcherRole, map);
        return map;
    }

    private void seedMemberArray(JsonNode array, Lab lab, boolean associated, Role researcherRole, Map<String, Member> map) {
        if (!array.isArray()) return;
        for (JsonNode node : array) {
            String fullName = text(node, "nom");
            NameParts parts = splitName(fullName);
            Member member = memberRepository
                    .findByLabIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(lab.getId(), parts.firstName(), parts.lastName())
                    .orElseGet(Member::new);

            member.setLab(lab);
            member.setFirstName(parts.firstName());
            member.setLastName(parts.lastName());
            member.setGrade(toGrade(text(node, "grade")));
            member.setSpeciality(text(node, "specialite"));
            member.setEstablishment(text(node, "etablissement"));
            member.setAssociated(associated);
            member.setRoleInLab(MemberRoleInLab.MEMBER);
            member.setPhdStudents(readStringArray(node.path("etudiants_doctorat")));

            String username = normalizeForUsername(parts.firstName() + "." + parts.lastName());
            String cin = randomCin(parts.firstName(), parts.lastName());
            User user = seedUser(username, cin, null, parts.firstName(), parts.lastName(), Set.of(researcherRole));
            member.setUser(user);

            Member saved = memberRepository.save(member);
            map.put(normalizeFullName(fullName), saved);
        }
    }

    private void seedLeadership(JsonNode directionNode, Lab lab, Map<String, Member> membersByName) {
        Member director = findOrCreateMinimalMember(directionNode.path("directeur"), lab, MemberRoleInLab.DIRECTOR, false, membersByName);
        Member deputy = findOrCreateMinimalMember(directionNode.path("directeur_adjoint"), lab, MemberRoleInLab.DEPUTY_DIRECTOR, false, membersByName);
        lab.setDirector(director);
        lab.setDeputyDirector(deputy);
        labRepository.save(lab);
    }

    private Member findOrCreateMinimalMember(JsonNode node, Lab lab, MemberRoleInLab role, boolean associated, Map<String, Member> membersByName) {
        String fullName = text(node, "nom");
        String key = normalizeFullName(fullName);
        Member existing = membersByName.get(key);
        if (existing != null) {
            existing.setRoleInLab(role);
            return memberRepository.save(existing);
        }

        NameParts parts = splitName(fullName);
        Member member = new Member();
        member.setLab(lab);
        member.setFirstName(parts.firstName());
        member.setLastName(parts.lastName());
        member.setGrade(toGrade(text(node, "grade")));
        member.setAssociated(associated);
        member.setRoleInLab(role);
        Member saved = memberRepository.save(member);
        membersByName.put(key, saved);
        return saved;
    }

    private void seedResearchItems(JsonNode array, ResearchItemType type, Lab lab) {
        if (!array.isArray()) return;
        for (JsonNode item : array) {
            String title = normalize(textValue(item));
            if (title == null) continue;
            ResearchItem researchItem = new ResearchItem();
            researchItem.setLab(lab);
            researchItem.setType(type);
            researchItem.setTitle(title);
            researchItemRepository.save(researchItem);
        }
    }

    private void seedCompetences(JsonNode node, Lab lab) {
        seedCompetenceGroup(node.path("scientifiques_et_methodologiques"), lab, CompetenceType.SCIENTIFIC);
        seedCompetenceGroup(node.path("savoir_faire_technologique"), lab, CompetenceType.TECHNOLOGICAL);
        seedCompetenceGroup(node.path("sectorielles"), lab, CompetenceType.SECTORIAL);
        seedCompetenceGroup(node.path("innovation_et_transfert"), lab, CompetenceType.INNOVATION);
    }

    private void seedCompetenceGroup(JsonNode array, Lab lab, CompetenceType type) {
        if (!array.isArray()) return;
        for (JsonNode item : array) {
            String description = normalize(textValue(item));
            if (description == null) continue;
            Competence competence = new Competence();
            competence.setLab(lab);
            competence.setType(type);
            competence.setDescription(description);
            competenceRepository.save(competence);
        }
    }

    private void seedEquipments(JsonNode node, Lab lab) {
        seedEquipmentGroup(node.path("scientifiques_laboratoire"), lab, EquipmentCategory.LAB);
        seedEquipmentGroup(node.path("dotations_universite"), lab, EquipmentCategory.UNIVERSITY);
        seedEquipmentGroup(node.path("etablissement_et_universite"), lab, EquipmentCategory.SHARED);
        seedEquipmentGroup(node.path("informatiques_laboratoire"), lab, EquipmentCategory.IT);
    }

    private void seedEquipmentGroup(JsonNode array, Lab lab, EquipmentCategory category) {
        if (!array.isArray()) return;
        for (JsonNode item : array) {
            String name = normalize(textValue(item));
            if (name == null) continue;
            Equipment equipment = new Equipment();
            equipment.setLab(lab);
            equipment.setCategory(category);
            equipment.setName(name);
            equipmentRepository.save(equipment);
        }
    }

    private void seedCollaborations(JsonNode node, Lab lab) {
        seedCollaborationGroup(node.path("regionales"), lab, CollaborationScope.REGIONAL);
        seedCollaborationGroup(node.path("nationales"), lab, CollaborationScope.NATIONAL);
        seedCollaborationGroup(node.path("etrangeres"), lab, CollaborationScope.INTERNATIONAL);
    }

    private void seedCollaborationGroup(JsonNode array, Lab lab, CollaborationScope scope) {
        if (!array.isArray()) return;
        for (JsonNode item : array) {
            Collaboration collaboration = new Collaboration();
            collaboration.setLab(lab);
            collaboration.setScope(scope);
            collaboration.setOrganization(text(item, "organisme"));
            collaboration.setTheme(text(item, "thematique"));
            collaboration.setNature(text(item, "nature"));
            if (collaboration.getOrganization() != null) {
                collaborationRepository.save(collaboration);
            }
        }
    }

    private void seedProduction(JsonNode node, Lab lab) {
        Production production = new Production();
        production.setLab(lab);

        Set<Publication> publications = new HashSet<>();
        for (JsonNode item : node.path("publications")) {
            publications.add(toPublication(item, lab, PublicationType.JOURNAL));
        }
        production.setPublications(publications);

        Set<Publication> communications = new HashSet<>();
        for (JsonNode item : node.path("communications")) {
            communications.add(toPublication(item, lab, PublicationType.COMMUNICATION));
        }
        production.setCommunications(communications);

        Set<Thesis> theses = new HashSet<>();
        for (JsonNode item : node.path("theses_soutenues")) {
            Thesis thesis = new Thesis();
            thesis.setLab(lab);
            thesis.setAuthor(text(item, "auteur"));
            thesis.setTitle(text(item, "titre"));
            thesis.setSupervisor(text(item, "directeur"));
            thesis.setDefenseDate(null);
            theses.add(thesisRepository.save(thesis));
        }
        production.setTheses(theses);
        productionRepository.save(production);
    }

    private Publication toPublication(JsonNode item, Lab lab, PublicationType defaultType) {
        Publication publication = new Publication();
        publication.setLab(lab);
        publication.setTitle(text(item, "titre"));
        publication.setPublicationYear(item.path("annee").isInt() ? item.path("annee").asInt() : null);
        publication.setType(defaultType);
        publication.setAuthors(readStringArray(item.path("auteurs")));
        publication.setJournal(text(item, "revue"));
        publication.setConference(text(item, "conference"));
        publication.setDoi(text(item, "doi"));
        publication.setPages(text(item, "pages"));
        return publicationRepository.save(publication);
    }

    private void seedRegulations(JsonNode node, Lab lab) {
        if (node.isMissingNode() || node.isNull()) return;
        node.fields().forEachRemaining(entry -> {
            Regulation regulation = new Regulation();
            regulation.setLab(lab);
            regulation.setTitle(entry.getKey().replace('_', ' '));
            regulation.setContent(entry.getValue().toString());
            regulationRepository.save(regulation);
        });
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

    private MemberGrade toGrade(String value) {
        if (value == null) return MemberGrade.OTHER;
        try {
            return MemberGrade.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return MemberGrade.OTHER;
        }
    }

    private NameParts splitName(String fullName) {
        String normalized = normalize(fullName);
        if (normalized == null) return new NameParts("Unknown", "Unknown");
        String[] tokens = normalized.split("\\s+");
        if (tokens.length == 1) return new NameParts(tokens[0], tokens[0]);
        String lastName = tokens[tokens.length - 1];
        String firstName = String.join(" ", Arrays.copyOf(tokens, tokens.length - 1));
        return new NameParts(firstName, lastName);
    }

    private String normalize(String value) {
        if (value == null) return null;
        String v = value.trim();
        return v.isEmpty() ? null : v;
    }

    private String normalizeFullName(String value) {
        if (value == null) return "";
        return normalizeForUsername(value.replace(' ', '.'));
    }

    private String normalizeForUsername(String value) {
        String base = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return base.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9.]", "").replaceAll("\\.+", ".");
    }

    private String randomCin(String firstName, String lastName) {
        String seed = normalizeForUsername(firstName + lastName);
        return (seed + "00000").substring(0, Math.min(8, seed.length() + 5)).toUpperCase(Locale.ROOT);
    }

    private String text(JsonNode node, String field) {
        return normalize(node.path(field).isMissingNode() || node.path(field).isNull() ? null : node.path(field).asText());
    }

    private String textValue(JsonNode node) {
        return normalize(node.isNull() ? null : node.asText());
    }

    private List<String> readStringArray(JsonNode arrayNode) {
        if (!arrayNode.isArray()) return new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (JsonNode n : arrayNode) {
            String value = textValue(n);
            if (value != null) values.add(value);
        }
        return values;
    }

    private record NameParts(String firstName, String lastName) {}

    // Kept inline for now; can be replaced by file/API source later.
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
              "membres_principaux": [
                {"nom":"Hidki Rachid","grade":"MCA","specialite":"Mécanique et énergétique","etablissement":"ENSA BM","etudiants_doctorat":[]},
                {"nom":"Hassoune Abdelilah","grade":"MCA","specialite":"Génie Electrique","etablissement":"ENSA BM","etudiants_doctorat":[]},
                {"nom":"Allaoui Rabha","grade":"PES","specialite":"Informatique","etablissement":"ENSA BM","etudiants_doctorat":["Bourzik Abdelaati","Mohamed Amine Nebri"]},
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
                  {"auteurs":["A. Esswidi"],"titre":"Traffic congestion multilevel classification using deep learning","conference":"ICRAMCS 2023","date":"16-17-18 March 2023"}
                ],
                "theses_soutenues": [
                  {"auteur":"Ouassam Elhoucine","titre":"Méthodes Heuristiques et Intelligence Artificielle","date_soutenance":"30 décembre 2023","directeur":"Pr. Belaid Bouikhalene"}
                ]
              },
              "reglement_interieur": {
                "article_1_definition": "Le laboratoire est une structure de formation et de recherche.",
                "article_2_missions": ["Participer activement à la recherche scientifique et à l'innovation"]
              }
            }
            """;
}
