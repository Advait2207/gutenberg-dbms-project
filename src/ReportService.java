import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ReportService {

    @SuppressWarnings("resource")
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\nReports Menu");
            System.out.println("1. Total revenue");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try {
                        //generateRevenueReport(null, null, null);
                        //generateRevenueReport(1, null, null);
                        //generateRevenueReport(null, "2026-02-01", "2026-02-28");
                        generateRevenueReport(1, "2026-01-01", "2026-12-31");
                    } catch (Exception e) {
                        System.out.println("Error generating report");
                    }
                    break;
                case 2:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void generateRevenueReport(Integer distributor_id, String start_date, String end_date)
            throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d.distributor_id, d.name, SUM(dp.amount) AS total_revenue ");
        sql.append("FROM DISTRIBUTOR_PAYMENT dp ");
        sql.append("JOIN DISTRIBUTOR d ON dp.distributor_id = d.distributor_id ");
        if (distributor_id != null || start_date != null || end_date != null) {
            sql.append("WHERE ");
            boolean first = true;
            if (distributor_id != null) {
                if (!first) {
                    sql.append("AND ");
                }
                sql.append("dp.distributor_id = ? ");
                first = false;
            }
            if (start_date != null) {
                if (!first) {
                    sql.append("AND ");
                }
                sql.append("dp.payment_date >= ? ");
                first = false;
            }
            if (end_date != null) {
                if (!first) {
                    sql.append("AND ");
                }
                sql.append("dp.payment_date <= ? ");
            }
        }
        sql.append(" GROUP BY d.distributor_id, d.name");

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (distributor_id != null) {
                ps.setInt(idx++, distributor_id);
            }
            if (start_date != null) {
                ps.setString(idx++, start_date);
            }
            if (end_date != null) {
                ps.setString(idx++, end_date);
            }
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("distributor_id | name | total_revenue");
                while (rs.next()) {
                    System.out.println(rs.getInt("distributor_id") + " | " + rs.getString("name") + " | "
                            + rs.getBigDecimal("total_revenue"));
                }
            }
        }
    }
}
