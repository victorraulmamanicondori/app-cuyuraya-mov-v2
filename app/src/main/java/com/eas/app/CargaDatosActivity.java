/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.eas.app.api.response.BaseResponse;
import com.eas.app.componentes.SpinnerItem;
import com.eas.app.model.CentroPoblado;
import com.eas.app.model.ComunidadCampesina;
import com.eas.app.model.ComunidadNativa;
import com.eas.app.model.Departamento;
import com.eas.app.model.Distrito;
import com.eas.app.model.Provincia;
import com.eas.app.model.Usuario;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
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

    List<Usuario> usuarios = new ArrayList<>();

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

        Button btnGuardarUsuarios = findViewById(R.id.btnGuardarUsuarios);
        btnGuardarUsuarios.setOnClickListener(v -> guardarUsuarios());

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
            assert inputStream != null;
            Workbook workbook = new HSSFWorkbook(inputStream);  // Solo para archivos .xls
            Sheet sheet = workbook.getSheet("USUARIOS");

            if (sheet == null) {
                tvNombreArchivoSeleccionado.setText("No se encontró la hoja 'USUARIOS'.");
                return;
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;  // Omitir encabezado

                String nombres = obtenerValorCelda(row.getCell(1));
                String paterno = obtenerValorCelda(row.getCell(2));
                String materno = obtenerValorCelda(row.getCell(3));
                String numeroPredio = obtenerValorCelda(row.getCell(4));
                String tmpDni = obtenerValorCelda(row.getCell(5));
                String dni = tmpDni != null ? tmpDni.replace("O", "0") : null;
                String codigoMedidor = obtenerValorCelda(row.getCell(6));
                String direccion = obtenerValorCelda(row.getCell(7));

                // Log.d("Usuario", "Nombres: " + nombres + ", Paterno: " + paterno + ", Materno: " + materno + ", predio: " + numeroPredio + ", DNI: " + dni + ", codigoMedidor: " + codigoMedidor + ", direccion: " + direccion + ", codDistrito: " + codDistrito + ", codCentroPoblado: " + codCentroPoblado + ", codComunidadCampesina: " + codComunidadCampesina + ", codComunidadNativa: " + codComunidadNativa);

                Usuario usuario = new Usuario();
                usuario.setNombres(nombres);
                usuario.setPaterno(paterno);
                usuario.setMaterno(materno);
                usuario.setDni(dni);
                usuario.setDireccion(direccion);
                if (codigoMedidor != null) {
                    usuario.setCodigoMedidor(codigoMedidor);
                } else if (numeroPredio != null) {
                    usuario.setCodigoMedidor(numeroPredio);
                }
                SpinnerItem distritoSeleccionado = (SpinnerItem) spinCargaDatosDistrito.getSelectedItem();
                SpinnerItem centroPobladoSeleccionado = (SpinnerItem) spinCargaDatosCentroPoblado.getSelectedItem();
                SpinnerItem comunidadCampesinaSeleccionado = (SpinnerItem) spinCargaDatosComunidadCampesina.getSelectedItem();
                SpinnerItem comunidadNativaSeleccionado = (SpinnerItem) spinCargaDatosComunidadNativa.getSelectedItem();

                if (distritoSeleccionado != null && !distritoSeleccionado.getCodigo().isEmpty()) {
                    usuario.setCodigoDistrito(distritoSeleccionado.getCodigo());
                }
                if (centroPobladoSeleccionado != null && !centroPobladoSeleccionado.getCodigo().isEmpty()) {
                    usuario.setCodigoCentroPoblado(centroPobladoSeleccionado.getCodigo());
                }
                if (comunidadCampesinaSeleccionado != null && !comunidadCampesinaSeleccionado.getCodigo().isEmpty()) {
                    usuario.setCodigoComunidadCampesina(comunidadCampesinaSeleccionado.getCodigo());
                }
                if (comunidadNativaSeleccionado != null && !comunidadNativaSeleccionado.getCodigo().isEmpty()) {
                    usuario.setCodigoComunidadNativa(comunidadNativaSeleccionado.getCodigo());
                }
                usuario.setFila(row.getRowNum());

                usuarios.add(usuario);
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            tvNombreArchivoSeleccionado.setText("Error al leer el archivo XLS.");
        }
    }

    private String obtenerValorCelda(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private void guardarUsuarios() {
        if (usuarios == null || usuarios.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Seleccione un archivo XLS", Toast.LENGTH_LONG).show();
            return;
        }

        SpinnerItem distritoSeleccionado = (SpinnerItem) spinCargaDatosDistrito.getSelectedItem();
        if (distritoSeleccionado == null || distritoSeleccionado.getCodigo().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Seleccione Distrito y/o Centro Poblado, Comuidad Campesina y Comuidad Nativa", Toast.LENGTH_LONG).show();
            return;
        }

        String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
        BaseApi baseApi = new BaseApi(token);

        baseApi.guardarUsuarios(usuarios, new BaseApiCallback<BaseResponse<List<Usuario>>>() {
            @Override
            public void onSuccess(BaseResponse<List<Usuario>> result) {
                List<Usuario> usuarios = result.getDatos();

                Toast.makeText(getApplicationContext(), "Se guardo usuarios desde excel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al guardar usuarios" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}