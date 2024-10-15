package com.eas.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

    // Método para mostrar un AlertDialog reutilizable
    public static void showAlertDialog(Context context, String title, String message,
                                       String positiveButtonText, DialogInterface.OnClickListener positiveAction,
                                       String negativeButtonText, DialogInterface.OnClickListener negativeAction) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Establecer el título y el mensaje
        builder.setTitle(title);
        builder.setMessage(message);

        // Botón positivo
        builder.setPositiveButton(positiveButtonText, positiveAction != null ? positiveAction : new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción por defecto para "Aceptar"
                dialog.dismiss();
            }
        });

        // Botón negativo (opcional)
        if (negativeButtonText != null && negativeAction != null) {
            builder.setNegativeButton(negativeButtonText, negativeAction);
        }

        // Crear y mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
