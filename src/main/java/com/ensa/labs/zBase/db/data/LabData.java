package com.ensa.labs.zBase.db.data;

import com.ensa.labs.recherche.bean.enums.CollaborationType;
import com.ensa.labs.recherche.bean.enums.MemberAssociationType;
import com.ensa.labs.recherche.bean.enums.CollaborationScope;
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
            List<String> demainesRecherche,
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
                    List.of(
                            "Mathématiques – Informatique et Applications",
                            "Physique et Applications",
                            "Optimisation et modélisation avancée",
                            "Analyse de données et Big Data",
                            "Intelligence artificielle et intelligence artificielle générative",
                            "Architecture des systèmes informatiques modernes",
                            "Transformation digitale et e-gouvernance",
                            "Industrie 4.0",
                            "Internet des objets (IoT)",
                            "Robotique et systèmes autonomes",
                            "Smart Grids et automatisation industrielle",
                            "Systèmes intelligents et ingénierie des systèmes avancés",
                            "Agriculture digitale et optimisation agricole par l’IA",
                            "E-learning et pédagogie numérique",
                            "Sciences éducatives et communication scientifique"
                    ),
                    List.of(
                            "Énergie et efficacité énergétique",
                            "Énergies alternatives et renouvelables",
                            "Mobilité électrique",
                            "Physique appliquée",
                            "Physique médicale",
                            "Automatique et automatisme",
                            "Smart Grids et automatisation des systèmes énergétiques",
                            "Dessalement de l’eau et de l’eau de mer",
                            "Propriétés des matériaux",
                            "Modélisation des systèmes énergétiques",
                            "Modélisation numérique et physique des systèmes",
                            "Systèmes d'information et bases de données",
                            "Internet des objets (IoT)",
                            "Apprentissage automatique (machine learning)",
                            "Optimisation des systèmes industriels et techniques",
                            "Mobilité intelligente et innovation urbaine",
                            "Gaming et pédagogie éducative",
                            "Systèmes et réseaux intelligents",
                            "Dessalement de l’eau (modélisation de procédés)",
                            "Cybersécurité, systèmes et réseaux",
                            "Intelligence artificielle et science des données",
                            "Analyse numérique",
                            "Analyse de Fourier, harmonique et équations fonctionnelles",
                            "Big Data et architecture moderne",
                            "Smart Cities",
                            "Technologies d’impression 3D et prototypage rapide",
                            "Transformation digitale",
                            "E-learning et pédagogie numérique",
                            "Métiers du numérique et technologies émergentes"
                    ),
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
                    List.of(
                            "Physique appliquée et applications industrielles",
                            "Optimisation, modélisation et simulation avancée",
                            "Industrie 4.0 et transformation digitale",
                            "Internet des objets (IoT) et systèmes connectés",
                            "Robotique, mécatronique et systèmes autonomes",
                            "Smart Grids et automatisation industrielle",
                            "Systèmes intelligents et ingénierie des systèmes avancés",
                            "Agriculture digitale et optimisation agricole par l’intelligence artificielle",
                            "Agronomie et gestion durable des ressources naturelles",
                            "Biotechnologies microbiennes appliquées à l’agriculture et à l’agroalimentaire",
                            "Valorisation des plantes aromatiques et médicinales",
                            "Étude et valorisation des microorganismes des biotopes marocains",
                            "Aliments fonctionnels, nutrition et santé",
                            "Sciences des arômes, formulation et évaluation sensorielle des produits alimentaires",
                            "Astronomie et astrophysique",
                            "Chimiométrie et agroalimentaire"
                    ),
                    List.of(),
                    List.of(
                            new ComiteRoleData("Soufiane BELHOUIDEG", List.of(
                                    "Président du conseil du Laboratoire",
                                    "Coordination scientifique et stratégique"
                            )),
                            new ComiteRoleData("Taj Eddine KHALILI", List.of(
                                    "Responsable du matériel scientifique et informatique",
                                    "Responsable des équipements du laboratoire"
                            )),
                            new ComiteRoleData("Aziz HIRRI", List.of(
                                    "Responsable administratif",
                                    "Rapports annuels",
                                    "Rapporteur des réunions et PVs",
                                    "Suivi des formations continues"
                            )),
                            new ComiteRoleData("Salwa TSOULI SARHIR", List.of(
                                    "Responsable de la communication",
                                    "Ouverture socio-économique",
                                    "Organisation des événements scientifiques"
                            ))
                    ),
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
            new MemberData("Soufiane", "Belhouideg", "MCH", "Mécanique et Matériaux", "ENSA BM", MemberRoleInLab.DIRECTEUR, MemberAssociationType.PERMENANET,
                    List.of("MOQIN E Younes", "AIT OUADDI Abderrazzak", "OUZGUID Abdelhak"), "LRSTA"),
            new MemberData("Aziz", "Hirri", "MCH", "Chimie et Environnement", "ENSA BM", MemberRoleInLab.DIRECTEUR_ADJOINT, MemberAssociationType.PERMENANET, List.of(), "LRSTA"),
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

    public static final List<CollabData> COLLAB_LRSTA = List.of(
            new CollabData("COSUMAR", "Oulad Ayyad - Beni Mellal", "Agro-alimentaire, Energie renouvelable, Mesures physiques", "Stages de recherche", CollaborationScope.REGIONAL, CollaborationType.INDUSTRIAL),
            new CollabData("OCP", "Khouribga", "Energie renouvelable, Informatique industrielle", "Stages de recherche", CollaborationScope.REGIONAL, CollaborationType.INDUSTRIAL),
            new CollabData("INRA", "Tadla", "Agriculture, IA", "Stages de recherche", CollaborationScope.REGIONAL, CollaborationType.INDUSTRIAL),
            new CollabData("FST Marrakech", null, "Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL, CollaborationType.ACADEMIC),
            new CollabData("Ecole Mohammadia d'Ingenieurs", "Mohammed V University in Rabat", "Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FS El Jadida", null, "Physique des matériaux", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("ENSA Kenitra", null, "Matériaux, Informatique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FSSM Marrakech", null, "Informatique, Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FS Agadir", null, "Physique des matériaux", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FS Rabat", null, "Informatique, Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FST Béni Mellal", null, "Agroalimentaire, Biotechnologie microbienne", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FSO Oujda", null, "Agroalimentaire, Biotechnologie microbienne", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FST Mohammadia", null, "Agroalimentaire, Biotechnologie microbienne", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("EST Fes", null, "Biotechnologie, agroalimentaire, santé", "Recherche scientifique – Encadrement commun", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FMP Casablanca", null, "Chimie analytique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Université Savoie Mont Blanc", "Polytech Annecy Chambéry", "Informatique, Matériaux, Mécatronique, Mécanique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("King Fahd University of Petroleum and Minerals", "Saudi Arabia", "Matériaux, Mécanique", "Recherche scientifique", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Istanbul University Cerrahpasa", "Turkey", "Environnement, Physique, Matériaux", "Recherche scientifique", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("École supérieure d’ingénieurs en agroalimentaire de Bretagne", "France", "Agroalimentaire, Biotechnologie microbienne", "Recherche scientifique – Encadrement", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Université du Littoral-Côte d'Opale", "France", "Agroalimentaire, Informatique, Mécatronique", "Recherche scientifique – Encadrement", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Université de Nantes", "France", "Informatique, Génie civil, Calcul de structure", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Université Inonu", "Malatya, Turquie", "Génie alimentaire", "Recherche scientifique", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC)
    );

    public static final List<CollabData> COLLAB_LARESI = List.of(
            new CollabData("COSUMAR", "Oulad Ayyad - Beni Mellal", "Agro-alimentaire, Energie renouvelable, Mesures physiques", "Stages de recherche", CollaborationScope.REGIONAL,CollaborationType.INDUSTRIAL),
            new CollabData("OCP", "Khouribga", "Energie renouvelable, Informatique industrielle", "Stages de recherche", CollaborationScope.REGIONAL,CollaborationType.INDUSTRIAL),
            new CollabData("INRA", "Tadla", "Agriculture, IA", "Stages de recherche", CollaborationScope.REGIONAL,CollaborationType.INDUSTRIAL),
            new CollabData("FST Marrakech", null, "Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Ecole Mohammadia d'Ingenieurs", "Mohammed V University in Rabat", "Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FS El Jadida", null, "Physique des matériaux", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("ENSA Kenitra", null, "Matériaux, Informatique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FSSM Marrakech", null, "Informatique, Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FS Agadir", null, "Physique des matériaux", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("FS Rabat", null, "Informatique, Mécanique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("ENSA Khouribga", null, "Informatique, Génie électrique, énergétique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.NATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Université Savoie Mont Blanc", "Polytech Annecy Chambéry", "Informatique, Matériaux, Mécatronique, Mécanique", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("King Fahd University of Petroleum and Minerals", "Saudi Arabia", "Matériaux, Mécanique", "Recherche scientifique", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Istanbul University Cerrahpasa", "Turkey", "Environnement, Physique, Matériaux", "Recherche scientifique", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC),
            new CollabData("Université de Nantes", "France", "Informatique, Génie civil, Calcul de structure", "Recherche scientifique – Encadrement commun de thèses", CollaborationScope.INTERNATIONAL,CollaborationType.ACADEMIC)
    );

    public static final List<PublicationData> PUBLICATIONS = List.of(
            // LaRESI
            new PublicationData("LaRESI", "Building-integrated passive and renewable solar technologies: A review from 3E perspectives", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("Y. Elaouzy", "A. El Fadar", "O.B. Achkari"), "Sustainable Energy Technologies and Assessments", null, null, "104079"),
            new PublicationData("LaRESI", "Assessing the 3E performance of multiple energy supply scenarios based on photovoltaic, wind turbine, battery and hydrogen systems", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("Y. Elaouzy", "A. El Fadar", "O.B. Achkari"), "Journal of Energy Storage", null, null, "113378"),
            new PublicationData("LaRESI", "Concentrating and non-concentrating photovoltaic thermal collectors: Technologies, applications, exhaustive assessment and challenges", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("M. Mortadi", "A. El Fadar", "O.B. Achkari"), "Solar Energy Materials and Solar Cells", null, null, "112858"),
            new PublicationData("LaRESI", "4E analysis of photovoltaic thermal collector-based tri-generation system with adsorption cooling", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("M. Mortadi", "A. El Fadar", "O. Achkari Begdouri"), "Renewable Energy", null, null, "119828"),
            new PublicationData("LaRESI", "Capacity of waste heat recovery-based polygeneration to achieve sustainable development goals", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("M. Ghema", "A. El Fadar", "O. Achkari Begdouri"), "Science of The Total Environment", null, null, "171983"),
            new PublicationData("LaRESI", "Five Levels of Traffic Congestion Classification Using CCTV Images and Traffic Jam Network Approach", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("A. Esswidi", "A. Daif", "S. Ardchir", "Y. Elghoumari", "M. Azzouazi"), "International Journal on Technical and Physical Problems of Engineering", null, null, null),
            new PublicationData("LaRESI", "Pythagorean fuzzy sublattices and ideals", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("I. Bakhadach", "M. Fakhraoui", "S. Achik", "S. Melliani"), "Journal of Fuzzy Extension and Applications", null, null, "365-373"),
            new PublicationData("LaRESI", "An Enhanced Anonymous ECC-based Mutual Authentication and Key Agreement Scheme for Lightweight Application in TMIS", com.ensa.labs.recherche.bean.enums.PublicationType.CONFERENCE, 2023, List.of("H. Idrissi", "M. Ennahbaoui"), null, "CS2I-23", null, null),
            new PublicationData("LaRESI", "Improving peer assessment validity and reliability through a fuzzy coherence measure", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("El Alaoui M."), "IEEE Transactions on Learning Technologies", null, null, "892-899"),
            new PublicationData("LaRESI", "Predicting the next word using the Markov chain model according to profiling personality", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("Elbaghazaoui B.E.", "Mohamed Amnai", "Youssef Fakhri"), "The Journal of Supercomputing", null, "10.1007/s11227-023-05125-2", null),
            new PublicationData("LaRESI", "Predicting user behavior using data profiling and hidden Markov model", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("Elbaghazaoui B.E.", "Mohamed Amnai", "Youssef Fakhri"), "International Journal of Electrical & Computer Engineering", null, "10.11591/ijece.v13i5", "5444-5453"),
            new PublicationData("LaRESI", "Effectiveness of the use of nanofluids in concentrated solar power plants", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("O. Achkari Begdouri", "A. El Fadar"), "Renewable Energy Focus", null, null, "10-20"),

            // LRSTA
            new PublicationData("LRSTA", "A Cascaded H Bridge Multilevel Inverter with DC Cells Input Fault Tolerance Capability Based on PSC-PWM Control", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("T. Khalili", "A. Raihani", "A. Kadri", "S. Saghir", "M. Oulcaid"), "International Journal of Electrical and Electronics Research", null, null, null),
            new PublicationData("LRSTA", "Effect of sisal fibers on physical characteristics of compacted bentonite/lime/sand mixtures", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("M. Essaleh", "R. Bouferra", "M. Mansori", "S. Lahbabi", "S. Belhouideg"), "Euro-Mediterranean Journal for Environmental Integration", null, null, "1-14"),
            new PublicationData("LRSTA", "Single star SCIDAR: Atmospheric parameters profiling using the power spectrum of scintillation", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("Y. Errazzoukia", "A. Habib", "A. Jabiria", "M. Sabil", "Z. Benkhaldoun", "Y. El Azharia", "O. Azagrouzea", "J. Chafia"), "Astronomy and Computing", null, "10.1016/j.ascom.2024.100817", null),
            new PublicationData("LRSTA", "Optimization approach on volatile composition and sensory properties of Lben", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("S. Tsouli Sarhir", "A.A. Hayaloglu", "R. Belkhou", "A. Bouseta"), "Journal of Food Composition and Analysis", null, null, null),
            new PublicationData("LRSTA", "Strength and stiffness enhancement of a wind turbine blade using experimental fatigue test", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("O. Rajad", "H. Mounir", "M. Rich", "S. Belhouideg", "C. Haidar", "A. El Kasri"), "International Journal on Interactive Design and Manufacturing", null, null, "149-158"),
            new PublicationData("LRSTA", "Mixture design for new starter formulation for Lben", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("S. Tsouli Sarhir", "A.A. Hayaloglu", "R. Belkhou", "A. Bouseta"), "International Dairy Journal", null, null, null),
            new PublicationData("LRSTA", "Flow and heat transfer performance in TPMS heat exchangers", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("I. El Khadiri", "M. Abouelmajd", "M. Zemzami", "N. Hmina", "M. Lagache", "S. Belhouideg"), "International Communications in Heat and Mass Transfer", null, null, "107617"),
            new PublicationData("LRSTA", "Relative humidity absorption in clay-kaolin materials reinforced with Alfa fibers", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("R. Bouferra", "A. Bouchehma", "Y. Bahammou", "M. Essaleh", "S. Belhouideg", "A. Lamharrar", "A. Idlimam"), "International Communications in Heat and Mass Transfer", null, null, "107416"),
            new PublicationData("LRSTA", "Lattice Boltzmann analysis of mixed convection in a cavity with elliptical block", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("A. Daiz", "A. Bahlaoui", "I. Arroub", "S. Belhouideg", "A. Raji", "M. Hasnaoui"), "Computational Thermal Sciences", null, null, null),
            new PublicationData("LRSTA", "Mediterranean fruit fly management strategies: a review", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("A. Hallouti", "M. Ben El Caid", "H. Boubaker"), "International Journal of Pest Management", null, null, "1-13"),
            new PublicationData("LRSTA", "Household food wastes in Morocco: drivers and policy integration", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("A. Zoubi", "A. Hallouti", "M. El Mderssa", "H. Lionboui", "A. Boulli", "Y. Abbas"), "Euro-Mediterranean Journal for Environmental Integration", null, null, "1-15"),
            new PublicationData("LRSTA", "Impact of probiotic Lactiplantibacillus plantarum on fermented orange juice", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("H. Abouloifa", "S. Gaamouche", "N. Ghabbour", "I. Hasnaoui", "S. Moumnassi", "N. Bentouhami", "M. Yahyaoui"), "Journal of Food Measurement and Characterization", null, null, "7044-7051"),
            new PublicationData("LRSTA", "Improving citric acid production using Aspergillus Niger", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2024, List.of("R. Bellaouchi", "I. Hasnaoui", "M. Yahyaoui", "N. Bentouhami"), "Food Science & Nutrition", null, null, "4248-4258"),
            new PublicationData("LRSTA", "Enhancing prediction of mechanical properties of 3D-printed parts using machine learning", com.ensa.labs.recherche.bean.enums.PublicationType.CONFERENCE, 2024, List.of("M. Abouelmajd", "H. Reddad", "I. El Khadiri", "I. Ezzaraa"), null, "ICDS 2024", null, "1-7"),
            new PublicationData("LRSTA", "Finite element analysis of TPMS lattice structures", com.ensa.labs.recherche.bean.enums.PublicationType.BOOK, 2024, List.of("M. Abouelmajd", "I. El Khadiri", "I. Ezzaraa", "M. Zemzami"), null, null, null, null),
            new PublicationData("LRSTA", "Evaluation of lactic acid bacteria for Moroccan fermented milk", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("S. Tsouli Sarhir", "A.A. Hayaloglu", "R. Belkhou", "A. Bouseta"), "International Dairy Journal", null, null, null),
            new PublicationData("LRSTA", "Structural and thermal properties of compacted bentonite material", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("M. Essaleh", "R. Bouferra", "Y. Chihab", "M. Mansori", "A. Bouchehma"), "Chemistry Africa", null, "10.1007/s42250-023-00693-5", null),
            new PublicationData("LRSTA", "Mixed convective cooling using nanofluids in a vented cavity", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("I. Arroub", "A. Bahlaoui", "S. Belhouideg", "A. Raji", "M. Hasnaoui"), "Physical Chemistry Research", null, null, "631-642"),
            new PublicationData("LRSTA", "Enhancement of photovoltaic performance of amorphous silicon solar cells", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("A. Hmairrou", "E. Chahid", "M. Azza", "R. Abdia", "S. Belhouideg"), "Biointerface Research in Applied Chemistry", null, null, null),
            new PublicationData("LRSTA", "Solar cell photocurrent comparison using Arduino and machine learning", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("E. Chahid", "E. Eljaoui", "M. Driouch", "S. Belhouideg"), "International Journal of Computer Engineering and Data Science", null, null, null),
            new PublicationData("LRSTA", "Controlled fermentation of Moroccan Picholine green olives", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("N. Ghabbour", "Y. Rokni", "H. Abouloifa", "R. Bellaouchi", "I. Hasnaoui"), "Grasas y Aceites", null, null, null),
            new PublicationData("LRSTA", "Antimicrobial activity of lactiplantibacillus strains for food biopreservation", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("I. Hasnaoui", "H. Abouloifa", "Y. Rokni"), "Journal of Microbiology, Biotechnology and Food Sciences", null, null, null),
            new PublicationData("LRSTA", "Numerical modeling of 3D-printed wood-PLA composites", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("I. Ezzaraa", "N. Ayrilmis", "M. Abouelmajd"), "Forests", null, "10.3390/f14010095", null),
            new PublicationData("LRSTA", "Wind speed prediction using deep learning models", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2023, List.of("I. Tyass", "T. Khalili", "R. Mohamed"), "International Journal of Renewable Energy Development", null, null, null),
            new PublicationData("LRSTA", "Potent odorants and sensory characteristics of Jben cheese", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("S. Tsouli Sarhir", "A. Amanpour", "A. Bouseta"), "Flavour and Fragrance Journal", null, null, null),
            new PublicationData("LRSTA", "Micromechanical models for 3D printed wood composites", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("I. Ezzaraa", "N. Ayrilmis", "M. Kuzman"), "Mechanics of Advanced Materials and Structures", null, null, null),
            new PublicationData("LRSTA", "Mechanical behavior of TPMS structures using additive manufacturing", com.ensa.labs.recherche.bean.enums.PublicationType.CONFERENCE, 2022, List.of("M. Abouelmajd", "I. El Khadiri", "I. Ezzaraa"), null, "ICOA 2022", null, null),
            new PublicationData("LRSTA", "Islanded microgrids with distributed delay observer design", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("L. Ammeh", "H. El Fadil", "F. Giri", "M. Oulcaid"), "IFAC-PapersOnLine", null, null, null),
            new PublicationData("LRSTA", "Electrical characterization of bentonite material", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("M. Essaleh", "R. Bouferra", "S. Belhouideg"), "EUREKA: Physics and Engineering", null, null, null),
            new PublicationData("LRSTA", "Impedance spectroscopy of clay materials", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("M. Essaleh", "R. Bouferra", "M. Mansori"), "International Journal of Mechanical Engineering", null, null, null),
            new PublicationData("LRSTA", "Electrical conduction mechanisms in ceramic materials", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("M. Essaleh", "S. Amhil", "R. Bouferra"), "Physics Letters A", null, null, null),
            new PublicationData("LRSTA", "Heat transfer in porous media with nanoparticles", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("S. Mohamed", "K. Gueraoui", "M. Driouich"), "Journal of Porous Media", null, null, null),
            new PublicationData("LRSTA", "Weeds detection using convolutional neural networks", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("A. Tannouche", "A. Gaga", "M. Boutalline"), "International Journal of Electrical and Computer Engineering", null, null, null),
            new PublicationData("LRSTA", "Antimicrobial compounds from Lactiplantibacillus plantarum", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("H. Abouloifa", "Y. Rokni", "I. Hasnaoui"), "Brazilian Journal of Microbiology", null, null, null),
            new PublicationData("LRSTA", "Antifungal activity of lactic acid bacteria in food preservation", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("H. Abouloifa", "I. Hasnaoui", "Y. Rokni"), "Advances in Applied Microbiology", null, null, null),
            new PublicationData("LRSTA", "Bio-preservation of poultry meat using probiotic strains", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2022, List.of("H. Abouloifa", "S. Gaamouche", "Y. Rokni"), "Current Microbiology", null, null, null),
            new PublicationData("LRSTA", "Fusarium isolates for biological control of Mediterranean fruit fly", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2021, List.of("A. Hallouti", "M. Ait Hamza", "A. Zahidi"), "Entomologia Experimentalis et Applicata", null, null, null),
            new PublicationData("LRSTA", "Evaluation of entomopathogenic fungi for pest control", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2021, List.of("A. Hallouti", "H. Benjlil", "A. El Hamdaoui"), "Sumerianz Journal of Biotechnology", null, null, null),
            new PublicationData("LRSTA", "Aroma compounds analysis in fermented butter (Smen)", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2021, List.of("S. Tsouli Sarhir", "A. Amanpour", "A. Bouseta"), "Journal of Food Composition and Analysis", null, null, null),
            new PublicationData("LRSTA", "Multilevel inverter with battery state of charge adjustment", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2021, List.of("M. Rafik", "T. Khalili", "O. Bouamrane"), "Journal of Theoretical and Applied Information Technology", null, null, null),
            new PublicationData("LRSTA", "Optimizing wind farm layout to minimize wake losses", com.ensa.labs.recherche.bean.enums.PublicationType.JOURNAL, 2021, List.of("A. Bellat", "K. Mansouri", "A. Raihani"), "Advances in Science, Technology and Engineering Systems", null, null, null)
    );

    public static final List<PublicationData> COMMUNICATIONS = List.of(
            new PublicationData("LaRESI", "Traffic congestion multilevel classification using deep learning", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2023, List.of("A. Esswidi", "K. Bayoude", "S. Ardchir", "Y. M. Elghoumari", "A. Daif", "M. Azzouazi"), null, "ICRAMCS 2023 - International Conference on Research in Applied Mathematics and Computer Science, FSBM Casablanca", null, null),
            new PublicationData("LaRESI", "Road users detection for traffic congestion classification", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("A. Es-swidi", "S. Ardchir", "A. Daif", "M. Azzouazi"), null, "TIM 2022 - International Conference of the Technologies of Information and Modelling, FSBM Casablanca", null, null),
            new PublicationData("LaRESI", "Traffic congestion and road anomalies detection using CCTV images processing: challenges and opportunities", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("A. Es-swidi", "S. Ardchir", "Y. Elghoumari", "A. Daif", "M. Azouazi"), null, "AI2SD 2022 - International Conference on Advanced Intelligent Systems for Sustainable Development, ENSAM Rabat", null, null),
            new PublicationData("LaRESI", "Road safety analysis: severity prediction and important factors of accidents", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("A. Esswidi", "K. Bayoude", "S. Ardchir", "Y. M. Elghoumari", "A. Daif", "M. Azzouazi"), null, "ICRAMCS 2022 - International Conference on Research in Applied Mathematics and Computer Science, FSBM Casablanca", null, null),
            new PublicationData("LaRESI", "Predictive analytics in email marketing based on machine learning tools", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("K. Bayoude", "A. Esswidi", "S. Ardchir", "M. Azzouazi"), null, "ICRAMCS 2022 - International Conference on Research in Applied Mathematics and Computer Science, FSBM Casablanca", null, null),

            new PublicationData("LRSTA", "Numerical homogenization to investigate the effective elastic properties of 3D-printed wood polymer composites", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2024, List.of("Ismail Ezzaraa", "Mouhcine Bakhaddache", "Mohamed Abouelmajd", "Jamaa Bengourram", "Soufiane Belhouideg"), null, "4th International Workshop on Advanced Computation Methods in Multiphysics, ENSEM Casablanca", null, null),
            new PublicationData("LRSTA", "Analytical homogenization modeling to investigate the mechanical behavior of clay/alfa short fiber composites", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2023, List.of("Ismail Ezzaraa", "Abdeltif Bouchehma", "Mohamed Abouelmajd", "Mohamed Essaleh", "Yassine Chihab", "Rachid Bouferra", "Ahmed Bahlaoui", "Ismail Arroub", "Jamaa Bengourram", "Soufiane Belhouideg"), null, "International Conference on Connected Innovation and Technology X.0, Rabat", null, null),
            new PublicationData("LRSTA", "Global MPP tracking under partial shading conditions using LandS algorithm: hardware implementation", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2023, List.of("M. Oulcaid", "E. M. Acim", "N. Hmina", "H. El Fadil", "A. Rachid"), null, "International Symposium on Automatic Control and Emerging Technologies", null, "348-361"),
            new PublicationData("LRSTA", "PEM fuel cell parameters identification based on Grey Wolf optimization algorithm", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2023, List.of("A. Rachid", "E. Saidi", "I. Mortabit", "N. Errifai", "H. E. Fadil", "M. Oulcaid"), null, "International Symposium on Automatic Control and Emerging Technologies", null, "155-166"),
            new PublicationData("LRSTA", "Nanoparticles shape effect on heat transfer by natural convection of nanofluid in a vertical porous cylindrical enclosure", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2023, List.of("Y. Foukhari", "M. Sammouda", "M. Driouich", "S. Belhouideg"), null, "International Conference on Partial Differential Equations and Applications", null, "437-445"),
            new PublicationData("LRSTA", "Comparative study on elastic properties of 3D printed continuous natural fiber reinforced polymer composites", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("Ismail Ezzaraa", "Nadir Ayrilmis", "Soufiane Belhouideg", "Jamaa Bengourram"), null, "Cukurova International Scientific Researches Conference, Turkey", null, null),
            new PublicationData("LRSTA", "TPMS lattice structure derived using topology optimization for additive manufacturing", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("I. El Khadiri", "M. Abouelmajd", "M. Zemzami", "N. Hmina", "M. Lagache", "B. AlMangour", "S. Belhouideg"), null, "ICOA 2022 - International Conference on Optimization and Applications", null, "1-4"),
            new PublicationData("LRSTA", "Topology optimization of 3D beam structures obtained by additive manufacturing", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2021, List.of("I. El Khadiri", "M. Zemzami", "N. Hmina", "M. Lagache", "S. Belhouideg"), null, "ICOA 2021 - International Conference on Optimization and Applications", null, "1-4"),
            new PublicationData("LRSTA", "Artificial intelligence and machine learning applications in material science", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("S. Samine", "M. Zemzami", "N. Hmina", "M. Lagache", "S. Belhouideg"), null, "ICOA 2022 - International Conference on Optimization and Applications", null, "1-5"),
            new PublicationData("LRSTA", "Heat transfer performance in a tilted cavity with nanofluid flow", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2021, List.of("I. Arroub", "A. Bahlaoui", "S. Belhouideg", "A. Raji", "M. Hasnaoui"), null, "AIP Conference Proceedings", null, null),
            new PublicationData("LRSTA", "Mechanical behavior of TPMS-based structures obtained by additive manufacturing", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("M. Abouelmajd", "I. El Khadiri", "I. Ezzaraa", "M. Zemzami", "M. El Afi", "M. Lagache", "S. Belhouideg"), null, "ICOA 2022 - International Conference on Optimization and Applications", null, "1-5"),
            new PublicationData("LRSTA", "Mechanical properties of 3D printed natural fiber reinforced polymer composites: a short review", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2021, List.of("Ismail Ezzaraa", "Nadir Ayrilmis", "Jamaa Bengourram", "Soufiane Belhouideg"), null, "ICASEM 2021 - International Conference on Applied Sciences, Engineering and Mathematics", null, null),
            new PublicationData("LRSTA", "Dielectric spectroscopy of n-type Cu5In9Se16 semiconductor compound", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2021, List.of("A. Bouchehma", "L. Essaleh", "G. Marín", "M. Essaleh", "S. M. Wasim", "S. Amhil", "S. Belhouideg"), "Physica B: Condensed Matter", null, null, "413356"),
            new PublicationData("LRSTA", "Determination of the geographical origins of olive mill wastewater using FT-MIR spectroscopy", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2023, List.of("F. Sbai El Otmani", "A. El Orche", "M. Mouhsin", "M. Kasbaji", "A. Ait Oumghar", "A. Hirri", "M. Oubenali", "M. Mbarki"), null, "JOUMAT 2023 - Journées des Matériaux, FST Béni Mellal", null, null),
            new PublicationData("LRSTA", "Detection and quantification of olive oil adulteration using MIR spectroscopy and chemometrics", com.ensa.labs.recherche.bean.enums.PublicationType.COMMUNICATION, 2022, List.of("A. Hirri", "H. Bouchafra", "L. Zarayby", "I. Sbai El Otmani"), null, "Workshop Technique de la Métrologie, FST Béni Mellal", null, null)
    );

    public static final List<ThesisData> THESES = List.of(
            new ThesisData("LaRESI", "Ouassam Elhoucine", "Méthodes heuristiques et intelligence artificielle : application aux systèmes complexes", java.time.LocalDate.parse("2023-12-30"), "Pr. Belaid Bouikhalene"),
            new ThesisData("LaRESI", "Radoine Hamzaoui", "Amélioration des techniques d’évaluation des apprentissages dans les plateformes e-learning en utilisant les algorithmes de l’intelligence artificielle", java.time.LocalDate.parse("2025-02-07"), "Pr. Belaid Bouikhalene"),
            new ThesisData("LaRESI", "Loubna Rabhi", "Approche holistique basée sur l’intelligence artificielle et big data analytics pour la modélisation de l’agriculture digitale", java.time.LocalDate.parse("2024-07-10"), "Pr. Belaid Bouikhalene"),
            new ThesisData("LaRESI", "Saadia Nemmaoui", "Sécurité des données de la vie privée face aux enjeux des nouvelles technologies de communication", java.time.LocalDate.parse("2025-05-24"), "Pr. Belaid Bouikhalene"),
            new ThesisData("LRSTA", "Ismail Ezzaraa", "Modélisation micromécanique et numérique des structures biocomposites obtenues par impression 3D", java.time.LocalDate.parse("2023-10-21"), "Pr. Soufiane Belhouideg; Pr. Jamaa Bengourram"),
            new ThesisData("LRSTA", "Abdeltif Bouchehma", "Étude mécanique, thermique, hygroscopique et électrique de l’argile kaolin renforcé par la fibre alfa pour une utilisation dans le domaine du génie civil", java.time.LocalDate.parse("2023-12-02"), "Pr. Soufiane Belhouideg; Pr. Rachid Bouferra"),
            new ThesisData("LRSTA", "Mohamed Abouelmajd", "Développement de structures multifonctionnelles par la technologie d’impression 3D pour la surveillance de la santé structurelle", java.time.LocalDate.parse("2025-04-26"), "Pr. Soufiane Belhouideg; Pr. Ahmed Bahlaoui"),
            new ThesisData("LRSTA", "Mohamed Essaleh", "Étude des propriétés mécaniques, thermiques et électriques des matériaux composites à base de l'argile bentonite pour une utilisation dans le domaine du génie civil", java.time.LocalDate.parse("2025-04-19"), "Pr. Rachid Bouferra; Pr. Soufiane Belhouideg"),
            new ThesisData("LRSTA", "Issam El Khadiri", "Optimisation topologique des structures obtenues par fabrication additive", java.time.LocalDate.parse("2025-07-28"), "Pr. Nabil Hmina; Pr. Soufiane Belhouideg")
    );
}
