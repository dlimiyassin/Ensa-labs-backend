package com.ensa.labs.zBase.db.data;

import java.time.LocalDate;

public record ThesisData(
        String labAcronym,
        String author,
        String title,
        LocalDate defenseDate,
        String supervisor
) {}
