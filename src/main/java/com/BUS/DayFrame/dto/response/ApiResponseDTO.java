package com.BUS.DayFrame.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", data);
    }

    // 성공 응답 (데이터 없음)
    public static ApiResponseDTO<?> success() {
        return new ApiResponseDTO<>(true, "SUCCESS", "요청이 성공적으로 처리되었습니다.", null);
    }

    // 실패 응답
    public static ApiResponseDTO<?> error(String code, String message) {
        return new ApiResponseDTO<>(false, code, message, null);
    }
}
