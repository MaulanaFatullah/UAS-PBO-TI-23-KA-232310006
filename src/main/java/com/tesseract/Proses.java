package com.tesseract;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Proses {
    private final Tesseract tesseract;

    public Proses() {
        tesseract = new Tesseract();
        
        // Mendapatkan direktori proyek saat runtime
        String projectPath = System.getProperty("user.dir");
        
        // Menggabungkan path relatif ke folder tessdata
        String tessDataPath = projectPath + File.separator + "Tess4J" + File.separator + "tessdata";
        
        tesseract.setDatapath(tessDataPath); // Menggunakan path relatif
    }

    public String extractText(File imageFile) {
        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }
}
