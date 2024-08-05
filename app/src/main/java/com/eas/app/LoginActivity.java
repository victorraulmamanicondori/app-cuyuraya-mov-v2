package com.eas.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eas.app.api.BaseApi;
import com.eas.app.utils.Almacenamiento;

public class LoginActivity extends AppCompatActivity {

    private EditText dniInput, claveInput;
    private TextView errorText;
    private ProgressBar progressBar;

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

        verificarInicioSesion();
    }

    private void verificarInicioSesion() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                // Implementación de verificación de inicio de sesión
                try {
                    String antiguoAccessToken = Almacenamiento.obtener(getApplicationContext(), "accessToken");
                    String antiguoRefreshToken = Almacenamiento.obtener(getApplicationContext(), "refreshToken");

                    if (antiguoAccessToken != null && antiguoRefreshToken != null) {
                        BaseApi api = new BaseApi();
                        BaseApi.TokenResponse tokenResponse = api.peticionPOST("/login/refreshToken", antiguoRefreshToken);

                        if (tokenResponse != null) {
                            Almacenamiento.guardar(getApplicationContext(), "accessToken", tokenResponse.accessToken);
                            Almacenamiento.guardar(getApplicationContext(), "refreshToken", tokenResponse.refreshToken);
                            return true;
                        }
                    }
                } catch (Exception e) {
                    Almacenamiento.eliminar(getApplicationContext(), "accessToken");
                    Almacenamiento.eliminar(getApplicationContext(), "refreshToken");
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    navigateToPrincipal();
                }
            }
        }.execute();
    }

    private void handleLogin() {
        String dni = dniInput.getText().toString().trim();
        String clave = claveInput.getText().toString().trim();

        if (dni.isEmpty() || clave.isEmpty()) {
            errorText.setVisibility(View.VISIBLE);
        } else {
            errorText.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    try {
                        BaseApi api = new BaseApi();
                        BaseApi.TokenResponse tokenResponse = api.peticionPOST("/login", dni, clave);

                        if (tokenResponse != null) {
                            Almacenamiento.guardar(getApplicationContext(), "accessToken", tokenResponse.accessToken);
                            Almacenamiento.guardar(getApplicationContext(), "refreshToken", tokenResponse.refreshToken);
                            return true;
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show());
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    progressBar.setVisibility(View.GONE);
                    if (result) {
                        navigateToPrincipal();
                    }
                }
            }.execute();
        }
    }

    private void handleCrearUsuario() {
        // Implementar la navegación a la actividad de registro de usuario
        startActivity(new Intent(LoginActivity.this, RegistroUsuarioActivity.class));
    }

    private void handleOlvidoContrasena() {
        // Implementar la navegación a la actividad de recuperación de contraseña
        startActivity(new Intent(LoginActivity.this, RecuperarContrasenaActivity.class));
    }

    private void navigateToPrincipal() {
        // Implementar la navegación a la actividad principal
        startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
        finish();
    }
}
