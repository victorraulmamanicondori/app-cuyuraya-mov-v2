package com.eas.app.api;

import androidx.annotation.NonNull;

import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseApi {
    private BaseApiService apiService;

    public BaseApi(String token) {
        apiService = ApiClient.getClient(token).create(BaseApiService.class);
    }

    public void login(LoginRequest requestData, BaseApiCallback<LoginResponse> callback) {
        Call<LoginResponse> call = apiService.login(requestData);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
