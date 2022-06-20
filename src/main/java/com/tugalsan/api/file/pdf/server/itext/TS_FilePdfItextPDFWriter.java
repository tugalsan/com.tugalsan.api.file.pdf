package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.pdf.PdfWriter;
import java.io.OutputStream;

public class TS_FilePdfItextPDFWriter extends PdfWriter implements AutoCloseable {

    public TS_FilePdfItextPDFWriter(TS_FilePdfItextPdfDocument pdf, OutputStream os) {
        super(pdf, os);
    }

    @Override
    public void close() {
        super.close();
    }

    public static TS_FilePdfItextPDFWriter getInstance(TS_FilePdfItextDocumentAutoClosable document, OutputStream os) {
        try {//dont try to autoclose!
            var pdf = new TS_FilePdfItextPdfDocument();
            document.addDocListener(pdf);
            var writer = new TS_FilePdfItextPDFWriter(pdf, os);
            pdf.addWriter(writer);
            return writer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
