package org.example.springmetricexternalapi.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

    @GET("/hello")
    Call<String> hello();
}
