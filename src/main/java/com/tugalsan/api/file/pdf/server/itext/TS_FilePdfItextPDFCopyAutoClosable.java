package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import java.io.FileOutputStream;

public class TS_FilePdfItextPDFCopyAutoClosable extends PdfCopy implements AutoCloseable {

    public TS_FilePdfItextPDFCopyAutoClosable(TS_FilePdfItextDocumentAutoClosable r, FileOutputStream zos) throws DocumentException {
        super(r, zos);
    }

    @Override
    public void close() {
        super.close();
    }

}
