package com.tesseract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

public class ImageService {

	public int uploadImage(String imageName) {
	    String sql = "INSERT INTO images (Image_Name, Upload_Date) VALUES (?, ?)";
	    try {
	    	Connection conn = dbconnection.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	        stmt.setString(1, imageName);
	        stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
	        stmt.executeUpdate();

	        ResultSet rs = stmt.getGeneratedKeys(); // Dapatkan ID yang baru saja di-generate
	        if (rs.next()) {
	            return rs.getInt(1); // Return ImageID
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Proses OCR gagal. Nama gambar terlalu panjang.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    return -1; // Jika gagal
	    }
}