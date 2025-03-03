package com.BUS.DayFrame.dto.Response;

import com.BUS.DayFrame.domain.User;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private final boolean success;
    private final UserData data;


    public UserInfoResponse(User user) {
        this.success = true;
        this.data = new UserData(user);
    }


    @Getter
    public static class UserData {
        private final Long id;
        private final String email;
        private final String name;
        private final String createdAt;

        public UserData(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.name = user.getName();
            this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;
        }
    }
}
