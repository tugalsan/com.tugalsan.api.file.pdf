package com.tugalsan.api.file.pdf.server.pdfbox.tut;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Fonts {

    public static void HelloWorldType1(String message, String file, String pfbPath) throws IOException {
        try ( PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font;
            try ( InputStream is = new FileInputStream(pfbPath)) {
                font = new PDType1Font(doc, is);
            }

            try ( PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(message);
                contents.endText();
            }

            doc.save(file);
            System.out.println(file + " created!");
        }
    }

    public static void HelloWorldTTF(String message, String pdfPath, String ttfPath) throws IOException {
        try ( PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font = PDType0Font.load(doc, new File(ttfPath));

            try ( PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(message);
                contents.endText();
            }

            doc.save(pdfPath);
            System.out.println(pdfPath + " created!");
        }
    }

    public static void EmbeddedFonts(String file) throws IOException {
        try ( PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            String dir = "../pdfbox/src/main/resources/org/apache/pdfbox/resources/ttf/";
            PDType0Font font = PDType0Font.load(document, new File(dir + "LiberationSans-Regular.ttf"));

            try ( PDPageContentStream stream = new PDPageContentStream(document, page)) {
                stream.beginText();
                stream.setFont(font, 12);
                stream.setLeading(12 * 1.2f);

                stream.newLineAtOffset(50, 600);
                stream.showText("PDFBox's Unicode with Embedded TrueType Font");
                stream.newLine();

                stream.showText("Supports full Unicode text â˜º");
                stream.newLine();

                stream.showText("English Ñ€ÑƒÑÑĞºĞ¸Ğ¹ ÑĞ·Ñ‹Ğº Tiáº¿ng Viá»‡t");
                stream.newLine();

                // ligature
                stream.showText("Ligatures: \uFB01lm \uFB02ood");

                stream.endText();
            }

            document.save("example.pdf");
        }
    }
}
