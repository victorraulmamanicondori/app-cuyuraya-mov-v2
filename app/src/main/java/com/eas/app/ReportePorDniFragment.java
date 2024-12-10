package com.eas.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.UsuarioResponse;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;

public class ReportePorDniFragment extends Fragment {

    private EditText txtDniUsuarioReporte;
    private TextView lblNombreUsuarioReporte;
    private TextView tvErrorDniUsuarioReporte;
    private ImageButton btnBuscarPorDniReporte;
    private Button btnImprimirBalanceUsuario;
    private Button btnImprimirPagosRecibos;
    private Button btnImprimirEgresos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reporte_por_dni, container, false);

        txtDniUsuarioReporte = view.findViewById(R.id.txtDniUsuarioReporte);
        lblNombreUsuarioReporte = view.findViewById(R.id.lblNombreUsuarioReporte);
        tvErrorDniUsuarioReporte = view.findViewById(R.id.tvErrorDniUsuarioReporte);
        btnBuscarPorDniReporte = view.findViewById(R.id.btnBuscarPorDniReporte);
        btnImprimirBalanceUsuario = view.findViewById(R.id.btnImprimirBalanceUsuario);
        btnImprimirPagosRecibos = view.findViewById(R.id.btnImprimirPagosRecibos);
        btnImprimirEgresos = view.findViewById(R.id.btnImprimirEgresos);

        btnBuscarPorDniReporte.setOnClickListener(v -> buscarUsuarioPorDni());
        btnImprimirBalanceUsuario.setOnClickListener(v -> imprimirBalanceUsuario());
        btnImprimirPagosRecibos.setOnClickListener(v -> imprimirPagosRecibos());
        btnImprimirEgresos.setOnClickListener(v -> imprimirEgresos());

        return view;
    }

    private void buscarUsuarioPorDni() {
        try {
            lblNombreUsuarioReporte.setText("");
            lblNombreUsuarioReporte.setVisibility(View.GONE);

            String token = Almacenamiento.obtener(requireActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String dni = txtDniUsuarioReporte.getText().toString().trim();

            if (dni.isEmpty()) {
                tvErrorDniUsuarioReporte.setText(R.string.este_campo_es_obligatorio);
                tvErrorDniUsuarioReporte.setVisibility(View.VISIBLE);
                return;
            }

            tvErrorDniUsuarioReporte.setVisibility(View.GONE);

            baseApi.getUsuario(dni, new BaseApiCallback<BaseResponse<UsuarioResponse>>() {
                @Override
                public void onSuccess(BaseResponse<UsuarioResponse> response) {
                    UsuarioResponse usuario = response.getDatos();

                    lblNombreUsuarioReporte.setText(String.format("%s %s %s", usuario.getNombres(), usuario.getPaterno(), usuario.getMaterno()));
                    lblNombreUsuarioReporte.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(requireActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("ReportePorDni", "Error:" + t.getMessage());
                    tvErrorDniUsuarioReporte.setText(R.string.usuario_no_encontrado);
                    tvErrorDniUsuarioReporte.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Log.e("ReportePorDni", "Excepción en obtener usuario", e);
        }
    }

    private void imprimirBalanceUsuario() {
    }

    private void imprimirPagosRecibos() {
    }

    private void imprimirEgresos() {
        String url = Constantes.BASE_URL + "cajas/reporte/" + Constantes.TIPO_RUBRO_EGRESO.toUpperCase();

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No se encontró una aplicación para abrir el PDF", Toast.LENGTH_SHORT).show();
        }
    }
}