package com.ensa.labs.recherche.mapper;

import com.ensa.labs.recherche.bean.Tag;
import com.ensa.labs.recherche.dto.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagDTO toDto(Tag tag) {
        return new TagDTO(tag.getId(), tag.getName());
    }
}
