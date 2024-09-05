package org.example.springmetricexternalapi.retrofit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrofit")
public class RetrofitController {

    private final RetrofitApiService retrofitApiService;

    @GetMapping("/status/200")
    public RetrofitResponse status200() throws IOException {
        return retrofitApiService.status200();
    }

    @GetMapping("/status/200/error")
    public RetrofitResponse status200Error() throws IOException {
        return retrofitApiService.status200Error();
    }

    @GetMapping("/status/400")
    public RetrofitResponse status400() throws IOException {
        return retrofitApiService.status400();
    }

    @GetMapping("/status/500")
    public RetrofitResponse status500() throws IOException {
        return retrofitApiService.status500();
    }

}
