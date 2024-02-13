package com.tugalsan.api.file.pdf.server.itext;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.tugalsan.api.unsafe.client.*;

public class TS_FilePdfItextUtilsPage {

    private static void combine_add(PdfCopy c, Path file) {
        TGS_UnSafe.run(() -> {
            try ( var r = new TS_FilePdfItextPDFReaderAutoClosable(file);) {
                var nop = r.getNumberOfPages();
                for (var p = 0; p < nop; p++) {
                    c.addPage(c.getImportedPage(r, (p + 1)));
                }
            }
        });
    }

    public static void combine(List<Path> pdfSrcFiles, Path pdfDstFile) {
        TGS_UnSafe.run(() -> {
            try ( var zos = new FileOutputStream(pdfDstFile.toAbsolutePath().toString());  var d = new TS_FilePdfItextDocumentAutoClosable();  var c = new TS_FilePdfItextPDFCopyAutoClosable(d, zos);) {
                d.open();
                pdfSrcFiles.stream().forEachOrdered(file -> combine_add(c, file));
            }
        });
    }

    public static int size(Path pdfFile) {
        return TGS_UnSafe.call(() -> {
            try ( var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfFile);) {
                return reader.getNumberOfPages();
            }
        });
    }

    public static void extract(Path pdfSrcFile, int pageNr, Path pdfDstFile) {
        TGS_UnSafe.run(() -> {
            try ( var zos = new FileOutputStream(pdfDstFile.toFile());  var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile);  var document = new TS_FilePdfItextDocumentAutoClosable(reader.getPageSizeWithRotation(1));  var writer = new TS_FilePdfItextPDFCopyAutoClosable(document, zos);) {
                document.open();
                writer.addPage(writer.getImportedPage(reader, pageNr + 1));
            }
        });
    }

    public static void extract(Path pdfSrcFile, int[] pageNrs, Path pdfDstFile) {
        TGS_UnSafe.run(() -> {
            try ( var zos = new FileOutputStream(pdfDstFile.toFile());  var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile);  var document = new TS_FilePdfItextDocumentAutoClosable(reader.getPageSizeWithRotation(1));  var writer = new TS_FilePdfItextPDFCopyAutoClosable(document, zos);) {
                document.open();
                Arrays.stream(pageNrs).forEach(pageNr -> {
                    TGS_UnSafe.run(() -> {
                        writer.addPage(writer.getImportedPage(reader, pageNr + 1));
                    }, e -> e.printStackTrace());
                });
            }
        });
    }

    public static void rotate(Path pdfSrcFile, Path pdfDstFile, int degree) {
        TGS_UnSafe.run(() -> {
            try ( var zos = new FileOutputStream(pdfDstFile.toFile());  var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile);) {
                var n = reader.getNumberOfPages();
                IntStream.rangeClosed(1, n).parallel().forEach(i -> {
                    var dictI = reader.getPageN(i);
                    var num = dictI.getAsNumber(PdfName.ROTATE);
                    dictI.put(PdfName.ROTATE, num == null ? new PdfNumber(degree) : new PdfNumber((num.intValue() + degree) % 360));
                });
                try ( var stamper = new TS_FilePdfItextPDFStamplerAutoClosable(reader, zos);) {
                }
            }
        });
    }

    public static void scale(Path pdfSrcFile, Path pdfDstFile, float scaleFactor) {
        TGS_UnSafe.run(() -> {
            try ( var zos = new FileOutputStream(pdfDstFile.toFile());  var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile);  var stamper = new TS_FilePdfItextPDFStamplerAutoClosable(reader, zos);) {
                var n = reader.getNumberOfPages();
                IntStream.rangeClosed(1, n).forEachOrdered(p -> {
                    var offsetX = (reader.getPageSize(p).getWidth() * (1 - scaleFactor)) / 2;
                    var offsetY = (reader.getPageSize(p).getHeight() * (1 - scaleFactor)) / 2;
                    stamper.getUnderContent(p).setLiteral(String.format("\nq %s 0 0 %s %s %s cm\nq\n", scaleFactor, scaleFactor, offsetX, offsetY));
                    stamper.getOverContent(p).setLiteral("\nQ\nQ\n");
                });
            }
        });
    }

    public static void compress(Path pdfSrcFile, Path pdfDstFile) {
        TGS_UnSafe.run(() -> {
            try ( var zos = new FileOutputStream(pdfDstFile.toFile());  var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile);  var stamper = new TS_FilePdfItextPDFStamplerAutoClosable(reader, zos, TS_FilePdfItextPDFStamplerAutoClosable.VERSION_1_5());) {
                stamper.getWriter().setCompressionLevel(9);
                var n = reader.getNumberOfPages();
                IntStream.rangeClosed(1, n).forEachOrdered(p -> {
                    TGS_UnSafe.run(() -> {
                        reader.setPageContent(p, reader.getPageContent(p));
                    });
                });
                stamper.setFullCompression();
            }
        });
    }

    public static void addHeader(Path pdfSrcFile, Path pdfDstFile) {
        TGS_UnSafe.run(() -> {
            var size = size(pdfSrcFile);
            try ( var zos = new FileOutputStream(pdfDstFile.toFile());  var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile);  var document = new TS_FilePdfItextDocumentAutoClosable(reader.getPageSizeWithRotation(1));  var writer = new TS_FilePdfItextPDFCopyAutoClosable(document, zos);) {
                document.open();
                var writerContent = writer.getDirectContent();
                IntStream.rangeClosed(1, size).forEachOrdered(p -> {
                    TGS_UnSafe.run(() -> {
                        var page = writer.getImportedPage(reader, 1);
                        document.newPage();
                        writerContent.addTemplate(page, 0, 0);
                        document.add(new Paragraph("my timestamp"));
                    });
                });
//            document.addHeader("Header Name", "Header Content");
            }
        });
    }

//    public static void main(String... s) {
//        var pdfSrcFile = Path.of("D:\\a.pdf");
//        var pdfDstFile = Path.of("D:\\b.pdf");
//        addHeader(pdfSrcFile, pdfDstFile);
//    }
}
