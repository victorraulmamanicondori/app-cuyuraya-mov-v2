package com.eas.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eas.app.utils.Almacenamiento;
import com.eas.app.utils.Constantes;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_principal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CardView cardRegistrarUsuario = findViewById(R.id.cardRegistrarUsuario);
        cardRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Almacenamiento.eliminar(getApplicationContext(), Constantes.KEY_ACCESS_TOKEN);
                Almacenamiento.eliminar(getApplicationContext(), Constantes.KEY_REFRESH_TOKEN);
                Intent intent = new Intent(PrincipalActivity.this, RegistroUsuarioActivity.class);
                startActivity(intent);
            }
        });

        CardView cardAsignarMedidor = findViewById(R.id.cardAsignarMedidor);
        cardAsignarMedidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, AsignarMedidorActivity.class);
                startActivity(intent);
            }
        });

        CardView cardRegistrarLectura = findViewById(R.id.cardRegistrarLectura);
        cardRegistrarLectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, RegistrarLecturaActivity.class);
                startActivity(intent);
            }
        });

        CardView cardRegistrarEgreso = findViewById(R.id.cardRegistrarEgreso);
        cardRegistrarEgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, RegistrarEgresoActivity.class);
                startActivity(intent);
            }
        });

        CardView cardRegistrarIngreso = findViewById(R.id.cardRegistrarIngreso);
        cardRegistrarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, RegistrarIngresoActivity.class);
                startActivity(intent);
            }
        });

        CardView cardGestionLecturas = findViewById(R.id.cardGestionLecturas);
        cardGestionLecturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, GestionLecturasActivity.class);
                startActivity(intent);
            }
        });

        CardView cardReportes = findViewById(R.id.cardReportes);
        cardReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, ReportesActivity.class);
                startActivity(intent);
            }
        });
    }
}