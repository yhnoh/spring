package org.example.springbatchitemwriterjpa.jpa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JdbcConnectionProperties {

    private String driverClassName;
    private String username;
    private String password;
    private String url;
    private Hikari hikari;


    @Getter
    @Setter
    public static class Hikari {

        private String poolName;
    }

}
