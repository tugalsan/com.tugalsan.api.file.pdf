package com.tugalsan.api.file.pdf.server.itext;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.tugalsan.api.charset.client.*;
import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;
import com.tugalsan.api.coronator.client.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import java.io.IOException;

public class TS_FilePdfItextUtils implements AutoCloseable {

    final private static TS_Log d = TS_Log.of(TS_FilePdfItextUtils.class);

//    private static String GOOGLEPDF () "https://docs.google.com/gview?url=";
    public static BaseColor getFONT_COLOR_BLACK() {
        return BaseColor.BLACK;
    }

    public static BaseColor getFONT_COLOR_BLUE() {
        return BaseColor.BLUE;
    }

    public static BaseColor getFONT_COLOR_CYAN() {
        return BaseColor.CYAN;
    }

    public static BaseColor getFONT_COLOR_DARK_GRAY() {
        return BaseColor.DARK_GRAY;
    }

    public static BaseColor getFONT_COLOR_GRAY() {
        return BaseColor.GRAY;
    }

    public static BaseColor getFONT_COLOR_GREEN() {
        return BaseColor.GREEN;
    }

    public static BaseColor getFONT_COLOR_LIGHT_GRAY() {
        return BaseColor.LIGHT_GRAY;
    }

    public static BaseColor getFONT_COLOR_MAGENTA() {
        return BaseColor.MAGENTA;
    }

    public static BaseColor getFONT_COLOR_ORANGE() {
        return BaseColor.ORANGE;
    }

    public static BaseColor getFONT_COLOR_PINK() {
        return BaseColor.PINK;
    }

    public static BaseColor getFONT_COLOR_RED() {
        return BaseColor.RED;
    }

    public static BaseColor getFONT_COLOR_YELLOW() {
        return BaseColor.YELLOW;
    }

    public static Rectangle getPAGE_SIZE_A0_LAND() {
        return PageSize.A0.rotate();
    }

    public static Rectangle getPAGE_SIZE_A0_PORT() {
        return PageSize.A0;
    }

    public static Rectangle getPAGE_SIZE_A1_LAND() {
        return PageSize.A1.rotate();
    }

    public static Rectangle getPAGE_SIZE_A1_PORT() {
        return PageSize.A1;
    }

    public static Rectangle getPAGE_SIZE_A2_LAND() {
        return PageSize.A2.rotate();
    }

    public static Rectangle getPAGE_SIZE_A2_PORT() {
        return PageSize.A2;
    }

    public static Rectangle getPAGE_SIZE_A3_LAND() {
        return PageSize.A3.rotate();
    }

    public static Rectangle getPAGE_SIZE_A3_PORT() {
        return PageSize.A3;
    }

    public static Rectangle getPAGE_SIZE_A4_LAND() {
        return PageSize.A4.rotate();
    }

    public static Rectangle getPAGE_SIZE_A4_PORT() {
        return PageSize.A4;
    }

    public static Rectangle getPAGE_SIZE_A5_LAND() {
        return PageSize.A5.rotate();
    }

    public static Rectangle getPAGE_SIZE_A5_PORT() {
        return PageSize.A5;
    }

    public static Rectangle getPAGE_SIZE_A6_LAND() {
        return PageSize.A6.rotate();
    }

    public static Rectangle getPAGE_SIZE_A6_PORT() {
        return PageSize.A6;
    }

    public TS_FilePdfItextPDFWriter getWriter() {
        return writer;
    }
    private TS_FilePdfItextPDFWriter writer;

    public Document getDocument() {
        return document;
    }
    private TS_FilePdfItextDocumentAutoClosable document;

    public Path getFile() {
        return file;
    }
    private final Path file;

    public TS_FilePdfItextUtils(Path file) {
        this.file = file;
    }

