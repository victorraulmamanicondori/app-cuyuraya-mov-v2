package com.eas.app.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eas.app.R;
import com.eas.app.api.response.UsuarioResponse;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UserViewHolder> {

    private final List<UsuarioResponse> userList;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(UsuarioResponse user);
    }

    public UsuarioAdapter(List<UsuarioResponse> userList, OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_tabla, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UsuarioResponse user = userList.get(position);

        holder.txtDni.setText(user.getDni());
        holder.txtCodigoMedidor.setText(user.getCodigoMedidor());
        holder.txtUserName.setText(String.format("%s %s %s", user.getNombres(), user.getPaterno(), user.getMaterno()));

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtDni, txtCodigoMedidor;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtNombreUsuario);
            txtDni = itemView.findViewById(R.id.txtDniUsuario);
            txtCodigoMedidor = itemView.findViewById(R.id.txtCodigoMedidor);
        }
    }
}

