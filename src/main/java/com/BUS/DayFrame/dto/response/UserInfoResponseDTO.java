package com.BUS.DayFrame.dto.response;

import com.BUS.DayFrame.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDTO {
    private final UserData data;

    public UserInfoResponseDTO(User user) {
        this.data = new UserData(user.getId(), user.getEmail(), user.getName(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
    }


    @Getter
    @AllArgsConstructor
    public static class UserData {
        private final Long id;
        private final String email;
        private final String name;
        private final String createdAt;
    }
}
