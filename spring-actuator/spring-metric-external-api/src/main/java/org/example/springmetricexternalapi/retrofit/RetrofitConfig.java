package org.example.springmetricexternalapi.retrofit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Configuration
public class RetrofitConfig {


    @Bean
    public RetrofitApi retrofitApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new okhttp3.OkHttpClient.Builder()
                        .eventListener(new RetrofitEventListener())
                        .addInterceptor(chain -> {
                            okhttp3.Request request = chain.request();
                            okhttp3.Response response = chain.proceed(request);
                            return response;
                        })
                        .build())
                .build();

        return retrofit.create(RetrofitApi.class);
    }

    @Bean
    public RetrofitApi retrofitApi2() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8081")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new okhttp3.OkHttpClient.Builder()
                        .eventListener(new RetrofitEventListener())
                        .addInterceptor(chain -> {
                            okhttp3.Request request = chain.request();
                            okhttp3.Response response = chain.proceed(request);
                            return response;
                        })
                        .build())
                .build();

        return retrofit.create(RetrofitApi.class);
    }

}
