package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.FileOutputStream;
import java.io.IOException;

public class TS_FilePdfItextPDFStamplerAutoClosable extends PdfStamper implements AutoCloseable {

    public static char VERSION_1_5() {
        return TS_FilePdfItextPDFWriter.VERSION_1_5;
    }

    public TS_FilePdfItextPDFStamplerAutoClosable(TS_FilePdfItextPDFReaderAutoClosable r, FileOutputStream zos, char ver) throws DocumentException, IOException {
        super(r, zos, ver);
    }

    public TS_FilePdfItextPDFStamplerAutoClosable(TS_FilePdfItextPDFReaderAutoClosable r, FileOutputStream zos) throws DocumentException, IOException {
        super(r, zos);
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
