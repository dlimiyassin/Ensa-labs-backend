package com.ensa.labs.zBase.transformer;

import java.util.Collections;
import java.util.List;

public abstract class AbstractTransformer<E, D> {

    public abstract E toEntity(D dto);

    public abstract D toDto(E entity);

    public List<E> toEntity(List<D> dtos) {
        if (dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toEntity).toList();
    }

    public List<D> toDto(List<E> entities) {
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).toList();
    }

}
