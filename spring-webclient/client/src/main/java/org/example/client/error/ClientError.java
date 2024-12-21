package org.example.client.error;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientError {
    private int code;
    private String message;

}
