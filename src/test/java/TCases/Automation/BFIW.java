package TCases.Automation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.sql.*;
import java.util.*;
import java.io.*;

import org.testng.annotations.Test;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BFIW {
	 private static final String DB_URL = "jdbc:mysql://apollo2.humanbrain.in:3306/HBA_V2";
	    private static final String DB_USER = "root";
	    private static final String DB_PASSWORD = "Health#123";

	    @Test
	    public void testBFIQuery() {
	        // Read parameters passed from Jenkins or manually
	        String biosampleId = System.getProperty("biosampleId");
	        String limitStr = System.getProperty("limit");

	        // If parameters are missing, ask for user input
	        if (biosampleId == null || biosampleId.isEmpty()) {
	            Scanner scanner = new Scanner(System.in);
	            System.out.print("Enter Biosample ID: ");
	            biosampleId = scanner.nextLine();
	        }

	        if (limitStr == null || limitStr.isEmpty()) {
	            Scanner scanner = new Scanner(System.in);
	            System.out.print("Enter Limit Value: ");
	            limitStr = scanner.nextLine();
	        }

	        // Validate limit value
	        int limit;
	        try {
	            limit = Integer.parseInt(limitStr);
	        } catch (NumberFormatException e) {
	            System.out.println("Error: Invalid limit value. Please enter a number.");
	            return;
	        }

	        System.out.println("Using Biosample ID: " + biosampleId);
	        System.out.println("Using Limit Value: " + limit);
	        
	        runQuery(biosampleId, limit);
	    }

	    private void runQuery(String biosampleId, int limit) {
	        // SQL Query
	        String queryBFIW = "SELECT DISTINCT " +
	                          "SUBSTRING( " +
	                          "s.jp2Path, " +
	                          "INSTR(s.jp2Path, 'BFIW-SE_') + LENGTH('BFIW-SE_'), " +
	                          "LOCATE('.jp2', s.jp2Path) - (INSTR(s.jp2Path, 'BFIW-SE_') + LENGTH('BFIW-SE_')) " +
	                          ") AS bfiW_section_number " +
	                          "FROM section s " +
	                          "JOIN biosample b ON s.jp2Path LIKE CONCAT('%/', b.id, '/%') " +
	                          "WHERE b.id = ? " +
	                          "AND s.jp2Path LIKE '%BFIW-SE_%' " +
	                          "ORDER BY CAST(bfiW_section_number AS UNSIGNED)";

	        Set<Integer> bfiWNumbers = new TreeSet<>();
	        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	             PreparedStatement pstmt = conn.prepareStatement(queryBFIW)) {

	            pstmt.setString(1, biosampleId);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                String numStr = rs.getString("bfiW_section_number");
	                if (numStr != null && !numStr.isEmpty()) {
	                    try {
	                        int num = Integer.parseInt(numStr);
	                        if (num <= limit) {
	                            bfiWNumbers.add(num);
	                        }
	                    } catch (NumberFormatException ignored) { }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Database Connection Failed! Check your network & DB credentials.");
	            return;
	        }

	        // Compute missing numbers
	        List<Integer> presentBFIW = new ArrayList<>(bfiWNumbers);
	        List<Integer> missingBFIW = computeMissingNumbers(presentBFIW, limit);

	        // Print Report
	        printReport(presentBFIW, missingBFIW);

	        // Save Report to File
	        saveReportToFile(presentBFIW, missingBFIW);
	    }

	    private void printReport(List<Integer> presentBFIW, List<Integer> missingBFIW) {
	        StringBuilder report = new StringBuilder();
	        report.append("\n================== BFIW Report ==================\n");
	        report.append("-------------------------------------\n");
	        report.append(String.format("| %-15s | %-15s |\n", "Present BFIW", "Missing BFIW"));
	        report.append("-------------------------------------\n");
	        int maxRows = Math.max(presentBFIW.size(), missingBFIW.size());
	        for (int i = 0; i < maxRows; i++) {
	            String present = i < presentBFIW.size() ? String.valueOf(presentBFIW.get(i)) : "";
	            String missing = i < missingBFIW.size() ? String.valueOf(missingBFIW.get(i)) : "";
	            report.append(String.format("| %-15s | %-15s |\n", present, missing));
	        }
	        report.append("-------------------------------------\n");
	        
	        System.out.println(report.toString());
	        System.out.flush();
	    }

	    private void saveReportToFile(List<Integer> presentBFIW, List<Integer> missingBFIW) {
	        StringBuilder report = new StringBuilder();
	        report.append("\n================== BFIW Report ==================\n");
	        report.append("-------------------------------------\n");
	        report.append(String.format("| %-15s | %-15s |\n", "Present BFIW", "Missing BFIW"));
	        report.append("-------------------------------------\n");
	        int maxRows = Math.max(presentBFIW.size(), missingBFIW.size());
	        for (int i = 0; i < maxRows; i++) {
	            String present = i < presentBFIW.size() ? String.valueOf(presentBFIW.get(i)) : "";
	            String missing = i < missingBFIW.size() ? String.valueOf(missingBFIW.get(i)) : "";
	            report.append(String.format("| %-15s | %-15s |\n", present, missing));
	        }
	        report.append("-------------------------------------\n");

	        try (BufferedWriter writer = new BufferedWriter(new FileWriter("BFIW_Output.txt"))) {
	            writer.write(report.toString());
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }

	    private List<Integer> computeMissingNumbers(List<Integer> presentNumbers, int limit) {
	        List<Integer> missing = new ArrayList<>();
	        for (int i = 1; i <= limit; i++) {
	            if (!presentNumbers.contains(i)) {
	                missing.add(i);
	            }
	        }
	        return missing;
	    }}