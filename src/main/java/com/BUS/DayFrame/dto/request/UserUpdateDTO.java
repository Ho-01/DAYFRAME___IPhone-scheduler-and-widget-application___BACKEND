package com.BUS.DayFrame.dto.request;

import com.BUS.DayFrame.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private String password;
    private String name;


    public void updateUser(User user, PasswordEncoder passwordEncoder) {
        if (this.password != null && !this.password.isEmpty()) {
            user.changePassword(this.password, passwordEncoder);
        }
        if (this.name != null && !this.name.isEmpty()) {
            user.changeName(this.name);
        }
    }
}
