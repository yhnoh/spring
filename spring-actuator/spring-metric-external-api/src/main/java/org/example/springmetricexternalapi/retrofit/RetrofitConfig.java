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
                .build();

        return retrofit.create(RetrofitApi.class);
    }

}
