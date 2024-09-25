package org.example.springmetricexternalapi.retrofit;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpObservationInterceptor;
import io.micrometer.observation.ObservationRegistry;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {


    @Bean
    @Primary
    public RetrofitExternalApi retrofitApi(ObservationRegistry observationRegistry, MeterRegistry registry) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .eventListener(OkHttpMetricsEventListener.builder(registry, "okhttp.requests")
                                .includeHostTag(true)
                                .build())
//                        .addInterceptor(OkHttpObservationInterceptor.builder(observationRegistry, "okhttp.requests")
//                                .includeHostTag(true)
//                                .uriMapper((request) -> request.url().uri().getPath())
//                                .build())
                        .build())
                .build();
        return retrofit.create(RetrofitExternalApi.class);
    }

    @Bean(name = "retrofitIOExceptionApi")
    public RetrofitExternalApi retrofitIOExceptionApi(ObservationRegistry observationRegistry, MeterRegistry registry) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .eventListener(OkHttpMetricsEventListener.builder(registry, "okhttp.requests")
                                .includeHostTag(true)
                                .build())
                        .addInterceptor(OkHttpObservationInterceptor.builder(observationRegistry, "okhttp.requests")
                                .includeHostTag(true)
                                .build())
                        .build())
                .build();

        return retrofit.create(RetrofitExternalApi.class);
    }

}
