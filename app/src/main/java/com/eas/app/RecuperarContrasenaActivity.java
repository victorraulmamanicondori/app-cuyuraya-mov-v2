package com.eas.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.request.ResetearContrasenaRequest;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.utils.Constantes;
import com.eas.app.util.DialogUtils;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    private EditText etDni;
    private TextView tvErrorDni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_contrasena);

        etDni = findViewById(R.id.etDni);
        tvErrorDni = findViewById(R.id.tvErrorDni);
        Button btnResetearContrasena = findViewById(R.id.btnResetearContrasena);
        btnResetearContrasena.setOnClickListener(v -> resetearContrasena());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void resetearContrasena() {
        if (validateFields()) {
            String dni = etDni.getText().toString();

            BaseApi baseApi = new BaseApi(null);

            ResetearContrasenaRequest request = new ResetearContrasenaRequest(dni);

            baseApi.resetearContrasena(request, new BaseApiCallback<BaseResponse<String>>() {
                @Override
                public void onSuccess(BaseResponse<String> response) {
                    runOnUiThread(() -> {
                        DialogUtils.showAlertDialog(
                                RecuperarContrasenaActivity.this,
                                Constantes.TITULO_INFORMATION,
                                response.getMensaje(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    navigateToLogin();
                                },
                                null,
                                null
                        );
                    });
                }

                @Override
                public void onError(Throwable t) {
                    DialogUtils.showAlertDialog(
                            RecuperarContrasenaActivity.this,
                            Constantes.TITULO_ERROR,
                            t.getMessage(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            Constantes.BOTON_TEXTO_CANCELAR,
                            (dialog, which) -> {
                                dialog.dismiss();
                                navigateToLogin();
                            }
                    );
                }
            });
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        tvErrorDni.setVisibility(View.GONE);

        if (etDni.getText().toString().trim().isEmpty()) {
            tvErrorDni.setText(R.string.este_campo_es_obligatorio);
            tvErrorDni.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void navigateToLogin() {
        startActivity(new Intent(RecuperarContrasenaActivity.this, LoginActivity.class));
        finish();
    }
}