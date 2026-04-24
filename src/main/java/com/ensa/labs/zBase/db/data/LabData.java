package com.ensa.labs.zBase.db.data;

import com.ensa.labs.recherche.bean.enums.MemberAssociationType;
import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;

import java.util.List;

public class LabData {

    public record ComiteRoleData(String nomEnseignant, List<String> roles) {}

    public record EquipeSeed(String name, String responsableFullName, List<String> axesRecherche, List<String> memberFullNames) {}

    public record LabSeed(
            String titleFr,
            String titleEn,
            String acronym,
            String university,
            String program,
            String establishment,
            String phone,
            String email,
            int accreditationStartYear,
            int accreditationEndYear,
            String department,
            String directeurFullName,
            String directeurAdjointFullName,
            List<String> axesRecherche,
            List<ComiteRoleData> comiteGestion,
            List<EquipeSeed> equipes
    ) {}

    public static final String ACRONYM = "LaRESI";

    public static final List<LabSeed> LABS = List.of(
            new LabSeed(
                    "Laboratoire de Recherche en Sciences de l'Ingénieur et Innovation (LaRSII)",
                    "Laboratory of Research in Engineering Sciences and Innovation (LaRESI)",
                    "LaRESI",
                    "Université Sultan Moulay Slimane",
                    "Programme de Structuration de la Recherche Scientifique",
                    "ENSA BM",
                    "06 61 48 74 20",
                    "r.allaoui@usms.ma",
                    2026,
                    2029,
                    "Informatique",
                    "Allaoui Rabha",
                    "El Alaoui Mohamed",
                    List.of(),
                    List.of(
                            new ComiteRoleData("Allaoui Rabha", List.of(
                                    "Président du conseil du Laboratoire",
                                    "Responsable du matériel (scientifique et informatique) et équipements du laboratoire"
                            )),
                            new ComiteRoleData("El Alaoui Mohamed", List.of(
                                    "Responsable de documentation",
                                    "Responsable des ressources humaines (adhésion des nouveaux membres, doctorants)"
                            ))
                    ),
                    List.of()
            ),
            new LabSeed(
                    "Laboratoire de Recherche en Sciences et Technologies Avancées",
                    "Research Laboratory in Advanced Sciences and Technologies",
                    "LRSTA",
                    "Université Sultan Moulay Slimane",
                    "Programme de Structuration de la Recherche Scientifique",
                    "Ecole Nationale des Sciences Appliquées de Beni Mellal",
                    "06 48 65 02 44",
                    "soufianebelhouideg@usms.ma",
                    2026,
                    2029,
                    "Informatique",
                    "Soufiane Belhouideg",
                    "Aziz Hirri",
                    List.of(),
                    List.of(),
                    List.of(
                            new EquipeSeed(
                                    "Equipe de Recherche en Ingénierie des systèmes avancés et Energies (ASEE)",
                                    "Taj Eddine Khalili",
                                    List.of(
                                            "Énergie et efficacité énergétique",
                                            "Énergies alternatives et renouvelables",
                                            "Hydrogène vert",
                                            "Mobilité électrique",
                                            "Optimisation du stockage d’énergie",
                                            "Physique appliquée",
                                            "Physique médicale",
                                            "Automatique et automatisme",
                                            "Smart Grids et automatisation des systèmes énergétiques",
                                            "Dessalement de l’eau et de l’eau de mer",
                                            "Propriétés des matériaux",
                                            "Modélisation des systèmes énergétiques",
                                            "Étude de l’atmosphère et météorologie régionale",
                                            "Caractérisation des sites astronomiques"
                                    ),
                                    List.of(
                                            "Taj Eddine Khalili",
                                            "Sabil Mohamed",
                                            "Kaab Mohamed",
                                            "Soufiane Belhouideg",
                                            "Ismail Ezzaraa",
                                            "Mostapha Oulcaid",
                                            "Soukaina Essaghir"
                                    )
                            ),
                            new EquipeSeed(
                                    "Equipe Agri Food Technologie and Smart System (AFTSS)",
                                    "Salwa Tsouli Sarhir",
                                    List.of(
                                            "Agriculture digitale et durable",
                                            "Robotique et IA pour l’optimisation agricole",
                                            "Biotechnologie appliquée à l’agriculture",
                                            "Dessalement de l’eau pour usage agricole",
                                            "Biotechnologies microbiennes",
                                            "Agroalimentaire et technologies de transformation alimentaire",
                                            "Biochimie et microbiologie",
                                            "Nutrition et santé",
                                            "Sécurité alimentaire",
                                            "Science des arômes et évaluation sensorielle",
                                            "Assurance qualité et management dans l’agroalimentaire",
                                            "Valorisation des plantes aromatiques et médicinales",
                                            "Capteurs et IoT pour l’agriculture intelligente"
                                    ),
                                    List.of(
                                            "Salwa Tsouli Sarhir",
                                            "Siham Ourouadi",
                                            "Aziz Hirri",
                                            "Yahya Rokni",
                                            "Ayoub Hallouti"
                                    )
                            )
                    )
            )
    );

