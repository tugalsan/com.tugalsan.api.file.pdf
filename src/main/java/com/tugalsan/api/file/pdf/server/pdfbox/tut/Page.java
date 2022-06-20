package com.tugalsan.api.file.pdf.server.pdfbox.tut;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

public class Page {
    
    public static void AddMessageToEachPage(String infile, String message, String outfile) throws IOException {
        try ( PDDocument doc = Loader.loadPDF(new File(infile))) {
            PDFont font = PDType1Font.HELVETICA_BOLD;
            float fontSize = 36.0f;

            for (PDPage page : doc.getPages()) {
                PDRectangle pageSize = page.getMediaBox();
                float stringWidth = font.getStringWidth(message) * fontSize / 1000f;
                // calculate to center of the page
                int rotation = page.getRotation();
                boolean rotate = rotation == 90 || rotation == 270;
                float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
                float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
                float centerX = rotate ? pageHeight / 2f : (pageWidth - stringWidth) / 2f;
                float centerY = rotate ? (pageWidth - stringWidth) / 2f : pageHeight / 2f;

                // append the content to the existing stream
                try ( PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    contentStream.beginText();
                    // set font and font size
                    contentStream.setFont(font, fontSize);
                    // set text color to red
                    contentStream.setNonStrokingColor(Color.red);
                    if (rotate) {
                        // rotate the text according to the page rotation
                        contentStream.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, centerX, centerY));
                    } else {
                        contentStream.setTextMatrix(Matrix.getTranslateInstance(centerX, centerY));
                    }
                    contentStream.showText(message);
                    contentStream.endText();
                }
            }

            doc.save(outfile);
        }
    }

    public static void CreateBlankPDF(String inputFile) throws IOException {
        try ( PDDocument doc = new PDDocument()) {
            // a valid PDF document requires at least one page
            PDPage blankPage = new PDPage();
            doc.addPage(blankPage);
            doc.save(inputFile);
        }
    }

    public static void CreateLandscapePDF(String message, String outfile) throws IOException {
        try ( PDDocument doc = new PDDocument()) {
            PDFont font = PDType1Font.HELVETICA;
            PDPage page = new PDPage(PDRectangle.A4);
            page.setRotation(90);
            doc.addPage(page);
            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();
            float fontSize = 12;
            float stringWidth = font.getStringWidth(message) * fontSize / 1000f;
            float startX = 100;
            float startY = 100;

            try ( PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.OVERWRITE, false)) {
                // add the rotation using the current transformation matrix
                // including a translation of pageWidth to use the lower left corner as 0,0 reference
                contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
                contentStream.setFont(font, fontSize);
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY);
                contentStream.showText(message);
                contentStream.newLineAtOffset(0, 100);
                contentStream.showText(message);
                contentStream.newLineAtOffset(100, 100);
                contentStream.showText(message);
                contentStream.endText();

                contentStream.moveTo(startX - 2, startY - 2);
                contentStream.lineTo(startX - 2, startY + 200 + fontSize);
                contentStream.stroke();

                contentStream.moveTo(startX - 2, startY + 200 + fontSize);
                contentStream.lineTo(startX + 100 + stringWidth + 2, startY + 200 + fontSize);
                contentStream.stroke();

                contentStream.moveTo(startX + 100 + stringWidth + 2, startY + 200 + fontSize);
                contentStream.lineTo(startX + 100 + stringWidth + 2, startY - 2);
                contentStream.stroke();

                contentStream.moveTo(startX + 100 + stringWidth + 2, startY - 2);
                contentStream.lineTo(startX - 2, startY - 2);
                contentStream.stroke();
            }

            doc.save(outfile);
        }
    }

    public static void RemoveFirstPage(String filePath) throws IOException {
        try ( PDDocument document = Loader.loadPDF(new File(filePath))) {
            if (document.isEncrypted()) {
                throw new IOException("Encrypted documents are not supported for this example");
            }
            if (document.getNumberOfPages() <= 1) {
                throw new IOException("Error: A PDF document must have at least one page, "
                        + "cannot remove the last page!");
            }
            document.removePage(0);
            document.save(filePath);
        }
    }
}
