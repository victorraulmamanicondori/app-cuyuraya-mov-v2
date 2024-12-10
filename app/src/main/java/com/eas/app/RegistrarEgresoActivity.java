package com.eas.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.request.MovimientoCajaRequest;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.MovimientoCajaResponse;
import com.eas.app.model.TipoMovimiento;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;
import com.eas.app.componentes.SpinnerItem;
import com.eas.app.util.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class RegistrarEgresoActivity extends AppCompatActivity {

    private Spinner spinnerTipoEgreso;
    private TextView tvErrorTipoEgreso;
    private EditText txtMonto;
    private TextView tvErrorMonto;
    private EditText txaDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_egreso);

        spinnerTipoEgreso = findViewById(R.id.spinnerTipoEgreso);
        tvErrorTipoEgreso = findViewById(R.id.tvErrorTipoEgreso);
        txtMonto = findViewById(R.id.txtMonto);
        tvErrorMonto = findViewById(R.id.tvErrorMonto);
        txaDescripcion = findViewById(R.id.txaDescripcion);

        Button btnRegistrarEgreso = findViewById(R.id.btnRegistrarEgreso);
        btnRegistrarEgreso.setOnClickListener(v -> registrarEgreso());
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.egresoLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cargarTiposEgresos();
        configurarListeners();
    }

    private void configurarListeners() {
        spinnerTipoEgreso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvErrorTipoEgreso.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvErrorTipoEgreso.setVisibility(View.VISIBLE);
            }
        });

        txtMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tvErrorMonto.getVisibility() == View.VISIBLE) {
                    tvErrorMonto.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void cargarTiposEgresos() {
        String token = Almacenamiento.obtener(RegistrarEgresoActivity.this, Constantes.KEY_ACCESS_TOKEN);
        BaseApi baseApi = new BaseApi(token);

        baseApi.getTiposMovimientos(Constantes.TIPO_RUBRO_EGRESO, new BaseApiCallback<BaseResponse<List<TipoMovimiento>>>() {
            @Override
            public void onSuccess(BaseResponse<List<TipoMovimiento>> result) {
                List<SpinnerItem> tiposMovimientos = new ArrayList<>();
                tiposMovimientos.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));

                if (result.getDatos() != null && !result.getDatos().isEmpty()) {
                    for (TipoMovimiento tipoMovimiento : result.getDatos()) {
                        tiposMovimientos.add(new SpinnerItem(tipoMovimiento.getIdTipoMovimiento(), tipoMovimiento.getConcepto()));
                    }
                }

                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(RegistrarEgresoActivity.this,
                        android.R.layout.simple_spinner_item, tiposMovimientos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTipoEgreso.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(RegistrarEgresoActivity.this, "Error al cargar tipos de egresos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registrarEgreso() {
        if (!validateFields()) {
            return;
        }

        try {
            String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            MovimientoCajaRequest movimientoCajaRequest = new MovimientoCajaRequest();
            movimientoCajaRequest.setIdTipoMovimiento(((SpinnerItem)spinnerTipoEgreso.getSelectedItem()).getCodigo());
            movimientoCajaRequest.setMonto(txtMonto.getText().toString().trim());
            movimientoCajaRequest.setDescripcion(txaDescripcion.getText().toString().trim());

            baseApi.registrarMovimientoCaja(movimientoCajaRequest, new BaseApiCallback<BaseResponse<MovimientoCajaResponse>>() {
                @Override
                public void onSuccess(BaseResponse<MovimientoCajaResponse> response) {
                    runOnUiThread(() -> {
                        DialogUtils.showAlertDialog(
                                RegistrarEgresoActivity.this,
                                Constantes.TITULO_REGISTRO_EXITOSO,
                                response.getMensaje(),
                                Constantes.BOTON_TEXTO_ACEPTAR,
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    spinnerTipoEgreso.setSelection(0);
                                    txtMonto.setText("");
                                    txaDescripcion.setText("");
                                },
                                null,
                                null
                        );
                    });
                }

                @Override
                public void onError(Throwable t) {
                    runOnUiThread(() -> DialogUtils.showAlertDialog(
                            RegistrarEgresoActivity.this,
                            Constantes.TITULO_REGISTRO_FALLIDO,
                            t.getMessage(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    ));
                    Log.e("RegistroIngreso", "Error en el registro de movimiento egreso en caja: " + t.getMessage());
                }
            });
        } catch(Exception ex) {
            runOnUiThread(() -> DialogUtils.showAlertDialog(
                    RegistrarEgresoActivity.this,
                    Constantes.TITULO_REGISTRO_FALLIDO,
                    ex.getMessage(),
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            ));
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        tvErrorTipoEgreso.setVisibility(View.GONE);
        tvErrorMonto.setVisibility(View.GONE);

        Log.d("RegistroEgreso", "TipoEgreso=[" + spinnerTipoEgreso.getSelectedItemPosition() + "]");

        int selectedItemPos = spinnerTipoEgreso.getSelectedItemPosition();
        if (selectedItemPos == -1 || selectedItemPos == 0) {
            tvErrorTipoEgreso.setText(R.string.este_campo_es_obligatorio);
            tvErrorTipoEgreso.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (txtMonto.getText().toString().trim().isEmpty()) {
            tvErrorMonto.setText(R.string.este_campo_es_obligatorio);
            tvErrorMonto.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void navigateToPrincipal() {
        startActivity(new Intent(RegistrarEgresoActivity.this, PrincipalActivity.class));
        finish();
    }
}