package com.eas.app.api;

import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.response.LoginResponse;
import com.eas.app.model.Departamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BaseApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("departamentos")
    Call<List<Departamento>> getDepartamentos();
}

