package com.BUS.DayFrame.controller;

import com.BUS.DayFrame.dto.request.TagRequestDTO;
import com.BUS.DayFrame.dto.response.ApiResponseDTO;
import com.BUS.DayFrame.dto.response.TagResponseDTO;
import com.BUS.DayFrame.security.service.CustomUserDetails;
import com.BUS.DayFrame.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping("/tags")
    public ApiResponseDTO<TagResponseDTO> createTag(
            @RequestBody TagRequestDTO tagRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(tagService.saveTag(userDetails.getUserId(), tagRequestDTO));
    }

    @GetMapping("/tags/mine")
    public ApiResponseDTO<List<TagResponseDTO>> getMyTags(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ApiResponseDTO.success(tagService.findMyTags(userDetails.getUserId()));
    }

    @PutMapping("/tags/{tagId}")
    public ApiResponseDTO<TagResponseDTO> updateTag(
            @PathVariable("tagId") Long tagId,
            @RequestBody TagRequestDTO tagRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(tagService.updateTag(userDetails.getUserId(), tagId, tagRequestDTO));
    }

    @DeleteMapping("/tags/{tagId}")
    public ApiResponseDTO<String> deleteTag(
            @PathVariable("tagId") Long tagId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ApiResponseDTO.success(tagService.deleteTag(userDetails.getUserId(), tagId));
    }
}
