package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.DocumentException;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;

public class TS_FilePdfItextUtilsPage {

    private static TGS_UnionExcuseVoid combine_add(PdfCopy c, Path file) {
        try (var r = new TS_FilePdfItextPDFReaderAutoClosable(file);) {
            var nop = r.getNumberOfPages();
            for (var p = 0; p < nop; p++) {
                c.addPage(c.getImportedPage(r, (p + 1)));
            }
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException | BadPdfFormatException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid combine(List<Path> pdfSrcFiles, Path pdfDstFile) {
        try (var zos = new FileOutputStream(pdfDstFile.toAbsolutePath().toString()); var d = new TS_FilePdfItextDocumentAutoClosable(); var c = new TS_FilePdfItextPDFCopyAutoClosable(d, zos);) {
            d.open();
            pdfSrcFiles.stream().forEachOrdered(file -> combine_add(c, file));
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException | DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuse<Integer> size(Path pdfFile) {
        try (var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfFile);) {
            return TGS_UnionExcuse.of(reader.getNumberOfPages());
        } catch (IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid extract(Path pdfSrcFile, int pageNr, Path pdfDstFile) {
        try (var zos = new FileOutputStream(pdfDstFile.toFile()); var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile); var document = new TS_FilePdfItextDocumentAutoClosable(reader.getPageSizeWithRotation(1)); var writer = new TS_FilePdfItextPDFCopyAutoClosable(document, zos);) {
            document.open();
            writer.addPage(writer.getImportedPage(reader, pageNr + 1));
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException | DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid extract(Path pdfSrcFile, int[] pageNrs, Path pdfDstFile) throws IOException {
        try (var zos = new FileOutputStream(pdfDstFile.toFile()); var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile); var document = new TS_FilePdfItextDocumentAutoClosable(reader.getPageSizeWithRotation(1)); var writer = new TS_FilePdfItextPDFCopyAutoClosable(document, zos);) {
            document.open();
            for (var pageNr : pageNrs) {
                writer.addPage(writer.getImportedPage(reader, pageNr + 1));
            }
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (DocumentException | IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid rotate(Path pdfSrcFile, Path pdfDstFile, int degree) {
        try (var zos = new FileOutputStream(pdfDstFile.toFile()); var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile);) {
            var n = reader.getNumberOfPages();
            IntStream.rangeClosed(1, n).parallel().forEach(i -> {
                var dictI = reader.getPageN(i);
                var num = dictI.getAsNumber(PdfName.ROTATE);
                dictI.put(PdfName.ROTATE, num == null ? new PdfNumber(degree) : new PdfNumber((num.intValue() + degree) % 360));
            });
            try (var stamper = new TS_FilePdfItextPDFStamplerAutoClosable(reader, zos);) {
            }
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException | DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid scale(Path pdfSrcFile, Path pdfDstFile, float scaleFactor) {
        try (var zos = new FileOutputStream(pdfDstFile.toFile()); var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile); var stamper = new TS_FilePdfItextPDFStamplerAutoClosable(reader, zos);) {
            var n = reader.getNumberOfPages();
            IntStream.rangeClosed(1, n).forEachOrdered(p -> {
                var offsetX = (reader.getPageSize(p).getWidth() * (1 - scaleFactor)) / 2;
                var offsetY = (reader.getPageSize(p).getHeight() * (1 - scaleFactor)) / 2;
                stamper.getUnderContent(p).setLiteral(String.format("\nq %s 0 0 %s %s %s cm\nq\n", scaleFactor, scaleFactor, offsetX, offsetY));
                stamper.getOverContent(p).setLiteral("\nQ\nQ\n");
            });
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException | DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid compress(Path pdfSrcFile, Path pdfDstFile) {
        try (var zos = new FileOutputStream(pdfDstFile.toFile()); var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile); var stamper = new TS_FilePdfItextPDFStamplerAutoClosable(reader, zos, TS_FilePdfItextPDFStamplerAutoClosable.VERSION_1_5());) {
            stamper.getWriter().setCompressionLevel(9);
            var n = reader.getNumberOfPages();
            for (var pageNr = 1; pageNr < n; pageNr++) {
                reader.setPageContent(pageNr, reader.getPageContent(pageNr));
            }
            stamper.setFullCompression();
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException | DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuseVoid addHeader(Path pdfSrcFile, Path pdfDstFile) {
        var u_size = size(pdfSrcFile);
        if (u_size.isExcuse()) {
            return u_size.toExcuseVoid();
        }
        var size = u_size.value();
        try (var zos = new FileOutputStream(pdfDstFile.toFile()); var reader = new TS_FilePdfItextPDFReaderAutoClosable(pdfSrcFile); var document = new TS_FilePdfItextDocumentAutoClosable(reader.getPageSizeWithRotation(1)); var writer = new TS_FilePdfItextPDFCopyAutoClosable(document, zos);) {
            document.open();
            var writerContent = writer.getDirectContent();
            for (var pageNr = 1; pageNr < size; pageNr++) {
                var page = writer.getImportedPage(reader, 1);
                document.newPage();
                writerContent.addTemplate(page, 0, 0);
                document.add(new Paragraph("my timestamp"));
            }
//            document.addHeader("Header Name", "Header Content");
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException | DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

//    public static void main(String... s) {
//        var pdfSrcFile = Path.of("D:\\a.pdf");
//        var pdfDstFile = Path.of("D:\\b.pdf");
//        addHeader(pdfSrcFile, pdfDstFile);
//    }
}
