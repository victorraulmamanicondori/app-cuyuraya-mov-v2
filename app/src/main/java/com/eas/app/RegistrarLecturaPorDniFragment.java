package com.eas.app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.eas.app.api.request.LecturaActualRequest;
import com.eas.app.api.response.AnomaliaResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.LecturaActualResponse;
import com.eas.app.api.response.UsuarioResponse;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;
import com.eas.app.util.DialogUtils;
import com.eas.app.viewmodel.UsuarioViewModel;

import java.util.List;

public class RegistrarLecturaPorDniFragment extends Fragment {

    private EditText txtDniUsuario;
    private EditText txtCodigoMedidor;
    private EditText txtLecturaActual;

    private TextView lblNombreUsuario;
    private TextView tvErrorDniUsuario;
    private TextView tvErrorCodigoMedidor;
    private TextView tvErrorLecturaActual;
    
    private ImageButton btnBuscarPorDni;
    private ImageButton btnBuscarPorCodigo;

    private UsuarioViewModel usuarioViewModel;
    private UsuarioResponse usuarioResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_lectura_por_dni, container, false);

        txtDniUsuario = view.findViewById(R.id.txtDniUsuario);
        txtCodigoMedidor = view.findViewById(R.id.txtCodigoMedidor);
        txtLecturaActual = view.findViewById(R.id.txtLecturaActual);

        lblNombreUsuario = view.findViewById(R.id.lblNombreUsuario);
        tvErrorDniUsuario = view.findViewById(R.id.tvErrorDniUsuario);
        tvErrorCodigoMedidor = view.findViewById(R.id.tvErrorCodigoMedidor);
        tvErrorLecturaActual = view.findViewById(R.id.tvErrorLecturaActual);

        btnBuscarPorDni = view.findViewById(R.id.btnBuscarPorDni);
        btnBuscarPorDni.setOnClickListener(v -> buscarLecturasPorDni());

        btnBuscarPorCodigo = view.findViewById(R.id.btnBuscarPorCodigo);
        btnBuscarPorCodigo.setOnClickListener(v -> buscarLecturasPorCodigo());
        
        Button detectarAnomaliasButton = view.findViewById(R.id.btnDetectarAnomalias);
        detectarAnomaliasButton.setOnClickListener(v -> detectarAnomaliasConsumo());

        Button registrarLecturaButton = view.findViewById(R.id.btnRegistrarLectura);
        registrarLecturaButton.setOnClickListener(v -> registrarLectura());

        usuarioViewModel = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class);
        usuarioViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            usuarioResponse = data;

            txtDniUsuario.setText(usuarioResponse.getDni());
            lblNombreUsuario.setText(String.format("%s %s %s", usuarioResponse.getNombres(), usuarioResponse.getPaterno(), usuarioResponse.getMaterno()));
            txtCodigoMedidor.setText(usuarioResponse.getCodigoMedidor().trim());
        });

        return view;
    }

    private void buscarLecturasPorDni() {
        try {
            lblNombreUsuario.setText("");
            lblNombreUsuario.setVisibility(View.GONE);

            String token = Almacenamiento.obtener(requireActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String dni = txtDniUsuario.getText().toString().trim();

            if (dni.isEmpty()) {
                tvErrorDniUsuario.setText(R.string.este_campo_es_obligatorio);
                tvErrorDniUsuario.setVisibility(View.VISIBLE);
                return;
            }

            tvErrorDniUsuario.setVisibility(View.GONE);

            baseApi.getUsuario(dni, new BaseApiCallback<BaseResponse<UsuarioResponse>>() {
                @Override
                public void onSuccess(BaseResponse<UsuarioResponse> response) {
                    UsuarioResponse usuario = response.getDatos();

                    lblNombreUsuario.setText(String.format("%s %s %s", usuario.getNombres(), usuario.getPaterno(), usuario.getMaterno()));
                    lblNombreUsuario.setVisibility(View.VISIBLE);

                    if (usuario.getCodigoMedidor() != null && !usuario.getCodigoMedidor().trim().isEmpty()) {
                        txtCodigoMedidor.setText(usuario.getCodigoMedidor().trim());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(requireActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("RegistrarLecturaPorDni", "Error:" + t.getMessage());
                    tvErrorDniUsuario.setText(R.string.usuario_no_encontrado);
                    tvErrorDniUsuario.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Log.e("RegistrarLecturaPorDni", "Excepción en obtener usuario", e);
        }
    }

    private void buscarLecturasPorCodigo() {
    }

    private void limpiarCampos() {
        lblNombreUsuario.setText("");
        lblNombreUsuario.setVisibility(View.GONE);
        tvErrorDniUsuario.setVisibility(View.GONE);
    }

    private void detectarAnomaliasConsumo() {
        if (txtCodigoMedidor.getText().toString().trim().isEmpty()) {
            tvErrorCodigoMedidor.setText(R.string.este_campo_es_obligatorio);
            tvErrorCodigoMedidor.setVisibility(View.VISIBLE);

            DialogUtils.showAlertDialog(
                    getActivity(),
                    Constantes.TITULO_ADVERTENCIA,
                    Constantes.CAMPO_OBLIGATORIO_DETECTOR_ANOMALIAS,
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            );

            return;
        }

        try {
            String token = Almacenamiento.obtener(getActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String codigoMedidor = txtCodigoMedidor.getText().toString().trim();

            baseApi.detectarAnomalias(codigoMedidor, new BaseApiCallback<BaseResponse<List<AnomaliaResponse>>>() {
                @Override
                public void onSuccess(BaseResponse<List<AnomaliaResponse>> response) {
                    StringBuilder mensaje = getListaAnomalias(response);

                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_INFORMATION,
                            mensaje.toString(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                }

                @Override
                public void onError(Throwable t) {
                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_ERROR,
                            t.getMessage(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                }
            });

        } catch (Exception e) {
            DialogUtils.showAlertDialog(
                    getActivity(),
                    Constantes.TITULO_ERROR,
                    e.getMessage(),
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            );
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
            String token = Almacenamiento.obtener(getActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String codigoMedidor = txtCodigoMedidor.getText().toString().trim();
            String lecturaActual = txtLecturaActual.getText().toString().trim();

            LecturaActualRequest lecturaActualRequest = new LecturaActualRequest(codigoMedidor, lecturaActual);

            baseApi.registrarLectura(lecturaActualRequest, new BaseApiCallback<BaseResponse<LecturaActualResponse>>() {
                @Override
                public void onSuccess(BaseResponse<LecturaActualResponse> response) {
                    DialogUtils.showAlertDialog(
                            getActivity(),
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

                    Log.d("RegistrarLectura", "Registro exitoso de la lectura");
                }

                @Override
                public void onError(Throwable t) {
                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_REGISTRO_FALLIDO,
                            t.getMessage(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                    Log.e("RegistrarLectura", "Error en registrar lectura: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            DialogUtils.showAlertDialog(
                    getActivity(),
                    Constantes.TITULO_REGISTRO_FALLIDO,
                    e.getMessage(),
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            );
            Log.e("RegistrarLectura", "Excepción en registrar lectura", e);
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        tvErrorDniUsuario.setVisibility(View.GONE);
        tvErrorCodigoMedidor.setVisibility(View.GONE);
        tvErrorLecturaActual.setVisibility(View.GONE);

        if (txtDniUsuario.getText().toString().trim().isEmpty()) {
            tvErrorDniUsuario.setText(R.string.este_campo_es_obligatorio);
            tvErrorDniUsuario.setVisibility(View.VISIBLE);
            isValid = false;
        }

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