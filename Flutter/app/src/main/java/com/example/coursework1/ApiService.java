package com.example.coursework1;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/")
    Call<String> executeQuery(@Body QueryRequest request);
}
