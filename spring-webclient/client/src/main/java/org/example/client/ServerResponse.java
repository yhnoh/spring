package org.example.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerResponse<T> {

    private String code;
    private String message;
    private T data;

    public static <T> ServerResponse<T> success(T data) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setCode("");
        response.setMessage("");
        response.setData(data);
        return response;
    }
}
