package com.example.coursework.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.coursework.Model.ApiResponse;
import com.example.coursework.Model.RequestModel;
import com.example.coursework.Service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class APIHelper {
    RequestModel request;
    // base url for Cloud
    private String baseUrl = "http://192.168.1.5:99/";
    private final Retrofit retrofit = RetrofitClient.getClient(baseUrl);
    private final ApiService apiService = retrofit.create(ApiService.class);

    public APIHelper(String newUrl) {
        this.baseUrl = newUrl;
    }

    // Request url for CRUD operations
    public void postQuery(Context context, RequestModel queryRequest) {
        request = new RequestModel();
        request.setStoredProcedure(queryRequest.isStoredProcedure());
        request.setQueryString(queryRequest.getQueryString());
        request.setProcedureName(queryRequest.getProcedureName());
        request.setParameters(queryRequest.getParameters());

        Call<ApiResponse> call = apiService.executeQuery(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getResponse();
                    Toast.makeText(context, status == null ? "" : status, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
