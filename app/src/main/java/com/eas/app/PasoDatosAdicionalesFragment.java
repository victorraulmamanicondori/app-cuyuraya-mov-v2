package com.eas.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eas.app.model.Usuario;

public class PasoDatosAdicionalesFragment extends Fragment {
    private EditText etTelefono;
    private EditText etDireccion;
    private EditText etNumeroContrato;

    private TextView tvErrorDireccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paso_datos_adicionales, container, false);

        etTelefono = view.findViewById(R.id.txtTelefono);
        etDireccion = view.findViewById(R.id.txtDireccion);
        etNumeroContrato = view.findViewById(R.id.txtNumeroContrato);

        tvErrorDireccion = view.findViewById(R.id.tvErrorDireccion);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        RegistroUsuarioActivity activity = (RegistroUsuarioActivity) getActivity();
        if (activity != null) {
            Usuario usuario = activity.getUsuario();
            if (etTelefono.getText().toString().trim().isEmpty()) {
                usuario.setTelefono(etTelefono.getText().toString().trim());
            }

            usuario.setDireccion(etDireccion.getText().toString().trim());

            if (etNumeroContrato.getText().toString().trim().isEmpty()) {
                usuario.setNumeroContrato(etNumeroContrato.getText().toString().trim());
            }
        }
    }

    public boolean validateFields() {
        boolean isValid = true;

        tvErrorDireccion.setVisibility(View.GONE);

        if (etDireccion.getText().toString().trim().isEmpty()) {
            tvErrorDireccion.setText(R.string.este_campo_es_obligatorio);
            tvErrorDireccion.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }
}