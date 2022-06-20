package com.tugalsan.api.file.pdf.server.pdfbox;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.awt.image.*;
import org.apache.pdfbox.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.*;
import org.apache.pdfbox.pdmodel.*;
import org.fit.pdfdom.*;
import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.file.html.server.element.*;
import com.tugalsan.api.file.img.server.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.file.txt.server.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.shape.client.*;

public class TS_FilePdfBoxUtils {

    final private static TS_Log d = TS_Log.of(TS_FilePdfBoxUtils.class.getSimpleName());

    public static Path castFromPDFtoHTM(Path srcPDF, Path dstHTM, CharSequence optionalTitle, CharSequence optionalHeaderContent, CharSequence optional_iframe_video, boolean addLoader) {
        d.ci("castFromPDFtoHTM", srcPDF, dstHTM);
        castFromPDFtoHTM_do(srcPDF, dstHTM);
        var strHtm = TS_FileTxtUtils.toString(dstHTM);
        if (addLoader) {
            strHtm = TS_FileHtmlUtils.addLoader(strHtm);
        }
        if (optional_iframe_video != null) {
            strHtm = TS_FileHtmlUtils.appendResponsiveVideo(strHtm, optional_iframe_video);
        }
        if (optionalHeaderContent != null){
            strHtm = TS_FileHtmlUtils.appendToBodyStartAfter(strHtm, optionalHeaderContent);
        }
        if (optionalTitle != null) {
            strHtm = TS_FileHtmlUtils.updateTitleContent(strHtm, optionalTitle);
        }
        TS_FileTxtUtils.toFile(strHtm, dstHTM, false);
        return dstHTM;
    }

    private static Path castFromPDFtoHTM_do(Path srcPDF, Path dstHTM) {
        d.cr("castFromPDFtoHTM", "init", srcPDF, dstHTM);
        try ( var pdf = Loader.loadPDF(srcPDF.toFile());  var output = new PrintWriter(dstHTM.toFile(), TGS_CharacterSets.UTF8());) {
            new PDFDomTree().writeText(pdf, output);
            d.cr("castFromPDFtoHTM", "success");
        } catch (Exception e) {
            d.ce("castFromPDFtoHTM", "failed", e.getMessage());
            throw new RuntimeException(e);
        }
        return dstHTM;
    }

    public static PDImageXObject getImage(Path imgFile, PDDocument document) {
        try {
            return PDImageXObject.createFromFile(imgFile.toAbsolutePath().toString(), document);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static PDImageXObject getImage(BufferedImage bi, PDDocument document) {
        try {
            return LosslessFactory.createFromImage(document, bi);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void insertImage(PDDocument document, PDPage page, PDImageXObject pdImage, int offsetX, int offsetY, float scale) {
        try ( PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            contentStream.drawImage(pdImage, offsetX, offsetY, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
        } catch (Exception e) {
            d.ce("insertImage", "failed", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static List<Path> castFromIMGtoPDF_A4PORT_AllFiles(Path directory, boolean skipIfExists, boolean deleteIMGAfterConversion) {
        var subFiles = TS_DirectoryUtils.subFiles(directory, null, false, false);
        List<Path> convertedFiles = TGS_ListUtils.of();
        subFiles.stream().filter(subFile -> TS_FilePdfBoxUtils.isSupportedIMG(subFile)).forEach(subImg -> {
            var subPdf = subImg.resolveSibling(TS_FileUtils.getNameLabel(subImg) + ".pdf");
            if (TS_FileUtils.isExistFile(subPdf)) {
                if (skipIfExists) {
                    return;
                } else {
                    TS_FileUtils.deleteFileIfExists(subPdf);
                }
            }
            TS_FilePdfBoxUtils.castFromIMGtoPDF_A4PORT(subImg, subPdf);
            convertedFiles.add(subPdf);
            if (deleteIMGAfterConversion) {
                TS_FileUtils.deleteFileIfExists(subImg);
            }
        });
        return convertedFiles;
    }

    public static Path castFromIMGtoPDF_A4PORT(Path srcIMG, Path dstPDF) {
        d.cr("castFromJPGtoPDF", "init", srcIMG, dstPDF);
        TS_FileUtils.deleteFileIfExists(dstPDF);
        var bi = TS_FileImageUtils.autoSizeRespectfully(TS_FileImageUtils.readImageFromFile(srcIMG, true),
                new TGS_ShapeDimension(612, 792),
                0.8f
        );
        try ( var document = new PDDocument();) {
            var blankPage = new PDPage();
            document.addPage(blankPage);
            var pdImage = getImage(bi, document);
            insertImage(document, blankPage, pdImage, 0, 0, 1f);
            document.save(dstPDF.toFile());
        } catch (Exception e) {
            d.ce("castFromJPGtoPDF", "failed", e.getMessage());
            throw new RuntimeException(e);
        }
        return dstPDF;
    }

    public static boolean isSupportedIMG(Path imgFile) {
        var fn = imgFile.getFileName().toString().toLowerCase(Locale.ROOT);
        return fn.endsWith(".jpg") || fn.endsWith(".jpeg") || fn.endsWith(".tif") || fn.endsWith(".tiff") || fn.endsWith(".gif") || fn.endsWith(".bmp") || fn.endsWith(".png");
    }

    public static Path createPageBlank(Path path) {
        try ( var document = new PDDocument();) {
            var blankPage = new PDPage();
            document.addPage(blankPage);
            document.save(path.toFile());
            return path;
        } catch (Exception e) {
            d.ce("createPageBlank", "failed", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Path createPageText(Path path, String text) {
        try ( var document = new PDDocument();) {
            var page = new PDPage();
            document.addPage(page);
            try ( var contentStream = new PDPageContentStream(document, page);) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Hello World");
                contentStream.endText();
            }
            document.save(path.toFile());
            return path;
        } catch (Exception e) {
            d.ce("createPageBlank", "failed", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
