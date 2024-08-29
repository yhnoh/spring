package org.example.springmetricexternalapi.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

    @GET("/hello")
    Call<String> hello();

    @GET("/status/200")
    Call<RetrofitResponse> status200();

    @GET("/status/200/error")
    Call<RetrofitResponse> status200Error();

    @GET("/status/400")
    Call<RetrofitResponse> status400();

    @GET("/status/500")
    Call<RetrofitResponse> status500();

}
