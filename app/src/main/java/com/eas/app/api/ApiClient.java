package com.eas.app.api;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    private static final String BASE_URL = "http://104.251.212.105:9000/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (token != null) {
            // Interceptor para añadir el token en la cabecera Authorization
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Log.d("URL", original.url().toString());

                    // Añadir cabecera Authorization con el token
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", token)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para la conversión de JSON
                    .build();
        }

        return retrofit;
    }
}

