package com.tugalsan.api.file.pdf.server;

import com.tugalsan.api.file.common.server.TS_FileCommonAbstract;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.tugalsan.api.string.server.TS_StringUtils;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.pdf.server.itext.TS_FilePdfItextUtils;
import com.tugalsan.api.file.common.server.TS_FileCommonFontTags;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.stream.IntStream;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.string.client.TGS_StringDouble;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.url.client.*;
import java.util.*;

public class TS_FilePdf extends TS_FileCommonAbstract {

    final private static TS_Log d = TS_Log.of(TS_FilePdf.class);

    public TS_FilePdfItextUtils pdf;
    public PdfPTable pdfTable = null;
    public PdfPCell pdfCell = null;
    public Paragraph pdfParag = null;
    public Font pdfFont;
    public Font pdfFont_half;
    public BaseColor pdfFontColor = BaseColor.BLACK;

    private TS_FileCommonConfig fileCommonConfig;

    private TS_FilePdf(boolean enabled, Path localFile, TGS_Url remoteFile) {
        super(enabled, localFile, remoteFile);
    }

    public static void use(boolean enabled, TS_FileCommonConfig fileCommonConfig, Path localFile, TGS_Url remoteFile, TGS_RunnableType1<TS_FilePdf> pdf) {
        var instance = new TS_FilePdf(enabled, localFile, remoteFile);
        try {
            instance.use_init(fileCommonConfig);
            pdf.run(instance);
        } catch (Exception e) {
            instance.saveFile(e.getMessage());
            throw e;
        } finally {
            instance.saveFile(null);
        }
    }

    private void use_init(TS_FileCommonConfig fileCommonConfig) {
        this.fileCommonConfig = fileCommonConfig;
        if (isClosed()) {
            return;
        }
        pdf = new TS_FilePdfItextUtils(localFile);
        setFontStyle();
    }

    @Override
    public boolean saveFile(String errorSource) {
        if (isClosed()) {
            return true;
        }
        setClosed();
        d.ci("saveFile", "saveFile.PDF->");
        if (pdf == null) {
            d.ci("saveFile", "PDF File is null");
        } else {
            pdf.close();
            if (TS_FileUtils.isExistFile(localFile)) {
                d.ci("saveFile", "successfull");
            } else {
                d.ce("saveFile", "failed");
            }
        }
        return errorSource == null;
    }

    @Override
    public void skipCloseFix() {
        if (isClosed()) {
            return;
        }
        pdf.skipCloseFix = true;
    }

    @Override
    public boolean createNewPage(int pageSizeAX, boolean landscape, Integer marginLeft, Integer marginRight, Integer marginTop, Integer marginBottom) {
        if (isClosed()) {
            return true;
        }
        d.ci("createNewPage", "createNewPage.INFO: MIFPDF.createNewPage");
        pdf.createNewPage(pageSizeAX, landscape, marginLeft, marginRight, marginTop, marginBottom);
        return true;
    }

    @Override
    public boolean addImage(BufferedImage pstImage, Path pstImageLoc, boolean textWrap, int left0_center1_right2, long imageCounter) {
        if (isClosed()) {
            return true;
        }
//        d.infoEnable = true;
        d.ci("addImage", "init", pstImageLoc);
        var result = addImagePDF(pstImage, textWrap, left0_center1_right2);
        d.ci("addImage", "fin");
//        d.infoEnable = false;
        return result;
    }

