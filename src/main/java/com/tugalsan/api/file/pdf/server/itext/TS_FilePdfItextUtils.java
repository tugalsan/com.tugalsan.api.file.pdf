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
import com.tugalsan.api.font.client.TGS_FontFamily;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;
import com.tugalsan.api.tuple.client.TGS_Tuple2;
import com.tugalsan.api.tuple.client.TGS_Tuple7;
import com.tugalsan.api.unsafe.client.*;

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

    public void createNewPage(int pageSizeAX0, boolean landscape, Integer marginLeft0, Integer marginRight0, Integer marginTop0, Integer marginBottom0) {
        TGS_UnSafe.run(() -> {
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
                writer = TS_FilePdfItextPDFWriter.getInstance(document, Files.newOutputStream(file));//I KNOW
                document.open();
            } else {
                document.setPageSize(pageSize);
                document.newPage();
            }
        });
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

    public Image createImage(java.awt.Image imageFile, Color color) {
        return TGS_UnSafe.call(() -> Image.getInstance(imageFile, color));
    }

    public Image createImage(CharSequence filePath) {
        return TGS_UnSafe.call(() -> Image.getInstance(filePath.toString()));
    }

    public void addImageToPageLeft(java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        addImageToPageLeft(createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK), textWrap, transperancyAsWhite);
    }

    public void addImageToPageLeft(Image image, boolean textWrap, boolean transperancyAsWhite) {
        TGS_UnSafe.run(() -> {
            if (image == null) {
                TGS_UnSafe.thrw(d.className, "addImageToPageLeft", "image == null");
                return;
            }
            if (textWrap) {
                image.setAlignment(/*Image.ALIGN_LEFT | */Image.TEXTWRAP);//allign left is 0 already
            } else {
                image.setAlignment(Image.ALIGN_LEFT);
            }
            document.add(image);
        });
    }

    public void addImageToPageRight(java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        addImageToPageRight(createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK), textWrap, transperancyAsWhite);
    }

    public void addImageToPageRight(Image image, boolean textWrap, boolean transperancyAsWhite) {
        TGS_UnSafe.run(() -> {
            if (image == null) {
                TGS_UnSafe.thrw(d.className, "addImageToPageRight", "image == null");
                return;
            }
            if (textWrap) {
                image.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
            } else {
                image.setAlignment(Image.ALIGN_RIGHT);
            }
            document.add(image);
        });
    }

    public void addImageToPageCenter(java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        addImageToPageCenter(createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK), textWrap, transperancyAsWhite);
    }

    public void addImageToPageCenter(Image image, boolean textWrap, boolean transperancyAsWhite) {
        TGS_UnSafe.run(() -> {
            if (image == null) {
                TGS_UnSafe.thrw(d.className, "addImageToPageCenter", "image == null");
                return;
            }
            if (textWrap) {
                image.setAlignment(Image.ALIGN_CENTER | Image.TEXTWRAP);
            } else {
                image.setAlignment(Image.ALIGN_CENTER);
            }
            document.add(image);
        });
    }

    public void addImageToCellLeft(PdfPCell cell, java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            d.ce("addImageToCellLeft.ERROR: TKPDFDocument.addImageToCellLeft.imageAWT == null");
            return;
        }
        var i = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (textWrap) {
            i.setAlignment(/*Image.ALIGN_LEFT | */Image.TEXTWRAP);//allign left is 0 already
        } else {
            i.setAlignment(Image.ALIGN_LEFT);
        }
        cell.addElement(i);
    }

    public void addImageToCellRight(PdfPCell cell, java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            d.ce("addImageToCellRight.ERROR: TKPDFDocument.addImageToCellRight.imageAWT == null");
            return;
        }
        var i = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (textWrap) {
            i.setAlignment(Image.ALIGN_RIGHT | Image.TEXTWRAP);
        } else {
            i.setAlignment(Image.ALIGN_RIGHT);
        }
        cell.addElement(i);
    }

    public void addImageToCellCenter(PdfPCell cell, java.awt.Image image, boolean textWrap, boolean transperancyAsWhite) {
        if (image == null) {
            d.ce("addImageToCellCenter.ERROR: TKPDFDocument.addImageToCellCenter.imageAWT == null");
            return;
        }
        var i = createImage(image, transperancyAsWhite ? Color.WHITE : Color.BLACK);
        if (textWrap) {
            i.setAlignment(Image.ALIGN_CENTER | Image.TEXTWRAP);
        } else {
            i.setAlignment(Image.ALIGN_CENTER);
        }
        cell.addElement(i);
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

    public void addParagraphToPage(Paragraph p) {
        TGS_UnSafe.run(() -> document.add(p));
    }

    public void addTableToPage(PdfPTable table) {
        TGS_UnSafe.run(() -> document.add(table));
    }

    public void addCellToTable(PdfPTable table, PdfPCell cell, int rotation_0_90_180_270) {
        cell.setRotation(rotation_0_90_180_270);
        table.addCell(cell);
    }

    final private static TS_ThreadSyncLst<TGS_Tuple7<Integer, Boolean, Boolean, BaseColor, Path, Float, Font>> getFontFrom_buffer = TS_ThreadSyncLst.of();

    public static Font getFontFrom(int fontSize, boolean bold, boolean italic, BaseColor fontColor,
            TGS_FontFamily<Path> fontFamilyPath, float fontSizeCorrectionForFontFile) {
        TGS_Tuple7<Integer, Boolean, Boolean, BaseColor, Path, Float, Font> tuple = TGS_Tuple7.of(
                fontSize, bold, italic, fontColor, null, fontSizeCorrectionForFontFile, null
        );
        if (bold && italic) {
            var fontAlreadyExists = getFontFrom_buffer.stream()
                    .filter(t -> t.value0.equals(fontSize))
                    .filter(t -> t.value1.equals(bold))
                    .filter(t -> t.value2.equals(italic))
                    .filter(t -> t.value3.equals(fontColor))
                    .filter(t -> t.value4.equals(fontFamilyPath.boldItalic()))
                    .filter(t -> t.value5.equals(fontSizeCorrectionForFontFile))
                    .map(t -> t.value6)
                    .findAny().orElse(null);
            if (fontAlreadyExists != null) {
                return fontAlreadyExists;
            }
            if (!TS_FileUtils.isExistFile(fontFamilyPath.boldItalic())) {
                d.ce("getFontFrom", "UTF8 font bold & italic not find!", fontFamilyPath.boldItalic());
                return getFontInternal(fontSize, bold, italic, fontColor);
            }
            var fontIsRegular = fontFamilyPath.regular().equals(fontFamilyPath.boldItalic());
            var newFont = new Font(
                    TGS_UnSafe.call(() -> {
                        return BaseFont.createFont(
                                fontFamilyPath.boldItalic().toAbsolutePath().normalize().toString(),
                                BaseFont.IDENTITY_H, BaseFont.EMBEDDED
                        );
                    }),
                    fontSize * fontSizeCorrectionForFontFile, fontIsRegular ? Font.BOLDITALIC : Font.NORMAL, fontColor
            );
            tuple.value4 = fontFamilyPath.boldItalic();
            tuple.value6 = newFont;
            getFontFrom_buffer.add(tuple);
            return newFont;
        }
        if (bold) {
            var fontAlreadyExists = getFontFrom_buffer.stream()
                    .filter(t -> t.value0.equals(fontSize))
                    .filter(t -> t.value1.equals(bold))
                    .filter(t -> t.value2.equals(italic))
                    .filter(t -> t.value3.equals(fontColor))
                    .filter(t -> t.value4.equals(fontFamilyPath.bold()))
                    .filter(t -> t.value5.equals(fontSizeCorrectionForFontFile))
                    .map(t -> t.value6)
                    .findAny().orElse(null);
            if (fontAlreadyExists != null) {
                return fontAlreadyExists;
            }
            if (!TS_FileUtils.isExistFile(fontFamilyPath.bold())) {
                d.ce("getFontFrom", "UTF8 font bold not find!", fontFamilyPath.bold());
                return getFontInternal(fontSize, bold, italic, fontColor);
            }
            var fontIsRegular = fontFamilyPath.regular().equals(fontFamilyPath.boldItalic());
            var newFont = new Font(
                    TGS_UnSafe.call(() -> {
                        return BaseFont.createFont(
                                fontFamilyPath.bold().toAbsolutePath().normalize().toString(),
                                BaseFont.IDENTITY_H, BaseFont.EMBEDDED
                        );
                    }),
                    fontSize * fontSizeCorrectionForFontFile, fontIsRegular ? Font.BOLD : Font.NORMAL, fontColor
            );
            tuple.value4 = fontFamilyPath.bold();
            tuple.value6 = newFont;
            getFontFrom_buffer.add(tuple);
            return newFont;
        }
        if (italic) {
            var fontAlreadyExists = getFontFrom_buffer.stream()
                    .filter(t -> t.value0.equals(fontSize))
                    .filter(t -> t.value1.equals(bold))
                    .filter(t -> t.value2.equals(italic))
                    .filter(t -> t.value3.equals(fontColor))
                    .filter(t -> t.value4.equals(fontFamilyPath.italic()))
                    .filter(t -> t.value5.equals(fontSizeCorrectionForFontFile))
                    .map(t -> t.value6)
                    .findAny().orElse(null);
            if (fontAlreadyExists != null) {
                return fontAlreadyExists;
            }
            if (!TS_FileUtils.isExistFile(fontFamilyPath.italic())) {
                d.ce("getFontFrom", "UTF8 font italic not find!", fontFamilyPath.italic());
                return getFontInternal(fontSize, bold, italic, fontColor);
            }
            var fontIsRegular = fontFamilyPath.regular().equals(fontFamilyPath.boldItalic());
            var newFont = new Font(
                    TGS_UnSafe.call(() -> {
                        return BaseFont.createFont(
                                fontFamilyPath.italic().toAbsolutePath().normalize().toString(),
                                BaseFont.IDENTITY_H, BaseFont.EMBEDDED
                        );
                    }),
                    fontSize * fontSizeCorrectionForFontFile, fontIsRegular ? Font.ITALIC : Font.NORMAL, fontColor
            );
            tuple.value4 = fontFamilyPath.italic();
            tuple.value6 = newFont;
            getFontFrom_buffer.add(tuple);
            return newFont;
        }
        var fontAlreadyExists = getFontFrom_buffer.stream()
                .filter(t -> t.value0.equals(fontSize))
                .filter(t -> t.value1.equals(bold))
                .filter(t -> t.value2.equals(italic))
                .filter(t -> t.value3.equals(fontColor))
                .filter(t -> t.value4.equals(fontFamilyPath.regular()))
                .filter(t -> t.value5.equals(fontSizeCorrectionForFontFile))
                .map(t -> t.value6)
                .findAny().orElse(null);
        if (fontAlreadyExists != null) {
            return fontAlreadyExists;
        }
        if (!TS_FileUtils.isExistFile(fontFamilyPath.regular())) {
            d.ce("getFontFrom", "UTF8 font regular not find!", fontFamilyPath.regular());
            return getFontInternal(fontSize, bold, italic, fontColor);
        }
        var newFont = new Font(
                TGS_UnSafe.call(() -> {
                    return BaseFont.createFont(
                            fontFamilyPath.regular().toAbsolutePath().normalize().toString(),
                            BaseFont.IDENTITY_H, BaseFont.EMBEDDED
                    );
                }),
                fontSize * fontSizeCorrectionForFontFile, Font.NORMAL, fontColor
        );
        tuple.value4 = fontFamilyPath.regular();
        tuple.value6 = newFont;
        getFontFrom_buffer.add(tuple);
        return newFont;
    }

    public static Font getFontInternal(int fontSize, boolean bold, boolean italic, BaseColor fontColor) {
        var fontStyle = (bold & italic ? Font.BOLDITALIC : (bold && !italic ? Font.BOLD : ((!bold && italic ? Font.ITALIC : Font.NORMAL))));
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
        TGS_UnSafe.run(() -> closeFix(), e -> d.ct("close.closeFix", e));
        TGS_UnSafe.run(() -> document.close(), e -> d.ct("close.document", e));
        TGS_UnSafe.run(() -> writer.close(), e -> d.ct("close.writer", e));
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
