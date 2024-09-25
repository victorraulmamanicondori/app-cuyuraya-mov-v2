package com.eas.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.eas.app.model.Usuario;
import com.eas.app.utils.Constantes;

public class PasoCredencialesFragment extends Fragment {

    private EditText etContrasenia;
    private TextView tvErrorContrasenia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paso_credenciales, container, false);

        etContrasenia = view.findViewById(R.id.txtContrasenia);
        tvErrorContrasenia = view.findViewById(R.id.tvErrorContrasenia);

        return view;
    }

    public void recolectarDatos() {
        super.onPause();

        RegistroUsuarioActivity activity = (RegistroUsuarioActivity) getActivity();
        if (activity != null) {
            Usuario usuario = activity.getUsuario();
            usuario.setClave(etContrasenia.getText().toString().trim());
            usuario.setEstado(Constantes.ESTADO_ACTIVO);
        }
    }

    public boolean validateFields() {
        boolean isValid = true;

        tvErrorContrasenia.setVisibility(View.GONE);

        if (etContrasenia.getText().toString().trim().isEmpty()) {
            tvErrorContrasenia.setText(R.string.este_campo_es_obligatorio);
            tvErrorContrasenia.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }
}
