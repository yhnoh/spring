package com.example.springbatchexample.customer_update.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class CustomerContactUpdate extends CustomerUpdate{

    private final String emailAddress;
    private final String homePhone;
    private final String cellPhone;
    private final String workPhone;
    private final Integer notificationPreferences;

    public CustomerContactUpdate(long customerId, String emailAddress, String homePhone, String cellPhone, String workPhone, Integer notificationPreferences) {
        super(customerId);
        this.emailAddress = getNullWhenEmpty(emailAddress);
        this.homePhone = getNullWhenEmpty(homePhone);
        this.cellPhone = getNullWhenEmpty(cellPhone);
        this.workPhone = getNullWhenEmpty(workPhone);
        this.notificationPreferences = notificationPreferences;
    }

    private String getNullWhenEmpty(String str){
        return StringUtils.hasText(str) ? str : null;
    }
}
