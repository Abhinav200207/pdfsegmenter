package org.example;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PDFWhiteSpaceAnalyzer extends PDFTextStripper {

    private final List<Float> yPositions = new ArrayList<>();
    private final List<List<TextPosition>> paragraphs = new ArrayList<>();
    private List<TextPosition> currentParagraph = new ArrayList<>();

    public PDFWhiteSpaceAnalyzer() throws IOException {
        super();
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) {
        for (TextPosition position : textPositions) {
            float y = position.getY();
            yPositions.add(y);

            if (!currentParagraph.isEmpty() && Math.abs(currentParagraph.getLast().getY() - y) > 20) {  // 20 is a sample threshold
                paragraphs.add(currentParagraph);
                currentParagraph = new ArrayList<>();
            }
            currentParagraph.add(position);
        }

        if (!currentParagraph.isEmpty()) {
            paragraphs.add(currentParagraph);
        }
    }

    public List<List<TextPosition>> getParagraphs() {
        return paragraphs;
    }

    public List<Float> getSignificantWhitespaces(int numberOfCuts) {
        Collections.sort(yPositions);
        List<Float> whitespaceGaps = new ArrayList<>();

        for (int i = 1; i < yPositions.size(); i++) {
            float gap = yPositions.get(i) - yPositions.get(i - 1);
            whitespaceGaps.add(gap);
        }

        whitespaceGaps.sort(Collections.reverseOrder());
        return whitespaceGaps.subList(0, Math.min(numberOfCuts, whitespaceGaps.size()));
    }
}
