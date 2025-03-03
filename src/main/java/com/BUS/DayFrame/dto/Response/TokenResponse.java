package com.BUS.DayFrame.dto.Response;

public class TokenResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;

    public TokenResponse(boolean success, String accessToken, String refreshToken) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public boolean isSuccess() { return success; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}
