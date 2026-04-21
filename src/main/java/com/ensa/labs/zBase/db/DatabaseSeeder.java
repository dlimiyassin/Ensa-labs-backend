package com.ensa.labs.zBase.db;

import com.ensa.labs.research.bean.*;
import com.ensa.labs.research.bean.enums.*;
import com.ensa.labs.research.dao.*;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import com.ensa.labs.zBase.security.bean.enums.UserStatus;
import com.ensa.labs.zBase.security.dao.facade.RoleDao;
import com.ensa.labs.zBase.security.dao.facade.UserDao;
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

    public DatabaseSeeder(RoleDao roleDao, UserDao userDao, LabRepository labRepository, MemberRepository memberRepository, ResearchFieldRepository researchFieldRepository, ResearchItemRepository researchItemRepository, CompetenceRepository competenceRepository, EquipmentRepository equipmentRepository, CollaborationRepository collaborationRepository, PublicationRepository publicationRepository, ThesisRepository thesisRepository, ProductionRepository productionRepository, PasswordEncoder passwordEncoder) {
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
    }

    @Override
    public void run(String... args) {
        Role admin = createRoleIfMissing("ADMIN");
        Role researcher = createRoleIfMissing("RESEARCHER");
        seedUser("admin", "AA111", "admin@ensa.ma", "System", "Admin", Set.of(admin));

        Lab lab = createLab();

        Map<String, Member> members = seedMembers(PRINCIPAL_MEMBRES, lab, false);
        members.putAll(seedMembers(MEMBRES_ASSOCIES, lab, true));
        assignDirection(lab, members);
        seedComiteGestion(lab);

        seedThematiques(lab);
        seedAxes(lab);
        seedCompetences(lab);
        seedEquipements(lab);
        seedCollaborations();
        seedProduction(lab);

        labRepository.save(lab);
    }

    private Lab createLab() {
        Lab lab = labRepository.findByAcronym("LaRESI").orElseGet(Lab::new);
        lab.setTitleFr("Laboratoire de Recherche en Sciences de l'Ingénieur et Innovation (LaRSII)");
        lab.setTitleEn("Laboratory of Research in Engineering Sciences and Innovation (LaRESI)");
        lab.setAcronym("LaRESI");
        lab.setUniversity("Université Sultan Moulay Slimane");
        lab.setProgram("Programme de Structuration de la Recherche Scientifique");
        lab.setEstablishment("Ecole Nationale des Sciences Appliquées de Beni Mellal (ENSA BM)");
        lab.setPhone("212661 48 74 20");
        lab.setEmail("r.allaoui@usms.ma");
        lab.setAccreditationStart(LocalDate.of(2026, 1, 1));
        lab.setAccreditationEnd(LocalDate.of(2029, 12, 31));
        return labRepository.save(lab);
    }

    private Map<String, Member> seedMembers(List<MembreSeed> membres, Lab lab, boolean associe) {
        Map<String, Member> map = new HashMap<>();
        for (MembreSeed seed : membres) {
            NameParts nameParts = splitName(seed.nomComplet);
            Member member = memberRepository.findByLaboratoireIdAndPrenomIgnoreCaseAndNomIgnoreCase(lab.getId(), nameParts.firstName(), nameParts.lastName()).orElseGet(Member::new);
            member.setLaboratoire(lab);
            member.setPrenom(nameParts.firstName());
            member.setNom(nameParts.lastName());
            member.setGrade(toGrade(seed.grade));
            member.setSpecialite(seed.specialite);
            member.setEtablissement(seed.etablissement);
            member.setAssocie(associe);
            member.setRoleDansLaboratoire(MemberRoleInLab.MEMBER);
            member.setDoctorantsEncadres(new ArrayList<>(seed.doctorants));
            Member saved = memberRepository.save(member);
            map.put(normalizeName(seed.nomComplet), saved);

            seedUser(username(nameParts), defaultCin(nameParts), null, nameParts.firstName(), nameParts.lastName(), Set.of(createRoleIfMissing("RESEARCHER")));
        }
        return map;
    }

    private void assignDirection(Lab lab, Map<String, Member> members) {
        Member director = updateDirectionMember(lab, members.get(normalizeName("Allaoui Rabha")), "Allaoui Rabha", "PES", MemberRoleInLab.DIRECTOR);
        Member deputy = updateDirectionMember(lab, members.get(normalizeName("El Alaoui Mohamed")), "El Alaoui Mohamed", "MCA", MemberRoleInLab.DEPUTY_DIRECTOR);
        lab.setDirector(director);
        lab.setDeputyDirector(deputy);
    }

    private Member updateDirectionMember(Lab lab, Member member, String nomComplet, String grade, MemberRoleInLab role) {
        if (member == null) {
            NameParts p = splitName(nomComplet);
            member = new Member();
            member.setLaboratoire(lab);
            member.setPrenom(p.firstName());
            member.setNom(p.lastName());
            member.setGrade(toGrade(grade));
        }
        member.setRoleDansLaboratoire(role);
        return memberRepository.save(member);
    }

    private void seedComiteGestion(Lab lab) {
        List<ComiteGestionMembre> comite = new ArrayList<>();
        comite.add(comite("Allaoui Rabha", "Président du conseil du Laboratoire"));
        comite.add(comite("El Alaoui Mohamed", "Responsable documentation et RH"));
        lab.setComiteGestion(comite);
    }

    private ComiteGestionMembre comite(String nom, String role) {
        ComiteGestionMembre membre = new ComiteGestionMembre();
        membre.setNomEnseignant(nom);
        membre.setRoleComite(role);
        return membre;
    }

    private void seedThematiques(Lab lab) {
        for (String value : List.of("Mathématiques – Informatique et Applications", "Physique et Applications")) {
            ResearchField field = researchFieldRepository.findByName(value).orElseGet(ResearchField::new);
            field.setName(value);
            researchFieldRepository.save(field);
            lab.getResearchFields().add(field);
        }
    }

    private void seedAxes(Lab lab) {
        for (String value : List.of("Énergie et efficacité énergétique", "Intelligence artificielle et science des données")) {
            ResearchItem item = new ResearchItem();
            item.setLab(lab);
            item.setTitle(value);
            researchItemRepository.save(item);
        }
    }

    private void seedCompetences(Lab lab) {
        seedCompetenceByType(List.of("Modélisation mathématique", "Simulation numérique"), lab, CompetenceType.SCIENTIFIC);
        seedCompetenceByType(List.of("Développement de solutions smart cities"), lab, CompetenceType.TECHNOLOGICAL);
        seedCompetenceByType(List.of("Agriculture numérique"), lab, CompetenceType.SECTORIAL);
        seedCompetenceByType(List.of("Conception de projets collaboratifs R&D"), lab, CompetenceType.INNOVATION);
    }

    private void seedCompetenceByType(List<String> entries, Lab lab, CompetenceType type) {
        for (String description : entries) {
            Competence c = new Competence();
            c.setLab(lab);
            c.setType(type);
            c.setDescription(description);
            competenceRepository.save(c);
        }
    }

    private void seedEquipements(Lab lab) {
        seedEquipementByCategorie(List.of(), lab, EquipmentCategory.LAB);
        seedEquipementByCategorie(List.of("Centre d'analyse de l'université"), lab, EquipmentCategory.UNIVERSITY);
        seedEquipementByCategorie(List.of("Chromatographie en phase gazeuse", "Imprimante 3D technologie FDM"), lab, EquipmentCategory.SHARED);
        seedEquipementByCategorie(List.of("Matériel de conception MOOCs"), lab, EquipmentCategory.IT);
    }

    private void seedEquipementByCategorie(List<String> entries, Lab lab, EquipmentCategory category) {
        for (String name : entries) {
            Equipment e = new Equipment();
            e.setLab(lab);
            e.setCategory(category);
            e.setName(name);
            equipmentRepository.save(e);
        }
    }

    private void seedCollaborations() {
        seedCollaborationsByScope(List.of(collab("COSUMAR Oulad Ayad Beni Mellal", "Agro-alimentaire", "Stages de recherche")), CollaborationScope.REGIONAL);
        seedCollaborationsByScope(List.of(collab("FST-Marrakech", "Mécanique, énergétique", "Recherche scientifique")), CollaborationScope.NATIONAL);
        seedCollaborationsByScope(List.of(collab("Université de Nantes", "Informatique", "Recherche scientifique")), CollaborationScope.INTERNATIONAL);
    }

    private CollaborationSeed collab(String organisme, String thematique, String nature) { return new CollaborationSeed(organisme, thematique, nature); }

    private void seedCollaborationsByScope(List<CollaborationSeed> entries, CollaborationScope scope) {
        for (CollaborationSeed n : entries) {
            Collaboration c = new Collaboration();
            c.setOrganization(n.organisme());
            c.setEstablishment("Ecole Nationale des Sciences Appliquées de Beni Mellal (ENSA BM)");
            c.setTheme(n.thematique());
            c.setNature(n.nature());
            c.setScope(scope);
            collaborationRepository.save(c);
        }
    }

    private void seedProduction(Lab lab) {
        Production production = new Production();
        production.setLab(lab);

        Set<Publication> publications = new HashSet<>();
        publications.add(savePublication(new PublicationSeed(List.of("Y. Elaouzy", "A. El Fadar"), "Building-integrated passive and renewable solar technologies", "Sustainable Energy Technologies and Assessments", null, null, null, 2024), PublicationType.JOURNAL, lab));
        publications.add(savePublication(new PublicationSeed(List.of("Elbaghazaoui, Bahaa Eddine"), "Predicting the next word using the Markov chain model", "The Journal of Supercomputing", null, "10.1007/s11227-023-05125-2", null, 2023), PublicationType.JOURNAL, lab));

        Set<Publication> communications = new HashSet<>();
        communications.add(savePublication(new PublicationSeed(List.of("A. Esswidi"), "Traffic congestion multilevel classification using deep learning", null, "ICRAMCS 2023", null, null, null), PublicationType.COMMUNICATION, lab));

        Thesis t = new Thesis();
        t.setLab(lab);
        t.setAuthor("Ouassam Elhoucine");
        t.setTitle("Méthodes Heuristiques et Intelligence Artificielle");
        t.setSupervisor("Pr. Belaid Bouikhalene");
        Set<Thesis> theses = Set.of(thesisRepository.save(t));

        production.setPublications(publications);
        production.setCommunications(communications);
        production.setTheses(theses);
        productionRepository.save(production);
    }

    private Publication savePublication(PublicationSeed n, PublicationType type, Lab lab) {
        Publication p = new Publication();
        p.setLab(lab);
        p.setType(type);
        p.setTitle(n.titre());
        p.setPublicationYear(n.annee());
        p.setAuthors(n.auteurs());
        p.setJournal(n.revue());
        p.setConference(n.conference());
        p.setDoi(n.doi());
        p.setPages(n.pages());
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

    private record NameParts(String firstName, String lastName) {}
    private record MembreSeed(String nomComplet, String grade, String specialite, String etablissement, List<String> doctorants) {}
    private record CollaborationSeed(String organisme, String thematique, String nature) {}
    private record PublicationSeed(List<String> auteurs, String titre, String revue, String conference, String doi, String pages, Integer annee) {}

    private static final List<MembreSeed> PRINCIPAL_MEMBRES = List.of(
            new MembreSeed("Hidki Rachid", "MCA", "Mécanique et énergétique", "ENSA BM", List.of()),
            new MembreSeed("Hassoune Abdelilah", "MCA", "Génie Electrique", "ENSA BM", List.of()),
            new MembreSeed("Allaoui Rabha", "PES", "Informatique", "ENSA BM", List.of("Bourzik Abdelaati")),
            new MembreSeed("El Alaoui Mohamed", "MCA", "Génie Industriel", "ENSA BM", List.of())
    );

    private static final List<MembreSeed> MEMBRES_ASSOCIES = List.of(
            new MembreSeed("Ouanan Hamid", "MCH", "Génie informatique", "ENSABM", List.of())
    );
}
