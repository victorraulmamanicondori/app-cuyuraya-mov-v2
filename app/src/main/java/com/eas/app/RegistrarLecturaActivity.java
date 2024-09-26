package com.eas.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.request.AsignarMedidorRequest;
import com.eas.app.api.request.LecturaActualRequest;
import com.eas.app.api.response.AsignarMedidorResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.LecturaActualResponse;
import com.eas.app.utils.Almacenamiento;
import com.eas.app.utils.Constantes;
import com.eas.util.DialogUtils;

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

        Button registrarLecturaButton = findViewById(R.id.btnRegistrarLectura);
        registrarLecturaButton.setOnClickListener(v -> registrarLectura());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                                (dialog, which) -> dialog.dismiss(),
                                Constantes.BOTON_TEXTO_CANCELAR,
                                (dialog, which) -> dialog.dismiss()
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
                                Constantes.BOTON_TEXTO_CANCELAR,
                                (dialog, which) -> dialog.dismiss()
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
                        Constantes.BOTON_TEXTO_CANCELAR,
                        (dialog, which) -> dialog.dismiss()
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