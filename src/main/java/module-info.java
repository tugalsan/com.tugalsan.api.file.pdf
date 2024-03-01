module com.tugalsan.api.file.pdf {
    requires java.desktop;
    requires itextpdf;
    requires jai.imageio.core;
    requires org.apache.pdfbox; //TODO https://stackoverflow.com/questions/18189314/convert-a-pdf-file-to-image
    requires org.apache.pdfbox.io;
    requires net.sf.cssbox.pdf2dom;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.runnable;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.coronator;
    requires com.tugalsan.api.font;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.validator;
    requires com.tugalsan.api.callable;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.charset;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.shape;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.file.txt;
    requires com.tugalsan.api.file.common;
    requires com.tugalsan.api.file.html;
    requires com.tugalsan.api.file.img;
    exports com.tugalsan.api.file.pdf.server;
    exports com.tugalsan.api.file.pdf.server.itext;
    exports com.tugalsan.api.file.pdf.server.pdfbox;
}
