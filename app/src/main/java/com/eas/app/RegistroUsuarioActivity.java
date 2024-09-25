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
import com.eas.app.utils.Almacenamiento;
import com.eas.app.utils.Constantes;
import com.eas.componentes.StepView;

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
            Toast.makeText(RegistroUsuarioActivity.this, "Step " + step, Toast.LENGTH_SHORT).show();
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
            } else if (currentFragment instanceof PasoCredencialesFragment
                    && !((PasoCredencialesFragment) currentFragment).validateFields()) {
                stepView.done(false);
            }

            if (currentStep < stepView.getStepCount() - 1) {
                currentStep++;
                stepView.go(currentStep, true);
                loadFragment(currentStep);
            } else {
                PasoCredencialesFragment pasoCredencialesFragment = (PasoCredencialesFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);

                if (pasoCredencialesFragment != null) {
                    pasoCredencialesFragment.recolectarDatos();
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
        steps.add(getString(R.string.credenciales));

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
            case 3:
                fragment = new PasoCredencialesFragment();
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
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    navigateToPrincipal();
                } else {
                    Toast.makeText(getApplicationContext(), "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(), "Error en el registro de usuario: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Registro Usuario", "Error en el registro de usuario: " + t.getMessage());
            }
        });
    }

    private void navigateToPrincipal() {
        startActivity(new Intent(RegistroUsuarioActivity.this, PrincipalActivity.class));
        finish();
    }
}
