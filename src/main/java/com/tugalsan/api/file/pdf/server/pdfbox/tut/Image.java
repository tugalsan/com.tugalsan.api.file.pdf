package com.tugalsan.api.file.pdf.server.pdfbox.tut;

import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Image {

    public static TGS_UnionExcuseVoid ImageToPDF(String imagePath, String pdfPath) {
        try (var doc = new PDDocument()) {
            var page = new PDPage();
            doc.addPage(page);

            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage, 
            // call LosslessFactory.createFromImage() instead
            var pdImage = PDImageXObject.createFromFile(imagePath, doc);

            // draw the image at full size at (x=20, y=20)
            try (var contents = new PDPageContentStream(doc, page)) {
                // draw the image at full size at (x=20, y=20)
                contents.drawImage(pdImage, 20, 20);

                // to draw the image at half size at (x=20, y=20) use
                // contents.drawImage(pdImage, 20, 20, pdImage.getWidth() / 2, pdImage.getHeight() / 2); 
            }
            doc.save(pdfPath);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid AddImageToPDF(String inputFile, String imagePath, String outputFile) {
        try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(inputFile))) {
            //we will add the image to the first page.
            var page = doc.getPage(0);

            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage, 
            // call LosslessFactory.createFromImage() instead
            var pdImage = PDImageXObject.createFromFile(imagePath, doc);

            try (var contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                // contentStream.drawImage(ximage, 20, 20 );
                // better method inspired by http://stackoverflow.com/a/22318681/535646
                // reduce this value if the image is too large
                var scale = 1f;
                contentStream.drawImage(pdImage, 20, 20, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
            }
            doc.save(outputFile);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

}
