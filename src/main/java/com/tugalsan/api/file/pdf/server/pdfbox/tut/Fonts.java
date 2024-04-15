package com.tugalsan.api.file.pdf.server.pdfbox.tut;

import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
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

    public static TGS_UnionExcuseVoid HelloWorldType1(String message, String file, String pfbPath) {
        try (var doc = new PDDocument()) {
            var page = new PDPage();
            doc.addPage(page);

            PDFont font;
            try (InputStream is = new FileInputStream(pfbPath)) {
                font = new PDType1Font(doc, is);
            }

            try (var contents = new PDPageContentStream(doc, page)) {
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(message);
                contents.endText();
            }

            doc.save(file);
            System.out.println(file + " created!");
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid HelloWorldTTF(String message, String pdfPath, String ttfPath) {
        try (var doc = new PDDocument()) {
            var page = new PDPage();
            doc.addPage(page);

            var font = PDType0Font.load(doc, new File(ttfPath));

            try (var contents = new PDPageContentStream(doc, page)) {
                contents.beginText();
                contents.setFont(font, 12);
                contents.newLineAtOffset(100, 700);
                contents.showText(message);
                contents.endText();
            }

            doc.save(pdfPath);
            System.out.println(pdfPath + " created!");
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid EmbeddedFonts(String file) {
        try (var document = new PDDocument()) {
            var page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            var dir = "../pdfbox/src/main/resources/org/apache/pdfbox/resources/ttf/";
            var font = PDType0Font.load(document, new File(dir + "LiberationSans-Regular.ttf"));

            try (var stream = new PDPageContentStream(document, page)) {
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
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }
}
