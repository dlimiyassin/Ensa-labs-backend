package com.ensa.labs.recherche.ws;

/**
 * {@code @PreAuthorize} expressions shared by the controllers. They only check the role;
 * lab / team ownership is enforced by {@link com.ensa.labs.recherche.service.AccessControlService}.
 */
public final class Authorities {
    public static final String SUPER_ADMIN = "hasRole('SUPER_ADMIN')";
    /** Lab structure: teams, members, collaborations, resources. */
    public static final String LAB_MANAGERS = "hasAnyRole('SUPER_ADMIN','LAB_DIRECTOR')";
    /** Scientific production of a lab: productions and theses. */
    public static final String PRODUCTION_EDITORS = "hasAnyRole('SUPER_ADMIN','LAB_DIRECTOR','TEAM_LEADER')";
    /** Publications: every role may create; updates are limited by ownership. */
    public static final String PUBLICATION_AUTHORS = "hasAnyRole('SUPER_ADMIN','LAB_DIRECTOR','TEAM_LEADER','RESEARCHER')";

    private Authorities() {
    }
}
