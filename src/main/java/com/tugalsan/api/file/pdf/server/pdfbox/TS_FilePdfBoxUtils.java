package com.tugalsan.api.file.pdf.server.pdfbox;

import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.charset.client.TGS_CharSetUTF8;
import com.tugalsan.api.file.html.server.TS_FileHtmlUtils;
import com.tugalsan.api.file.img.server.TS_FileImageUtils;
import com.tugalsan.api.file.server.TS_DirectoryUtils;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.txt.server.TS_FileTxtUtils;
import com.tugalsan.api.list.client.TGS_ListUtils;
import java.io.*;
import java.awt.image.*;
import org.apache.pdfbox.pdmodel.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.shape.client.TGS_ShapeDimension;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;
import org.fit.pdfdom.PDFDomTree;

public class TS_FilePdfBoxUtils {

    final private static TS_Log d = TS_Log.of(TS_FilePdfBoxUtils.class);

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static Optional<Path> toJpg(Path pdfSrcFile, Path jpgDstFile, int pageNumber) {
        return TGS_UnSafe.call(() -> {
            TS_FileUtils.deleteFileIfExists(jpgDstFile);
            if (TS_FileUtils.isExistFile(jpgDstFile)) {
                return Optional.empty();
            }
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                var renderer = new PDFRenderer(doc);
                var image = renderer.renderImage(pageNumber);
                var result = ImageIO.write(image, "JPEG", jpgDstFile.toFile());
                if (!result) {
                    return Optional.empty();
                }
            }
            return TS_FileUtils.isExistFile(jpgDstFile) ? Optional.of(jpgDstFile) : Optional.empty();
        }, e -> {
            e.printStackTrace();
            return Optional.empty();
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static boolean combine(List<Path> pdfSrcFiles, Path pdfDstFile) {
        return TGS_UnSafe.call(() -> {
            TS_FileUtils.deleteFileIfExists(pdfDstFile);
            if (TS_FileUtils.isExistFile(pdfDstFile)) {
                return false;
            }
            var pdfMerger = new PDFMergerUtility();
            pdfMerger.setDestinationFileName(pdfDstFile.toAbsolutePath().toString());
            for (var nextPdfSrcFile : pdfSrcFiles) {
                pdfMerger.addSource(nextPdfSrcFile.toFile());
            }
            pdfMerger.mergeDocuments(null);
            return TS_FileUtils.isExistFile(pdfDstFile);
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static Optional<Integer> size(Path pdfFile) {
        return TGS_UnSafe.call(() -> {
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFile.toAbsolutePath().toString()))) {
                return Optional.of(doc.getNumberOfPages());
            }
        }, e -> {
            e.printStackTrace();
            return Optional.empty();
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static boolean extract(Path pdfSrcFile, int pageNr, Path pdfDstFile) {
        return TGS_UnSafe.call(() -> {
            TS_FileUtils.deleteFileIfExists(pdfDstFile);
            if (TS_FileUtils.isExistFile(pdfDstFile)) {
                return false;
            }
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
//                var fromPage = pageNr;
//                var toPage = pageNr;
//                var splitter = new Splitter();
//                splitter.setStartPage(fromPage);
//                splitter.setEndPage(toPage);
//                splitter.setSplitAtPage(toPage - fromPage + 1);
//                var lst = splitter.split(doc);
//                var pdfDocPartial = lst.get(0);
//                pdfDocPartial.save(pdfDstFile.toFile());
                try (var out = new PDDocument();) {
                    out.addPage(doc.getPage(pageNr));
                    out.save(pdfDstFile.toFile());
                }
            }
            return TS_FileUtils.isExistFile(pdfDstFile);
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static boolean extract(Path pdfSrcFile, int[] pageNrs, Path pdfDstFile) {
        return TGS_UnSafe.call(() -> {
            TS_FileUtils.deleteFileIfExists(pdfDstFile);
            if (TS_FileUtils.isExistFile(pdfDstFile)) {
                return false;
            }
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                try (var out = new PDDocument();) {
                    for (var pageNr : pageNrs) {
//                        var fromPage = pageNr;
//                        var toPage = pageNr;
//                        var splitter = new Splitter();
//                        splitter.setStartPage(fromPage);
//                        splitter.setEndPage(toPage);
//                        splitter.setSplitAtPage(toPage - fromPage + 1);
//                        var lst = splitter.split(doc);
//                        var pdfDocPartial = lst.get(0);
                        out.addPage(doc.getPage(pageNr));
                    }
                    out.save(pdfDstFile.toFile());
                }
            }
            return TS_FileUtils.isExistFile(pdfDstFile);
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static void rotatePage(Path pdfSrcFile, Path pdfDstFile, int degree, float rotateX, float rotateY) {
        TGS_UnSafe.run(() -> {
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                TGS_StreamUtils.of(doc.getDocumentCatalog().getPages()).forEach(page -> {
                    page.setRotation(degree);
                });
                doc.save(pdfDstFile.toFile());
            }
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static void rotateWithCropBox(Path pdfSrcFile, Path pdfDstFile, int degree, float rotateX, float rotateY) {
        TGS_UnSafe.run(() -> {
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                var page = doc.getDocumentCatalog().getPages().get(0);
                var matrix = Matrix.getRotateInstance(Math.toRadians(degree), rotateX, rotateY);
                try (var cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, false, false);) {
                    cs.transform(matrix);
                }
                var cropBox = page.getCropBox();
                var rectangle = cropBox.transform(matrix).getBounds();
                var newBox = new PDRectangle((float) rectangle.getX(), (float) rectangle.getY(), (float) rectangle.getWidth(), (float) rectangle.getHeight());
                page.setCropBox(newBox);
                page.setMediaBox(newBox);
                doc.save(pdfDstFile.toFile());
            }
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static void rotateAndFitContent(Path pdfSrcFile, Path pdfDstFile, int degree, float rotateX, float rotateY) {
        TGS_UnSafe.run(() -> {
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                var page = doc.getDocumentCatalog().getPages().get(0);
                try (var cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, false, false);) {
                    var matrix = Matrix.getRotateInstance(Math.toRadians(degree), rotateX, rotateY);
                    var cropBox = page.getCropBox();
                    var tx = (cropBox.getLowerLeftX() + cropBox.getUpperRightX()) / 2;
                    var ty = (cropBox.getLowerLeftY() + cropBox.getUpperRightY()) / 2;
                    var rectangle = cropBox.transform(matrix).getBounds();
                    var scale = Math.min(cropBox.getWidth() / (float) rectangle.getWidth(), cropBox.getHeight() / (float) rectangle.getHeight());
                    cs.transform(Matrix.getTranslateInstance(tx, ty));
                    cs.transform(matrix);
                    cs.transform(Matrix.getScaleInstance(scale, scale));
                    cs.transform(Matrix.getTranslateInstance(-tx + tx, -ty + ty));
                }
                doc.save(pdfDstFile.toFile());
            }
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static void scale(Path pdfSrcFile, Path pdfDstFile, float xScale, float yScale) {
        TGS_UnSafe.run(() -> {
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                var page = doc.getPage(0);
                try (var cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, true);) {
                    var matrix = new Matrix();
                    matrix.scale(xScale, yScale);
                    cs.transform(matrix);
                }
                doc.save(pdfDstFile.toFile());
            }
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static void scaleToA4(Path pdfSrcFile, Path pdfDstFile, float scaleFactor) {
        TGS_UnSafe.run(() -> {
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                var page = doc.getPage(0);
                try (var cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, true);) {
                    var matrix = new Matrix();
                    var xScale = PDRectangle.A4.getWidth() / page.getMediaBox().getWidth();
                    var yScale = PDRectangle.A4.getHeight() / page.getMediaBox().getHeight();
                    matrix.scale(xScale, yScale);
                    cs.transform(matrix);
                }
                doc.save(pdfDstFile.toFile());
            }
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    public static void compress(Path pdfSrcFile, Path pdfDstFile, float compQual_fr0_to1, boolean lossless) {
        TGS_UnSafe.run(() -> {
            float compQual = Math.max(0, Math.min(compQual_fr0_to1, 1));
            try (var doc = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfSrcFile.toAbsolutePath().toString()))) {
                var pages = doc.getPages();
                final ImageWriter imgWriter;
                final ImageWriteParam iwp;
                if (lossless) {
                    final Iterator<ImageWriter> tiffWriters
                            = ImageIO.getImageWritersBySuffix("png");
                    imgWriter = tiffWriters.next();
                    iwp = imgWriter.getDefaultWriteParam();
                    //iwp.setCompressionMode(ImageWriteParam.MODE_DISABLED);
                } else {
                    final Iterator<ImageWriter> jpgWriters
                            = ImageIO.getImageWritersByFormatName("jpeg");
                    imgWriter = jpgWriters.next();
                    iwp = imgWriter.getDefaultWriteParam();
                    iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    iwp.setCompressionQuality(compQual);
                }
                for (PDPage p : pages) {
                    compress_scanResources(p.getResources(), doc, imgWriter, iwp, lossless);
                }
                doc.save(pdfDstFile.toFile());
            }
        });
    }

    @Deprecated //TODO: I just wrote it. Not Tested!
    private static void compress_scanResources(
            final PDResources rList,
            final PDDocument doc,
            final ImageWriter imgWriter,
            final ImageWriteParam iwp, boolean lossless)
            throws FileNotFoundException, IOException {
        Iterable<COSName> xNames = rList.getXObjectNames();
        for (COSName xName : xNames) {
            final PDXObject xObj = rList.getXObject(xName);
            if (!(xObj instanceof PDImageXObject)) {
                continue;
            }
            var o = (PDFormXObject) xObj;
            compress_scanResources(o.getResources(), doc, imgWriter, iwp, lossless);
            final PDImageXObject img = (PDImageXObject) xObj;
            System.out.println("Compressing image: " + xName.getName());
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgWriter.setOutput(ImageIO.createImageOutputStream(baos));
            BufferedImage bi = img.getImage();
            IIOImage iioi;
            iioi = switch (bi.getTransparency()) {
                case BufferedImage.OPAQUE ->
                    new IIOImage(bi, null, null);
                case BufferedImage.TRANSLUCENT ->
                    new IIOImage(img.getOpaqueImage(), null, null);
                default ->
                    new IIOImage(img.getOpaqueImage(), null, null);
            };
            imgWriter.write(null, iioi, iwp);
            final ByteArrayInputStream bais
                    = new ByteArrayInputStream(baos.toByteArray());
            final PDImageXObject imgNew;
            if (lossless) {
                imgNew = LosslessFactory.createFromImage(doc, img.getImage());
            } else {
                imgNew = JPEGFactory.createFromStream(doc, bais);
            }
            rList.put(xName, imgNew);
        }
    }

    public static Path castFromPDFtoHTM(Path srcPDF, Path dstHTM, CharSequence optionalTitle, CharSequence optionalHeaderContent, CharSequence optional_iframe_video, boolean addLoader) {
        if (true) {
            return TS_FileTxtUtils.toFile("""
            PDF'den HTM ön izlene dosyası oluşturuken bir hata oluştu. Lütfen orjinal pdf dosyayı indiriniz.<br>
            An error occured creating HTM preview file from PDF. Please download the original pdf file.<br>
            <br>
            %s
            """.formatted("ERROR: org.apache.pdfbox version incompatible; disabled until further notice!"), dstHTM, false);
        }
        d.ci("castFromPDFtoHTM", srcPDF, dstHTM);
        castFromPDFtoHTM_do(srcPDF, dstHTM);
        var strHtm = TS_FileTxtUtils.toString(dstHTM);
        if (addLoader) {
            strHtm = TS_FileHtmlUtils.addLoader(strHtm);
        }
        if (optional_iframe_video != null) {
            strHtm = TS_FileHtmlUtils.appendResponsiveVideo(strHtm, optional_iframe_video);
        }
        if (optionalHeaderContent != null) {
            strHtm = TS_FileHtmlUtils.appendToBodyStartAfter(strHtm, optionalHeaderContent);
        }
        if (optionalTitle != null) {
            strHtm = TS_FileHtmlUtils.updateTitleContent(strHtm, optionalTitle);
        }
        return TS_FileTxtUtils.toFile(strHtm, dstHTM, false);
    }

    private static Path castFromPDFtoHTM_do(Path srcPDF, Path dstHTM) {
        return TGS_UnSafe.call(() -> {
            d.cr("castFromPDFtoHTM", "init", srcPDF, dstHTM);
            try (var pdf = Loader.loadPDF(new RandomAccessReadBufferedFile(srcPDF.toFile())); var output = new PrintWriter(dstHTM.toFile(), TGS_CharSetUTF8.UTF8);) {
                new PDFDomTree().writeText(pdf, output);
                d.cr("castFromPDFtoHTM", "success");
            }
            return dstHTM;
        });
    }

    public static PDImageXObject getImage(Path imgFile, PDDocument document) {
        return TGS_UnSafe.call(() -> PDImageXObject.createFromFile(imgFile.toAbsolutePath().toString(), document));
    }

    public static PDImageXObject getImage(BufferedImage bi, PDDocument document) {
        return TGS_UnSafe.call(() -> LosslessFactory.createFromImage(document, bi));
    }

    public static void insertImage(PDDocument document, PDPage page, PDImageXObject pdImage, int offsetX, int offsetY, float scale) {
        TGS_UnSafe.run(() -> {
            try (var contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.drawImage(pdImage, offsetX, offsetY, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
            }
        });
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
        return TGS_UnSafe.call(() -> {
            d.cr("castFromJPGtoPDF", "init", srcIMG, dstPDF);
            TS_FileUtils.deleteFileIfExists(dstPDF);
            var bi = TS_FileImageUtils.autoSizeRespectfully(TS_FileImageUtils.readImageFromFile(srcIMG, true),
                    new TGS_ShapeDimension(612, 792),
                    0.8f
            );
            try (var document = new PDDocument();) {
                var blankPage = new PDPage();
                document.addPage(blankPage);
                var pdImage = getImage(bi, document);
                insertImage(document, blankPage, pdImage, 0, 0, 1f);
                document.save(dstPDF.toFile());
            }
            return dstPDF;
        }, e -> {
            d.ce("castFromIMGtoPDF_A4PORT", "failed", e.getMessage());
            return TGS_UnSafe.thrwReturns(e);
        });
    }

    public static boolean isSupportedIMG(Path imgFile) {
        var fn = TGS_CharSetCast.toLocaleLowerCase(imgFile.getFileName().toString());
        return fn.endsWith(".jpg") || fn.endsWith(".jpeg") || fn.endsWith(".tif") || fn.endsWith(".tiff") || fn.endsWith(".gif") || fn.endsWith(".bmp") || fn.endsWith(".png");
    }

    public static Path createPageBlank(Path path) {
        return TGS_UnSafe.call(() -> {
            try (var document = new PDDocument();) {
                var blankPage = new PDPage();
                document.addPage(blankPage);
                document.save(path.toFile());
                return path;
            }
        }, e -> {
            d.ce("createPageBlank", "failed", e.getMessage());
            return TGS_UnSafe.thrwReturns(e);
        });
    }

    public static Path createPageText(Path path, String text) {
        return TGS_UnSafe.call(() -> {
            try (var document = new PDDocument();) {
                var page = new PDPage();
                document.addPage(page);
                try (var contentStream = new PDPageContentStream(document, page);) {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                    contentStream.newLineAtOffset(100, 700);
                    contentStream.showText("Hello World");
                    contentStream.endText();
                }
                document.save(path.toFile());
                return path;
            }
        }, e -> {
            d.ce("createPageText", "failed", e.getMessage());
            return TGS_UnSafe.thrwReturns(e);
        });
    }
}
