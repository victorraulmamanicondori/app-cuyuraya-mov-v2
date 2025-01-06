package com.eas.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.componentes.SpinnerItem;
import com.eas.app.model.CentroPoblado;
import com.eas.app.model.ComunidadCampesina;
import com.eas.app.model.ComunidadNativa;
import com.eas.app.model.Departamento;
import com.eas.app.model.Distrito;
import com.eas.app.model.Provincia;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CargaDatosActivity extends AppCompatActivity {

    private BaseApi baseApi;

    private Spinner spinCargaDatosDepartamento;
    private Spinner spinCargaDatosProvincia;
    private Spinner spinCargaDatosDistrito;
    private Spinner spinCargaDatosCentroPoblado;
    private Spinner spinCargaDatosComunidadCampesina;
    private Spinner spinCargaDatosComunidadNativa;

    private TextView lblCargaDatosCodigoDistrito;
    private TextView lblCargaDatosCodigoCentroPoblado;
    private TextView lblCargaDatosCodigoComunidadCampesina;
    private TextView lblCargaDatosCodigoComunidadNativa;

    private TextView tvNombreArchivoSeleccionado;

    // ActivityResultLauncher reemplaza startActivityForResult
    private final ActivityResultLauncher<Intent> seleccionarArchivoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        tvNombreArchivoSeleccionado.setText(uri.getPath());
                        leerDatosExcel(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carga_datos);

        spinCargaDatosDepartamento = findViewById(R.id.spinCargaDatosDepartamento);
        spinCargaDatosProvincia = findViewById(R.id.spinCargaDatosProvincia);
        spinCargaDatosDistrito = findViewById(R.id.spinCargaDatosDistrito);
        spinCargaDatosCentroPoblado = findViewById(R.id.spinCargaDatosCentroPoblado);
        spinCargaDatosComunidadCampesina = findViewById(R.id.spinCargaDatosComunidadCampesina);
        spinCargaDatosComunidadNativa = findViewById(R.id.spinCargaDatosComunidadNativa);

        lblCargaDatosCodigoDistrito = findViewById(R.id.lblCargaDatosCodigoDistrito);
        lblCargaDatosCodigoCentroPoblado = findViewById(R.id.lblCargaDatosCodigoCentroPoblado);
        lblCargaDatosCodigoComunidadCampesina = findViewById(R.id.lblCargaDatosCodigoComunidadCampensina);
        lblCargaDatosCodigoComunidadNativa = findViewById(R.id.lblCargaDatosCodigoComunidadNativa);

        tvNombreArchivoSeleccionado = findViewById(R.id.tvNombreArchivoSeleccionado);
        Button btnSeleccionarArchivo = findViewById(R.id.btnSeleccionarArchivo);

        btnSeleccionarArchivo.setOnClickListener(v -> abrirSelectorDeArchivos());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cargaDatos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
        baseApi = new BaseApi(token);

        cargarDepartamentos();

        configurarListeners();
    }

    private void configurarListeners() {
        spinCargaDatosDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem departamentoSeleccionado = (SpinnerItem) parent.getSelectedItem();
                if (departamentoSeleccionado != null) {
                    cargarProvincias(departamentoSeleccionado.getCodigo());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinCargaDatosProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem provinciaSeleccionada = (SpinnerItem) parent.getSelectedItem();
                if (provinciaSeleccionada != null) {
                    cargarDistritos(provinciaSeleccionada.getCodigo());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinCargaDatosDistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem distritoSeleccionado = (SpinnerItem) parent.getSelectedItem();
                if (distritoSeleccionado != null && !distritoSeleccionado.getCodigo().isEmpty()) {
                    cargarCentroPoblado(distritoSeleccionado.getCodigo());
                    cargarComunidadCampesina(distritoSeleccionado.getCodigo());
                    cargarComunidadNativa(distritoSeleccionado.getCodigo());

                    lblCargaDatosCodigoDistrito.setText(String.format("%s: %s", Constantes.CODIGO, distritoSeleccionado.getCodigo()));
                    lblCargaDatosCodigoDistrito.setVisibility(TextView.VISIBLE);
                } else {
                    lblCargaDatosCodigoDistrito.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinCargaDatosCentroPoblado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem centroPobladoSeleccionado = (SpinnerItem) adapterView.getSelectedItem();
                if (centroPobladoSeleccionado != null && !centroPobladoSeleccionado.getCodigo().isEmpty()) {
                    lblCargaDatosCodigoCentroPoblado.setText(String.format("%s: %s", Constantes.CODIGO, centroPobladoSeleccionado.getCodigo()));
                    lblCargaDatosCodigoCentroPoblado.setVisibility(TextView.VISIBLE);
                } else {
                    lblCargaDatosCodigoCentroPoblado.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinCargaDatosComunidadCampesina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem comunidadCampesinaSeleccionado = (SpinnerItem) adapterView.getSelectedItem();
                if (comunidadCampesinaSeleccionado != null && !comunidadCampesinaSeleccionado.getCodigo().isEmpty()) {
                    lblCargaDatosCodigoComunidadCampesina.setText(String.format("%s: %s", Constantes.CODIGO, comunidadCampesinaSeleccionado.getCodigo()));
                    lblCargaDatosCodigoComunidadCampesina.setVisibility(TextView.VISIBLE);
                } else {
                    lblCargaDatosCodigoComunidadCampesina.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinCargaDatosComunidadNativa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem comunidadNativaSeleccionado = (SpinnerItem) adapterView.getSelectedItem();
                if (comunidadNativaSeleccionado != null && !comunidadNativaSeleccionado.getCodigo().isEmpty()) {
                    lblCargaDatosCodigoComunidadNativa.setText(String.format("%s: %s", Constantes.CODIGO, comunidadNativaSeleccionado.getCodigo()));
                    lblCargaDatosCodigoComunidadNativa.setVisibility(TextView.VISIBLE);
                } else {
                    lblCargaDatosCodigoComunidadNativa.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void cargarDepartamentos() {
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

                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, departamentos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCargaDatosDepartamento.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar departamentos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarProvincias(String codigoDepartamento) {
        if (codigoDepartamento.isEmpty()) {
            List<SpinnerItem> provincias = new ArrayList<>();
            provincias.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
            ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, provincias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCargaDatosProvincia.setAdapter(adapter);
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

                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, provincias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCargaDatosProvincia.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar provincias: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarDistritos(String codigoProvincia) {
        if (codigoProvincia.isEmpty()) {
            List<SpinnerItem> distritos = new ArrayList<>();
            distritos.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
            ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, distritos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCargaDatosDistrito.setAdapter(adapter);
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

                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, distritos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCargaDatosDistrito.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar distritos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarCentroPoblado(String codigoDistrito) {
        if (codigoDistrito.isEmpty()) {
            findViewById(R.id.lblCargaDatosCentroPoblado).setVisibility(View.GONE);
            spinCargaDatosCentroPoblado.setVisibility(View.GONE);
            return;
        }

        baseApi.getCentroPoblado(codigoDistrito, new BaseApiCallback<List<CentroPoblado>>() {
            @Override
            public void onSuccess(List<CentroPoblado> result) {
                if (result == null || result.isEmpty()) {
                    spinCargaDatosCentroPoblado.setVisibility(View.GONE);
                    findViewById(R.id.lblCargaDatosCentroPoblado).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.lblCargaDatosCentroPoblado).setVisibility(View.VISIBLE);
                    spinCargaDatosCentroPoblado.setVisibility(View.VISIBLE);
                    List<SpinnerItem> centros = new ArrayList<>();
                    centros.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));

                    for (CentroPoblado cp : result) {
                        centros.add(new SpinnerItem(cp.getCodigo(), cp.getNombre()));
                    }

                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, centros);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCargaDatosCentroPoblado.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar centros poblados: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarComunidadCampesina(String codigoDistrito) {
        if (codigoDistrito.isEmpty()) {
            findViewById(R.id.lblCargaDatosComunidadCampesina).setVisibility(View.GONE);
            spinCargaDatosComunidadCampesina.setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadCampesina(codigoDistrito, new BaseApiCallback<List<ComunidadCampesina>>() {
            @Override
            public void onSuccess(List<ComunidadCampesina> result) {
                if (result == null || result.isEmpty()) {
                    spinCargaDatosComunidadCampesina.setVisibility(View.GONE);
                    findViewById(R.id.lblCargaDatosComunidadCampesina).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.lblCargaDatosComunidadCampesina).setVisibility(View.VISIBLE);
                    spinCargaDatosComunidadCampesina.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    comunidades.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
                    for (ComunidadCampesina cc : result) {
                        comunidades.add(new SpinnerItem(cc.getCodigo(), cc.getNombre()));
                    }
                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, comunidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCargaDatosComunidadCampesina.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar comunidades campesinas: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarComunidadNativa(String codigoDistrito) {
        if (codigoDistrito.isEmpty()) {
            spinCargaDatosComunidadNativa.setVisibility(View.GONE);
            findViewById(R.id.lblCargaDatosComunidadNativa).setVisibility(View.GONE);
            return;
        }

        baseApi.getComunidadNativa(codigoDistrito, new BaseApiCallback<List<ComunidadNativa>>() {
            @Override
            public void onSuccess(List<ComunidadNativa> result) {
                if (result == null || result.isEmpty()) {
                    spinCargaDatosComunidadNativa.setVisibility(View.GONE);
                    findViewById(R.id.lblCargaDatosComunidadNativa).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.lblCargaDatosComunidadNativa).setVisibility(View.VISIBLE);
                    spinCargaDatosComunidadNativa.setVisibility(View.VISIBLE);
                    List<SpinnerItem> comunidades = new ArrayList<>();
                    comunidades.add(new SpinnerItem("", Constantes.ITEM_SELECCIONE));
                    for (ComunidadNativa cn : result) {
                        comunidades.add(new SpinnerItem(cn.getCodigo(), cn.getNombre()));
                    }
                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, comunidades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCargaDatosComunidadNativa.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar comunidades nativas: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirSelectorDeArchivos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.ms-excel");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // startActivityForResult(Intent.createChooser(intent, "Seleccione un archivo XLS"), PICK_XLS_FILE);
        seleccionarArchivoLauncher.launch(Intent.createChooser(intent, "Selecciona un archivo Excel XLS"));
    }

    private void leerDatosExcel(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            Workbook workbook = new HSSFWorkbook(inputStream); // Para archivos XLS
            Sheet sheet = workbook.getSheet("USUARIOS"); // Especifica la hoja
            StringBuilder xlsData = new StringBuilder();

            for (Row row : sheet) {
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    xlsData.append(row.getCell(cn).toString()).append(" ");
                }
                xlsData.append("\n");
            }

            Log.d("Excel:", xlsData.toString());
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            tvNombreArchivoSeleccionado.setText("Error al leer el archivo.");
        }
    }
}