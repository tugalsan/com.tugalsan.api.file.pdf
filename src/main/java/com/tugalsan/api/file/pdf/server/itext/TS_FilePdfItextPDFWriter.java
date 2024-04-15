package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import java.io.OutputStream;

public class TS_FilePdfItextPDFWriter extends PdfWriter implements AutoCloseable {

    public TS_FilePdfItextPDFWriter(TS_FilePdfItextPdfDocument pdf, OutputStream os) {
        super(pdf, os);
    }

    public static TGS_UnionExcuse<TS_FilePdfItextPDFWriter> getInstance(TS_FilePdfItextDocumentAutoClosable document, OutputStream os) {
        try {
            var pdf = new TS_FilePdfItextPdfDocument();
            document.addDocListener(pdf);
            var writer = new TS_FilePdfItextPDFWriter(pdf, os);
            pdf.addWriter(writer);
            return TGS_UnionExcuse.of(writer);
        } catch (DocumentException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

}
