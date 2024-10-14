package com.eas.pdf;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileOutputStream;

public class GeneradorPdf {

    public void generarReciboAgua(String nombreArchivo) {
        try {
            // Ruta del archivo PDF
            File pdfFile = new File(Environment.getExternalStorageDirectory(), nombreArchivo);
            FileOutputStream fos = new FileOutputStream(pdfFile);

            // Crear un documento PDF
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Título del recibo
            Paragraph title = new Paragraph("Recibo de Consumo de Agua")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(18)
                    .setFontColor(ColorConstants.BLUE);
            document.add(title);

            // Información del recibo
            Paragraph info = new Paragraph("Cliente: Juan Pérez\nDirección: Calle Falsa 123\nPeriodo: Septiembre 2024\n\n")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(12);
            document.add(info);

            // Tabla de detalles de consumo
            float[] columnWidths = {100f, 100f, 100f};
            Table table = new Table(columnWidths);
            table.addCell("Concepto");
            table.addCell("Cantidad");
            table.addCell("Precio");
            table.addCell("Consumo de Agua");
            table.addCell("20 m³");
            table.addCell("$10.00");
            table.addCell("Cargo Fijo");
            table.addCell("1");
            table.addCell("$5.00");
            table.addCell("Total");
            table.addCell("");
            table.addCell("$15.00");

            document.add(table);

            // Cerrar el documento
            document.close();
            writer.close();

        } catch (Exception e) {
            Log.e("GeneradorPdf", "Error en generar recibo pdf de agua", e);
        }
    }
}
