/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app.adaptador;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eas.app.AsignacionPorUbigeoFragment;
import com.eas.app.AsignarPorDniFragment;

public class MedidorViewPagerAdapter extends FragmentStateAdapter {

    public MedidorViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new AsignacionPorUbigeoFragment();
        }
        return new AsignarPorDniFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

