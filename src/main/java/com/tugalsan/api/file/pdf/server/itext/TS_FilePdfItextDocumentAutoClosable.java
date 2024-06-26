package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;

public class TS_FilePdfItextDocumentAutoClosable extends Document implements AutoCloseable {

//    public static Document use(TGS_CallableType1_Run<Document> doc) {
//        return use(null, null, null, null, null, doc);
//    }
//
//    public static Document usePageAs(Rectangle pageSize, TGS_CallableType1_Run<Document> doc) {
//        return use(pageSize, null, null, null, null, doc);
//    }
//
//    public static Document usePageAndMarginAs(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom, TGS_CallableType1_Run<Document> doc) {
//        return use(pageSize, null, null, null, null, doc);
//    }
//
//    private static Document use(Rectangle pageSize, Float marginLeft, Float marginRight, Float marginTop, Float marginBottom, TGS_CallableType1_Run<Document> doc) {
//        Document d = null;
//        try {
//            if (pageSize == null) {
//                d = new Document();
//            } else {
//                if (marginLeft == null) {
//                    d = new Document(pageSize);
//                } else {
//                    d = new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom);
//                }
//            }
//            doc.run(d);
//            return d;
//        } finally {
//            if (d != null) {
//                d.close();
//            }
//        }
//    }

    public TS_FilePdfItextDocumentAutoClosable() {
        super();
    }

    public TS_FilePdfItextDocumentAutoClosable(Rectangle pageSize) {
        super(pageSize);
    }

    public TS_FilePdfItextDocumentAutoClosable(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom) {
        super(pageSize, marginLeft, marginRight, marginTop, marginBottom);
    }

    @Override
    public void close() {
        super.close();
    }
}
