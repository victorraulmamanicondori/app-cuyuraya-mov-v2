package com.eas.app.util;

public class Constantes {
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String KEY_ACCESS_TOKEN = "accessToken";
    public static final String KEY_REFRESH_TOKEN = "refreshToken";
    public static final String TITULO_REGISTRO_EXITOSO = "Registro exitoso";
    public static final String TITULO_REGISTRO_FALLIDO = "Registro fallido";
    public static final String BOTON_TEXTO_ACEPTAR = "Aceptar";
    public static final String BOTON_TEXTO_CANCELAR = "Cancelar";
    public static final String TIPO_RUBRO_INGRESO = "Ingreso";
    public static final String TIPO_RUBRO_EGRESO = "Egreso";
    public static final String ITEM_SELECCIONE = "Seleccione";
    public static final String CAMPO_OBLIGATORIO_DETECTOR_ANOMALIAS = "Ingrese codigo del medidor para detectar anomalias de consumos ya registrados";
    public static final String TITULO_ADVERTENCIA = "Advertencia";
    public static final String TITULO_INFORMATION = "Informacion";
    public static final String SIN_ANOMALIAS = "Medidor no tiene anomalias en las lecturas de consumos de agua";
    public static final String TITULO_ERROR = "Error";
    public static final String TITULO_ASIGNACION_EXISTOSA = "Asignacion exitosa";
    public static final String TITULO_ASIGNACION_FALLIDA = "Asignacion fallida";
    public static final String CODIGO_TARIFA_BASE = "BASE";
    public static final String ESTADO_PAGADO = "PAGADO";
    public static final String USUARIO_SIN_MEDIDOR = "Usuario sin medidor";
    public static final String MENSAJE_ASIGNAR_MEDIDOR = "Debe asignar un medidor a este usuario para poder registrar lecturas";
    public static final int PARAM_PAGE = 1;
    public static final int PARAM_LIMIT = 1;

    public static final String BASE_URL = "http://104.251.212.105:9000/api/";
    //public static final String BASE_URL = "http://10.0.2.2:9000/api/";

    public static final String title = "Junta Administradora de Servicios de Saneamiento de Agua\nPotable de la comunidad campesina de CUYURAYA - HUANCANE";
    public static final String subtitle = "Partida registral n°1109264 de fecha 29 de octubre del año 2012";
    public static final String receiptInfo = "Recibo: 202408-1-1    Periodo: Agosto";

    public static final String[] headerRow1 = {"Usuario", "Medidor Nro", "M3 Consumo", "Lectura Actual", "Lectura Anterior"};
    public static final String[] dataRow1 = {"ALVA MAMANI IRMA ROSA", "1234556", "12334", "21323", "12312"};

    public static final String[] headerRow2 = {"Periodo", "Fecha Límite de Pago", "Deuda Anterior", "Deuda Actual", "TOTAL A PAGAR"};
    public static final String[] dataRow2 = {"01/08/2024", "20/08/2024", "S/. 10.50", "S/. 50.50", "S/. 120.50"};

}
