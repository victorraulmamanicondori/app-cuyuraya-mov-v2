package com.eas.app;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import android.net.Uri;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.api.request.LecturaActualRequest;
import com.eas.app.api.response.AnomaliaResponse;
import com.eas.app.api.response.BaseResponse;
import com.eas.app.api.response.LecturaActualResponse;
import com.eas.app.api.response.LecturaPaginadoResponse;
import com.eas.app.api.response.UsuarioResponse;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;
import com.eas.app.util.DialogUtils;
import com.eas.app.util.FechaUtil;
import com.eas.app.viewmodel.UsuarioViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistrarLecturaPorDniFragment extends Fragment {

    private Integer idLecturaActual;
    private EditText txtDniUsuario;
    private EditText txtCodigoMedidor;
    private EditText txtLecturaActual;

    private EditText txtLecturaAnterior;
    private EditText txtFechaLectura;
    private EditText txtRecibo;
    private EditText txtM3Consumidos;
    private EditText txtMontoTotal;
    private EditText txtFechaLimitePago;
    private CheckBox chkPagado;

    private TextView lblNombreUsuario;
    private TextView tvErrorDniUsuario;
    private TextView tvErrorCodigoMedidor;
    private TextView tvErrorLecturaActual;
    
    private ImageButton btnBuscarPorDni;
    private ImageButton btnBuscarPorCodigo;
    private ImageButton btnFechaLectura;
    private ImageButton btnInicio;
    private ImageButton btnAtras;
    private ImageButton btnAdelante;
    private ImageButton btnFinal;

    private UsuarioViewModel usuarioViewModel;
    private UsuarioResponse usuarioResponse;

    private int page = 1;
    private int limit = Constantes.PARAM_LIMIT;
    private int total = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_lectura_por_dni, container, false);

        idLecturaActual = 0;
        txtDniUsuario = view.findViewById(R.id.txtDniUsuario);
        txtCodigoMedidor = view.findViewById(R.id.txtCodigoMedidor);
        txtLecturaActual = view.findViewById(R.id.txtLecturaActual);
        txtLecturaAnterior = view.findViewById(R.id.txtLecturaAnterior);
        txtFechaLectura = view.findViewById(R.id.txtFechaLectura);
        txtRecibo = view.findViewById(R.id.txtRecibo);
        txtM3Consumidos = view.findViewById(R.id.txtM3Consumidos);
        txtMontoTotal = view.findViewById(R.id.txtMontoTotal);
        txtFechaLimitePago = view.findViewById(R.id.txtFechaLimitePago);
        chkPagado = view.findViewById(R.id.chkPagado);

        lblNombreUsuario = view.findViewById(R.id.lblNombreUsuario);
        tvErrorDniUsuario = view.findViewById(R.id.tvErrorDniUsuario);
        tvErrorCodigoMedidor = view.findViewById(R.id.tvErrorCodigoMedidor);
        tvErrorLecturaActual = view.findViewById(R.id.tvErrorLecturaActual);

        btnBuscarPorDni = view.findViewById(R.id.btnBuscarPorDni);
        btnBuscarPorDni.setOnClickListener(v -> buscarLecturasPorDni());

        btnBuscarPorCodigo = view.findViewById(R.id.btnBuscarPorCodigo);
        btnBuscarPorCodigo.setOnClickListener(v -> buscarLecturasPorCodigo());

        btnFechaLectura = view.findViewById(R.id.btnFechaLectura);

        btnInicio = view.findViewById(R.id.btnInicio);
        btnInicio.setOnClickListener(v -> irAlInicio());

        btnAtras = view.findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v -> irAtras());

        btnAdelante = view.findViewById(R.id.btnAdelante);
        btnAdelante.setOnClickListener(v -> irAdelante());

        btnFinal = view.findViewById(R.id.btnFinal);
        btnFinal.setOnClickListener(v -> irAlFinal());
        
        Button detectarAnomaliasButton = view.findViewById(R.id.btnDetectarAnomalias);
        detectarAnomaliasButton.setOnClickListener(v -> detectarAnomaliasConsumo());

        Button registrarLecturaButton = view.findViewById(R.id.btnRegistrarLectura);
        registrarLecturaButton.setOnClickListener(v -> borradorLectura());

        Button imprimirButton = view.findViewById(R.id.btnImprimirRecibo);
        imprimirButton.setOnClickListener(v -> imprimirRecibo());

        Button nuevoButton = view.findViewById(R.id.btnNuevo);
        nuevoButton.setOnClickListener(v -> limpiarCampos());

        // Establece la fecha actual por defecto
        Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        // Mostrar fecha actual en el TextView
        actualizarFechaLectura(anio, mes, dia);

        btnFechaLectura.setOnClickListener(v -> mostrarDialogoSeleccionarFecha(anio, mes, dia));

        usuarioViewModel = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class);
        usuarioViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            usuarioResponse = data;

            idLecturaActual = usuarioResponse.getIdLectura();
            txtDniUsuario.setText(usuarioResponse.getDni());
            lblNombreUsuario.setText(String.format("%s %s %s", usuarioResponse.getNombres(), usuarioResponse.getPaterno(), usuarioResponse.getMaterno()));
            lblNombreUsuario.setVisibility(View.VISIBLE);
            txtCodigoMedidor.setText(usuarioResponse.getCodigoMedidor().trim());

            obtenerDatos(usuarioResponse.getCodigoMedidor(), Constantes.PARAM_PAGE, Constantes.PARAM_LIMIT);
        });

        return view;
    }

    private void mostrarDialogoSeleccionarFecha(int anio, int mes, int dia) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (DatePicker view, int anioSeleccionado, int mesSeleccionado, int diaSeleccionado) -> {
                    // Actualiza el TextView con la fecha seleccionada
                    actualizarFechaLectura(anioSeleccionado, mesSeleccionado, diaSeleccionado);
                },
                anio, mes, dia
        );

        datePickerDialog.show();
    }

    private void actualizarFechaLectura(int anio, int mes, int dia) {
        String fechaFormateada = String.format("%02d/%02d/%04d", dia, mes + 1, anio);
        txtFechaLectura.setText(fechaFormateada);
    }

    private void irAlInicio() {
        setPage(total);
        obtenerDatos(txtCodigoMedidor.getText().toString().trim(), total, limit);
    }

    private void irAtras() {
        if (page < total) {
            setPage(page + 1);
            obtenerDatos(txtCodigoMedidor.getText().toString().trim(), page, limit);
        }
    }

    private void irAdelante() {
        if (page > 1) {
            setPage(page - 1);
            obtenerDatos(txtCodigoMedidor.getText().toString().trim(), page, limit);
        }
    }

    private void irAlFinal() {
        setPage(1);
        obtenerDatos(txtCodigoMedidor.getText().toString().trim(), page, limit);
    }

    private void setPage(int page) {
        this.page = page;
    }

    private void setLimit(int limit) {
        this.limit = limit;
    }

    private void setTotal(int total) {
        this.total = total;
    }

    private void obtenerDatos(String codigoMedidor, int page, int limit) {
        try {
            String token = Almacenamiento.obtener(requireActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            baseApi.listarLecturas(codigoMedidor, page, limit, new BaseApiCallback<BaseResponse<LecturaPaginadoResponse>>() {
                @Override
                public void onSuccess(BaseResponse<LecturaPaginadoResponse> response) {
                    LecturaPaginadoResponse lecturasPaginado = response.getDatos();
                    List<LecturaActualResponse> lecturas = lecturasPaginado.getResultados();

                    setPage(lecturasPaginado.getPage());
                    setLimit(lecturasPaginado.getLimit());
                    setTotal(lecturasPaginado.getTotal());

                    if (lecturas != null && !lecturas.isEmpty()) {
                        if (lecturas.get(0).getIdLectura() != null) {
                            idLecturaActual = lecturas.get(0).getIdLectura();
                        }

                        if (lecturas.get(0).getLecturaActual() != null) {
                            txtLecturaActual.setText(lecturas.get(0).getLecturaActual().toString());
                        }

                        if (lecturas.get(0).getLecturaAnterior() != null) {
                            txtLecturaAnterior.setText(lecturas.get(0).getLecturaAnterior().toString());
                        }

                        if (lecturas.get(0).getFechaLectura() != null) {
                            txtFechaLectura.setText(FechaUtil.convertDateFormat2(lecturas.get(0).getFechaLectura()));
                        }

                        if (lecturas.get(0).getNumeroRecibo() != null) {
                            txtRecibo.setText(lecturas.get(0).getNumeroRecibo());
                        }

                        if (lecturas.get(0).getM3Consumido() != null) {
                            txtM3Consumidos.setText(lecturas.get(0).getM3Consumido().toString());
                        }

                        if (lecturas.get(0).getMontoPagar() != null) {
                            txtMontoTotal.setText(lecturas.get(0).getMontoPagar().toString());
                        }

                        if (lecturas.get(0).getFechaLimitePago() != null) {
                            txtFechaLimitePago.setText(FechaUtil.convertDateFormat2(lecturas.get(0).getFechaLimitePago()));
                        }

                        if (Constantes.ESTADO_PAGADO.equals(lecturas.get(0).getEstado())) {
                            chkPagado.setChecked(true);
                        } else {
                            chkPagado.setChecked(false);
                        }
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(requireActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("RegistrarLecturaPorDni", "Error:" + t.getMessage());
                }
            });
        } catch(Exception ex) {
            Toast.makeText(requireActivity(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("RegistrarLecturaPorDni", "Error:" + ex.getMessage());
        }
    }

    private void buscarLecturasPorDni() {
        try {
            lblNombreUsuario.setText("");
            lblNombreUsuario.setVisibility(View.GONE);

            String token = Almacenamiento.obtener(requireActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String dni = txtDniUsuario.getText().toString().trim();

            if (dni.isEmpty()) {
                tvErrorDniUsuario.setText(R.string.este_campo_es_obligatorio);
                tvErrorDniUsuario.setVisibility(View.VISIBLE);
                return;
            }

            tvErrorDniUsuario.setVisibility(View.GONE);

            baseApi.getUsuario(dni, new BaseApiCallback<BaseResponse<UsuarioResponse>>() {
                @Override
                public void onSuccess(BaseResponse<UsuarioResponse> response) {
                    UsuarioResponse usuario = response.getDatos();

                    lblNombreUsuario.setText(String.format("%s %s %s", usuario.getNombres(), usuario.getPaterno(), usuario.getMaterno()));
                    lblNombreUsuario.setVisibility(View.VISIBLE);

                    if (usuario.getCodigoMedidor() != null && !usuario.getCodigoMedidor().trim().isEmpty()) {
                        txtCodigoMedidor.setText(usuario.getCodigoMedidor().trim());

                        obtenerDatos(usuario.getCodigoMedidor(), page, limit);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(requireActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("RegistrarLecturaPorDni", "Error:" + t.getMessage());
                    tvErrorDniUsuario.setText(R.string.usuario_no_encontrado);
                    tvErrorDniUsuario.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Log.e("RegistrarLecturaPorDni", "Excepción en obtener usuario", e);
        }
    }

    private void buscarLecturasPorCodigo() {

    }

    private void limpiarCampos() {
        idLecturaActual = 0;
        //txtDniUsuario.setText("");
        //txtCodigoMedidor.setText("");
        txtLecturaActual.setText("");

        //lblNombreUsuario.setText("");
        //lblNombreUsuario.setVisibility(View.GONE);
        //tvErrorDniUsuario.setVisibility(View.GONE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = sdf.format(new Date());
        txtFechaLectura.setText(fechaActual);

        txtLecturaAnterior.setText("");
        txtRecibo.setText("");
        txtM3Consumidos.setText("");
        txtMontoTotal.setText("");
        txtFechaLimitePago.setText("");
        chkPagado.setChecked(false);
    }

    private void detectarAnomaliasConsumo() {
        if (txtCodigoMedidor.getText().toString().trim().isEmpty()) {
            tvErrorCodigoMedidor.setText(R.string.este_campo_es_obligatorio);
            tvErrorCodigoMedidor.setVisibility(View.VISIBLE);

            DialogUtils.showAlertDialog(
                    getActivity(),
                    Constantes.TITULO_ADVERTENCIA,
                    Constantes.CAMPO_OBLIGATORIO_DETECTOR_ANOMALIAS,
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            );

            return;
        }

        try {
            String token = Almacenamiento.obtener(getActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String codigoMedidor = txtCodigoMedidor.getText().toString().trim();

            baseApi.detectarAnomalias(codigoMedidor, new BaseApiCallback<BaseResponse<List<AnomaliaResponse>>>() {
                @Override
                public void onSuccess(BaseResponse<List<AnomaliaResponse>> response) {
                    StringBuilder mensaje = getListaAnomalias(response);

                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_INFORMATION,
                            mensaje.toString(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                }

                @Override
                public void onError(Throwable t) {
                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_ERROR,
                            t.getMessage(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                }
            });

        } catch (Exception e) {
            DialogUtils.showAlertDialog(
                    getActivity(),
                    Constantes.TITULO_ERROR,
                    e.getMessage(),
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            );
        }
    }

    private static @NonNull StringBuilder getListaAnomalias(BaseResponse<List<AnomaliaResponse>> response) {
        List<AnomaliaResponse> anomalias = response.getDatos();
        StringBuilder mensaje = new StringBuilder("El medidor tiene las siguientes anomalias:");

        if (anomalias != null && !anomalias.isEmpty()) {
            for (int i = 0; i < 5 && i < anomalias.size(); i++) {
                mensaje.append(anomalias.get(i).getDate()).append(": ").append(anomalias.get(i).getValue()).append("\n");
            }
        } else {
            mensaje = new StringBuilder(Constantes.SIN_ANOMALIAS);
        }
        return mensaje;
    }

    private void borradorLectura() {
        if (!validateFields()) {
            return;
        }

        try {
            String token = Almacenamiento.obtener(getActivity(), Constantes.KEY_ACCESS_TOKEN);
            BaseApi baseApi = new BaseApi(token);

            String dni = txtDniUsuario.getText().toString().trim();
            String codigoMedidor = txtCodigoMedidor.getText().toString().trim();
            String lecturaActual = txtLecturaActual.getText().toString().trim();
            String fechaLectura = txtFechaLectura.getText().toString().trim();

            fechaLectura = FechaUtil.convertDateFormat(fechaLectura);

            LecturaActualRequest lecturaActualRequest = new LecturaActualRequest();
            lecturaActualRequest.setIdLectura(idLecturaActual);
            lecturaActualRequest.setDni(dni);
            lecturaActualRequest.setCodigoMedidor(codigoMedidor);
            lecturaActualRequest.setLecturaActual(lecturaActual);
            lecturaActualRequest.setFechaLectura(fechaLectura);

            baseApi.borradorLectura(lecturaActualRequest, new BaseApiCallback<BaseResponse<LecturaActualResponse>>() {
                @Override
                public void onSuccess(BaseResponse<LecturaActualResponse> response) {
                    LecturaActualResponse lecturaBorrador = response.getDatos();

                    if (lecturaBorrador.getLecturaAnterior() != null) {
                        txtLecturaAnterior.setText(lecturaBorrador.getLecturaAnterior().toString());
                    }
                    txtRecibo.setText(lecturaBorrador.getNumeroRecibo());
                    txtM3Consumidos.setText(lecturaBorrador.getM3Consumido().toString());
                    txtMontoTotal.setText(lecturaBorrador.getMontoPagar().toString());
                    txtFechaLimitePago.setText(FechaUtil.convertDateFormat2(lecturaBorrador.getFechaLimitePago()));

                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_REGISTRO_CONFIRMACION,
                            response.getMensaje(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> {
                                dialog.dismiss();
                                registrarLectura(baseApi, lecturaActualRequest);
                            },
                            Constantes.BOTON_TEXTO_CANCELAR,
                            (dialog, which) -> {
                                dialog.dismiss();
                            }
                    );

                    Log.d("RegistrarLectura", "Registro exitoso de la lectura");
                }

                @Override
                public void onError(Throwable t) {
                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_REGISTRO_FALLIDO,
                            t.getMessage(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                    Log.e("RegistrarLectura", "Error en registrar lectura: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            DialogUtils.showAlertDialog(
                    getActivity(),
                    Constantes.TITULO_REGISTRO_FALLIDO,
                    e.getMessage(),
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            );
            Log.e("RegistrarLectura", "Excepción en registrar lectura", e);
        }
    }

    private void registrarLectura(BaseApi baseApi, LecturaActualRequest lecturaActualRequest) {
        try {
            baseApi.registrarLectura(lecturaActualRequest, new BaseApiCallback<BaseResponse<LecturaActualResponse>>() {
                @Override
                public void onSuccess(BaseResponse<LecturaActualResponse> response) {
                    LecturaActualResponse lectura = response.getDatos();
                    idLecturaActual = lectura.getIdLectura();

                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_REGISTRO_EXITOSO,
                            response.getMensaje(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> {
                                dialog.dismiss();
                            },
                            null,
                            null
                    );

                    Log.d("RegistrarLectura", "Registro exitoso de la lectura");
                }

                @Override
                public void onError(Throwable t) {
                    DialogUtils.showAlertDialog(
                            getActivity(),
                            Constantes.TITULO_REGISTRO_FALLIDO,
                            t.getMessage(),
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            null,
                            null
                    );
                    Log.e("RegistrarLectura", "Error en registrar lectura: " + t.getMessage());
                }
            });
        } catch(Exception e) {
            DialogUtils.showAlertDialog(
                    getActivity(),
                    Constantes.TITULO_REGISTRO_FALLIDO,
                    e.getMessage(),
                    Constantes.BOTON_TEXTO_ACEPTAR,
                    (dialog, which) -> dialog.dismiss(),
                    null,
                    null
            );
            Log.e("RegistrarLectura", "Excepción en registrar lectura", e);
        }
    }

    private void imprimirRecibo() {
        String url = Constantes.BASE_URL + "lecturas/recibo/" + idLecturaActual;

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No se encontró una aplicación para abrir el PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        tvErrorDniUsuario.setVisibility(View.GONE);
        tvErrorCodigoMedidor.setVisibility(View.GONE);
        tvErrorLecturaActual.setVisibility(View.GONE);

        if (txtDniUsuario.getText().toString().trim().isEmpty()) {
            tvErrorDniUsuario.setText(R.string.este_campo_es_obligatorio);
            tvErrorDniUsuario.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (txtCodigoMedidor.getText().toString().trim().isEmpty()) {
            tvErrorCodigoMedidor.setText(R.string.este_campo_es_obligatorio);
            tvErrorCodigoMedidor.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (txtLecturaActual.getText().toString().trim().isEmpty()) {
            tvErrorLecturaActual.setText(R.string.este_campo_es_obligatorio);
            tvErrorLecturaActual.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }
}