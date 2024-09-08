package com.eas.app.api;

import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaseApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}

