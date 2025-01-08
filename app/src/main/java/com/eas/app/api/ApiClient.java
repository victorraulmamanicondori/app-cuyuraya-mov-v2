/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.eas.app.util.Constantes;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    public static Retrofit getClient(final String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (token != null && !token.isEmpty()) {
            // Interceptor para añadir el token en la cabecera Authorization
            httpClient.addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request original = chain.request();

                    // Log para depurar el URL
                    Log.d("URL", original.url().toString());

                    // Añadir cabecera Authorization con el token si está disponible
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", token)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        } else {
            Log.e("ApiClient", "Token is null or empty!");
        }

        // Siempre recrea el cliente Retrofit si es necesario
        return new Retrofit.Builder()
                .baseUrl(Constantes.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para la conversión de JSON
                .build();
    }
}
