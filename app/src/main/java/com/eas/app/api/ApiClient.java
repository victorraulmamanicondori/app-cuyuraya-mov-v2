package com.eas.app.api;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    private static final String BASE_URL = "http://104.251.212.105:9000/api/";

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
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para la conversión de JSON
                .build();
    }
}
