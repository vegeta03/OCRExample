package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OCRExample {
    public static void main(String[] args) {
        // Path to the folder containing image files
        String folderPath = "src/main/resources/ocr";

        // Use absolute path relative to the project root
        Path folderAbsolutePath = Paths.get(System.getProperty("user.dir"), folderPath);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");

        // Modern error handling and file processing
        try (var filesStream = Files.walk(Path.of(folderPath))) {
            List<File> files = filesStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toList();

            files.forEach(file -> performOCR(tesseract, file));
        } catch (IOException e) {
            System.err.println("Error walking the file tree: " + e.getMessage());
        }
    }

    private static void performOCR(Tesseract tesseract, File file) {
        try {
            String result = tesseract.doOCR(file);
            System.out.printf("OCR Result for %s: %s%n", file.getName(), result);
        } catch (TesseractException e) {
            System.err.printf("Error during OCR for %s: %s%n", file.getName(), e.getMessage());
        }
    }
}
