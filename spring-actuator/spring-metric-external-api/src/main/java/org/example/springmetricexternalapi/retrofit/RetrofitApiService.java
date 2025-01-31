package org.example.springmetricexternalapi.retrofit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import retrofit2.Call;

//@Component
public class RetrofitApiService {

    private final RetrofitExternalApiTemplate retrofitExternalApiTemplate;
    private final RetrofitExternalApi retrofitExternalApi;
    private final RetrofitExternalApi retrofitIOExceptionApi;

    public RetrofitApiService(RetrofitExternalApiTemplate retrofitExternalApiTemplate,
                              RetrofitExternalApi retrofitExternalApi,
                              @Qualifier("retrofitIOExceptionApi") RetrofitExternalApi retrofitIOExceptionApi) {
        this.retrofitExternalApiTemplate = retrofitExternalApiTemplate;
        this.retrofitExternalApi = retrofitExternalApi;
        this.retrofitIOExceptionApi = retrofitIOExceptionApi;
    }

    public RetrofitExternalApiResponse status200() {
        Call<RetrofitExternalApiResponse> call = retrofitExternalApi.status200();
        return retrofitExternalApiTemplate.handleResponse(call);
    }

    public RetrofitExternalApiResponse status200Error() {
        Call<RetrofitExternalApiResponse> call = retrofitExternalApi.status200Error();
        return retrofitExternalApiTemplate.handleResponse(call);
    }

    public RetrofitExternalApiResponse status400() {
        Call<RetrofitExternalApiResponse> call = retrofitExternalApi.status400();
        return retrofitExternalApiTemplate.handleResponse(call);
    }

    public RetrofitExternalApiResponse status500() {
        Call<RetrofitExternalApiResponse> call = retrofitExternalApi.status500();
        return retrofitExternalApiTemplate.handleResponse(call);
    }

    public RetrofitExternalApiResponse ioException() {
        Call<RetrofitExternalApiResponse> call = retrofitIOExceptionApi.status200();
        return retrofitExternalApiTemplate.handleResponse(call);
    }
}
