package com.eas.app.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eas.app.api.request.AsignarMedidorRequest;
import com.eas.app.api.request.LecturaActualRequest;
import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.request.MovimientoCajaRequest;
import com.eas.app.api.request.ResetearContrasenaRequest;
import com.eas.app.api.request.UbigeoRequest;
import com.eas.app.api.response.AnomaliaResponse;
import com.eas.app.api.response.AsignarMedidorResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.LecturaActualResponse;
import com.eas.app.api.response.LoginResponse;
import com.eas.app.api.response.MovimientoCajaResponse;
import com.eas.app.api.response.UsuarioResponse;
import com.eas.app.model.CentroPoblado;
import com.eas.app.model.ComunidadCampesina;
import com.eas.app.model.ComunidadNativa;
import com.eas.app.model.Departamento;
import com.eas.app.model.Distrito;
import com.eas.app.model.Provincia;
import com.eas.app.model.TipoMovimiento;
import com.eas.app.model.Usuario;
import com.google.gson.Gson;

import java.io.IOException;
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

    private <T> void makeApiCall(Call<T> call, BaseApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        // callback.onError(new Throwable("Error: " + response.message() + ", " + errorBody));

                        Gson gson = new Gson();
                        Log.d("BaseApi", "Error response body: " + response.errorBody());
                        BaseResponse<String> errorResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                        callback.onError(new Throwable(errorResponse.getMensaje()));
                    } catch (IOException e) {
                        callback.onError(new Throwable("Error HTTP " + response.code() + ": " + response.message()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void login(LoginRequest requestData, BaseApiCallback<LoginResponse> callback) {
        Call<LoginResponse> call = apiService.login(requestData);
        makeApiCall(call, callback);
    }

    public void getDepartamentos(BaseApiCallback<List<Departamento>> callback) {
        Call<List<Departamento>> call = apiService.getDepartamentos();
        makeApiCall(call, callback);
    }

    public void getProvincias(String codigoDepartamento, BaseApiCallback<List<Provincia>> callback) {
        Call<List<Provincia>> call = apiService.getProvincias(codigoDepartamento);
        makeApiCall(call, callback);
    }

    public void getDistritos(String codigoProvincia, BaseApiCallback<List<Distrito>> callback) {
        Call<List<Distrito>> call = apiService.getDistritos(codigoProvincia);
        makeApiCall(call, callback);
    }

    public void getCentroPoblado(String codigoDistrito, BaseApiCallback<List<CentroPoblado>> callback) {
        Call<List<CentroPoblado>> call = apiService.getCentrosPoblados(codigoDistrito);
        makeApiCall(call, callback);
    }

    public void getComunidadCampesina(String codigoDistrito, BaseApiCallback<List<ComunidadCampesina>> callback) {
        Call<List<ComunidadCampesina>> call = apiService.getComunidadesCampesinas(codigoDistrito);
        makeApiCall(call, callback);
    }

    public void getComunidadNativa(String codigoDistrito, BaseApiCallback<List<ComunidadNativa>> callback) {
        Call<List<ComunidadNativa>> call = apiService.getComunidadesNativas(codigoDistrito);
        makeApiCall(call, callback);
    }

    public void registrarUsuario(Usuario usuario, BaseApiCallback<Usuario> callback) {
        Call<Usuario> call = apiService.registrarUsuario(usuario);
        makeApiCall(call, callback);
    }

    public void asignarMedidor(AsignarMedidorRequest asignarMedidorRequest, BaseApiCallback<BaseResponse<AsignarMedidorResponse>> callback) {
        Call<BaseResponse<AsignarMedidorResponse>> call = apiService.asignarMedidor(asignarMedidorRequest);
        makeApiCall(call, callback);
    }

    public void registrarLectura(LecturaActualRequest lecturaActualRequest, BaseApiCallback<BaseResponse<LecturaActualResponse>> callback) {
        Call<BaseResponse<LecturaActualResponse>> call = apiService.registrarLectura(lecturaActualRequest);
        makeApiCall(call, callback);
    }

    public void getTiposMovimientos(String tipoRubroIngreso, BaseApiCallback<BaseResponse<List<TipoMovimiento>>> callback) {
        Call<BaseResponse<List<TipoMovimiento>>> call = apiService.getTiposMovimientos(tipoRubroIngreso);
        makeApiCall(call, callback);
    }

    public void registrarMovimientoCaja(MovimientoCajaRequest movimientoCajaRequest, BaseApiCallback<BaseResponse<MovimientoCajaResponse>> callback) {
        Call<BaseResponse<MovimientoCajaResponse>> call = apiService.registrarMovimientoCaja(movimientoCajaRequest);
        makeApiCall(call, callback);
    }

    public void detectarAnomalias(String codigoMedidor, BaseApiCallback<BaseResponse<List<AnomaliaResponse>>> callback) {
        Call<BaseResponse<List<AnomaliaResponse>>> call = apiService.detectarAnomalias(codigoMedidor);
        makeApiCall(call, callback);
    }

    public void getUsuario(String dni, BaseApiCallback<BaseResponse<UsuarioResponse>> callback) {
        Call<BaseResponse<UsuarioResponse>> call = apiService.getUsuario(dni);
        makeApiCall(call, callback);
    }

    public void resetearContrasena(ResetearContrasenaRequest request, BaseApiCallback<BaseResponse<String>> callback) {
        Call<BaseResponse<String>> call = apiService.resetearContrasena(request);
        makeApiCall(call, callback);
    }

    public void listarUsuariosPorUbigeo(UbigeoRequest request, BaseApiCallback<BaseResponse<List<UsuarioResponse>>> callback) {
        Call<BaseResponse<List<UsuarioResponse>>> call =
                apiService.listarUsuariosPorUbigeo(request.getCodigoDistrito(),
                        request.getCodigoCentroPoblado(),
                        request.getCodigoComunidadCampesina(),
                        request.getCodigoComunidadNativa());
        makeApiCall(call, callback);
    }
}

