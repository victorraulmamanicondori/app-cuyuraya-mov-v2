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
