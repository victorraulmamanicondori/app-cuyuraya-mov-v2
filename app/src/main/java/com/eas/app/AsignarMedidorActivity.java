package com.eas.app;

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
import com.eas.app.api.request.LoginRequest;
import com.eas.app.api.response.AsignarMedidorResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.LoginResponse;
import com.eas.app.utils.Almacenamiento;
import com.eas.app.utils.Constantes;
import com.eas.util.DialogUtils;

public class AsignarMedidorActivity extends AppCompatActivity {

    private EditText etCodigoMedidor;
    private EditText etDNIUsuario;

    private TextView tvErrorCodigoMedidor;
    private TextView tvErrorDNIUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asignar_medidor);

        etCodigoMedidor = findViewById(R.id.txtCodigoMedidor);
        etDNIUsuario = findViewById(R.id.txtDNIUsuario);

        tvErrorCodigoMedidor = findViewById(R.id.tvErrorCodigoMedidor);
        tvErrorDNIUsuario = findViewById(R.id.tvErrorDNIUsuario);

        Button asignarMedidorButton = findViewById(R.id.btnAsignarMedidor);
        asignarMedidorButton.setOnClickListener(v -> asignarMedidor());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void asignarMedidor() {
        if (!validateFields()) {
            return;
        }

        try {
            String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String codigoMedidor = etCodigoMedidor.getText().toString().trim();
            String dni = etDNIUsuario.getText().toString().trim();

            AsignarMedidorRequest asignarMedidorRequest = new AsignarMedidorRequest(codigoMedidor, dni);

            baseApi.asignarMedidor(asignarMedidorRequest, new BaseApiCallback<BaseResponse<AsignarMedidorResponse>>() {
                @Override
                public void onSuccess(BaseResponse<AsignarMedidorResponse> response) {
                    runOnUiThread(() -> {
                        DialogUtils.showAlertDialog(
                                AsignarMedidorActivity.this,
                                Constantes.TITULO_ASIGNACION_EXISTOSA,
                                response.getMensaje(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    etCodigoMedidor.setText("");
                                    etDNIUsuario.setText("");
                                },
                                null,
                                null
                        );
                    });

                    Log.d("AsignarMedidor", "Asignacion exitosa del medidor");
                }

                @Override
                public void onError(Throwable t) {
                    runOnUiThread(() -> {
                        runOnUiThread(() -> DialogUtils.showAlertDialog(
                                AsignarMedidorActivity.this,
                                Constantes.TITULO_ASIGNACION_FALLIDA,
                                t.getMessage(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> dialog.dismiss(),
                                null,
                                null
                        ));
                    });
                    Log.e("AsignarMedidor", "Error en asignar medidor: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            runOnUiThread(() -> {
                DialogUtils.showAlertDialog(
                        AsignarMedidorActivity.this,
                        Constantes.TITULO_ERROR,
                        e.getMessage(),
                        Constantes.BOTON_TEXTO_ACEPTAR,
                        (dialog, which) -> dialog.dismiss(),
                        null,
                        null
                );
            });
            Log.e("AsignarMedidor", "Excepción en asignar medidor", e);
        }
    }

    public boolean validateFields() {
        boolean isValid = true;

        tvErrorCodigoMedidor.setVisibility(View.GONE);
        tvErrorDNIUsuario.setVisibility(View.GONE);

        if (etCodigoMedidor.getText().toString().trim().isEmpty()) {
            tvErrorCodigoMedidor.setText(R.string.este_campo_es_obligatorio);
            tvErrorCodigoMedidor.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (etDNIUsuario.getText().toString().trim().isEmpty()) {
            tvErrorDNIUsuario.setText(R.string.este_campo_es_obligatorio);
            tvErrorDNIUsuario.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }
}