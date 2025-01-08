/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eas.app.api.response.UsuarioResponse;

public class UsuarioViewModel extends ViewModel {
    private final MutableLiveData<UsuarioResponse> data = new MutableLiveData<>();

    public void setData(UsuarioResponse value) {
        data.setValue(value);
    }

    public LiveData<UsuarioResponse> getData() {
        return data;
    }
}
