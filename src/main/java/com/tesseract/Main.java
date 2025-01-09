package com.tesseract;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        ActivityLogService activityLogService = new ActivityLogService();
        ExtractionService extractionService = new ExtractionService();
        ImageService imageService = new ImageService();
        Proses ocrService = new Proses();

        // Input nama pengguna
        boolean isFilled = false;
        
        while(!isFilled) {
        	String userName = JOptionPane.showInputDialog("Masukkan nama Anda:");
        	
        	if(!userName.isEmpty()) {
        		if (!userService.isUserExists(userName)) {
                    userService.registerUser(userName);
                    JOptionPane.showMessageDialog(null, "Pengguna baru terdaftar");
                } else {
                	JOptionPane.showMessageDialog(null, "Pengguna sudah terdaftar");
                	
                }
        		int userID = userService.getUserID(userName);
            	activityLogService.logAction(userID, "User logged in");
            	
            	// Opsi menu
            	int choice = JOptionPane.showOptionDialog(null, "Pilih opsi:", "OCR App",
            			JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
            			new String[]{"Unggah Gambar", "Lihat Riwayat"}, "Unggah Gambar");
            	
            	if (choice == 0) {
            		// Unggah gambar dan proses OCR
            		JFileChooser fileChooser = new JFileChooser();
            		int result = fileChooser.showOpenDialog(null);
            		if (result == JFileChooser.APPROVE_OPTION) {
            			File imageFile = fileChooser.getSelectedFile();
            			String imageName = imageFile.getName();
            			
            			int imageID = imageService.uploadImage(imageName); // Simpan ID gambar
            			if (imageID != -1) {
            				String extractedText = ocrService.extractText(imageFile);
            				if (extractedText != null) {
            					extractionService.saveExtractionResult(userID, extractedText, imageID); // Simpan hasil OCR menggunakan ID gambar
            					activityLogService.logAction(userID, "Upload " + imageName);
            					
            					SwingUtilities.invokeLater(() -> {
            						ExtractionHistoryFrame extractionHistoryFrame = new ExtractionHistoryFrame();
            						extractionHistoryFrame.showExtractionHistory(userID);
            					});
            				} else {
            					JOptionPane.showMessageDialog(null, "Proses OCR gagal. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            				}
            			}
            			
            		}
            	} else if (choice == 1) {
            		// Tampilkan riwayat ekstraksi pengguna
            		SwingUtilities.invokeLater(() -> {
            			ExtractionHistoryFrame extractionHistoryFrame = new ExtractionHistoryFrame();
            			extractionHistoryFrame.showExtractionHistory(userID);
            		});
            	}
        		isFilled = true;
            } else {
            	JOptionPane.showMessageDialog(null, "Harus Input nama!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            	isFilled = false;
            }
        }
        

    }
}
