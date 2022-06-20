package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.pdf.PdfReader;
import java.io.IOException;
import java.nio.file.Path;

public class TS_FilePdfItextPDFReaderAutoClosable extends PdfReader implements AutoCloseable {

    public TS_FilePdfItextPDFReaderAutoClosable(Path file) throws IOException{
        super(file.toAbsolutePath().toString());
    }
    
    @Override
    public void close() {
        super.close();
    }

}
