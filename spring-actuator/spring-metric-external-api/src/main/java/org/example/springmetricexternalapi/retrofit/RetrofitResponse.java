package org.example.springmetricexternalapi.retrofit;

import lombok.Data;

@Data
public class RetrofitResponse implements ExternalApiResponse {
    private String result;

    @Override
    public boolean isSuccess() {
        return "success".equals(result);
    }

    @Override
    public String getExternalApiName() {
        return "ExternalApi";
    }
}
