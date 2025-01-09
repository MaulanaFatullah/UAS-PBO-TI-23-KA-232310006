package com.tesseract;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExtractionHistoryFrame {

    public void showExtractionHistory(int userID) {
        JFrame frame = new JFrame("Riwayat Ekstraksi OCR");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        
        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Tabel riwayat ekstraksi
        String[] columnNames = {"Nama Gambar", "Teks Ekstraksi", "Waktu Ekstraksi"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Scroll pane untuk tabel
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Panel teks ekstraksi dengan scroll
        JTextArea textArea = new JTextArea(5, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false); // Set default as not editable
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Teks Ekstraksi"));

        // Tombol salin
        JButton copyButton = new JButton("Salin Teks");
        copyButton.setEnabled(false);
        
        // Tombol untuk mengedit teks
        JButton editButton = new JButton("Edit Teks");
        editButton.setEnabled(false); // Disabled until a row is selected

        // Panel untuk teks dan tombol
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(textScrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(copyButton);
        buttonPanel.add(editButton);
        textPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Listener untuk tabel
        table.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String selectedText = (String) table.getValueAt(selectedRow, 1);
                textArea.setText(selectedText);
                copyButton.setEnabled(true);
                editButton.setEnabled(true); // Enable the Edit button when a row is selected
            } else {
                textArea.setText("");
                copyButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        });

        // Listener untuk tombol salin
        copyButton.addActionListener(e -> {
            String textToCopy = textArea.getText();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textToCopy), null);
            JOptionPane.showMessageDialog(frame, "Teks berhasil disalin ke clipboard!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        // Listener untuk tombol edit
        editButton.addActionListener(e -> {
            // Toggle the editing mode of the text area
            if (textArea.isEditable()) {
                textArea.setEditable(false); // Disable editing
                editButton.setText("Edit Teks");
            } else {
                textArea.setEditable(true); // Enable editing
                editButton.setText("Selesai Mengedit");
            }
        });

        // Tambahkan komponen ke frame
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(textPanel, BorderLayout.EAST);
        frame.add(mainPanel);

        // Mengisi data tabel dari database
        loadExtractionHistory(userID, tableModel);

        frame.setVisible(true);
    }

    private void loadExtractionHistory(int userID, DefaultTableModel tableModel) {
        String sql = "SELECT images.Image_Name, extraction_results.ExtractedText, extraction_results.Extraction_Time "
                + "FROM extraction_results "
                + "LEFT JOIN images ON extraction_results.ID_Image = images.ID_Images WHERE userID = ?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String imageName = rs.getString("Image_Name");
                String extractedText = rs.getString("ExtractedText");
                String extractionTime = rs.getString("Extraction_Time");
                tableModel.addRow(new Object[]{imageName, extractedText, extractionTime});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
