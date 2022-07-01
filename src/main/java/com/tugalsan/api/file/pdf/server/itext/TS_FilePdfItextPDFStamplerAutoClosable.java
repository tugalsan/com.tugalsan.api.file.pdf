package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfStamper;
import com.tugalsan.api.unsafe.client.*;
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
        TGS_UnSafe.execute(() -> super.close());
    }
}
