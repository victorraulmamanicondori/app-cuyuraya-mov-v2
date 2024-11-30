package com.eas.app.pdf;

import android.os.Environment;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;

public class GeneradorPdf {

    public void imprimirRecibo(String title, String subtitle, String receiptInfo,
                                String[] headerRow1, String[] dataRow1,
                                String[] headerRow2, String[] dataRow2) {
        try {
            // Ruta donde se guardará el PDF
            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "recibo_agua.pdf");

            // Crear PdfWriter y PdfDocument
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Fuentes
            PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            // Título
            Paragraph titleParagraph = new Paragraph(title)
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(titleParagraph);

            // Subtítulo
            Paragraph subtitleParagraph = new Paragraph(subtitle)
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(subtitleParagraph);

            // Recibo y período
            Paragraph receiptParagraph = new Paragraph(receiptInfo)
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(receiptParagraph);

            // Crear tabla
            float[] columnWidths = {2, 2, 2, 2, 2};
            Table table = new Table(columnWidths);

            // Agregar primera fila de etiquetas
            for (String header : headerRow1) {
                table.addCell(new Cell().add(new Paragraph(header).setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            // Agregar primera fila de datos
            for (String data : dataRow1) {
                table.addCell(new Cell().add(new Paragraph(data).setFont(normalFont)));
            }

            // Agregar segunda fila de etiquetas
            for (String header : headerRow2) {
                table.addCell(new Cell().add(new Paragraph(header).setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            // Agregar segunda fila de datos
            for (String data : dataRow2) {
                table.addCell(new Cell().add(new Paragraph(data).setFont(normalFont)));
            }

            // Añadir tabla al documento
            document.add(table);

            // Cerrar documento
            document.close();

            System.out.println("PDF generado en: " + pdfFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
