package org.example.client.timeout;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientTimeoutResponse {
    private String code;
    private String message;
    private String data;

}
