package com.eas.app.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eas.app.api.request.AsignarMedidorRequest;
import com.eas.app.api.request.LecturaActualRequest;
import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.request.MovimientoCajaRequest;
import com.eas.app.api.request.ResetearContrasenaRequest;
import com.eas.app.api.response.AnomaliaResponse;
import com.eas.app.api.response.AsignarMedidorResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.LecturaActualResponse;
import com.eas.app.api.response.LoginResponse;
import com.eas.app.api.response.MovimientoCajaResponse;
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

    public void registrarUsuario(Usuario usuario, BaseApiCallback<Usuario> callback) {
        Call<Usuario> call = apiService.registrarUsuario(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void asignarMedidor(AsignarMedidorRequest asignarMedidorRequest, BaseApiCallback<BaseResponse<AsignarMedidorResponse>> callback) {
        Call<BaseResponse<AsignarMedidorResponse>> call = apiService.asignarMedidor(asignarMedidorRequest);
        call.enqueue(new Callback<BaseResponse<AsignarMedidorResponse>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<AsignarMedidorResponse>> call, @NonNull Response<BaseResponse<AsignarMedidorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<AsignarMedidorResponse> respuesta = response.body();
                    if (respuesta.getCodigo() == 200) {
                        // Éxito
                        callback.onSuccess(respuesta);
                    } else {
                        // Error del servidor con código 200 pero mensaje de error
                        callback.onError(new Throwable(respuesta.getMensaje()));
                    }
                } else {
                    try {
                        // Deserializar el cuerpo del error usando Gson u otro convertidor
                        Gson gson = new Gson();
                        BaseResponse<AsignarMedidorResponse> errorResponse = gson.fromJson(response.errorBody().string(),
                                BaseResponse.class);
                        callback.onError(new Throwable(errorResponse.getMensaje()));
                    } catch (IOException e) {
                        callback.onError(new Throwable("Error HTTP " + response.code() + ": " + response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<AsignarMedidorResponse>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void registrarLectura(LecturaActualRequest lecturaActualRequest, BaseApiCallback<BaseResponse<LecturaActualResponse>> callback) {
        Call<BaseResponse<LecturaActualResponse>> call = apiService.registrarLectura(lecturaActualRequest);
        call.enqueue(new Callback<BaseResponse<LecturaActualResponse>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<LecturaActualResponse>> call, @NonNull Response<BaseResponse<LecturaActualResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<LecturaActualResponse> respuesta = response.body();
                    if (respuesta.getCodigo() == 200) {
                        // Éxito
                        callback.onSuccess(respuesta);
                    } else {
                        // Error del servidor con código 200 pero mensaje de error
                        callback.onError(new Throwable(respuesta.getMensaje()));
                    }
                } else {
                    try {
                        // Deserializar el cuerpo del error usando Gson u otro convertidor
                        Gson gson = new Gson();
                        BaseResponse<LecturaActualResponse> errorResponse = gson.fromJson(response.errorBody().string(),
                                BaseResponse.class);
                        callback.onError(new Throwable(errorResponse.getMensaje()));
                    } catch (IOException e) {
                        callback.onError(new Throwable("Error HTTP " + response.code() + ": " + response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<LecturaActualResponse>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getTiposMovimientos(String tipoRubroIngreso, BaseApiCallback<BaseResponse<List<TipoMovimiento>>> callback) {
        Call<BaseResponse<List<TipoMovimiento>>> call = apiService.getTiposMovimientos(tipoRubroIngreso);
        call.enqueue(new Callback<BaseResponse<List<TipoMovimiento>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<List<TipoMovimiento>>> call, @NonNull Response<BaseResponse<List<TipoMovimiento>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        BaseResponse<List<TipoMovimiento>> respuesta = response.body();
                        if (respuesta.getCodigo() == 200) {
                            callback.onSuccess(respuesta);
                        } else {
                            callback.onError(new Throwable(respuesta.getMensaje()));
                        }
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable(e.getMessage()));
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        BaseResponse<List<TipoMovimiento>> errorResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                        callback.onError(new Throwable(errorResponse.getMensaje()));
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable("Error HTTP " + response.code() + ": " + response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<TipoMovimiento>>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void registrarMovimientoCaja(MovimientoCajaRequest movimientoCajaRequest, BaseApiCallback<BaseResponse<MovimientoCajaResponse>> callback) {
        Call<BaseResponse<MovimientoCajaResponse>> call = apiService.registrarMovimientoCaja(movimientoCajaRequest);
        call.enqueue(new Callback<BaseResponse<MovimientoCajaResponse>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<MovimientoCajaResponse>> call, @NonNull Response<BaseResponse<MovimientoCajaResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        BaseResponse<MovimientoCajaResponse> respuesta = response.body();
                        if (respuesta.getCodigo() == 200) {
                            callback.onSuccess(respuesta);
                        } else {
                            callback.onError(new Throwable(respuesta.getMensaje()));
                        }
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable(e.getMessage()));
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        BaseResponse<MovimientoCajaResponse> errorResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                        callback.onError(new Throwable(errorResponse.getMensaje()));
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable("Error HTTP " + response.code() + ": " + response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<MovimientoCajaResponse>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void detectarAnomalias(String codigoMedidor, BaseApiCallback<BaseResponse<List<AnomaliaResponse>>> callback) {
        Call<BaseResponse<List<AnomaliaResponse>>> call = apiService.detectarAnomalias(codigoMedidor);
        call.enqueue(new Callback<BaseResponse<List<AnomaliaResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<List<AnomaliaResponse>>> call, @NonNull Response<BaseResponse<List<AnomaliaResponse>>> response) {
                Log.d("BaseApi", "response.isSuccessful(): " + response.isSuccessful());
                Log.d("BaseApi", "response.body(): " + response.body());

                if (response.isSuccessful()) {
                    try {
                        BaseResponse<List<AnomaliaResponse>> respuesta = response.body();
                        if (respuesta.getCodigo() == 200) {
                            callback.onSuccess(respuesta);
                        } else {
                            callback.onError(new Throwable(respuesta.getMensaje()));
                        }
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable(e.getMessage()));
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        Log.d("BaseApi", "Error response body: " + response.errorBody());
                        BaseResponse<List<AnomaliaResponse>> errorResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                        callback.onError(new Throwable(errorResponse.getMensaje()));
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable("Error HTTP " + response.code() + ": " + response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<AnomaliaResponse>>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void resetearContrasena(ResetearContrasenaRequest request, BaseApiCallback<BaseResponse<String>> callback) {
        Call<BaseResponse<String>> call = apiService.resetearContrasena(request);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<String>> call, @NonNull Response<BaseResponse<String>> response) {
                Log.d("BaseApi", "response.isSuccessful(): " + response.isSuccessful());
                Log.d("BaseApi", "response.body(): " + response.body());

                if (response.isSuccessful()) {
                    try {
                        BaseResponse<String> respuesta = response.body();
                        assert respuesta != null;
                        if (respuesta.getCodigo() == 200) {
                            callback.onSuccess(respuesta);
                        } else {
                            callback.onError(new Throwable(respuesta.getMensaje()));
                        }
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable(e.getMessage()));
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        Log.d("BaseApi", "Error response body: " + response.errorBody());
                        BaseResponse<String> errorResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
                        callback.onError(new Throwable(errorResponse.getMensaje()));
                    } catch (Exception e) {
                        Log.e("BaseApi", "Error:", e);
                        callback.onError(new Throwable("Error HTTP " + response.code() + ": " + response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
