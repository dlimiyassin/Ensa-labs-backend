package com.ensa.labs.zBase.db.data;

import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;

import java.util.List;

public class LabData {

    public static final String TITLE_FR =
            "Laboratoire de Recherche en Sciences de l'Ingénieur et Innovation (LaRSII)";
    public static final String TITLE_EN =
            "Laboratory of Research in Engineering Sciences and Innovation (LaRESI)";
    public static final String ACRONYM = "LaRESI";

    // ================= MEMBERS =================
    public static final List<MemberData> MAIN_MEMBERS = List.of(
            new MemberData("Hidki", "Rachid", "MCA", "Mécanique et énergétique", "ENSA BM", MemberRoleInLab.MEMBER),
            new MemberData("Hassoune", "Abdelilah", "MCA", "Génie Electrique", "ENSA BM",MemberRoleInLab.MEMBER),
            new MemberData("Allaoui", "Rabha", "PES", "Informatique", "ENSA BM",MemberRoleInLab.DIRECTEUR),
            new MemberData("El Alaoui", "Mohamed", "MCA", "Génie Industriel", "ENSA BM",MemberRoleInLab.DIRECTEUR_ADJOINT)
    );

    // ================= RESEARCH =================
    public static final List<String> DOMAINES = List.of(
            "Mathématiques – Informatique et Applications",
            "Physique et Applications"
    );

    public static final List<String> AXES = List.of(
            "Énergie et efficacité énergétique",
            "Intelligence artificielle et science des données"
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

    // ================= EQUIPMENTS =================
    public static final List<String> EQUIP_LAB = List.of(
            // empty in original data
    );

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

    // ================= COLLABORATIONS =================
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

    // ================= PUBLICATIONS =================
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

    // ================= THESES =================
    public static final List<ThesisData> THESES = List.of(
            new ThesisData(
                    "Ouassam Elhoucine",
                    "Méthodes Heuristiques et Intelligence Artificielle",
                    "Pr. Belaid Bouikhalene"
            )
    );
}