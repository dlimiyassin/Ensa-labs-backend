package com.ensa.labs.zBase.security.constant;

import java.util.List;

/**
 * The only authorization roles of the platform (stored with the Spring "ROLE_" prefix).
 * Business meaning inside a lab stays on {@code Member.roleInLab}.
 */
public final class Roles {
    public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String LAB_DIRECTOR = "ROLE_LAB_DIRECTOR";
    public static final String TEAM_LEADER = "ROLE_TEAM_LEADER";
    public static final String RESEARCHER = "ROLE_RESEARCHER";

    public static final List<String> ALL = List.of(SUPER_ADMIN, LAB_DIRECTOR, TEAM_LEADER, RESEARCHER);

    private Roles() {
    }
}
