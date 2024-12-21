package org.example.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerResponse<T> {

    private int code;
    private String message;
    private T data;

    public static <T> ServerResponse<T> success(T data) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setCode(200);
        response.setMessage("");
        response.setData(data);
        return response;
    }

    public static ServerResponse error(int code, String message) {
        ServerResponse response = new ServerResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
