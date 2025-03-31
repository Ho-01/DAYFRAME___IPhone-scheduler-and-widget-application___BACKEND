package com.BUS.DayFrame.service;

import com.BUS.DayFrame.domain.Widget;
import com.BUS.DayFrame.domain.WidgetBoard;
import com.BUS.DayFrame.dto.request.WidgetCreateDTO;
import com.BUS.DayFrame.dto.request.WidgetUpdateDTO;
import com.BUS.DayFrame.dto.response.WidgetResponseDTO;
import com.BUS.DayFrame.repository.WidgetBoardJpaRepository;
import com.BUS.DayFrame.repository.WidgetJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WidgetService {
    @Autowired
    private WidgetJpaRepository widgetJpaRepository;
    @Autowired
    private WidgetBoardJpaRepository widgetBoardJpaRepository;

    @Transactional
    public WidgetResponseDTO saveWidget(Long userId, Long boardId, WidgetCreateDTO widgetCreateDTO) {
        WidgetBoard widgetBoard = widgetBoardJpaRepository.findById(boardId)
                .orElseThrow(()-> new EntityNotFoundException("id: "+boardId+" 에 해당하는 widgetBoard를 찾을 수 없음."));
        if(!userId.equals(widgetBoard.getUserId())){
            throw new RuntimeException("사용자 소유의 보드가 아닙니다");
        }
        widgetCreateDTO.getStyleConfig().validate();

        Widget savedWidget = widgetJpaRepository.save(new Widget(
                boardId,
                widgetCreateDTO.getWidgetType(),
                widgetCreateDTO.getX(),
                widgetCreateDTO.getY(),
                widgetCreateDTO.getWidth(),
                widgetCreateDTO.getHeight(),
                widgetCreateDTO.getStyleConfig()
        ));
        return WidgetResponseDTO.fromEntity(savedWidget);
    }

    public List<WidgetResponseDTO> findWidgetsByBoardId(Long userId, Long boardId) {
        WidgetBoard widgetBoard = widgetBoardJpaRepository.findById(boardId)
                .orElseThrow(()-> new EntityNotFoundException("id: "+boardId+" 에 해당하는 widgetBoard를 찾을 수 없음."));
        if(!userId.equals(widgetBoard.getUserId())){
            throw new RuntimeException("사용자 소유의 위젯보드가 아닙니다");
        }
        List<Widget> widgetList = widgetJpaRepository.findByBoardId(boardId);
        return WidgetResponseDTO.fromEntityList(widgetList);
    }

    @Transactional
    public WidgetResponseDTO updateWidget(Long userId, Long widgetId, WidgetUpdateDTO widgetUpdateDTO) {
        Widget widget = widgetJpaRepository.findById(widgetId)
                .orElseThrow(()-> new EntityNotFoundException("id: "+widgetId+" 에 해당하는 widget을 찾을 수 없음."));
        WidgetBoard widgetBoard = widgetBoardJpaRepository.findById(widget.getBoardId())
                .orElseThrow(()-> new EntityNotFoundException("id: "+widget.getBoardId()+" 에 해당하는 widgetBoard를 찾을 수 없음."));
        if(!userId.equals(widgetBoard.getUserId())){
            throw new RuntimeException("사용자 소유의 위젯이 아닙니다");
        }
        widgetUpdateDTO.getStyleConfig().validate();

        widget.updateWidget(
                widgetUpdateDTO.getX(),
                widgetUpdateDTO.getY(),
                widgetUpdateDTO.getWidth(),
                widgetUpdateDTO.getHeight(),
                widgetUpdateDTO.getStyleConfig()
        );
        return WidgetResponseDTO.fromEntity(widget);
    }

    @Transactional
    public void deleteWidget(Long userId, Long widgetId) {
        Widget widget = widgetJpaRepository.findById(widgetId)
                .orElseThrow(()-> new EntityNotFoundException("id: "+widgetId+" 에 해당하는 widget을 찾을 수 없음."));
        WidgetBoard widgetBoard = widgetBoardJpaRepository.findById(widget.getBoardId())
                .orElseThrow(()-> new EntityNotFoundException("id: "+widget.getBoardId()+" 에 해당하는 widgetBoard를 찾을 수 없음."));
        if(!userId.equals(widgetBoard.getUserId())){
            throw new RuntimeException("사용자 소유의 위젯이 아닙니다");
        }
        widgetJpaRepository.deleteById(widgetId);
    }


}
