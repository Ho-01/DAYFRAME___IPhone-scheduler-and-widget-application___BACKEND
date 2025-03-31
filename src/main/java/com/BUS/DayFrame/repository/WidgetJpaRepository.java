package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WidgetJpaRepository extends JpaRepository<Widget, Long> {
    @Modifying
    @Query("DELETE from Widget w WHERE w.boardId = :boardId")
    void deleteByWidgetBoardId(@Param("boardId") Long boardId);

    List<Widget> findByBoardId(Long boardId);
}
