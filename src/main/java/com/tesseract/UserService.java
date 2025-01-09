package com.tesseract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public void registerUser(String userName) {
        String sql = "INSERT INTO user (UserName) VALUES (?)";
        try {
        	Connection conn = dbconnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.executeUpdate();
            System.out.println("Pengguna baru terdaftar.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExists(String userName) {
        String sql = "SELECT * FROM user WHERE UserName = ?";
        
        try {
        	Connection conn = dbconnection.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
            
    }

    public int getUserID(String userName) {
        String sql = "SELECT UserID FROM user WHERE UserName = ?";
        try {
        	Connection conn = dbconnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // User tidak ditemukan
    }
} 
