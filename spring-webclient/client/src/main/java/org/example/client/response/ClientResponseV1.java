package org.example.client.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientResponseV1 {
    private String code;
    private String message;
    private String data;
}
