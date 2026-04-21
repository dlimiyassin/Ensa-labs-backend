package com.ensa.labs.zBase.db.data;

import java.util.List;

public record PublicationData(
        String title,
        Integer year,
        List<String> authors,
        String journal,
        String doi
) {}
