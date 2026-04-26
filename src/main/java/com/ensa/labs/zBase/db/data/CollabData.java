package com.ensa.labs.zBase.db.data;

import com.ensa.labs.recherche.bean.enums.CollaborationScope;

public record CollabData(
        String organization,
        String establishment,
        String theme,
        String nature,
        CollaborationScope scope
) {}
