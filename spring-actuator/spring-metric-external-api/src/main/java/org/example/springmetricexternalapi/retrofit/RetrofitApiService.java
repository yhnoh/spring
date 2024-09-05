package org.example.springmetricexternalapi.retrofit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RetrofitApiService {

    private final RetrofitApi retrofitApi;

    public RetrofitResponse status200() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status200();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }

    public RetrofitResponse status200Error() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status200Error();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }

    public RetrofitResponse status400() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status400();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }

    public RetrofitResponse status500() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status500();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }
}
