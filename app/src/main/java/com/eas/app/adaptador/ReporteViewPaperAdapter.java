package com.eas.app.adaptador;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eas.app.ReportePorDniFragment;
import com.eas.app.ReportePorUbigeoFragment;

public class ReporteViewPaperAdapter extends FragmentStateAdapter {

    public ReporteViewPaperAdapter(@NonNull FragmentActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ReportePorUbigeoFragment();
        }
        return new ReportePorDniFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
