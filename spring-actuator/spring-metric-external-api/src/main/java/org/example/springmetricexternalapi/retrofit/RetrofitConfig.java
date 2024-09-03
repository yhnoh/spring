package org.example.springmetricexternalapi.retrofit;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpObservationInterceptor;
import io.micrometer.observation.ObservationRegistry;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {


    @Bean
    public RetrofitApi retrofitApi(ObservationRegistry observationRegistry, MeterRegistry registry) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .eventListener(OkHttpMetricsEventListener.builder(registry, "okhttp.requests").build())
                        .addInterceptor(OkHttpObservationInterceptor.builder(observationRegistry, "http.client.requests").build())
                        .addInterceptor(chain -> {
                            System.out.println("Interceptor.intercept");
                            return chain.proceed(chain.request());
                        })
                        .build())
                .build();
        return retrofit.create(RetrofitApi.class);
    }

    @Bean
    public RetrofitApi retrofitApi2() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new okhttp3.OkHttpClient.Builder()
                        .eventListener(new RetrofitEventListener())
                        .addInterceptor(chain -> {
                            System.out.println("Interceptor.intercept");
                            return chain.proceed(chain.request());
                        })
                        .build())
                .build();

        return retrofit.create(RetrofitApi.class);
    }

}
