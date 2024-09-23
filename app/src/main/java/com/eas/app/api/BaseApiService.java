package com.eas.app.api;

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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("departamentos")
    Call<List<Departamento>> getDepartamentos();

    @GET("provincias/departamento/{codigoDepartamento}")
    Call<List<Provincia>> getProvincias(@Path("codigoDepartamento") String codigoDepartamento);

    @GET("distritos/provincia/{codigoProvincia}")
    Call<List<Distrito>> getDistritos(@Path("codigoProvincia") String codigoProvincia);

    @GET("centros-poblados/distrito/{codigoDistrito}")
    Call<List<CentroPoblado>> getCentrosPoblados(@Path("codigoDistrito") String codigoDistrito);

    @GET("comunidades-campesinas/distrito/{codigoDistrito}")
    Call<List<ComunidadCampesina>> getComunidadesCampesinas(@Path("codigoDistrito") String codigoDistrito);

    @GET("comunidades-nativas/distrito/{codigoDistrito}")
    Call<List<ComunidadNativa>> getComunidadesNativas(@Path("codigoDistrito") String codigoDistrito);
}
