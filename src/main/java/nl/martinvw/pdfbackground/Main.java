package org.example;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        byte[] backgroundBytes = Main.class.getClassLoader().getResourceAsStream("2020-briefpapier.png").readAllBytes();

        String inputFilename = args[0];
        String outputFilename = inputFilename.replace(".pdf", "-bg.pdf");
        try (PdfReader reader = new PdfReader(inputFilename); PdfWriter writer = new PdfWriter(inputFilename)) {

            PdfDocument pdfDocument = new PdfDocument(reader, writer);
            PageSize pageSize = PageSize.A4;
            Document doc = new Document(pdfDocument, pageSize);

            PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(1));
            canvas.addImageFittedIntoRectangle(ImageDataFactory.create(backgroundBytes), pageSize, true);

            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}