    private boolean addImagePDF(Image pstImage, boolean textWrap, int left0_center1_right2) {
        if (isClosed()) {
            return true;
        }
        return TGS_UnSafe.call(() -> {
            d.ci("addImagePDF");
            if (pdfTable == null && pdfCell == null) {
                switch (left0_center1_right2) {
                    case 0 ->
                        pdf.addImageToPageLeft(pstImage, textWrap, true);
                    case 1 ->
                        pdf.addImageToPageCenter(pstImage, textWrap, true);
                    default ->
                        pdf.addImageToPageRight(pstImage, textWrap, true);
                }
            } else if (pdfTable != null && pdfCell == null) {
                d.ce("addImagePDF", "ERROR: cell not exits error ");
                d.ce("addImagePDF", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
                d.ce("addImagePDF", "compile_INSERT_IMAGE_COMMON cell not exits error ");
                return false;
            } else if (pdfTable == null && pdfCell != null) {
                d.ce("addImagePDF", "ERROR: table not exits error ");
                d.ce("addImagePDF", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
                d.ce("addImagePDF", "compile_INSERT_IMAGE_COMMON table not exits error ");
                return false;
            } else if (pdfTable != null && pdfCell != null) {
                switch (left0_center1_right2) {
                    case 0 -> //TODO FIX CELL IMG SİZE
                        pdf.addImageToCellLeft(pdfCell, pstImage, textWrap, true);
                    case 1 ->
                        pdf.addImageToCellCenter(pdfCell, pstImage, textWrap, true);
                    default ->
                        pdf.addImageToCellRight(pdfCell, pstImage, textWrap, true);
                }
            }
            d.ci("addImagePDF", "addImagePDF is ok");
            return true;
        }, e -> {
            d.ct("addImagePDF", e);
            return false;
        });
    }

    @Override
    public boolean beginTableCell(int rowSpan, int colSpan, Integer cellHeight) {
        if (isClosed()) {
            return true;
        }
        d.ci("beginTableCell");
        if (pdfTable == null) {
            d.ce("beginTableCell", "ERROR: table not exists error ");
            d.ce("beginTableCell", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        if (pdfCell != null) {
            d.ce("beginTableCell", "ERROR: cell already exists error ");
            d.ce("beginTableCell", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        pdfCell = new PdfPCell();
        pdfCell.setRowspan(rowSpan);
        pdfCell.setColspan(colSpan);
        if (cellHeight != null) {
            pdfCell.setFixedHeight(cellHeight);
        }
        pdfCell.setBorder(fileCommonConfig.enableTableCellBorder ? Rectangle.BOX : Rectangle.NO_BORDER);
        return true;
    }

    @Override
    public boolean endTableCell(int rotationInDegrees_0_90_180_270) {
        if (isClosed()) {
            return true;
        }
        return TGS_UnSafe.call(() -> {
            d.ci("endTableCell");
            if (pdfTable == null) {
                d.ce("endTableCell", "ERROR: table not exists error CODE_END_TABLECELL");
                d.ce("endTableCell", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
                return false;
            }
            if (pdfCell == null) {
                d.ce("endTableCell", "ERROR: cell not exists error CODE_END_TABLECELL");
                d.ce("endTableCell", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
                return false;
            }
            pdf.addCellToTable(pdfTable, pdfCell, rotationInDegrees_0_90_180_270);
            pdfCell = null;
            return true;
        }, e -> {
            d.ct("endTableCell", e);
            return false;
        });
    }

    @Override
    public boolean beginTable(int[] relColSizes) {
        if (isClosed()) {
            return true;
        }
        d.ci("beginTable");
        if (pdfTable != null) {
            d.ce("ERROR:CODE_BEGIN_TABLE table already exists error ");
            d.ce(TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        pdfTable = pdf.createTable(relColSizes);
        pdfTable.setWidthPercentage(100);
        return true;
    }

    @Override
    public boolean endTable() {
        if (isClosed()) {
            return true;
        }
        return TGS_UnSafe.call(() -> {
            d.ci("endTable");
            if (pdfTable == null) {
                d.ce("ERROR:CODE_END_TABLE table not exists error ");
                d.ce(TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
                return false;
            }
            pdf.addTableToPage(pdfTable);
            pdfTable = null;
            return true;
        }, e -> {
            d.ct("endTable", e);
            return false;
        });
    }

    @Override
    public boolean beginText(int allign_Left0_center1_right2_just3) {
        if (isClosed()) {
            return true;
        }
        d.ci("beginText");
        if (pdfParag != null) {
            d.ce("ERROR:CODE_BEGIN_TEXT paragraph already exits error ");
            d.ce(TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        pdfParag = pdf.createParagraph(pdfFont);
        switch (allign_Left0_center1_right2_just3) {
            case 3 ->
                pdf.setAlignLeft(pdfParag);
            case 2 ->
                pdf.setAlignRight(pdfParag);
            case 1 ->
                pdf.setAlignCenter(pdfParag);
            default ->
                pdf.setAlignLeft(pdfParag);
        }
        return true;
    }

    @Override
    public boolean endText() {
        if (isClosed()) {
            return true;
        }
        d.ci("endText");
        if (pdfParag == null) {
            d.ce("ERROR:paragraph not exits error ");
            d.ce(TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        if (pdfTable == null && pdfCell == null) {
            return TGS_UnSafe.call(() -> {
                pdf.addParagraphToPage(pdfParag);
                pdfParag = null;
                return true;
            }, e -> {
                d.ce("endText", e);
                return false;
            });
        }
        if (pdfTable != null && pdfCell == null) {
            d.ce("endText", "ERROR:cell not exits error ");
            d.ce("endText", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        if (pdfTable == null && pdfCell != null) {
            d.ce("endText", "ERROR:table not exits error ");
            d.ce(TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        if (pdfTable != null && pdfCell != null) {
            pdfCell.addElement(pdfParag);
        }
        pdfParag = null;
        return true;
    }

    @Override
    public boolean addText(String text) {
        if (isClosed()) {
            return true;
        }
        d.ci("addText", text);
        if (pdfParag == null) {
            d.ce("addText", "ERROR:paragraph not exits error ");
            d.ce("addText", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return true;
        }
        var lines = TS_StringUtils.toList(text, "\n");
        IntStream.range(0, lines.size()).forEachOrdered(i -> {
            var line = lines.get(i);
            d.ci("addText", "line", line);
            if (!line.isEmpty()) {
                if (!TGS_StringDouble.may(text)) {
                    d.ci("addText", "line", "addTextToParagraph", "mayNot", line);
                    pdf.addTextToParagraph(line, pdfParag, pdfFont);
                } else {
                    var tags = TS_StringUtils.toList_spc(line);
                    IntStream.range(0, tags.size()).forEachOrdered(j -> {
                        var tag = tags.get(j);
                        var dbl = TGS_StringDouble.of(text);
                        if (dbl.isEmpty()) {
                            pdf.addTextToParagraph(tag, pdfParag, pdfFont);
                            d.ci("addText", "line", "addTextToParagraph", "mayEmpty", line);
                        } else {
                            d.ci("addText", "line", "addTextToParagraph", "mayDbl", line);
                            pdf.addTextToParagraph(String.valueOf(dbl.get().left), pdfParag, pdfFont);
                            pdf.addTextToParagraph(String.valueOf(dbl.get().dim()) + String.valueOf(dbl.get().right), pdfParag, pdfFont_half);
                        }
                        if (tags.size() - 1 != j) {
                            pdf.addTextToParagraph(" ", pdfParag, pdfFont);
                        }
                    });
                }
            }
            if (i != lines.size() - 1 || text.endsWith("\n")) {
                addLineBreak();
            }
        });
        return true;
    }

    @Override
    public boolean addLineBreak() {
        if (isClosed()) {
            return true;
        }
        d.ci("addLineBreak");
        if (pdfParag == null) {
            d.ce("addLineBreak", "ERROR:paragraph not exits error ");
            d.ce("addLineBreak", TGS_StringUtils.toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        pdf.addLineSeperatorParagraph(pdfParag);
        return true;
    }

    @Override
    public boolean setFontStyle() {
        if (isClosed()) {
            return true;
        }
        d.ci("setFontStyle");
        var k_half = 0.8f;
        var k_file = 1f;
        pdfFont = TS_FilePdfItextUtils.getFontFrom(fileCommonConfig.fontHeight, fileCommonConfig.fontBold, fileCommonConfig.fontItalic, pdfFontColor,
                fileCommonConfig.fontPathBold, fileCommonConfig.fontPathBoldItalic, fileCommonConfig.fontPathItalic, fileCommonConfig.fontPathRegular, k_file);
        pdfFont_half = TS_FilePdfItextUtils.getFontFrom((int) (fileCommonConfig.fontHeight * k_half), fileCommonConfig.fontBold, fileCommonConfig.fontItalic, pdfFontColor,
                fileCommonConfig.fontPathBold, fileCommonConfig.fontPathBoldItalic, fileCommonConfig.fontPathItalic, fileCommonConfig.fontPathRegular, k_file);
        return true;
    }

    @Override
    public boolean setFontHeight() {
        if (isClosed()) {
            return true;
        }
        d.ci("setFontSize");
        return setFontStyle();
    }

    @Override
    public boolean setFontColor() {
        if (isClosed()) {
            return true;
        }
        d.ci("setFontColor");
        if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLACK())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_BLACK();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLUE())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_BLUE();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_CYAN())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_CYAN();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_DARK_GRAY();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GRAY())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_GRAY();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GREEN())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_GREEN();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_LIGHT_GRAY();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_MAGENTA();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_ORANGE())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_ORANGE();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_PINK())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_PINK();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_RED();
        } else if (Objects.equals(fileCommonConfig.fontColor, TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_YELLOW())) {
            pdfFontColor = TS_FilePdfItextUtils.getFONT_COLOR_YELLOW();
        } else {
            d.ce("setFontColor", "ERROR: CODE_SET_FONT_COLOR code token[1] error!");
            return false;
        }
        setFontStyle();
        return true;
    }

}
