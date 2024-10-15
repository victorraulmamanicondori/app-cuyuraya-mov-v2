package com.eas.app;

import android.os.Bundle;
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
import com.eas.app.model.Usuario;
import com.eas.app.util.Constantes;
import com.eas.app.componentes.SpinnerItem;

import java.util.ArrayList;
import java.util.List;

public class PasoUbigeoFragment extends Fragment {

    private BaseApi baseApi;

    private View view;

    private Spinner spinnerDepartamento;
    private Spinner spinnerProvincia;
    private Spinner spinnerDistrito;

    private TextView lblCentroPoblado;
    private TextView lblComunidadParcialidad;
    private TextView lblComunidadNativa;

    private Spinner spinnerCentroPoblado;
    private Spinner spinnerComunidadParcialidad;
    private Spinner spinnerComunidadNativa;

    private TextView tvErrorDepartamento;
    private TextView tvErrorProvincia;
    private TextView tvErrorDistrito;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paso_ubigeo, container, false);

        spinnerDepartamento = view.findViewById(R.id.spinnerDepartamento);
        spinnerProvincia = view.findViewById(R.id.spinnerProvincia);
        spinnerDistrito = view.findViewById(R.id.spinnerDistrito);

        lblCentroPoblado = view.findViewById(R.id.lblCentroPoblado);
        lblComunidadParcialidad = view.findViewById(R.id.lblComunidadCampesina);
        lblComunidadNativa = view.findViewById(R.id.lblComunidadNativa);

        spinnerCentroPoblado = view.findViewById(R.id.spinnerCentroPoblado);
        spinnerComunidadParcialidad = view.findViewById(R.id.spinnerComunidadParcialidad);
        spinnerComunidadNativa = view.findViewById(R.id.spinnerComunidadNativa);

        tvErrorDepartamento = view.findViewById(R.id.tvErrorDepartamento);
        tvErrorProvincia = view.findViewById(R.id.tvErrorProvincia);
        tvErrorDistrito = view.findViewById(R.id.tvErrorDistrito);

        baseApi = new BaseApi(null);

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
                tvErrorDepartamento.setVisibility(View.GONE);
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
                tvErrorProvincia.setVisibility(View.GONE);
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
                tvErrorDistrito.setVisibility(View.GONE);
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
                departamentos.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));

                if (result != null && !result.isEmpty()) {
                    for (Departamento dep : result) {
                        departamentos.add(new SpinnerItem(dep.getCodigo(), dep.getNombre()));
                    }
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
        if (codigoDepartamento.isEmpty()) {
            List<SpinnerItem> provincias = new ArrayList<>();
            provincias.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
            ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, provincias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProvincia.setAdapter(adapter);
            return;
        }

        baseApi.getProvincias(codigoDepartamento, new BaseApiCallback<List<Provincia>>() {
            @Override
            public void onSuccess(List<Provincia> result) {
                List<SpinnerItem> provincias = new ArrayList<>();
                provincias.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));

                if (result != null && !result.isEmpty()) {
                    for (Provincia prov : result) {
                        provincias.add(new SpinnerItem(prov.getCodigo(), prov.getNombre()));
                    }
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
        if (codigoProvincia.isEmpty()) {
            List<SpinnerItem> distritos = new ArrayList<>();
            distritos.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
            ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, distritos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDistrito.setAdapter(adapter);
            return;
        }

        baseApi.getDistritos(codigoProvincia, new BaseApiCallback<List<Distrito>>() {
            @Override
            public void onSuccess(List<Distrito> result) {
                List<SpinnerItem> distritos = new ArrayList<>();
                distritos.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));

                if (result != null && !result.isEmpty()) {
                    for (Distrito distrito : result) {
                        distritos.add(new SpinnerItem(distrito.getCodigo(), distrito.getNombre()));
                    }
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
        if (codigoDistrito.isEmpty()) {
            view.findViewById(R.id.lblCentroPoblado).setVisibility(View.GONE);
            spinnerCentroPoblado.setVisibility(View.GONE);
            return;
        }

        baseApi.getCentroPoblado(codigoDistrito, new BaseApiCallback<List<CentroPoblado>>() {
            @Override
            public void onSuccess(List<CentroPoblado> result) {
                if (result == null || result.isEmpty()) {
                    spinnerCentroPoblado.setVisibility(View.GONE);
                    view.findViewById(R.id.lblCentroPoblado).setVisibility(View.GONE);
                } else {
                    lblCentroPoblado.setVisibility(View.VISIBLE);
                    spinnerCentroPoblado.setVisibility(View.VISIBLE);
                    List<SpinnerItem> centros = new ArrayList<>();
                    centros.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));

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
                Toast.makeText(getContext(), "Error al cargar centros poblados: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadComunidadCampesina(String codigoDistrito) {
        if (codigoDistrito.isEmpty()) {
            spinnerComunidadParcialidad.setVisibility(View.GONE);
            view.findViewById(R.id.lblComunidadCampesina).setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadCampesina(codigoDistrito, new BaseApiCallback<List<ComunidadCampesina>>() {
            @Override
            public void onSuccess(List<ComunidadCampesina> result) {
                if (result == null || result.isEmpty()) {
                    spinnerComunidadParcialidad.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadCampesina).setVisibility(View.GONE);
                } else {
                    lblComunidadParcialidad.setVisibility(View.VISIBLE);
                    spinnerComunidadParcialidad.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    comunidades.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
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
        if (codigoDistrito.isEmpty()) {
            spinnerComunidadNativa.setVisibility(View.GONE);
            view.findViewById(R.id.lblComunidadNativa).setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadNativa(codigoDistrito, new BaseApiCallback<List<ComunidadNativa>>() {
            @Override
            public void onSuccess(List<ComunidadNativa> result) {
                if (result == null || result.isEmpty()) {
                    spinnerComunidadNativa.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadNativa).setVisibility(View.GONE);
                } else {
                    lblComunidadNativa.setVisibility(View.VISIBLE);
                    spinnerComunidadNativa.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    comunidades.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
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

    public boolean validateFields() {
        boolean isValid = true;

        tvErrorDepartamento.setVisibility(View.GONE);
        tvErrorProvincia.setVisibility(View.GONE);
        tvErrorDistrito.setVisibility(View.GONE);

        int selectedItemPosition = spinnerDepartamento.getSelectedItemPosition();

        if (selectedItemPosition == -1 || selectedItemPosition == 0) {
            tvErrorDepartamento.setText(R.string.este_campo_es_obligatorio);
            tvErrorDepartamento.setVisibility(View.VISIBLE);
            isValid = false;
        }

        selectedItemPosition = spinnerProvincia.getSelectedItemPosition();

        if (selectedItemPosition == -1 || selectedItemPosition == 0) {
            tvErrorProvincia.setText(R.string.este_campo_es_obligatorio);
            tvErrorProvincia.setVisibility(View.VISIBLE);
            isValid = false;
        }

        selectedItemPosition = spinnerDistrito.getSelectedItemPosition();

        if (selectedItemPosition == -1 || selectedItemPosition == 0) {
            tvErrorDistrito.setText(R.string.este_campo_es_obligatorio);
            tvErrorDistrito.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    public void recolectarDatos() {
        RegistroUsuarioActivity activity = (RegistroUsuarioActivity) getActivity();
        if (activity != null) {
            Usuario usuario = activity.getUsuario();
            usuario.setCodigoDepartamento(((SpinnerItem)spinnerDepartamento.getSelectedItem()).getCodigo());
            usuario.setCodigoProvincia(((SpinnerItem)spinnerProvincia.getSelectedItem()).getCodigo());
            usuario.setCodigoDistrito(((SpinnerItem)spinnerDistrito.getSelectedItem()).getCodigo());

            if (spinnerCentroPoblado != null && spinnerCentroPoblado.getSelectedItemPosition() != -1 && spinnerCentroPoblado.getSelectedItemPosition() != 0) {
                usuario.setCodigoCentroPoblado(((SpinnerItem)spinnerCentroPoblado.getSelectedItem()).getCodigo());
            }

            if (spinnerComunidadParcialidad != null && spinnerComunidadParcialidad.getSelectedItemPosition() != -1 && spinnerComunidadParcialidad.getSelectedItemPosition() != 0) {
                usuario.setCodigoComunidadCampesina(((SpinnerItem)spinnerComunidadParcialidad.getSelectedItem()).getCodigo());
            }

            if (spinnerComunidadNativa != null && spinnerComunidadNativa.getSelectedItemPosition() != -1 && spinnerComunidadNativa.getSelectedItemPosition() != 0) {
                usuario.setCodigoComunidadNativa(((SpinnerItem)spinnerComunidadNativa.getSelectedItem()).getCodigo());
            }
        }
    }
}
