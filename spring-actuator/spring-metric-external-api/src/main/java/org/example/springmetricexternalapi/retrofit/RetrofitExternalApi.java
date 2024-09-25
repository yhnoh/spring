package org.example.springmetricexternalapi.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitExternalApi {

    @GET("/hello")
    Call<String> hello();

    @GET("/status/200")
    Call<RetrofitExternalApiResponse> status200();

    @GET("/status/200/error")
    Call<RetrofitExternalApiResponse> status200Error();

    @GET("/status/400")
    Call<RetrofitExternalApiResponse> status400();

    @GET("/status/500")
    Call<RetrofitExternalApiResponse> status500();

}
