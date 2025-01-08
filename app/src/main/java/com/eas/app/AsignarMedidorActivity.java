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

import com.eas.app.adaptador.MedidorViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AsignarMedidorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_medidor);

        TabLayout tabLayout = findViewById(R.id.tabLayoutMedidor);
        ViewPager2 viewPager = findViewById(R.id.viewPagerMedidor);

        MedidorViewPagerAdapter adapter = new MedidorViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Asignar por DNI");
                    tab.setContentDescription("Asignar por DNI");
                    break;
                case 1:
                    tab.setText("Asignar por Ubigeo");
                    tab.setContentDescription("Asignar por Ubigeo");
                    break;
            }
        }).attach();
    }
}