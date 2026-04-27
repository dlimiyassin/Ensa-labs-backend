package com.ensa.labs.zBase.db.data;

import com.ensa.labs.recherche.bean.enums.PublicationType;

import java.util.List;

public record PublicationData(
        String labAcronym,
        String title,
        PublicationType type,
        Integer year,
        List<String> authors,
        String journal,
        String conference,
        String doi,
        String pages
) {}
