/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

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
    private EditText etDireccion;
    private TextView tvErrorDireccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paso_datos_adicionales, container, false);

        etDireccion = view.findViewById(R.id.txtDireccion);
        tvErrorDireccion = view.findViewById(R.id.tvErrorDireccion);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        RegistroUsuarioActivity activity = (RegistroUsuarioActivity) getActivity();
        if (activity != null) {
            Usuario usuario = activity.getUsuario();
            usuario.setDireccion(etDireccion.getText().toString().trim());
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