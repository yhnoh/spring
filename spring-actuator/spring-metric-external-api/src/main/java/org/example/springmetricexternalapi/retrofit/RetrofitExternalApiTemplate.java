package org.example.springmetricexternalapi.retrofit;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;


@Slf4j
//@Component
@RequiredArgsConstructor
public class RetrofitExternalApiTemplate {

    private final MeterRegistry registry;

    public RetrofitExternalApiResponse handleResponse(Call<RetrofitExternalApiResponse> call) {
        try {
            Response<RetrofitExternalApiResponse> response = call.execute();
            if (response.isSuccessful()) {
                RetrofitExternalApiResponse body = response.body();

                if (!body.isSuccess()) {

                    log.error("response = " + body);
                    List<Tag> tags = List.of(Tag.of("host", call.request().url().host()));
                    registry.counter("okhttp.requests.fail", tags).increment();
                }
                return body;
            }

            log.error("errorBody = " + response.errorBody().string());
        } catch (IOException e) {
            log.error("IOException", e);
            throw new RuntimeException(e);
        }

        return null;
    }

}