    public TGS_UnionExcuseVoid createNewPage(int pageSizeAX0, boolean landscape, Integer marginLeft0, Integer marginRight0, Integer marginTop0, Integer marginBottom0) {
        try {
            d.ci("createNewPage");
            var marginLeft = marginLeft0 == null ? 50 : marginLeft0;
            var marginRight = marginRight0 == null ? 10 : marginRight0;
            var marginTop = marginTop0 == null ? 10 : marginTop0;
            var marginBottom = marginBottom0 == null ? 10 : marginBottom0;
            var pageSizeAX = TGS_Coronator.of(pageSizeAX0)
                    .anointIf(val -> val < 0, val -> 0)
                    .anointIf(val -> val > 6, val -> 6)
                    .coronate();
            var pageSize = TGS_Coronator.of(TS_FilePdfItextUtils.getPAGE_SIZE_A4_PORT())
                    .anointIf(val -> pageSizeAX == 0, val -> landscape ? TS_FilePdfItextUtils.getPAGE_SIZE_A0_LAND() : TS_FilePdfItextUtils.getPAGE_SIZE_A0_PORT())
                    .anointIf(val -> pageSizeAX == 1, val -> landscape ? TS_FilePdfItextUtils.getPAGE_SIZE_A1_LAND() : TS_FilePdfItextUtils.getPAGE_SIZE_A1_PORT())
                    .anointIf(val -> pageSizeAX == 2, val -> landscape ? TS_FilePdfItextUtils.getPAGE_SIZE_A2_LAND() : TS_FilePdfItextUtils.getPAGE_SIZE_A2_PORT())
                    .anointIf(val -> pageSizeAX == 3, val -> landscape ? TS_FilePdfItextUtils.getPAGE_SIZE_A3_LAND() : TS_FilePdfItextUtils.getPAGE_SIZE_A3_PORT())
                    .anointIf(val -> pageSizeAX == 4, val -> landscape ? TS_FilePdfItextUtils.getPAGE_SIZE_A4_LAND() : TS_FilePdfItextUtils.getPAGE_SIZE_A4_PORT())
                    .anointIf(val -> pageSizeAX == 5, val -> landscape ? TS_FilePdfItextUtils.getPAGE_SIZE_A5_LAND() : TS_FilePdfItextUtils.getPAGE_SIZE_A5_PORT())
                    .anointIf(val -> pageSizeAX == 6, val -> landscape ? TS_FilePdfItextUtils.getPAGE_SIZE_A6_LAND() : TS_FilePdfItextUtils.getPAGE_SIZE_A6_PORT())
                    .coronate();
            if (document == null) {
                document = new TS_FilePdfItextDocumentAutoClosable(pageSize, marginLeft, marginRight, marginTop, marginBottom);
                var u_writer = TS_FilePdfItextPDFWriter.getInstance(document, Files.newOutputStream(file));//I KNOW;
                if (u_writer.isExcuse()) {
                    return u_writer.toExcuseVoid();
                }
                writer = u_writer.value();
                document.open();
            } else {
                document.setPageSize(pageSize);
                document.newPage();
            }
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public void setAlignLeft(Paragraph p) {
        p.setAlignment(Element.ALIGN_LEFT);
    }

    public void setAlignRight(Paragraph p) {
        p.setAlignment(Element.ALIGN_RIGHT);
    }

    public void setAlignCenter(Paragraph p) {
        p.setAlignment(Element.ALIGN_CENTER);
    }

    public void setAlignJustified(Paragraph p) {
        p.setAlignment(Element.ALIGN_JUSTIFIED);
    }

    public Paragraph createParagraph() {
        return new Paragraph();
    }

    public Paragraph createParagraph(Font font) {
        var p = new Paragraph();
        p.setFont(font);
        return p;
    }

    private Chunk createChunkText(CharSequence text) {
        return new Chunk(text.toString());
    }

//    private Chunk createChunkLineSeperator(LineSeparator ls) {
//        return new Chunk(ls);
//    }
    private Chunk createChunkText(CharSequence text, Font font) {
        var c = new Chunk(text.toString());
        c.setFont(font);
        return c;
    }

    private Chunk createChunkNewLine() {
        return Chunk.NEWLINE;
    }

    public TGS_UnionExcuse<Image> createImage(java.awt.Image imageFile, Color color) {
        try {
            return TGS_UnionExcuse.of(Image.getInstance(imageFile, color));
        } catch (BadElementException | IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuse<Image> createImage(CharSequence filePath) {
        try {
            return TGS_UnionExcuse.of(Image.getInstance(filePath.toString()));
        } catch (BadElementException | IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid addImageToPageLeft(java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        var u = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        return addImageToPageLeft(u.value(), textWrap, transperancyAsWhite);
    }

    public TGS_UnionExcuseVoid addImageToPageLeft(Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "addImageToPageLeft", "image == null");
        }
        if (textWrap) {
            image.setAlignment(/*Image.ALIGN_LEFT | */Image.TEXTWRAP);//allign left is 0 already
        } else {
            image.setAlignment(Image.ALIGN_LEFT);
        }
        try {
            document.add(image);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid addImageToPageRight(java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        var u = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        return addImageToPageRight(u.value(), textWrap, transperancyAsWhite);
    }

    public TGS_UnionExcuseVoid addImageToPageRight(Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "addImageToPageRight", "image == null");
        }
        if (textWrap) {
            image.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
        } else {
            image.setAlignment(Image.ALIGN_RIGHT);
        }
        try {
            document.add(image);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid addImageToPageCenter(java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        var u = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        return addImageToPageCenter(u.value(), textWrap, transperancyAsWhite);
    }

    public TGS_UnionExcuseVoid addImageToPageCenter(Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "addImageToPageCenter", "image == null");
        }
        if (textWrap) {
            image.setAlignment(Image.ALIGN_CENTER | Image.TEXTWRAP);
        } else {
            image.setAlignment(Image.ALIGN_CENTER);
        }
        try {
            document.add(image);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid addImageToCellLeft(PdfPCell cell, java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "addImageToCellLeft", "addImageToCellLeft.ERROR: TKPDFDocument.addImageToCellLeft.imageAWT == null");
        }
        var u = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        var img = u.value();
        if (textWrap) {
            img.setAlignment(/*Image.ALIGN_LEFT | */Image.TEXTWRAP);//allign left is 0 already
        } else {
            img.setAlignment(Image.ALIGN_LEFT);
        }
        cell.addElement(img);
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public TGS_UnionExcuseVoid addImageToCellRight(PdfPCell cell, java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "addImageToCellRight", "addImageToCellRight.ERROR: TKPDFDocument.addImageToCellRight.imageAWT == null");
        }
        var u = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        var img = u.value();
        if (textWrap) {
            img.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
        } else {
            img.setAlignment(Image.ALIGN_RIGHT);
        }
        cell.addElement(img);
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public TGS_UnionExcuseVoid addImageToCellCenter(PdfPCell cell, java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "addImageToCellCenter", "addImageToCellCenter.ERROR: TKPDFDocument.addImageToCellCenter.imageAWT == null");
        }
        var u = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        var img = u.value();
        if (textWrap) {
            img.setAlignment(Image.ALIGN_CENTER | Image.TEXTWRAP);
        } else {
            img.setAlignment(Image.ALIGN_CENTER);
        }
        cell.addElement(img);
        return TGS_UnionExcuseVoid.ofVoid();
    }

    private void addChunkToParagraph(Chunk c, Paragraph p) {
        p.add(c);
    }

    public void addTextToParagraph(CharSequence text, Paragraph p) {
        addChunkToParagraph(createChunkText(text), p);
    }

    public void addTextToParagraph(CharSequence text, Paragraph p, Font font) {
        addChunkToParagraph(createChunkText(text, font), p);
    }

    public void addLineSeperatorParagraph(Paragraph p) {
        addChunkToParagraph(Chunk.NEWLINE, p);
    }

    public void addNewLineToParagraph(Paragraph p) {
        p.add(createChunkNewLine());
    }

    public TGS_UnionExcuseVoid addParagraphToPage(Paragraph p) {
        try {
            document.add(p);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid addTableToPage(PdfPTable table) {
        try {
            document.add(table);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (DocumentException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public void addCellToTable(PdfPTable table, PdfPCell cell, int rotation_0_90_180_270) {
        cell.setRotation(rotation_0_90_180_270);
        table.addCell(cell);
    }

//    final private record FontBufferItem(Path path, int height, boolean bold, boolean italic, BaseColor fontColor, float fontSizeCorrectionForFontFile, Font pdfFont) {
//
//    }
//    final private static TS_ThreadSyncLst<FontBufferItem> fontBuffer = TS_ThreadSyncLst.of();
    public static TGS_UnionExcuse<Font> getFontFrom(int height, boolean bold, boolean italic, BaseColor fontColor, Path path, float fontSizeCorrectionForFontFile) {
        try {
            var style = TGS_Coronator.ofInt().coronateAs(__ -> {
                if (bold && italic) {
                    return Font.BOLDITALIC;
                }
                if (bold) {
                    return Font.BOLD;
                }
                if (italic) {
                    return Font.ITALIC;
                }
                return Font.NORMAL;
            });
//        var fontAlreadyExists = fontBuffer.stream()
//                .filter(t -> t.path.equals(path))
//                .filter(t -> t.height == height)
//                .filter(t -> t.bold == bold)
//                .filter(t -> t.italic == italic)
//                .filter(t -> t.fontColor.equals(fontColor))
//                .filter(t -> t.fontSizeCorrectionForFontFile == fontSizeCorrectionForFontFile)
//                .map(t -> t.pdfFont)
//                .findAny().orElse(null);
//        if (fontAlreadyExists != null) {
//            return fontAlreadyExists;
//        }
            if (!TS_FileUtils.isExistFile(path)) {
                d.ce("getFontFrom", "UTF8 font bold not find!", path);
                return TGS_UnionExcuse.of(getFontInternal(height, bold, italic, fontColor));
            }
            var baseFont = BaseFont.createFont(
                    path.toAbsolutePath().normalize().toString(),
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED
            );
            var newPdfFont = new Font(baseFont,
                    height * fontSizeCorrectionForFontFile,
                    style,
                    fontColor
            );
//        fontBuffer.add(new FontBufferItem(path, height, bold, italic, fontColor, fontSizeCorrectionForFontFile, newPdfFont));
            return TGS_UnionExcuse.of(newPdfFont);
        } catch (DocumentException | IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public static Font getFontInternal(int fontSize, boolean bold, boolean italic, BaseColor fontColor) {
        var fontStyle = TGS_Coronator.ofInt().coronateAs(__ -> {
            if (bold && italic) {
                return Font.BOLDITALIC;
            }
            if (bold) {
                return Font.BOLD;
            }
            if (italic) {
                return Font.ITALIC;
            }
            return Font.NORMAL;
        });
        return FontFactory.getFont(BaseFont.TIMES_ROMAN, TGS_CharSet.IBM_TURKISH(), true, fontSize, fontStyle, fontColor);
        //return FontFactory.getFont("arialuni", "Cp857", true, fontSize, (bold & italic ? Font.BOLDITALIC : (bold && !italic ? Font.BOLD : ((!bold && italic ? Font.ITALIC : Font.NORMAL)))), fontColor);
        //return FontFactory.getFont("arialuni", "Identity-H", fontSize, (bold & italic ? Font.BOLDITALIC : (bold && !italic ? Font.BOLD : ((!bold && italic ? Font.ITALIC : Font.NORMAL)))), fontColor);
        //return FontFactory.getFont(FontFactory.HELVETICA, TK_GWTCharacterSets.DEFAULT, fontSize, (bold & italic ? Font.BOLDITALIC : (bold && !italic ? Font.BOLD : ((!bold && italic ? Font.ITALIC : Font.NORMAL)))), fontColor);
    }

    public void setFont(Font font, Paragraph p) {
        p.setFont(font);
    }

    public void setFont(Font font, Chunk chunk) {
        chunk.setFont(font);
    }

    public PdfPCell createCell(Paragraph paragraph) {
        return new PdfPCell(paragraph);
    }

    public void setCellColSpan(PdfPCell cell, int colSpan) {
        cell.setColspan(colSpan);
    }

    public PdfPTable createTable(int colCount) {
        return new PdfPTable(colCount);
    }

    public PdfPTable createTable(int[] relColWidths) {
        var frelColWidths = new float[relColWidths.length];
        IntStream.range(0, relColWidths.length).parallel().forEach(i -> frelColWidths[i] = relColWidths[i]);
        return new PdfPTable(frelColWidths);
    }

    public void setCellPadding(PdfPCell cell, float padding) {
        cell.setPadding(padding);//10.0f
    }

    public void setCellBackground(PdfPCell cell, BaseColor color) {
        cell.setBackgroundColor(color);
    }

    public void setCellAlignLeft(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    }

    public void setCellAlignRight(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    }

    public void setCellAlignCenter(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    }

    public void getPageNumber() {
        document.getPageNumber();
    }

    @Override
    public void close() {
        try {
            closeFix();
        } finally {
            try {
                document.close();
            } finally {
                writer.close();
            }
        }
    }

    private void closeFix() {
        if (skipCloseFix) {
            return;
        }
        var p = createParagraph();
        addChunkToParagraph(createChunkText("."), p);
        addParagraphToPage(p);
    }
    public boolean skipCloseFix = true;

}
