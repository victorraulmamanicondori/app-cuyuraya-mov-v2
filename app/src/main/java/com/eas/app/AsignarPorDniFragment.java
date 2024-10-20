package com.eas.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.BaseApi;
import com.eas.app.api.request.AsignarMedidorRequest;
import com.eas.app.api.response.AsignarMedidorResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.UsuarioResponse;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;
import com.eas.app.util.DialogUtils;

public class AsignarPorDniFragment extends Fragment {

    private EditText etCodigoMedidor;
    private EditText etDNIUsuario;

    private TextView tvErrorCodigoMedidor;
    private TextView tvErrorDNIUsuario;

    private TextView tvUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_por_dni, container, false);

        etCodigoMedidor = view.findViewById(R.id.txtCodigoMedidor);
        etDNIUsuario = view.findViewById(R.id.txtDniUsuario);

        tvErrorCodigoMedidor = view.findViewById(R.id.tvErrorCodigoMedidor);
        tvErrorDNIUsuario = view.findViewById(R.id.tvErrorDNIUsuario);

        tvUsuario = view.findViewById(R.id.lblUsuario);

        Button asignarMedidorButton = view.findViewById(R.id.btnAsignarMedidor);
        asignarMedidorButton.setOnClickListener(v -> asignarMedidor());

        etDNIUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvErrorDNIUsuario.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 8) {
                    etDNIUsuario.setEnabled(false);

                    try {
                        AsignarMedidorActivity activity = (AsignarMedidorActivity) getActivity();

                        assert activity != null;

                        String token = Almacenamiento.obtener(activity, Constantes.KEY_ACCESS_TOKEN);
                        BaseApi baseApi = new BaseApi(token);

                        String dni = etDNIUsuario.getText().toString().trim();

                        baseApi.getUsuario(dni, new BaseApiCallback<BaseResponse<UsuarioResponse>>() {
                            @Override
                            public void onSuccess(BaseResponse<UsuarioResponse> response) {
                                tvUsuario.setVisibility(View.VISIBLE);

                                UsuarioResponse usuario = response.getDatos();
                                String infoUsuario = String.format("Usuario: %s %s %s\nDepartamento: %s\nProvincia: %s\nDistrito: %s\nCentro Poblado: %s\nComunidad Campesina: %s\nComunidad Nativa: %s\nDirección: %s\nMedidor: %s",
                                        usuario.getNombres(), usuario.getPaterno(), usuario.getMaterno(),
                                        usuario.getNombreDepartamento(),
                                        usuario.getNombreProvincia(),
                                        usuario.getNombreDistrito(),
                                        (usuario.getNombreCentroPoblado() != null ? usuario.getNombreCentroPoblado() : ""),
                                        (usuario.getNombreComunidadCampesina() != null ? usuario.getNombreComunidadCampesina() : ""),
                                        (usuario.getNombreComunidadNativa() != null ? usuario.getNombreComunidadNativa() : ""),
                                        usuario.getDireccion(),
                                        (usuario.getCodigoMedidor() != null ? usuario.getCodigoMedidor() : ""));
                                tvUsuario.setText(infoUsuario);
                                etDNIUsuario.setEnabled(true);

                                if (usuario.getCodigoMedidor() != null && !usuario.getCodigoMedidor().trim().isEmpty()) {
                                    etCodigoMedidor.setText(usuario.getCodigoMedidor().trim());
                                }

                                Log.d("AsignarMedidor", infoUsuario);
                            }

                            @Override
                            public void onError(Throwable t) {
                                etDNIUsuario.setEnabled(true);
                                Log.e("AsignarMedidor", "Error en obtener usuario: " + t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        etDNIUsuario.setEnabled(true);
                        Log.e("AsignarMedidor", "Excepción en obtener usuario", e);
                    }
                } else {
                    tvUsuario.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void asignarMedidor() {
        AsignarMedidorActivity activity = (AsignarMedidorActivity) getActivity();

        assert activity != null;

        if (!validateFields()) {
            return;
        }

        try {
            String token = Almacenamiento.obtener(activity, Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String codigoMedidor = etCodigoMedidor.getText().toString().trim();
            String dni = etDNIUsuario.getText().toString().trim();

            AsignarMedidorRequest asignarMedidorRequest = new AsignarMedidorRequest(codigoMedidor, dni);

            baseApi.asignarMedidor(asignarMedidorRequest, new BaseApiCallback<BaseResponse<AsignarMedidorResponse>>() {
                @Override
                public void onSuccess(BaseResponse<AsignarMedidorResponse> response) {
                    DialogUtils.showAlertDialog(
                        activity,
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

                    Log.d("AsignarMedidor", "Asignacion exitosa del medidor");
                }

                @Override
                public void onError(Throwable t) {
                    DialogUtils.showAlertDialog(
                        activity,
                        Constantes.TITULO_ASIGNACION_FALLIDA,
                        t.getMessage(),
                        Constantes.BOTON_TEXTO_ACEPTAR,
                        (dialog, which) -> dialog.dismiss(),
                        null,
                        null
                    );
                    Log.e("AsignarMedidor", "Error en asignar medidor: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            DialogUtils.showAlertDialog(
                activity,
                Constantes.TITULO_ERROR,
                e.getMessage(),
                Constantes.BOTON_TEXTO_ACEPTAR,
                (dialog, which) -> dialog.dismiss(),
                null,
                null
            );
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