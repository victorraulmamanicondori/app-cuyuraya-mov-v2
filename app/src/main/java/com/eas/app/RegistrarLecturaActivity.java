package com.eas.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.request.LecturaActualRequest;
import com.eas.app.api.response.AnomaliaResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.LecturaActualResponse;
import com.eas.app.utils.Almacenamiento;
import com.eas.app.utils.Constantes;
import com.eas.util.DialogUtils;

import java.util.List;

public class RegistrarLecturaActivity extends AppCompatActivity {

    private EditText txtCodigoMedidor;
    private EditText txtLecturaActual;

    private TextView tvErrorCodigoMedidor;
    private TextView tvErrorLecturaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_lectura);

        txtCodigoMedidor = findViewById(R.id.txtCodigoMedidor);
        txtLecturaActual = findViewById(R.id.txtLecturaActual);

        tvErrorCodigoMedidor = findViewById(R.id.tvErrorCodigoMedidor);
        tvErrorLecturaActual = findViewById(R.id.tvErrorLecturaActual);

        Button detectarAnomaliasButton = findViewById(R.id.btnDetectarAnomalias);
        detectarAnomaliasButton.setOnClickListener(v -> detectarAnomaliasConsumo());

        Button registrarLecturaButton = findViewById(R.id.btnRegistrarLectura);
        registrarLecturaButton.setOnClickListener(v -> registrarLectura());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void detectarAnomaliasConsumo() {
        if (txtCodigoMedidor.getText().toString().trim().isEmpty()) {
            tvErrorCodigoMedidor.setText(R.string.este_campo_es_obligatorio);
            tvErrorCodigoMedidor.setVisibility(View.VISIBLE);

            runOnUiThread(() -> {
                DialogUtils.showAlertDialog(
                        RegistrarLecturaActivity.this,
                        Constantes.TITULO_ADVERTENCIA,
                        Constantes.CAMPO_OBLIGATORIO_DETECTOR_ANOMALIAS,
                        Constantes.BOTON_TEXTO_ACEPTAR,
                        (dialog, which) -> dialog.dismiss(),
                        null,
                        null
                );
            });

            return;
        }

        try {
            String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String codigoMedidor = txtCodigoMedidor.getText().toString().trim();

            baseApi.detectarAnomalias(codigoMedidor, new BaseApiCallback<BaseResponse<List<AnomaliaResponse>>>() {
                @Override
                public void onSuccess(BaseResponse<List<AnomaliaResponse>> response) {
                    runOnUiThread(() -> {
                        StringBuilder mensaje = getListaAnomalias(response);

                        DialogUtils.showAlertDialog(
                                RegistrarLecturaActivity.this,
                                Constantes.TITULO_INFORMATION,
                                mensaje.toString(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> dialog.dismiss(),
                                null,
                                null
                        );
                    });
                }

                @Override
                public void onError(Throwable t) {
                    runOnUiThread(() -> {
                        DialogUtils.showAlertDialog(
                                RegistrarLecturaActivity.this,
                                Constantes.TITULO_ERROR,
                                t.getMessage(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> dialog.dismiss(),
                                null,
                                null
                        );
                    });
                }
            });

        } catch (Exception e) {
            runOnUiThread(() -> {
                DialogUtils.showAlertDialog(
                        RegistrarLecturaActivity.this,
                        Constantes.TITULO_ERROR,
                        e.getMessage(),
                        Constantes.BOTON_TEXTO_ACEPTAR,
                        (dialog, which) -> dialog.dismiss(),
                        null,
                        null
                );
            });
        }
    }

    private static @NonNull StringBuilder getListaAnomalias(BaseResponse<List<AnomaliaResponse>> response) {
        List<AnomaliaResponse> anomalias = response.getDatos();
        StringBuilder mensaje = new StringBuilder("El medidor tiene las siguientes anomalias:");

        if (anomalias != null && !anomalias.isEmpty()) {
            for (int i = 0; i < 5 && i < anomalias.size(); i++) {
                mensaje.append(anomalias.get(i).getDate()).append(": ").append(anomalias.get(i).getValue()).append("\n");
            }
        } else {
            mensaje = new StringBuilder(Constantes.SIN_ANOMALIAS);
        }
        return mensaje;
    }

    private void registrarLectura() {
        if (!validateFields()) {
            return;
        }

        try {
            String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String codigoMedidor = txtCodigoMedidor.getText().toString().trim();
            String lecturaActual = txtLecturaActual.getText().toString().trim();

            LecturaActualRequest lecturaActualRequest = new LecturaActualRequest(codigoMedidor, lecturaActual);

            baseApi.registrarLectura(lecturaActualRequest, new BaseApiCallback<BaseResponse<LecturaActualResponse>>() {
                @Override
                public void onSuccess(BaseResponse<LecturaActualResponse> response) {
                    runOnUiThread(() -> {
                        DialogUtils.showAlertDialog(
                                RegistrarLecturaActivity.this,
                                Constantes.TITULO_REGISTRO_EXITOSO,
                                response.getMensaje(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    txtCodigoMedidor.setText("");
                                    txtLecturaActual.setText("");
                                },
                                null,
                                null
                        );


                    });

                    Log.d("RegistrarLectura", "Registro exitoso de la lectura");
                }

                @Override
                public void onError(Throwable t) {
                    runOnUiThread(() -> {
                        DialogUtils.showAlertDialog(
                                RegistrarLecturaActivity.this,
                                Constantes.TITULO_REGISTRO_FALLIDO,
                                t.getMessage(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> dialog.dismiss(),
                                null,
                                null
                        );
                    });
                    Log.e("RegistrarLectura", "Error en registrar lectura: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            runOnUiThread(() -> {
                DialogUtils.showAlertDialog(
                        RegistrarLecturaActivity.this,
                        Constantes.TITULO_REGISTRO_FALLIDO,
                        e.getMessage(),
                        Constantes.BOTON_TEXTO_ACEPTAR,
                        (dialog, which) -> dialog.dismiss(),
                        null,
                        null
                );
            });
            Log.e("RegistrarLectura", "Excepción en registrar lectura", e);
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        tvErrorCodigoMedidor.setVisibility(View.GONE);
        tvErrorLecturaActual.setVisibility(View.GONE);

        if (txtCodigoMedidor.getText().toString().trim().isEmpty()) {
            tvErrorCodigoMedidor.setText(R.string.este_campo_es_obligatorio);
            tvErrorCodigoMedidor.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (txtLecturaActual.getText().toString().trim().isEmpty()) {
            tvErrorLecturaActual.setText(R.string.este_campo_es_obligatorio);
            tvErrorLecturaActual.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }
}