package com.example.coursework1;

import android.util.Log;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHelper {
    // base url for Cloud
    private static final String BASE_URL = "http://localhost:5000/";
    private static Retrofit retrofit;
    QueryRequest request;
    ApiService apiService = getRetrofitInstance().create(ApiService.class);

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Request url for CRUD operations
    public String postQuery(QueryRequest queryRequest) {
        request = new QueryRequest();
        request.setStoredProcedure(queryRequest.isStoredProcedure());
        request.setQueryString(queryRequest.getQueryString());
        request.setProcedureName(queryRequest.getProcedureName());
        request.setParameters(queryRequest.getParameters().size()==0?new HashMap<>():queryRequest.getParameters());

        final String[] result = {""};

        Call<String> call = apiService.executeQuery(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    result[0] = response.body();
                } else {
                    Log.e("API Error", "Request failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API Failure", t.getMessage());
            }
        });
        return result[0];
    }
}
