/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eas.app.model.Usuario;

public class PasoDatosPersonalesFragment extends Fragment {

    private EditText etNombres;
    private EditText etPaterno;
    private EditText etMaterno;
    private EditText etDni;
    private TextView tvErrorNombres;
    private TextView tvErrorPaterno;
    private TextView tvErrorMaterno;
    private TextView tvErrorDni;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paso_datos_personales, container, false);

        etNombres = view.findViewById(R.id.txtNombres);
        etPaterno = view.findViewById(R.id.txtPaterno);
        etMaterno = view.findViewById(R.id.txtMaterno);
        etDni = view.findViewById(R.id.txtDni);

        tvErrorNombres = view.findViewById(R.id.tvErrorNombres);
        tvErrorPaterno = view.findViewById(R.id.tvErrorPaterno);
        tvErrorMaterno = view.findViewById(R.id.tvErrorMaterno);
        tvErrorDni = view.findViewById(R.id.tvErrorDni);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        RegistroUsuarioActivity activity = (RegistroUsuarioActivity) getActivity();
        if (activity != null) {
            Usuario usuario = activity.getUsuario();
            usuario.setNombres(etNombres.getText().toString().trim());
            usuario.setPaterno(etPaterno.getText().toString().trim());
            usuario.setMaterno(etMaterno.getText().toString().trim());
            usuario.setDni(etDni.getText().toString().trim());
            usuario.setClave(etDni.getText().toString().trim()); // Establecer la clave como el DNI
        }
    }

    public boolean validateFields() {
        boolean isValid = true;

        // Reset visibility de los mensajes de error
        tvErrorNombres.setVisibility(View.GONE);
        tvErrorPaterno.setVisibility(View.GONE);
        tvErrorMaterno.setVisibility(View.GONE);
        tvErrorDni.setVisibility(View.GONE);

        // Validar que el campo de nombres no esté vacío
        if (etNombres.getText().toString().trim().isEmpty()) {
            tvErrorNombres.setText(R.string.este_campo_es_obligatorio);
            tvErrorNombres.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Validar que el campo de apellido paterno no esté vacío
        if (etPaterno.getText().toString().trim().isEmpty()) {
            tvErrorPaterno.setText(R.string.este_campo_es_obligatorio);
            tvErrorPaterno.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Validar que el campo de apellido materno no esté vacío
        if (etMaterno.getText().toString().trim().isEmpty()) {
            tvErrorMaterno.setText(R.string.este_campo_es_obligatorio);
            tvErrorMaterno.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Validar que el campo de DNI no esté vacío
        if (etDni.getText().toString().trim().isEmpty()) {
            tvErrorDni.setText(R.string.este_campo_es_obligatorio);
            tvErrorDni.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }
}

