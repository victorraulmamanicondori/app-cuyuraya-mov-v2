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

import com.eas.app.adaptador.ReporteViewPaperAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReportesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_lectura);

        TabLayout tabLayout = findViewById(R.id.tabLayoutLectura);
        ViewPager2 viewPager = findViewById(R.id.viewPagerLectura);

        ReporteViewPaperAdapter adapter = new ReporteViewPaperAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Reporte por DNI");
                    tab.setContentDescription("Reporte por DNI");
                    break;
                case 1:
                    tab.setText("Reporte por Ubigeo");
                    tab.setContentDescription("Reporte por Ubigeo");
                    break;
            }
        }).attach();
    }
}