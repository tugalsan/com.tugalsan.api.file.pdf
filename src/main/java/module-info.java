module com.tugalsan.api.file.pdf {
    requires java.desktop;
    requires itextpdf;
    requires pdfbox;
    requires net.sf.cssbox.pdf2dom;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.charset;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.shape;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.file.txt;
    requires com.tugalsan.api.file.html;
    requires com.tugalsan.api.file.img;
    exports com.tugalsan.api.file.pdf.server.itext;
    exports com.tugalsan.api.file.pdf.server.pdfbox;
}
