package com.tesseract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.tesseract.dbconnection;

public class ExtractionService {

	public void saveExtractionResult(int userID, String extractedText, int imageID) {
	    String sql = "INSERT INTO extraction_results (ID_Image, ExtractedText, Extraction_Time, userID) VALUES (?, ?, NOW(), ?)";
	    try (Connection conn = dbconnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, imageID); // Simpan ID gambar
	        stmt.setString(2, extractedText);
	        stmt.setInt(3, userID);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


    // Menambahkan metode untuk mengambil riwayat hasil ekstraksi
    public void fetchExtractionHistory(int userID) {
        String sql = "SELECT ID_Image, ExtractedText, Extraction_Time FROM extraction_results WHERE userID = ?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Tidak ada riwayat ekstraksi untuk pengguna ini.");
                return;
            }

            System.out.println("Riwayat Ekstraksi:");
            do {
                String imageName = rs.getString("ID_Image");
                String extractedText = rs.getString("ExtractedText");
                String extractionTime = rs.getString("Extraction_Time");
                System.out.println("Gambar: " + imageName + ", Teks: " + extractedText + ", Waktu: " + extractionTime);
            } while (rs.next());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
