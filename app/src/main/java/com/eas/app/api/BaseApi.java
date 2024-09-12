package com.eas.app.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.response.LoginResponse;
import com.eas.app.model.Departamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseApi {
    private final BaseApiService apiService;

    public BaseApi(String token) {
        // Imprimir el token para verificar si llega correctamente
        Log.d("BaseApi", "Token recibido: " + token);

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

    public void getDepartamentos(BaseApiCallback<List<Departamento>> callback) {
        Call<List<Departamento>> call = apiService.getDepartamentos();
        call.enqueue(new Callback<List<Departamento>>() {
            @Override
            public void onResponse(@NonNull Call<List<Departamento>> call, @NonNull Response<List<Departamento>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Departamento>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