    public static final List<MemberData> MAIN_MEMBERS = List.of(
            // LaRESI
            new MemberData("Hidki", "Rachid", "MCA", "Mécanique et énergétique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Hassoune", "Abdelilah", "MCA", "Génie Electrique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Achkari Begdouri", "Oussama", "MCA", "Energétique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Aouraghe", "Ibtissam", "MCA", "Informatique et Systèmes Intelligents", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Esswidi", "Ayoub", "MCA", "Mathématiques - informatique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Gouskir", "Mohamed", "MCA", "Informatique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Ennahbaoui", "Mohamed", "MCA", "Informatique et sécurité de l'information", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("ELbaghazaoui", "Bahaa Eddine", "MCA", "Informatique et intelligence artificielle", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("El Alaoui", "Mohamed", "MCA", "Génie Industriel", "ENSA BM", MemberRoleInLab.DIRECTEUR_ADJOINT, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Bouaouid", "Mohamed", "MCA", "Mathématique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Allaoui", "Rabha", "PES", "Informatique", "ENSA BM", MemberRoleInLab.DIRECTEUR, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Bakhadach", "Idris", "MCA", "Mathématique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Sadiki", "Hamid", "MCA", "Mathématique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LaRESI"),
            new MemberData("Bouikhalene", "Belaid", "PES", "Mathématiques - Informatique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET,
                    List.of("Bourzik Abdelaati", "Mohamed Amine Nebri", "Karim Farhat", "Meriem Debbagh", "Maryem Ouchella"), "LaRESI"),
            new MemberData("Ouanan", "Hamid", "MCH", "Génie informatique", "ENSABM", MemberRoleInLab.MEMBER, MemberAssociationType.ASSOCIATED, List.of(), "LaRESI"),
            new MemberData("Hachimi", "Abdelfattah", "MCA", "Langue et littérature française", "ENSABM", MemberRoleInLab.MEMBER, MemberAssociationType.ASSOCIATED, List.of(), "LaRESI"),
            new MemberData("Ounachad", "Khalid", "MCA", "Informatique", "ENSABM", MemberRoleInLab.MEMBER, MemberAssociationType.ASSOCIATED, List.of(), "LaRESI"),
            new MemberData("Tannouch", "Adil", "MCH", "Systèmes Embarquées et Intelligence Artificielle Mécatronique", "EST BM", MemberRoleInLab.MEMBER, MemberAssociationType.ASSOCIATED, List.of(), "LaRESI"),

            // LRSTA
            new MemberData("Soufiane", "Belhouideg", "OTHER", "Mécanique et Matériaux", "ENSA BM", MemberRoleInLab.DIRECTEUR, MemberAssociationType.PERMENANET,
                    List.of("MOQIN E Younes", "AIT OUADDI Abderrazzak", "OUZGUID Abdelhak"), "LRSTA"),
            new MemberData("Aziz", "Hirri", "OTHER", "Chimie et Environnement", "ENSA BM", MemberRoleInLab.DIRECTEUR_ADJOINT, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Taj Eddine", "Khalili", "MCH", "Génie électrique et énergies renouvelables", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Sabil", "Mohamed", "MCA", "Astrophysique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Kaab", "Mohamed", "MCA", "Astrophysique", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Ismail", "Ezzaraa", "MCA", "Mécanique et Matériaux", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Mostapha", "Oulcaid", "MCA", "Génie Electrique - Energies renouvelable", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Soukaina", "Essaghir", "MCA", "Génie Electrique - Energie renouvelables", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Salwa", "Tsouli Sarhir", "MCA", "Biochimie et Biotechnologie", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Siham", "Ourouadi", "MCA", "Génie des procédés agro-industriels et environnement", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Yahya", "Rokni", "MCA", "Microbiologie, Biochimie et Biotechnologie", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Ayoub", "Hallouti", "MCA", "Biotechnologie et devellopement durable", "ENSA BM", MemberRoleInLab.MEMBER, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
            new MemberData("Hasna", "Chakir", "MCA", "Langue, littérature et art en anglais", "ENSABM", MemberRoleInLab.MEMBER, MemberAssociationType.ASSOCIATED, List.of(), "LRSTA")
    );

    // ================= COMPETENCES =================
    public static final List<String> COMPETENCES_SCIENTIFIC = List.of(
            "Modélisation mathématique",
            "Simulation numérique"
    );

    public static final List<String> COMPETENCES_TECH = List.of(
            "Développement de solutions smart cities"
    );

    public static final List<String> COMPETENCES_SECTOR = List.of(
            "Agriculture numérique"
    );

    public static final List<String> COMPETENCES_INNOVATION = List.of(
            "Conception de projets collaboratifs R&D"
    );

    public static final List<String> EQUIP_LAB = List.of();

    public static final List<String> EQUIP_UNIV = List.of(
            "Centre d'analyse de l'université"
    );

    public static final List<String> EQUIP_SHARED = List.of(
            "Chromatographie en phase gazeuse",
            "Imprimante 3D technologie FDM"
    );

    public static final List<String> EQUIP_IT = List.of(
            "Matériel de conception MOOCs"
    );

    public static final List<CollabData> COLLAB_REGIONAL = List.of(
            new CollabData(
                    "COSUMAR Oulad Ayad Beni Mellal",
                    "Agro-alimentaire",
                    "Stages de recherche"
            )
    );

    public static final List<CollabData> COLLAB_NATIONAL = List.of(
            new CollabData(
                    "FST-Marrakech",
                    "Mécanique, énergétique",
                    "Recherche scientifique"
            )
    );

    public static final List<CollabData> COLLAB_INT = List.of(
            new CollabData(
                    "Université de Nantes",
                    "Informatique",
                    "Recherche scientifique"
            )
    );

    public static final List<PublicationData> PUBLICATIONS = List.of(
            new PublicationData(
                    "Building-integrated passive and renewable solar technologies",
                    2024,
                    List.of("Y. Elaouzy", "A. El Fadar"),
                    "Sustainable Energy Technologies and Assessments",
                    null
            ),
            new PublicationData(
                    "Predicting the next word using the Markov chain model",
                    2023,
                    List.of("Elbaghazaoui, Bahaa Eddine"),
                    "The Journal of Supercomputing",
                    "10.1007/s11227-023-05125-2"
            )
    );

    public static final List<ThesisData> THESES = List.of(
            new ThesisData(
                    "Ouassam Elhoucine",
                    "Méthodes Heuristiques et Intelligence Artificielle",
                    "Pr. Belaid Bouikhalene"
            )
    );
}
