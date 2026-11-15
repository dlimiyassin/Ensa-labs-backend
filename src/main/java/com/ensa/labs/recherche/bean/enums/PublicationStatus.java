package com.ensa.labs.recherche.bean.enums;

/**
 * Review state of a publication / communication. Only {@link #VALIDATED} records are shown on
 * the public website; a researcher's submission waits in {@link #PENDING} until a team leader or
 * the lab director approves or rejects it.
 */
public enum PublicationStatus {
    PENDING,
    VALIDATED,
    REJECTED
}
