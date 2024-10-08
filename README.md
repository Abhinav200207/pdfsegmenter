# PDF Segmentation Tool

This project provides a tool for analyzing and segmenting a PDF file based on whitespace positions using Apache PDFBox. The tool identifies significant gaps in the content and splits the PDF into multiple segments accordingly.

## Features

- Analyze PDF whitespace and text positions.
- Segment PDF into multiple files based on identified whitespace.
- Automatically name and store the segmented PDF files.
- Handle exceptions such as invalid input and file not found.

## Prerequisites

- **Java 8** or above
- **Maven** (for dependency management and building the project)
- **Apache PDFBox** library
- **Intellij Idea** Editor

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/Abhinav200207/pdfsegmenter
    cd pdfsegmenter
    ```

2. Build the project using Maven:
    Open the file in Intellij Idea Editor

3. Place your input PDF file in the `src/main/java/org/example/` directory.

## Running the Tool

To run the tool and segment your PDF:

1. Navigate to the run button and click on it:

2. The segmented PDF files will be saved in the project root directory as `segment_1.pdf`, `segment_2.pdf`, etc.
