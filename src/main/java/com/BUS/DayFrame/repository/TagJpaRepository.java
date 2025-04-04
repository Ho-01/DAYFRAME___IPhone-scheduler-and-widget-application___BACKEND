package com.BUS.DayFrame.repository;

import com.BUS.DayFrame.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
}
