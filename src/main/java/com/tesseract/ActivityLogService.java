package com.tesseract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import com.tesseract.dbconnection;

public class ActivityLogService {

    public void logAction(int userID, String actionLog) {
        String sql = "INSERT INTO activitylog (userID, actionLog, actionDate) VALUES (?, ?, ?)";
        try  {
        	Connection conn = dbconnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userID);
            stmt.setString(2, actionLog);
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
