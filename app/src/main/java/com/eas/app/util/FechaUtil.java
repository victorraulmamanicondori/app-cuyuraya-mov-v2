/*
 * Sistema de Gestión de Agua Potable Rural
 * Copyright (c) Victor Raul Mamani Condori, 2025
 *
 * Este software está distribuido bajo los términos de la Licencia de Uso y Distribución.
 * Para más detalles, consulta el archivo LICENSE en el directorio raíz de este proyecto.
 */

package com.eas.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaUtil {

    public static String convertDateFormat(String date) {
        // Formato original
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Formato deseado
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Convertir cadena de texto a objeto Date
            Date parsedDate = originalFormat.parse(date);
            // Reformatear la fecha al nuevo formato
            return targetFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Retorna null si la conversión falla
        }
    }

    public static String convertDateFormat2(String date) {
        // Formato original
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Formato deseado
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Convertir cadena de texto a objeto Date
            Date parsedDate = originalFormat.parse(date);
            // Reformatear la fecha al nuevo formato
            return targetFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Retorna null si la conversión falla
        }
    }
}
