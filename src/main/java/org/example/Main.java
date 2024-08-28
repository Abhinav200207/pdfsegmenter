package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        String filePath = path + "\\src\\main\\java\\org\\example\\input.pdf";
        int numberOfCuts = 3;

        try {
            PDDocument document = PDDocument.load(new File(filePath));
            analyzeAndSegmentPDF(document, numberOfCuts);
            document.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void analyzeAndSegmentPDF(PDDocument document, int numberOfCuts) {
        try {
            PDFWhiteSpaceAnalyzer analyzer = new PDFWhiteSpaceAnalyzer();
            analyzer.setSortByPosition(true);
            analyzer.getText(document);

            List<Float> significantWhitespaces = analyzer.getSignificantWhitespaces(numberOfCuts);
            System.out.println("Significant whitespace positions (Y-coordinates): " + significantWhitespaces);

            segmentPDF(analyzer.getParagraphs(), significantWhitespaces);

        } catch (IOException e) {
            System.err.println("Error analyzing PDF: " + e.getMessage());
        }
    }

    public static void segmentPDF(List<List<TextPosition>> paragraphs, List<Float> cutPositions) {
        try {
            int segmentCount = 1;
            PDDocument segmentDocument = new PDDocument();
            PDPage segmentPage = new PDPage();
            segmentDocument.addPage(segmentPage);
            PDPageContentStream contentStream = new PDPageContentStream(segmentDocument, segmentPage);

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);

            float currentCutPosition = cutPositions.isEmpty() ? Float.MAX_VALUE : cutPositions.getFirst();
            int cutIndex = 0;

            for (List<TextPosition> paragraph : paragraphs) {
                float paragraphY = paragraph.getFirst().getY();

                if (paragraphY > currentCutPosition) {
                    contentStream.endText();
                    contentStream.close();

                    String outputFilePath = "segment_" + segmentCount + ".pdf";
                    segmentDocument.save(outputFilePath);
                    System.out.println("Segment " + segmentCount + " saved as " + outputFilePath);
                    segmentDocument.close();
                    segmentCount++;

                    segmentDocument = new PDDocument();
                    segmentPage = new PDPage();
                    segmentDocument.addPage(segmentPage);
                    contentStream = new PDPageContentStream(segmentDocument, segmentPage);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 700);

                    cutIndex++;
                    if (cutIndex < cutPositions.size()) {
                        currentCutPosition = cutPositions.get(cutIndex);
                    } else {
                        currentCutPosition = Float.MAX_VALUE;
                    }
                }

                for (TextPosition textPosition : paragraph) {
                    contentStream.showText(textPosition.getUnicode());
                }
                contentStream.newLineAtOffset(0, -20);
            }

            contentStream.endText();
            contentStream.close();
            String outputFilePath = "segment_" + segmentCount + ".pdf";
            segmentDocument.save(outputFilePath);
            System.out.println("Segment " + segmentCount + " saved as " + outputFilePath);
            segmentDocument.close();

        } catch (IOException e) {
            System.err.println("Error segmenting PDF: " + e.getMessage());
        }
    }
}