package com.BUS.DayFrame.Repository;

import com.BUS.DayFrame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //이메일이 중복되어있는지 확인용
    boolean existsByEmail(String email);
}
