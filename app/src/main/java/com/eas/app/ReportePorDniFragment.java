/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

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

    private Button btnImprimirIngresos;
    private Button btnImprimirEgresos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reporte_por_dni, container, false);

        btnImprimirIngresos = view.findViewById(R.id.btnImprimirIngresos);
        btnImprimirEgresos = view.findViewById(R.id.btnImprimirEgresos);

        btnImprimirIngresos.setOnClickListener(v -> imprimirIngresos());
        btnImprimirEgresos.setOnClickListener(v -> imprimirEgresos());

        return view;
    }

    private void imprimirIngresos() {
        String url = Constantes.BASE_URL + "cajas/reporte/" + Constantes.TIPO_RUBRO_INGRESO.toUpperCase();

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No se encontró una aplicación para abrir el PDF", Toast.LENGTH_SHORT).show();
        }
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