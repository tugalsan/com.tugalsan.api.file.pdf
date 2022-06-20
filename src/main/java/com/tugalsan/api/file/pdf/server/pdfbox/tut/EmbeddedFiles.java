package com.tugalsan.api.file.pdf.server.pdfbox.tut;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.Map.*;
import org.apache.pdfbox.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.common.filespecification.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;
import com.tugalsan.api.list.client.*;

public class EmbeddedFiles {

    public static void EmbeddedFiles(String file) throws IOException {
        try ( PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDFont font = PDType1Font.HELVETICA_BOLD;

            try ( PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Go to Document->File Attachments to View Embedded Files");
                contentStream.endText();
            }

            //embedded files are stored in a named tree
            PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();

            //first create the file specification, which holds the embedded file
            PDComplexFileSpecification fs = new PDComplexFileSpecification();

            // use both methods for backwards, cross-platform and cross-language compatibility.
            fs.setFile("Test.txt");
            fs.setFileUnicode("Test.txt");

            //create a dummy file stream, this would probably normally be a FileInputStream
            byte[] data = "This is the contents of the embedded file".getBytes(StandardCharsets.ISO_8859_1);
            ByteArrayInputStream fakeFile = new ByteArrayInputStream(data);
            PDEmbeddedFile ef = new PDEmbeddedFile(doc, fakeFile);
            //now lets some of the optional parameters
            ef.setSubtype("text/plain");
            ef.setSize(data.length);
            ef.setCreationDate(new GregorianCalendar());

            // use both methods for backwards, cross-platform and cross-language compatibility.
            fs.setEmbeddedFile(ef);
            fs.setEmbeddedFileUnicode(ef);

            // create a new tree node and add the embedded file
            PDEmbeddedFilesNameTreeNode treeNode = new PDEmbeddedFilesNameTreeNode();
            treeNode.setNames(Collections.singletonMap("My first attachment", fs));
            // add the new node as kid to the root node
            List<PDEmbeddedFilesNameTreeNode> kids = TGS_ListUtils.of();
            kids.add(treeNode);
            efTree.setKids(kids);
            // add the tree to the document catalog
            PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
            names.setEmbeddedFiles(efTree);
            doc.getDocumentCatalog().setNames(names);

            // show attachments panel in some viewers 
            doc.getDocumentCatalog().setPageMode(PageMode.USE_ATTACHMENTS);

            doc.save(file);
        }
    }

    public static void ExtractEmbeddedFiles(String file) throws IOException {
        File pdfFile = new File(file);
        String filePath = pdfFile.getParent() + System.getProperty("file.separator");
        try ( PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDDocumentNameDictionary namesDictionary
                    = new PDDocumentNameDictionary(document.getDocumentCatalog());
            PDEmbeddedFilesNameTreeNode efTree = namesDictionary.getEmbeddedFiles();
            if (efTree != null) {
                extractFilesFromEFTree(efTree, filePath);
            }

            // extract files from page annotations
            for (PDPage page : document.getPages()) {
                extractFilesFromPage(page, filePath);
            }
        }
    }

    private static void extractFilesFromPage(PDPage page, String filePath) throws IOException {
        for (PDAnnotation annotation : page.getAnnotations()) {
            if (annotation instanceof PDAnnotationFileAttachment) {
                PDAnnotationFileAttachment annotationFileAttachment = (PDAnnotationFileAttachment) annotation;
                PDFileSpecification fileSpec = annotationFileAttachment.getFile();
                if (fileSpec instanceof PDComplexFileSpecification) {
                    PDComplexFileSpecification complexFileSpec = (PDComplexFileSpecification) fileSpec;
                    PDEmbeddedFile embeddedFile = getEmbeddedFile(complexFileSpec);
                    if (embeddedFile != null) {
                        extractFile(filePath, complexFileSpec.getFilename(), embeddedFile);
                    }
                }
            }
        }
    }

    private static void extractFilesFromEFTree(PDEmbeddedFilesNameTreeNode efTree, String filePath) throws IOException {
        Map<String, PDComplexFileSpecification> names = efTree.getNames();
        if (names != null) {
            extractFiles(names, filePath);
        } else {
            List<PDNameTreeNode<PDComplexFileSpecification>> kids = efTree.getKids();
            for (PDNameTreeNode<PDComplexFileSpecification> node : kids) {
                names = node.getNames();
                extractFiles(names, filePath);
            }
        }
    }

    private static void extractFiles(Map<String, PDComplexFileSpecification> names, String filePath)
            throws IOException {
        for (Entry<String, PDComplexFileSpecification> entry : names.entrySet()) {
            PDComplexFileSpecification fileSpec = entry.getValue();
            PDEmbeddedFile embeddedFile = getEmbeddedFile(fileSpec);
            if (embeddedFile != null) {
                extractFile(filePath, fileSpec.getFilename(), embeddedFile);
            }
        }
    }

    private static void extractFile(String filePath, String filename, PDEmbeddedFile embeddedFile)
            throws IOException {
        String embeddedFilename = filePath + filename;
        File file = new File(filePath + filename);
        System.out.println("Writing " + embeddedFilename);
        try ( var fos = new FileOutputStream(file)) {
            fos.write(embeddedFile.toByteArray());
        }
    }

    private static PDEmbeddedFile getEmbeddedFile(PDComplexFileSpecification fileSpec) {
        // search for the first available alternative of the embedded file
        PDEmbeddedFile embeddedFile = null;
        if (fileSpec != null) {
            embeddedFile = fileSpec.getEmbeddedFileUnicode();
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFileDos();
            }
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFileMac();
            }
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFileUnix();
            }
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFile();
            }
        }
        return embeddedFile;
    }
}
