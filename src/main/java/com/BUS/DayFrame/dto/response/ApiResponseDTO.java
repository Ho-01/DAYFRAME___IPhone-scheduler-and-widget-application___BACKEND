package com.BUS.DayFrame.dto.response;

import lombok.Getter;

@Getter
public class ApiResponse<D> {
    private final boolean success;
    private final D data;
    private final ErrorDetail error;

    private ApiResponse(boolean success, D data, ErrorDetail error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }


    public static <D> ApiResponse<D> success(D data) {
        return new ApiResponse<>(true, data, null);
    }


    public static <D> ApiResponse<D> error(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorDetail(code, message));
    }

    @Getter
    public static class ErrorDetail {
        private final String code;
        private final String message;

        public ErrorDetail(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
