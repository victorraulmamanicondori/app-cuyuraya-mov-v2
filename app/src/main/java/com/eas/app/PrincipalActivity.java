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

public class PrincipalActivity extends AppCompatActivity {

    private CardView cardRegistrarUsuario;
    private CardView cardAsignarMedidor;
    private CardView cardRegistrarLectura;
    private CardView cardRegistrarEgreso;
    private CardView cardRegistrarIngreso;
    private CardView cardGestionLecturas;

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

        cardRegistrarUsuario = findViewById(R.id.cardRegistrarUsuario);
        cardRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PrincipalActivity.this, RegistroUsuarioActivity.class);
                //startActivity(intent);
                Toast.makeText(PrincipalActivity.this, "Card clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}