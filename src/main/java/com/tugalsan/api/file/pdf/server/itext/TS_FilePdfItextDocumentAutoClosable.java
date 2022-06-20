package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;

public class TS_FilePdfItextDocumentAutoClosable extends Document implements AutoCloseable {

    public TS_FilePdfItextDocumentAutoClosable() {
        super();
    }

    public TS_FilePdfItextDocumentAutoClosable(Rectangle rect) {
        super(rect);
    }

    public TS_FilePdfItextDocumentAutoClosable(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom) {
        super(pageSize, marginLeft, marginRight, marginTop, marginBottom);
    }

    @Override
    public void close() {
        super.close();
    }

}
