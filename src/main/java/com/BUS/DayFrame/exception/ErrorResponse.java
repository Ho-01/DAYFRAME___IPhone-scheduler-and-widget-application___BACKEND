package com.BUS.DayFrame.exception;

public class ErrorResponse {
    private boolean success = false;
    private final ErrorDetail error;
    private final String message; //

    // ✅ 실패 응답 생성
    public static ErrorResponse error(String code, String message) {
        return new ErrorResponse(code, message);
    }

    // ✅ 성공 응답 생성
    public static ErrorResponse success(String message) {
        return new ErrorResponse(null, message);
    }

    public ErrorResponse(String code, String message) {
        this.success = success;
        this.message = success ? message : null; //
        this.error = success ? null : new ErrorDetail(code, message);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ErrorDetail getError() { return error; }

    public static class ErrorDetail {
        private final String code;
        private final String message;

        public ErrorDetail(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() { return code; }
        public String getMessage() { return message; }
    }
}
