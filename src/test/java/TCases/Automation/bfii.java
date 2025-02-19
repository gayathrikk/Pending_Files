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

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class bfii {
    private static final String DB_URL = "jdbc:mysql://dev2mani.humanbrain.in:3306/HBA_V2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Health#123";

    @Parameters({"biosampleId"})
    @Test
    public void testBFIQuery(@Optional String biosampleId) {
        if (biosampleId == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter biosample ID: ");
            biosampleId = scanner.nextLine();
        }

        // SQL query for BFI
        String query = "SELECT DISTINCT CAST(SUBSTRING_INDEX(s.jp2Path, '_', -1) AS UNSIGNED) AS number " +
                       "FROM section s " +
                       "WHERE s.jp2Path LIKE '%BFI%' " +
                       "AND EXISTS (SELECT 1 FROM biosample b WHERE b.id = ?) " +
                       "ORDER BY number";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, biosampleId);
            ResultSet rs = pstmt.executeQuery();

            // Use a TreeSet to store and sort the numbers
            Set<Integer> bfiNumbers = new TreeSet<>();
            while (rs.next()) {
                int number = rs.getInt("number");
                bfiNumbers.add(number);
            }

            // Convert to a list for easier handling
            List<Integer> bfiList = new ArrayList<>(bfiNumbers);

            // Compute missing numbers from the smallest BFI to 3000
            List<Integer> missingNumbers = new ArrayList<>();
            if (!bfiNumbers.isEmpty()) {
                int min = bfiList.get(0); // smallest BFI number
                for (int i = min; i <= 3000; i++) {
                    if (!bfiNumbers.contains(i)) {
                        missingNumbers.add(i);
                    }
                }
            }

            // Print results in a table with two columns: BFI List and Missing Numbers
            System.out.println("\n-----------------------------------------");
            System.out.printf("| %-13s | %-15s |%n", "BFI List", "Missing Numbers");
            System.out.println("-----------------------------------------");

            int maxRows = Math.max(bfiList.size(), missingNumbers.size());
            for (int i = 0; i < maxRows; i++) {
                String bfiValue = i < bfiList.size() ? String.valueOf(bfiList.get(i)) : "";
                String missingValue = i < missingNumbers.size() ? String.valueOf(missingNumbers.get(i)) : "";
                System.out.printf("| %-13s | %-15s |%n", bfiValue, missingValue);
            }
            System.out.println("-----------------------------------------");

            if (bfiList.isEmpty()) {
                System.out.println("No BFI records found for biosample ID: " + biosampleId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
