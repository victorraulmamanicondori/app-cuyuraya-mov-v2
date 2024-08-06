package com.eas.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eas.app.api.BaseApi;
import com.eas.app.utils.Almacenamiento;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText dniInput, claveInput;
    private TextView errorText;
    private ProgressBar progressBar;
    private ExecutorService executorService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        dniInput = findViewById(R.id.dni_input);
        claveInput = findViewById(R.id.clave_input);
        errorText = findViewById(R.id.error_text);
        progressBar = findViewById(R.id.progress_bar);

        Button loginButton = findViewById(R.id.login_button);
        Button registroButton = findViewById(R.id.registro_button);
        Button olvideButton = findViewById(R.id.olvide_button);

        loginButton.setOnClickListener(v -> handleLogin());
        registroButton.setOnClickListener(v -> handleCrearUsuario());
        olvideButton.setOnClickListener(v -> handleOlvidoContrasena());

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        verificarInicioSesion();
    }

    private void verificarInicioSesion() {
        executorService.execute(() -> {
            boolean result = false;
            try {
                String antiguoAccessToken = Almacenamiento.obtener(getApplicationContext(), "accessToken");
                String antiguoRefreshToken = Almacenamiento.obtener(getApplicationContext(), "refreshToken");

                if (antiguoAccessToken != null && antiguoRefreshToken != null) {
                    BaseApi api = new BaseApi();
                    BaseApi.TokenResponse tokenResponse = api.peticionPOST("/login/refreshToken", antiguoRefreshToken);

                    if (tokenResponse != null) {
                        Almacenamiento.guardar(getApplicationContext(), "accessToken", tokenResponse.accessToken);
                        Almacenamiento.guardar(getApplicationContext(), "refreshToken", tokenResponse.refreshToken);
                        result = true;
                    }
                }
            } catch (Exception e) {
                Almacenamiento.eliminar(getApplicationContext(), "accessToken");
                Almacenamiento.eliminar(getApplicationContext(), "refreshToken");
            }

            boolean finalResult = result;
            handler.post(() -> {
                if (finalResult) {
                    navigateToPrincipal();
                }
            });
        });
    }

    private void handleLogin() {
        String dni = dniInput.getText().toString().trim();
        String clave = claveInput.getText().toString().trim();

        if (dni.isEmpty() || clave.isEmpty()) {
            errorText.setVisibility(View.VISIBLE);
        } else {
            errorText.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            executorService.execute(() -> {
                boolean result = false;
                try {
                    BaseApi api = new BaseApi();
                    BaseApi.TokenResponse tokenResponse = api.peticionPOST("/login", dni, clave);

                    if (tokenResponse != null) {
                        Almacenamiento.guardar(getApplicationContext(), "accessToken", tokenResponse.accessToken);
                        Almacenamiento.guardar(getApplicationContext(), "refreshToken", tokenResponse.refreshToken);
                        result = true;
                    }
                } catch (Exception e) {
                    handler.post(() -> Toast.makeText(getApplicationContext(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show());
                }

                boolean finalResult = result;
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (finalResult) {
                        navigateToPrincipal();
                    }
                });
            });
        }
    }

    private void handleCrearUsuario() {
        startActivity(new Intent(LoginActivity.this, RegistroUsuarioActivity.class));
    }

    private void handleOlvidoContrasena() {
        startActivity(new Intent(LoginActivity.this, RecuperarContrasenaActivity.class));
    }

    private void navigateToPrincipal() {
        startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
