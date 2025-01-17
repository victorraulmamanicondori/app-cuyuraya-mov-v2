/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eas.app.api.BaseApi;
import com.eas.app.api.BaseApiCallback;
import com.eas.app.model.Usuario;
import com.eas.app.util.Almacenamiento;
import com.eas.app.util.Constantes;
import com.eas.app.componentes.StepView;
import com.eas.app.util.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private int currentStep = 0;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        usuario = new Usuario();

        final StepView stepView = findViewById(R.id.step_view);

        stepView.setOnStepClickListener(step -> {
            Toast.makeText(RegistroUsuarioActivity.this, "Paso " + step, Toast.LENGTH_SHORT).show();
            loadFragment(currentStep);
        });

        findViewById(R.id.next).setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (currentFragment instanceof PasoDatosPersonalesFragment
                    && !((PasoDatosPersonalesFragment) currentFragment).validateFields()) {
                stepView.done(false);
                return;
            } else if (currentFragment instanceof PasoDatosAdicionalesFragment
                    && !((PasoDatosAdicionalesFragment) currentFragment).validateFields()) {
                stepView.done(false);
                return;
            } else if (currentFragment instanceof PasoUbigeoFragment
                    && !((PasoUbigeoFragment) currentFragment).validateFields()) {
                stepView.done(false);
                return;
            }

            if (currentStep < stepView.getStepCount() - 1) {
                currentStep++;
                stepView.go(currentStep, true);
                loadFragment(currentStep);
            } else {
                PasoUbigeoFragment pasoUbigeoFragment = (PasoUbigeoFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);

                if (pasoUbigeoFragment != null) {
                    pasoUbigeoFragment.recolectarDatos();
                }

                enviarDatosAlServidor(usuario);
                stepView.done(true);
            }
        });

        findViewById(R.id.back).setOnClickListener(v -> {
            if (currentStep > 0) {
                currentStep--;
                stepView.go(currentStep, true);
                loadFragment(currentStep);
            }
            stepView.done(false);
        });

        List<String> steps = new ArrayList<>();
        steps.add(getString(R.string.datos_personales));
        steps.add(getString(R.string.datos_adicionales));
        steps.add(getString(R.string.ubigeo));

        stepView.setSteps(steps);

        loadFragment(currentStep);
    }

    private void loadFragment(int step) {
        Fragment fragment;

        switch(step) {
            case 1:
                fragment = new PasoDatosAdicionalesFragment();
                break;
            case 2:
                fragment = new PasoUbigeoFragment();
                break;
            default:
                fragment = new PasoDatosPersonalesFragment();
                break;
        }

        FragmentManager firstFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = firstFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    private void enviarDatosAlServidor(Usuario usuario) {

        Log.d("PasoCredencialesFrag", "Enviando datos usuario al servidor...");
        Log.d("Usuario:", usuario.toString());

        String token = Almacenamiento.obtener(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
        BaseApi baseApi = new BaseApi(token);

        baseApi.registrarUsuario(usuario, new BaseApiCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario response) {
                if (response.getId() != null)  {
                    DialogUtils.showAlertDialog(
                            RegistroUsuarioActivity.this,
                            Constantes.TITULO_REGISTRO_EXITOSO,
                            "Registro exitoso del usuario",
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> {
                                dialog.dismiss();
                                navigateToLogin();
                            },
                            null,
                            null
                    );
                } else {
                    DialogUtils.showAlertDialog(
                            RegistroUsuarioActivity.this,
                            Constantes.TITULO_ASIGNACION_FALLIDA,
                            "Error en el registro, intente nuevamente",
                            Constantes.BOTON_TEXTO_ACEPTAR,
                            (dialog, which) -> dialog.dismiss(),
                            Constantes.BOTON_TEXTO_CANCELAR,
                            (dialog, which) -> {
                                dialog.dismiss();
                                navigateToLogin();
                            }
                    );
                }
            }

            @Override
            public void onError(Throwable t) {
                DialogUtils.showAlertDialog(
                        RegistroUsuarioActivity.this,
                        Constantes.TITULO_ERROR,
                        t.getMessage(),
                        Constantes.BOTON_TEXTO_ACEPTAR,
                        (dialog, which) -> dialog.dismiss(),
                        Constantes.BOTON_TEXTO_CANCELAR,
                        (dialog, which) -> {
                            dialog.dismiss();
                            navigateToLogin();
                        }
                );
                Log.e("Registro Usuario", "Error en el registro de usuario: " + t.getMessage());
            }
        });
    }

    private void navigateToLogin() {
        startActivity(new Intent(RegistroUsuarioActivity.this, LoginActivity.class));
        finish();
    }
}
