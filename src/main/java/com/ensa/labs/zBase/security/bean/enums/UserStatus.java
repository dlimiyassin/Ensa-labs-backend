package com.ensa.labs.zBase.security.bean.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIF("Actif"),
    EN_ATTENTE("En attente"),
    BLOQUE("Bloqué");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }
}
