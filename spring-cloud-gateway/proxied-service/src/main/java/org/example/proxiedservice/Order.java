package org.example.proxiedservice;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Order {

    private long id;
    private String name;
    private String state;
}
