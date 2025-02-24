package com.BUS.DayFrame.DTO;

public class ErrorResponse {
    private boolean success;
    private ErrorDetail error;

    public ErrorResponse(String code, String message) {
        this.success = false;
        this.error = new ErrorDetail(code, message);
    }

    public boolean isSuccess() { return success; }
    public ErrorDetail getError() { return error; }

    public static class ErrorDetail {
        private String code;
        private String message;

        public ErrorDetail(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() { return code; }
        public String getMessage() { return message; }
    }
}
