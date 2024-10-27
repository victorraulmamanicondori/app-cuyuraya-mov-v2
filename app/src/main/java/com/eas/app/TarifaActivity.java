package com.eas.app;

import android.os.Bundle;
import android.view.View;
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
import com.eas.app.api.response.BaseResponse;
import com.eas.app.model.Tarifa;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;
import com.eas.app.util.DialogUtils;

public class TarifaActivity extends AppCompatActivity {

    private BaseApi baseApi;
    private EditText txtCodigoTarifa;
    private TextView txtErrorCodigoTarifa;
    private EditText txtDescTarifa;
    private TextView txtErrorDescTarifa;
    private EditText txtMontoTarifa;
    private TextView txtErrorMontoTarifa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tarifa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCodigoTarifa = findViewById(R.id.txtCodigoTarifa);
        txtErrorCodigoTarifa = findViewById(R.id.txtErrorCodigoTarifa);
        txtDescTarifa = findViewById(R.id.txtDescTarifa);
        txtErrorDescTarifa = findViewById(R.id.txtErrorDescTarifa);
        txtMontoTarifa = findViewById(R.id.txtMontoTarifa);
        txtErrorMontoTarifa = findViewById(R.id.txtErrorMontoTarifa);

        findViewById(R.id.btnGuardarTarifa).setOnClickListener(v -> guardarTarifa());

        String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
        baseApi = new BaseApi(token);

        txtCodigoTarifa.setEnabled(false);

        cargarTarifaBase();
    }

    private void guardarTarifa() {
        if (!validateFields()) {
            return;
        }

        Tarifa tarifaRequest = new Tarifa();
        tarifaRequest.setCodigoTarifa(txtCodigoTarifa.getText().toString().trim());
        tarifaRequest.setDescripcion(txtDescTarifa.getText().toString().trim());
        tarifaRequest.setMontoTarifa(Double.valueOf(txtMontoTarifa.getText().toString().trim()));

        baseApi.guardarTarifa(tarifaRequest, new BaseApiCallback<BaseResponse<Tarifa>>() {
            @Override
            public void onSuccess(BaseResponse<Tarifa> result) {
                Tarifa tarifa = result.getDatos();
                if (result.getCodigo() == 200) {
                    txtCodigoTarifa.setText(tarifa.getCodigoTarifa());
                    txtDescTarifa.setText(tarifa.getDescripcion());
                    txtMontoTarifa.setText(String.format("%s", tarifa.getMontoTarifa()));

                    DialogUtils.showAlertDialog(
                            TarifaActivity.this,
                            Constantes.TITULO_INFORMATION,
                            result.getMensaje(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                } else {
                    Toast.makeText(getApplicationContext(), "Error al registrar tarifa base: " + result.getMensaje(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar tarifa base: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean validateFields() {
        boolean isValid = true;

        if (txtCodigoTarifa.getText().toString().trim().isEmpty()) {
            txtErrorCodigoTarifa.setText(R.string.este_campo_es_obligatorio);
            txtErrorCodigoTarifa.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (txtDescTarifa.getText().toString().trim().isEmpty()) {
            txtErrorDescTarifa.setText(R.string.este_campo_es_obligatorio);
            txtErrorDescTarifa.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (txtMontoTarifa.getText().toString().trim().isEmpty()) {
            txtErrorMontoTarifa.setText(R.string.este_campo_es_obligatorio);
            txtErrorMontoTarifa.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void cargarTarifaBase() {
        baseApi.obtenerTarifa(Constantes.CODIGO_TARIFA_BASE, new BaseApiCallback<BaseResponse<Tarifa>>() {
            @Override
            public void onSuccess(BaseResponse<Tarifa> result) {
                Tarifa tarifa = result.getDatos();
                if (result.getCodigo() == 200) {
                    txtCodigoTarifa.setText(tarifa.getCodigoTarifa());
                    txtDescTarifa.setText(tarifa.getDescripcion());
                    txtMontoTarifa.setText(String.format("%s", tarifa.getMontoTarifa()));
                } else {
                    Toast.makeText(getApplicationContext(), "Error al cargar tarifa base: " + result.getMensaje(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar tarifa base: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}