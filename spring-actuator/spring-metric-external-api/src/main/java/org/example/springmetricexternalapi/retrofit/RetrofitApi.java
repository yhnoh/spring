package org.example.springmetricexternalapi.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

    @GET("/hello")
    Call<String> hello();

    @GET("/exception")
    Call<String> exception();

    @GET("/error/400")
    Call<String> error400();

    @GET("/error/500")
    Call<String> error500();

}
