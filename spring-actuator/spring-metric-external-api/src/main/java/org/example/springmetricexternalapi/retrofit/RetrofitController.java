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

}
