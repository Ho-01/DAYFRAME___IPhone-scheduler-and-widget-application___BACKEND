package com.BUS.DayFrame.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private  T data;
    private  ErrorDetail error;

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(data, null);
    }

    public static <T> ApiResponseDTO<T> error(String code, String message) {
        return new ApiResponseDTO<>(null, new ErrorDetail(code, message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorDetail {
        private final String code;
        private final String message;
    }
}
