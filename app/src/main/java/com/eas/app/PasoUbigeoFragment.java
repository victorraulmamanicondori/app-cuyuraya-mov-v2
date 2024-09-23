package com.eas.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.model.CentroPoblado;
import com.eas.app.model.ComunidadCampesina;
import com.eas.app.model.ComunidadNativa;
import com.eas.app.model.Departamento;
import com.eas.app.model.Distrito;
import com.eas.app.model.Provincia;
import com.eas.app.utils.Almacenamiento;
import com.eas.componentes.SpinnerItem;

import java.util.ArrayList;
import java.util.List;

public class PasoUbigeoFragment extends Fragment {

    private BaseApi baseApi;

    private View view;

    private TextView lblCentroPoblado;
    private TextView lblComunidadParcialidad;
    private TextView lblSector;
    private TextView lblComunidadNativa;

    private Spinner spinnerDepartamento;
    private Spinner spinnerProvincia;
    private Spinner spinnerDistrito;
    private Spinner spinnerCentroPoblado;
    private Spinner spinnerComunidadParcialidad;
    private Spinner spinnerSector;
    private Spinner spinnerComunidadNativa;

    private TextView tvErrorDepartamento;
    private TextView tvErrorProvincia;
    private TextView tvErrorDistrito;
    private TextView tvErrorCentroPoblado;
    private TextView tvErrorComunidadCampesina;
    private TextView tvErrorSector;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paso_ubigeo, container, false);

        // Inicializar el Spinner
        spinnerDepartamento = view.findViewById(R.id.spinnerDepartamento);
        spinnerProvincia = view.findViewById(R.id.spinnerProvincia);
        spinnerDistrito = view.findViewById(R.id.spinnerDistrito);
        spinnerCentroPoblado = view.findViewById(R.id.spinnerCentroPoblado);
        spinnerComunidadParcialidad = view.findViewById(R.id.spinnerComunidadParcialidad);
        spinnerSector = view.findViewById(R.id.spinnerSector);
        spinnerComunidadNativa = view.findViewById(R.id.spinnerComunidadNativa);

        lblCentroPoblado = view.findViewById(R.id.lblCentroPoblado);
        lblComunidadParcialidad = view.findViewById(R.id.lblComunidadParcialidad);
        lblSector = view.findViewById(R.id.lblSector);
        lblComunidadNativa = view.findViewById(R.id.lblComunidadNativa);

        // Inicializar TextViews de error
        tvErrorDepartamento = view.findViewById(R.id.tvErrorDepartamento);
        tvErrorProvincia = view.findViewById(R.id.tvErrorProvincia);
        tvErrorDistrito = view.findViewById(R.id.tvErrorDistrito);
        tvErrorCentroPoblado = view.findViewById(R.id.tvErrorCentroPoblado);
        tvErrorComunidadCampesina = view.findViewById(R.id.tvErrorComunidadParcialidad);
        tvErrorSector = view.findViewById(R.id.tvErrorSector);

        // Inicializar BaseApi con token (puedes obtenerlo de SharedPreferences o donde lo almacenes)
        String token = Almacenamiento.obtener(getContext(), "accessToken");

        if (token == null) {
            // Manejar el caso en que el token no está disponible
            Toast.makeText(getContext(), "Token no disponible", Toast.LENGTH_LONG).show();
            return view;
        }

        baseApi = new BaseApi(token);

        // Cargar departamentos
        loadDepartamentos();

        // Configurar listeners
        configurarListeners();

        return view;
    }

    private void configurarListeners() {
        spinnerDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem departamentoSeleccionado = (SpinnerItem) parent.getSelectedItem();
                if (departamentoSeleccionado != null) {
                    loadProvincias(departamentoSeleccionado.getCodigo());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem provinciaSeleccionada = (SpinnerItem) parent.getSelectedItem();
                if (provinciaSeleccionada != null) {
                    loadDistritos(provinciaSeleccionada.getCodigo());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerDistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem distritoSeleccionado = (SpinnerItem) parent.getSelectedItem();
                if (distritoSeleccionado != null) {
                    loadCentroPoblado(distritoSeleccionado.getCodigo());
                    loadComunidadCampesina(distritoSeleccionado.getCodigo());
                    loadComunidadNativa(distritoSeleccionado.getCodigo());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void loadDepartamentos() {
        baseApi.getDepartamentos(new BaseApiCallback<List<Departamento>>() {
            @Override
            public void onSuccess(List<Departamento> result) {
                List<SpinnerItem> departamentos = new ArrayList<>();
                for (Departamento dep : result) {
                    departamentos.add(new SpinnerItem(dep.getCodigo(), dep.getNombre()));
                }
                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, departamentos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDepartamento.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Error al cargar departamentos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadProvincias(String codigoDepartamento) {
        Log.d("PasoUbigeoFragment", "Cargando provincias para el departamento: " + codigoDepartamento);

        baseApi.getProvincias(codigoDepartamento, new BaseApiCallback<List<Provincia>>() {
            @Override
            public void onSuccess(List<Provincia> result) {
                List<SpinnerItem> provincias = new ArrayList<>();
                for (Provincia prov : result) {
                    provincias.add(new SpinnerItem(prov.getCodigo(), prov.getNombre()));
                }
                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, provincias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProvincia.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Error al cargar provincias: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadDistritos(String codigoProvincia) {
        baseApi.getDistritos(codigoProvincia, new BaseApiCallback<List<Distrito>>() {
            @Override
            public void onSuccess(List<Distrito> result) {
                List<SpinnerItem> distritos = new ArrayList<>();
                for (Distrito distrito : result) {
                    distritos.add(new SpinnerItem(distrito.getCodigo(), distrito.getNombre()));
                }
                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, distritos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDistrito.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Error al cargar distritos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCentroPoblado(String codigoDistrito) {
        baseApi.getCentroPoblado(codigoDistrito, new BaseApiCallback<List<CentroPoblado>>() {
            @Override
            public void onSuccess(List<CentroPoblado> result) {
                if (result.isEmpty()) {
                    // Ocultar Spinner y etiqueta
                    spinnerCentroPoblado.setVisibility(View.GONE);
                    view.findViewById(R.id.lblCentroPoblado).setVisibility(View.GONE);
                } else {
                    // Mostrar Spinner y llenar datos
                    lblCentroPoblado.setVisibility(View.VISIBLE);
                    spinnerCentroPoblado.setVisibility(View.VISIBLE);
                    List<SpinnerItem> centros = new ArrayList<>();
                    for (CentroPoblado cp : result) {
                        centros.add(new SpinnerItem(cp.getCodigo(), cp.getNombre()));
                    }
                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, centros);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCentroPoblado.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable t) {
                // Manejar error si es necesario
                Toast.makeText(getContext(), "Error al cargar centros poblados: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadComunidadCampesina(String codigoDistrito) {
        baseApi.getComunidadCampesina(codigoDistrito, new BaseApiCallback<List<ComunidadCampesina>>() {
            @Override
            public void onSuccess(List<ComunidadCampesina> result) {
                if (result.isEmpty()) {
                    spinnerComunidadParcialidad.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadParcialidad).setVisibility(View.GONE);
                } else {
                    lblComunidadParcialidad.setVisibility(View.VISIBLE);
                    spinnerComunidadParcialidad.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    for (ComunidadCampesina cc : result) {
                        comunidades.add(new SpinnerItem(cc.getCodigo(), cc.getNombre()));
                    }
                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, comunidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerComunidadParcialidad.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Error al cargar comunidades campesinas: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadComunidadNativa(String codigoDistrito) {
        baseApi.getComunidadNativa(codigoDistrito, new BaseApiCallback<List<ComunidadNativa>>() {
            @Override
            public void onSuccess(List<ComunidadNativa> result) {
                if (result.isEmpty()) {
                    spinnerComunidadNativa.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadNativa).setVisibility(View.GONE);
                } else {
                    lblComunidadNativa.setVisibility(View.VISIBLE);
                    spinnerComunidadNativa.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    for (ComunidadNativa cn : result) {
                        comunidades.add(new SpinnerItem(cn.getCodigo(), cn.getNombre()));
                    }
                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, comunidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerComunidadNativa.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Error al cargar comunidades nativas: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
