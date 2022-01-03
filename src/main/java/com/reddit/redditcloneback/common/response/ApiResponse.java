package com.reddit.redditcloneback.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private Long timestamp;
    private T data;

    public static ApiResponse success() {
        return new ApiResponse(
                HttpStatus.OK.toString(),
                "성공했습니다.",
                Instant.now().getEpochSecond(),
                null
        );
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(
                HttpStatus.OK.toString(),
                "성공했습니다.",
                Instant.now().getEpochSecond(),
                data
        );
    }
}
