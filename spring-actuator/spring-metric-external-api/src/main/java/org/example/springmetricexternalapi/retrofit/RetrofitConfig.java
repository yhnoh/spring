package org.example.springmetricexternalapi.retrofit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Configuration
public class RetrofitConfig {


    @Bean
    public RetrofitApi retrofitApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .eventListener(new RetrofitEventListener())
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
