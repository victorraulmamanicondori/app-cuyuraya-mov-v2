package com.eas.app.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.response.LoginResponse;
import com.eas.app.model.CentroPoblado;
import com.eas.app.model.ComunidadCampesina;
import com.eas.app.model.ComunidadNativa;
import com.eas.app.model.Departamento;
import com.eas.app.model.Distrito;
import com.eas.app.model.Provincia;

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

    public void getProvincias(String codigoDepartamento, BaseApiCallback<List<Provincia>> callback) {
        Call<List<Provincia>> call = apiService.getProvincias(codigoDepartamento);
        call.enqueue(new Callback<List<Provincia>>() {
            @Override
            public void onResponse(@NonNull Call<List<Provincia>> call, @NonNull Response<List<Provincia>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Provincia>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getDistritos(String codigoProvincia, BaseApiCallback<List<Distrito>> callback) {
        Call<List<Distrito>> call = apiService.getDistritos(codigoProvincia);
        call.enqueue(new Callback<List<Distrito>>() {
            @Override
            public void onResponse(@NonNull Call<List<Distrito>> call, @NonNull Response<List<Distrito>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Distrito>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getCentroPoblado(String codigoDistrito, BaseApiCallback<List<CentroPoblado>> callback) {
        Call<List<CentroPoblado>> call = apiService.getCentrosPoblados(codigoDistrito);
        call.enqueue(new Callback<List<CentroPoblado>>() {
            @Override
            public void onResponse(@NonNull Call<List<CentroPoblado>> call, @NonNull Response<List<CentroPoblado>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CentroPoblado>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getComunidadCampesina(String codigoDistrito, BaseApiCallback<List<ComunidadCampesina>> callback) {
        Call<List<ComunidadCampesina>> call = apiService.getComunidadesCampesinas(codigoDistrito);
        call.enqueue(new Callback<List<ComunidadCampesina>>() {
            @Override
            public void onResponse(@NonNull Call<List<ComunidadCampesina>> call, @NonNull Response<List<ComunidadCampesina>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ComunidadCampesina>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getComunidadNativa(String codigoDistrito, BaseApiCallback<List<ComunidadNativa>> callback) {
        Call<List<ComunidadNativa>> call = apiService.getComunidadesNativas(codigoDistrito);
        call.enqueue(new Callback<List<ComunidadNativa>>() {
            @Override
            public void onResponse(@NonNull Call<List<ComunidadNativa>> call, @NonNull Response<List<ComunidadNativa>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ComunidadNativa>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
