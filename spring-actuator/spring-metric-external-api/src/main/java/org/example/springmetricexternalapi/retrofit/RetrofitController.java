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

    @GetMapping("/exception")
    public String exception() throws IOException {
        Call<String> call = retrofitApi.exception();
        Response<String> execute = call.execute();
        return execute.body();
    }

    @GetMapping("/error/400")
    public String error400() throws IOException {
        Call<String> call = retrofitApi.error400();
        Response<String> execute = call.execute();
        return execute.body();
    }

    @GetMapping("/error/500")
    public String error500() throws IOException {
        Call<String> call = retrofitApi.error500();
        Response<String> execute = call.execute();
        return execute.body();
    }
}
