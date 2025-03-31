package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.User;
import com.BUS.DayFrame.domain.WidgetBoard;
import com.BUS.DayFrame.dto.request.WidgetBoardCreateDTO;
import com.BUS.DayFrame.dto.request.WidgetBoardUpdateDTO;
import com.BUS.DayFrame.dto.response.WidgetBoardResponseDTO;
import com.BUS.DayFrame.repository.UserJpaRepository;
import com.BUS.DayFrame.repository.WidgetBoardJpaRepository;
import com.BUS.DayFrame.repository.WidgetJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WidgetBoardService {
    @Autowired
    private WidgetBoardJpaRepository widgetBoardJpaRepository;
    @Autowired
    private WidgetJpaRepository widgetJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Transactional
    public WidgetBoardResponseDTO saveWidgetBoard(Long userId, WidgetBoardCreateDTO widgetBoardCreateDTO) {
        WidgetBoard savedBoard = widgetBoardJpaRepository.save(new WidgetBoard(
                userId,
                widgetBoardCreateDTO.getBoardType(),
                widgetBoardCreateDTO.getBoardName()));

        return WidgetBoardResponseDTO.fromEntity(savedBoard);
    }

    public List<WidgetBoardResponseDTO> findWidgetBoardsByUserId(Long userId) {
        List<WidgetBoard> widgetBoards = widgetBoardJpaRepository.findByUserId(userId);
        return WidgetBoardResponseDTO.fromEntityList(widgetBoards);
    }

    @Transactional
    public WidgetBoardResponseDTO updateWidgetBoard(Long userId, Long boardId, WidgetBoardUpdateDTO widgetBoardUpdateDTO) {
        WidgetBoard widgetBoard = widgetBoardJpaRepository.findById(boardId)
                .orElseThrow(()-> new EntityNotFoundException("id: "+boardId+" 에 해당하는 widgetBoard를 찾을 수 없음."));
        if(!userId.equals(widgetBoard.getUserId())){
            throw new RuntimeException("사용자 소유의 보드가 아닙니다");
        }
        widgetBoard.updateWidgetBoard(widgetBoardUpdateDTO.getBoardName());
        return WidgetBoardResponseDTO.fromEntity(widgetBoard);
    }

    @Transactional
    public void deleteWidgetBoard(Long userId, Long boardId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("id: "+userId+" 에 해당하는 user를 잧을 수 없음."));
        widgetJpaRepository.deleteByWidgetBoardId(boardId);
        widgetBoardJpaRepository.deleteById(boardId);
    }
}
