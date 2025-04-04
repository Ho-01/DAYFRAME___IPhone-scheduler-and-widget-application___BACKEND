package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.ScheduleTag;
import com.BUS.DayFrame.domain.Tag;
import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.dto.request.TagRequestDTO;
import com.BUS.DayFrame.dto.response.TagResponseDTO;
import com.BUS.DayFrame.repository.ScheduleTagJpaRepository;
import com.BUS.DayFrame.repository.TagJpaRepository;
import com.BUS.DayFrame.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagJpaRepository tagJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ScheduleTagJpaRepository scheduleTagJpaRepository;

    @Transactional
    public TagResponseDTO saveTag(Long userId, TagRequestDTO tagRequestDTO) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        Tag tag = new Tag(
                user,
                tagRequestDTO.getName(),
                tagRequestDTO.getColor()
        );
        user.getTags().add(tag);
        return TagResponseDTO.fromEntity(tagJpaRepository.save(tag));
    }

    public List<TagResponseDTO> findMyTags(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        return TagResponseDTO.fromEntityList(user.getTags());
    }

    @Transactional
    public TagResponseDTO updateTag(Long userId, Long tagId, TagRequestDTO tagRequestDTO) {
        Tag tag = tagJpaRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+tagId+" 에 해당하는 tag를 잧을 수 없음."));
        if(!userId.equals(tag.getUser().getId())){
            throw new IllegalArgumentException("id: "+tagId+"에 해당하는 tag는 사용자 소유가 아닙니다");
        }
        tag.updateTag(tagRequestDTO.getName(), tagRequestDTO.getColor());
        return TagResponseDTO.fromEntity(tag);
    }

    @Transactional
    public String deleteTag(Long userId, Long tagId) {
        Tag tag = tagJpaRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("id: "+tagId+" 에 해당하는 tag를 잧을 수 없음."));
        if(!userId.equals(tag.getUser().getId())){
            throw new IllegalArgumentException("id: "+tagId+"에 해당하는 tag는 사용자 소유가 아닙니다");
        }
        List<ScheduleTag> scheduleTagList = scheduleTagJpaRepository.findByTag(tag);
        for(ScheduleTag scheduleTag: scheduleTagList){
            scheduleTag.removeRelationship();
            scheduleTagJpaRepository.delete(scheduleTag);
        }
        tag.getUser().getTags().remove(tag);
        tagJpaRepository.delete(tag);
        return "태그 삭제 성공";
    }
}
