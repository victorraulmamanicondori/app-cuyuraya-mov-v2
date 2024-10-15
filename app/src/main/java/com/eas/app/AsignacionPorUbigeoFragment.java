package com.eas.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.BaseApi;
import com.eas.app.api.response.UsuarioResponse;
import com.eas.app.componentes.SpinnerItem;
import com.eas.app.model.CentroPoblado;
import com.eas.app.model.ComunidadCampesina;
import com.eas.app.model.ComunidadNativa;
import com.eas.app.model.Departamento;
import com.eas.app.model.Distrito;
import com.eas.app.model.Provincia;
import com.eas.app.util.Constantes;

import java.util.ArrayList;
import java.util.List;

public class AsignacionPorUbigeoFragment extends Fragment {
    private BaseApi baseApi;

    private View view;

    private Spinner spinDepartamento;
    private Spinner spinProvincia;
    private Spinner spinDistrito;
    private Spinner spinCentroPoblado;
    private Spinner spinComunidadCampesina;
    private Spinner spinComunidadNativa;

    private TableLayout tableLayout;
    private List<UsuarioResponse> userList;
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
        view = inflater.inflate(R.layout.fragment_asignacion_por_ubigeo, container, false);

        spinDepartamento = view.findViewById(R.id.spinDepartamento);
        spinProvincia = view.findViewById(R.id.spinProvincia);
        spinDistrito = view.findViewById(R.id.spinDistrito);
        spinCentroPoblado = view.findViewById(R.id.spinCentroPoblado);
        spinComunidadCampesina = view.findViewById(R.id.spinComunidadCampesina);
        spinComunidadNativa = view.findViewById(R.id.spinComunidadNativa);

        tableLayout = view.findViewById(R.id.tableLayout);

        view.findViewById(R.id.btnInicio).setOnClickListener(v -> loadPage(0));
        view.findViewById(R.id.btnAtras).setOnClickListener(v -> loadPage(currentPage - 1));
        view.findViewById(R.id.btnAdelante).setOnClickListener(v -> loadPage(currentPage + 1));
        view.findViewById(R.id.btnFinal).setOnClickListener(v -> loadPage(userList.size() / pageSize));

        pagination = view.findViewById(R.id.paginacionTablaUsuarios);

        baseApi = new BaseApi(null);

        loadDepartamentos();

        configurarListeners();

        userList = getUserData();
        loadPage(currentPage);

        return view;
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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
            view.findViewById(R.id.lblCentroPoblado).setVisibility(View.GONE);
            spinCentroPoblado.setVisibility(View.GONE);
            return;
        }

        baseApi.getCentroPoblado(codigoDistrito, new BaseApiCallback<List<CentroPoblado>>() {
            @Override
            public void onSuccess(List<CentroPoblado> result) {
                if (result == null || result.isEmpty()) {
                    spinCentroPoblado.setVisibility(View.GONE);
                    view.findViewById(R.id.lblCentroPoblado).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.lblCentroPoblado).setVisibility(View.VISIBLE);
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
            view.findViewById(R.id.lblComunidadCampesina).setVisibility(View.GONE);
            spinComunidadCampesina.setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadCampesina(codigoDistrito, new BaseApiCallback<List<ComunidadCampesina>>() {
            @Override
            public void onSuccess(List<ComunidadCampesina> result) {
                if (result == null || result.isEmpty()) {
                    spinComunidadCampesina.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadCampesina).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.lblComunidadCampesina).setVisibility(View.VISIBLE);
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
            view.findViewById(R.id.lblComunidadNativa).setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadNativa(codigoDistrito, new BaseApiCallback<List<ComunidadNativa>>() {
            @Override
            public void onSuccess(List<ComunidadNativa> result) {
                if (result == null || result.isEmpty()) {
                    spinComunidadNativa.setVisibility(View.GONE);
                    view.findViewById(R.id.lblComunidadNativa).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.lblComunidadNativa).setVisibility(View.VISIBLE);
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

    private void loadPage(int page) {
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

            row.setOnClickListener(v -> {
                // Restaurar los colores de las filas al color original (patrón zebra)
                for (int j = 0; j < tableLayout.getChildCount(); j++) {
                    View child = tableLayout.getChildAt(j);
                    if (child instanceof TableRow && child != headerRow) {
                        if (j % 2 == 0) {
                            child.setBackgroundColor(getResources().getColor(R.color.gray_400));
                        } else {
                            child.setBackgroundColor(getResources().getColor(android.R.color.white));
                        }
                    }
                }

                // Cambiar la fila seleccionada a color amarillo
                row.setBackgroundColor(getResources().getColor(R.color.light_blue_400));

                // Inflar el diseño del diálogo
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_asignar_medidor, null);

                // Crear el diálogo
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);

                // Obtener referencias a los elementos del diálogo
                TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
                EditText editTextMedidor = dialogView.findViewById(R.id.editTextMedidor);
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
                Button btnRegistrar = dialogView.findViewById(R.id.btnRegistrar);

                // Configurar el diálogo
                AlertDialog alertDialog = builder.create();

                // Establecer el título del diálogo
                dialogTitle.setText(String.format("Asignar medidor a %s %s %s (DNI: %s)", user.getNombres(), user.getPaterno(), user.getMaterno(), user.getDni()));

                // Configurar el botón Cancelar
                btnCancelar.setOnClickListener(cancelView -> alertDialog.dismiss());

                // Configurar el botón Registrar
                btnRegistrar.setOnClickListener(registerView -> {
                    String codigoMedidor = editTextMedidor.getText().toString();
                    if (!codigoMedidor.isEmpty()) {
                        // Lógica para asignar el medidor al usuario
                        user.setCodigoMedidor(codigoMedidor);
                        Toast.makeText(getActivity(), "Medidor asignado a " + user.getNombres(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Ingrese un código de medidor válido", Toast.LENGTH_SHORT).show();
                    }
                });

                // Mostrar el diálogo
                alertDialog.show();
            });

            tableLayout.addView(row);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        return textView;
    }

    private List<UsuarioResponse> getUserData() {
        List<UsuarioResponse> users = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            UsuarioResponse user = new UsuarioResponse();
            user.setNombres("Miguel " + i);
            user.setPaterno("Mamani " + i);
            user.setMaterno("Condori " + i);
            user.setDni("1234567" + i);
            if (i % 3 == 0)
                user.setCodigoMedidor("1234" + i);

            users.add(user);
        }
        return users;
    }
}
