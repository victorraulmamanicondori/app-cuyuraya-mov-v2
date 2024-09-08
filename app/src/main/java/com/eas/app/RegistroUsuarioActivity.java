package com.eas.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eas.componentes.StepView;

import java.util.ArrayList;
import java.util.List;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private int currentStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        final StepView stepView = findViewById(R.id.step_view);

        stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                Toast.makeText(RegistroUsuarioActivity.this, "Step " + step, Toast.LENGTH_SHORT).show();
                loadFragment(currentStep);
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (currentFragment instanceof PasoDatosPersonalesFragment) {
                    if (!((PasoDatosPersonalesFragment) currentFragment).validateFields()) {
                        // Si la validación falla, no avanzar
                        return;
                    }
                }

                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                    loadFragment(currentStep);
                } else {
                    stepView.done(true);
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    stepView.go(currentStep, true);
                    loadFragment(currentStep);
                }
                stepView.done(false);
            }
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
            case 0:
                fragment = new PasoDatosPersonalesFragment();
                break;
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
}
