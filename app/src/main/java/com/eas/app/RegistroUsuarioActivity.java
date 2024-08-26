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
                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
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
                }
                stepView.done(false);
                stepView.go(currentStep, true);
            }
        });

        List<String> steps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            steps.add("Step " + (i + 1));
        }
        steps.set(steps.size() - 1, steps.get(steps.size() - 1) + " last one");
        stepView.setSteps(steps);

        loadFragment(currentStep);
    }

    private void loadFragment(int step) {
        Fragment fragment = new PasoDatosPersonalesFragment();
        FragmentManager firstFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = firstFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, "pasoDatosPersonalesFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        System.out.println("Llego....");
    }
}
