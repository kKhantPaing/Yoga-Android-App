package com.example.coursework.Service;

import com.example.coursework.Model.ApiResponse;
import com.example.coursework.Model.RequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/")
    Call<ApiResponse> executeQuery(@Body RequestModel request);
}
