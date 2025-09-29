package com.BUS.DayFrame.dto.response;

import com.BUS.DayFrame.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class TagResponseDTO {
    private Long tagId;
    private String name;
    private String color;

    public static TagResponseDTO fromEntity(Tag tag) {
        return new TagResponseDTO(
              tag.getId(),
              tag.getName(),
              tag.getColor()
        );
    }

    public static List<TagResponseDTO> fromEntityList(List<Tag> tags) {
        List<TagResponseDTO> tagResponseDTOList = new ArrayList<>();
        for(Tag tag: tags){
            tagResponseDTOList.add(TagResponseDTO.fromEntity(tag));
        }
        return tagResponseDTOList;
    }
}
