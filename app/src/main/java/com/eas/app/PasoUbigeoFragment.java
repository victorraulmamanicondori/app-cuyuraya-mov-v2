package com.eas.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.model.Departamento;
import com.eas.app.utils.Almacenamiento;

import java.util.ArrayList;
import java.util.List;

public class PasoUbigeoFragment extends Fragment {

    private Spinner spinnerDepartamento;
    private BaseApi baseApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paso_ubigeo, container, false);

        // Inicializar el Spinner
        spinnerDepartamento = view.findViewById(R.id.spinnerDepartamento);

        // Inicializar BaseApi con token (puedes obtenerlo de SharedPreferences o donde lo almacenes)
        String token = Almacenamiento.obtener(getContext(), "accessToken");

        if (token == null) {
            // Manejar el caso en que el token no está disponible
            Toast.makeText(getContext(), "Token no disponible", Toast.LENGTH_LONG).show();
            return view;
        }

        baseApi = new BaseApi(token);

        // Llamar a la API para obtener los departamentos
        loadDepartamentos();

        return view;
    }

    private void loadDepartamentos() {
        baseApi.getDepartamentos(new BaseApiCallback<List<Departamento>>() {
            @Override
            public void onSuccess(List<Departamento> result) {
                // Llenar el Spinner con los datos recibidos
                List<String> departamentoNames = new ArrayList<>();
                for (Departamento departamento : result) {
                    departamentoNames.add(departamento.getNombre()); // Asume que tienes un método getNombre()
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, departamentoNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDepartamento.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                // Mostrar error en caso de fallo de la API
                Toast.makeText(getContext(), "Error al cargar departamentos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
