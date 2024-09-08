package com.eas.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.response.LoginResponse;
import com.eas.app.utils.Almacenamiento;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText dniInput, claveInput;
    private TextView errorText;
    private ProgressBar progressBar;
    private ExecutorService executorService;

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
                try {
                    BaseApi loginApi = new BaseApi(null);
                    LoginRequest loginRequest = new LoginRequest(dni, clave); // Usa los valores ingresados

                    loginApi.login(loginRequest, new BaseApiCallback<LoginResponse>() {
                        @Override
                        public void onSuccess(LoginResponse response) {
                            String accessToken = response.getAccessToken();
                            String refreshToken = response.getRefreshToken();

                            // Guardar los tokens
                            Almacenamiento.guardar(getApplicationContext(), "accessToken", accessToken);
                            Almacenamiento.guardar(getApplicationContext(), "refreshToken", refreshToken);

                            // Actualizar la UI en el hilo principal
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                navigateToPrincipal();
                            });

                            Log.d("Login", "Login exitoso: ");
                        }

                        @Override
                        public void onError(Throwable t) {
                            // Manejar el error y actualizar la UI en el hilo principal
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error en el login: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                            Log.e("Login", "Error en el login: " + t.getMessage());
                        }
                    });

                } catch (Exception e) {
                    // Manejo de excepciones
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error en el inicio de sesión: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    Log.e("Login", "Excepción en el inicio de sesión", e);
                }
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
