package com.example.springrestdocs.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomResponse<T> {
    private int status;
    private T data;
    private String message;

    public static <T> CustomResponse<T> success(T data){
        CustomResponse<T> response = new CustomResponse<>();
        response.status = HttpStatus.OK.value();
        response.data = data;
        response.message = "성공입니다.";
        return response;
    }
}
