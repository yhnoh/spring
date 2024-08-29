package org.example.springmetricexternalapi.retrofit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrofit")
public class RetrofitController {

    private final RetrofitApi retrofitApi;

    @GetMapping("/hello")
    public String hello() throws IOException {
        Call<String> call = retrofitApi.hello();
        Response<String> execute = call.execute();
        return execute.body();
    }

    @GetMapping("/status/200")
    public RetrofitResponse status200() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status200();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }

    @GetMapping("/status/200/error")
    public RetrofitResponse status200Error() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status200Error();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }

    @GetMapping("/status/400")
    public RetrofitResponse status400() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status400();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }

    @GetMapping("/status/500")
    public RetrofitResponse status500() throws IOException {
        Call<RetrofitResponse> call = retrofitApi.status500();
        Response<RetrofitResponse> execute = call.execute();
        return execute.body();
    }

}
