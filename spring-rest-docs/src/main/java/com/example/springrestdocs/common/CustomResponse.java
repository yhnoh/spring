package com.example.springrestdocs.common;

import org.springframework.http.HttpStatus;

public class CustomResponse<T> {
    private int status;
    private T data;
    private String msg;

    public static <T> CustomResponse<T> success(T data){
        CustomResponse<T> response = new CustomResponse<>();
        response.status = HttpStatus.OK.value();
        response.data = data;
        response.msg = "성공입니다.";
        return response;
    }
}
