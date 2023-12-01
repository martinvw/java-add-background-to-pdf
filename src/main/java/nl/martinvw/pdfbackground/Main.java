package nl.martinvw.pdfbackground;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    private static final PageSize PAGE_SIZE = PageSize.A4;

    public static void main(String[] args) throws IOException {
        byte[] backgroundBytes = loadBackground();

        String inputFilename = args[0];
        String outputFilename = getOutputFilename(args, inputFilename);

        try (PdfReader reader = new PdfReader(inputFilename); PdfWriter writer = new PdfWriter(outputFilename)) {

            PdfDocument pdfDocument = new PdfDocument(reader, writer);
            Document doc = new Document(pdfDocument, PAGE_SIZE);

            int pageCount = pdfDocument.getNumberOfPages();
            for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
                processPage(pdfDocument.getPage(pageNumber), backgroundBytes);
            }

            doc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void processPage(PdfPage page, byte[] backgroundBytes) {
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), page.getDocument());
        canvas.addImageFittedIntoRectangle(ImageDataFactory.create(backgroundBytes), PAGE_SIZE, true);
    }

    private static String getOutputFilename(String[] args, String inputFilename) {
        String outputFilename;
        if (args.length > 1) {
            outputFilename = args[1];
        } else {
            outputFilename = inputFilename.replace(".pdf", "-bg.pdf");
        }
        return outputFilename;
    }

    private static byte[] loadBackground() throws IOException {
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("2020-briefpapier.png")) {
            if (inputStream == null) {
                throw new IOException("File could not be opened");
            }

            return inputStream.readAllBytes();
        }
    }
}