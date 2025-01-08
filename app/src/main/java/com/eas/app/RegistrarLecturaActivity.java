/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.eas.app.adaptador.LecturaViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RegistrarLecturaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_lectura);

        TabLayout tabLayout = findViewById(R.id.tabLayoutLectura);
        ViewPager2 viewPager = findViewById(R.id.viewPagerLectura);

        LecturaViewPagerAdapter adapter = new LecturaViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Lectura por DNI");
                    tab.setContentDescription("Lectura por DNI");
                    break;
                case 1:
                    tab.setText("Lectura por Ubigeo");
                    tab.setContentDescription("Lectura por Ubigeo");
                    break;
            }
        }).attach();
    }
}