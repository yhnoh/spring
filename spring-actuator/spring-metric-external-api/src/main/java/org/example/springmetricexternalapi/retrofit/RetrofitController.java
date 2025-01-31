package org.example.springmetricexternalapi.retrofit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequiredArgsConstructor
//@RequestMapping("/retrofit")
public class RetrofitController {

    private final RetrofitApiService retrofitApiService;

    @GetMapping("/status/200")
    public RetrofitExternalApiResponse status200() {
        return retrofitApiService.status200();
    }

    @GetMapping("/status/200/error")
    public RetrofitExternalApiResponse status200Error() {
        return retrofitApiService.status200Error();
    }

    @GetMapping("/status/400")
    public RetrofitExternalApiResponse status400() {
        return retrofitApiService.status400();
    }

    @GetMapping("/status/500")
    public RetrofitExternalApiResponse status500() {
        return retrofitApiService.status500();
    }

    @GetMapping("/ioException")
    public RetrofitExternalApiResponse ioException() {
        return retrofitApiService.ioException();
    }

}
