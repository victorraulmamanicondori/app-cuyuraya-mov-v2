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
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.request.UbigeoRequest;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.UsuarioResponse;
import com.eas.app.componentes.SpinnerItem;
import com.eas.app.model.CentroPoblado;
import com.eas.app.model.ComunidadCampesina;
import com.eas.app.model.ComunidadNativa;
import com.eas.app.model.Departamento;
import com.eas.app.model.Distrito;
import com.eas.app.model.Provincia;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;
import com.eas.app.util.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportePorUbigeoFragment extends Fragment {
    private BaseApi baseApi;

    private View view;

    private Spinner spinDepartamento;
    private Spinner spinProvincia;
    private Spinner spinDistrito;
    private Spinner spinCentroPoblado;
    private Spinner spinComunidadCampesina;
    private Spinner spinComunidadNativa;

    private TableLayout tableLayout;
    private List<UsuarioResponse> userList = new ArrayList<>();
    private TextView pagination;
    private int pageSize = 4;
    private int currentPage = 0;
    private int totalPages = 0;
    private int totalUsuarios = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reporte_por_ubigeo, container, false);

        spinDepartamento = view.findViewById(R.id.spinDepartamentoReporte);
        spinProvincia = view.findViewById(R.id.spinProvinciaReporte);
        spinDistrito = view.findViewById(R.id.spinDistritoReporte);
        spinCentroPoblado = view.findViewById(R.id.spinCentroPobladoReporte);
        spinComunidadCampesina = view.findViewById(R.id.spinComunidadCampesinaReporte);
        spinComunidadNativa = view.findViewById(R.id.spinComunidadNativaReporte);

        tableLayout = view.findViewById(R.id.tableLayoutReporte);

        view.findViewById(R.id.btnInicioReporte).setOnClickListener(v -> loadPage(0));
        view.findViewById(R.id.btnAtrasReporte).setOnClickListener(v -> loadPage(currentPage - 1));
        view.findViewById(R.id.btnAdelanteReporte).setOnClickListener(v -> loadPage(currentPage + 1));
        view.findViewById(R.id.btnFinalReporte).setOnClickListener(v -> loadPage(userList.size() / pageSize));
        view.findViewById(R.id.btnImprimirPadronUsuarios).setOnClickListener(v -> imprimirPadronUsuarios());

        pagination = view.findViewById(R.id.paginacionTablaUsuariosReporte);

        String token = Almacenamiento.obtener(requireActivity(), Constantes.KEY_ACCESS_TOKEN);
        baseApi = new BaseApi(token);

        loadDepartamentos();

        configurarListeners();

        loadPage(currentPage);

        return view;
    }

    private void imprimirPadronUsuarios() {

        UbigeoRequest request = getUbigeoRequest();

        String url = Constantes.BASE_URL + "usuarios/padron/" + request.getCodigoDistrito() + "?p=1";

        try {
            if (request.getCodigoCentroPoblado() != null && !request.getCodigoCentroPoblado().trim().isEmpty()) {
                url = url + "&codigoCentroPoblado=" + request.getCodigoCentroPoblado().trim();
            }

            if (request.getCodigoComunidadCampesina() != null && !request.getCodigoComunidadCampesina().trim().isEmpty()) {
                url = url + "&codigoComunidadCampesina=" + request.getCodigoComunidadCampesina().trim();
            }

            if (request.getCodigoComunidadNativa() != null && !request.getCodigoComunidadNativa().trim().isEmpty()) {
                url = url + "&codigoComunidadNativa=" + request.getCodigoComunidadNativa().trim();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No se encontró una aplicación para abrir el PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarListeners() {
        spinDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinDistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem distritoSeleccionado = (SpinnerItem) parent.getSelectedItem();
                if (distritoSeleccionado != null) {
                    loadCentroPoblado(distritoSeleccionado.getCodigo());
                    loadComunidadCampesina(distritoSeleccionado.getCodigo());
                    loadComunidadNativa(distritoSeleccionado.getCodigo());
                    getUserData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinCentroPoblado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem centroPobladoSeleccionado = (SpinnerItem) adapterView.getSelectedItem();
                if (centroPobladoSeleccionado != null) {
                    getUserData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinComunidadCampesina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem comunidadCampesinaSeleccionado = (SpinnerItem) adapterView.getSelectedItem();
                if (comunidadCampesinaSeleccionado != null) {
                    getUserData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinComunidadNativa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem comunidadNativaSeleccionado = (SpinnerItem) adapterView.getSelectedItem();
                if (comunidadNativaSeleccionado != null) {
                    getUserData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getUserData() {
        UbigeoRequest request = getUbigeoRequest();
        currentPage = 0;
        totalPages = 0;
        totalUsuarios = 0;
        userList = new ArrayList<>();

        if (request.getCodigoDistrito() != null && !request.getCodigoDistrito().isEmpty()) {
            baseApi.listarUsuariosPorUbigeo(request, new BaseApiCallback<BaseResponse<List<UsuarioResponse>>>() {
                @Override
                public void onSuccess(BaseResponse<List<UsuarioResponse>> result) {
                    if (result != null && result.getDatos() != null && !result.getDatos().isEmpty()) {
                        Log.i("UsuariosPorUbigeo", "Actualizando lista de usuarios");
                        userList = result.getDatos();
                    }
                    loadPage(currentPage);
                }

                @Override
                public void onError(Throwable t) {
                    loadPage(currentPage);
                    Toast.makeText(getContext(), "Error al listar usuarios por ubigeo: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            loadPage(currentPage);
        }
    }

    private UbigeoRequest getUbigeoRequest() {
        SpinnerItem distritoSeleccionado = (SpinnerItem) spinDistrito.getSelectedItem();
        SpinnerItem centroPobladoSeleccionado = (SpinnerItem) spinCentroPoblado.getSelectedItem();
        SpinnerItem comunidadCampesinaSeleccionado = (SpinnerItem) spinComunidadCampesina.getSelectedItem();
        SpinnerItem comunidadNativaSeleccionado = (SpinnerItem) spinComunidadNativa.getSelectedItem();

        UbigeoRequest request = new UbigeoRequest();
        request.setCodigoDistrito(distritoSeleccionado.getCodigo());

        if (centroPobladoSeleccionado != null) {
            request.setCodigoCentroPoblado(centroPobladoSeleccionado.getCodigo());
        }

        if (comunidadCampesinaSeleccionado != null) {
            request.setCodigoComunidadCampesina(comunidadCampesinaSeleccionado.getCodigo());
        }

        if (comunidadNativaSeleccionado != null) {
            request.setCodigoComunidadNativa(comunidadNativaSeleccionado.getCodigo());
        }

        return request;
    }

    private void loadPage(int page) {
        Log.i("LoadPage", "page=" + page);

        totalUsuarios = userList.size();
        totalPages = (int) Math.ceil((double) userList.size() / pageSize);
        currentPage = Math.max(0, Math.min(page, totalPages - 1));
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, userList.size());
        pagination.setText(String.format("Total Usuarios: %s, Página %s / %s", totalUsuarios, currentPage + 1, totalPages));

        tableLayout.removeAllViews(); // Limpiar la tabla

        // Agregar cabecera
        TableRow headerRow = new TableRow(getContext());
        headerRow.addView(createTextView("DNI"));
        headerRow.addView(createTextView("Medidor"));
        headerRow.addView(createTextView("Usuario"));

        headerRow.setBackgroundColor(getResources().getColor(R.color.primaryColor)); // Asegúrate de tener el color definido en colors.xml

        tableLayout.addView(headerRow);

        // Agregar filas
        for (int i = start; i < end; i++) {
            UsuarioResponse user = userList.get(i);
            TableRow row = new TableRow(getContext());

            if (i % 2 == 0) {
                row.setBackgroundColor(getResources().getColor(android.R.color.white)); // Blanco para filas impares
            } else {
                row.setBackgroundColor(getResources().getColor(R.color.gray_400)); // Gris claro para filas pares
            }

            row.addView(createTextView(user.getDni()));
            row.addView(createTextView(user.getCodigoMedidor()));
            row.addView(createTextView(String.format("%s %s %s", user.getNombres(), user.getPaterno(), user.getMaterno())));

            tableLayout.addView(row);
        }
    }

    private View createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        return textView;
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
                spinDepartamento.setAdapter(adapter);
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
            spinProvincia.setAdapter(adapter);
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
                spinProvincia.setAdapter(adapter);
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
            spinDistrito.setAdapter(adapter);
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
                spinDistrito.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Error al cargar distritos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCentroPoblado(String codigoDistrito) {
        if (codigoDistrito.isEmpty()) {
            view.findViewById(R.id.lblCentroPobladoReporte).setVisibility(View.GONE);
            spinCentroPoblado.setVisibility(View.GONE);
            return;
        }

        baseApi.getCentroPoblado(codigoDistrito, new BaseApiCallback<List<CentroPoblado>>() {
            @Override
            public void onSuccess(List<CentroPoblado> result) {
                if (result == null || result.isEmpty()) {
                    spinCentroPoblado.setVisibility(View.GONE);
                    view.findViewById(R.id.lblCentroPobladoReporte).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.lblCentroPobladoReporte).setVisibility(View.VISIBLE);
                    spinCentroPoblado.setVisibility(View.VISIBLE);
                    List<SpinnerItem> centros = new ArrayList<>();
                    centros.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));

                    for (CentroPoblado cp : result) {
                        centros.add(new SpinnerItem(cp.getCodigo(), cp.getNombre()));
                    }

                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, centros);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCentroPoblado.setAdapter(adapter);
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
            view.findViewById(R.id.lblComunidadCampesinaReporte).setVisibility(View.GONE);
            spinComunidadCampesina.setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadCampesina(codigoDistrito, new BaseApiCallback<List<ComunidadCampesina>>() {
            @Override
            public void onSuccess(List<ComunidadCampesina> result) {
                if (result == null || result.isEmpty()) {
                    spinComunidadCampesina.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadCampesinaReporte).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.lblComunidadCampesinaReporte).setVisibility(View.VISIBLE);
                    spinComunidadCampesina.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    comunidades.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
                    for (ComunidadCampesina cc : result) {
                        comunidades.add(new SpinnerItem(cc.getCodigo(), cc.getNombre()));
                    }
                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, comunidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinComunidadCampesina.setAdapter(adapter);
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
            spinComunidadNativa.setVisibility(View.GONE);
            view.findViewById(R.id.lblComunidadNativaReporte).setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadNativa(codigoDistrito, new BaseApiCallback<List<ComunidadNativa>>() {
            @Override
            public void onSuccess(List<ComunidadNativa> result) {
                if (result == null || result.isEmpty()) {
                    spinComunidadNativa.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadNativaReporte).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.lblComunidadNativaReporte).setVisibility(View.VISIBLE);
                    spinComunidadNativa.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    comunidades.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
                    for (ComunidadNativa cn : result) {
                        comunidades.add(new SpinnerItem(cn.getCodigo(), cn.getNombre()));
                    }
                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, comunidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinComunidadNativa.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Error al cargar comunidades nativas: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}