import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.Scanner;

public class ReportService {

    @SuppressWarnings("resource")
    static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== Reports ===");
            System.out.println("-- Weekly Reports --");
            System.out.println("40. Weekly copies report");
            System.out.println("41. Weekly price report");
            System.out.println("-- Monthly Reports --");
            System.out.println("42. Monthly copies report");
            System.out.println("43. Monthly price report");
            System.out.println("-- Financial Reports --");
            System.out.println("44. Total revenue");
            System.out.println("45. Total expenses");
            System.out.println("-- Distributor Analytics --");
            System.out.println("46. Total distributor count");
            System.out.println("47. Revenue by city");
            System.out.println("48. Revenue by distributor");
            System.out.println("-- Staff Payment Analytics --");
            System.out.println("49. Staff payments by month");
            System.out.println("50. Staff payments by work type");
            System.out.println("0.  Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 40:  weeklyCopiesReport();              break;
                    case 41:  weeklyPriceReport();               break;
                    case 42:  monthlyCopiesReport();             break;
                    case 43:  monthlyPriceReport();              break;
                    case 44:  totalRevenue();                    break;
                    case 45:  totalExpenses();                   break;
                    case 46:  totalDistributorCount();          break;
                    case 47:  revenueByCity();                   break;
                    case 48:  revenueByDistributor();            break;
                    case 49:  staffPaymentsByMonth();            break;
                    case 50:  staffPaymentsByWorkType();       break;
                    case 0:   inMenu = false;                   break;
                    default:  System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ── 40. Weekly copies report ─────────────────────────────────────────────
    static void weeklyCopiesReport() throws Exception {
        final String sql = "SELECT D.name AS distributor, P.title AS publication, "
                + "YEAR(DO.required_date) AS order_year, "
                + "WEEK(DO.required_date) AS order_week, "
                + "SUM(DO.quantity) AS total_copies "
                + "FROM DIST_ORDER DO "
                + "JOIN DISTRIBUTOR D ON DO.distributor_id = D.distributor_id "
                + "LEFT JOIN BOOK_EDITION BE ON DO.edition_id = BE.edition_id "
                + "LEFT JOIN ISSUE I ON DO.issue_id = I.issue_id "
                + "LEFT JOIN PUBLICATION P ON (BE.publication_id = P.publication_id OR I.publication_id = P.publication_id) "
                + "GROUP BY D.name, P.title, YEAR(DO.required_date), WEEK(DO.required_date) "
                + "ORDER BY D.name, P.title, order_year, order_week";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-30s %-40s %-10s %-10s %-14s%n",
                    "distributor", "publication", "order_year", "order_week", "total_copies");
            System.out.println("-".repeat(110));
            boolean found = false;
            while (rs.next()) {
                found = true;
                String pub = rs.getString("publication");
                System.out.printf("%-30s %-40s %-10d %-10d %-14s%n",
                        truncate(rs.getString("distributor"), 30),
                        truncate(pub, 40),
                        rs.getInt("order_year"),
                        rs.getInt("order_week"),
                        String.valueOf(rs.getLong("total_copies")));
            }
            if (!found) {
                System.out.println("No data for weekly copies report.");
            }
        }
    }

    // ── 41. Weekly price report ────────────────────────────────────────────
    static void weeklyPriceReport() throws Exception {
        final String sql = "SELECT D.name AS distributor, P.title AS publication, "
                + "YEAR(DO.required_date) AS order_year, "
                + "WEEK(DO.required_date) AS order_week, "
                + "SUM(DO.quantity * DO.price) AS total_price "
                + "FROM DIST_ORDER DO "
                + "JOIN DISTRIBUTOR D ON DO.distributor_id = D.distributor_id "
                + "LEFT JOIN BOOK_EDITION BE ON DO.edition_id = BE.edition_id "
                + "LEFT JOIN ISSUE I ON DO.issue_id = I.issue_id "
                + "LEFT JOIN PUBLICATION P ON (BE.publication_id = P.publication_id OR I.publication_id = P.publication_id) "
                + "GROUP BY D.name, P.title, YEAR(DO.required_date), WEEK(DO.required_date) "
                + "ORDER BY D.name, P.title, order_year, order_week";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-30s %-40s %-10s %-10s %-14s%n",
                    "distributor", "publication", "order_year", "order_week", "total_price");
            System.out.println("-".repeat(110));
            boolean found = false;
            while (rs.next()) {
                found = true;
                String pub = rs.getString("publication");
                BigDecimal tp = rs.getBigDecimal("total_price");
                System.out.printf("%-30s %-40s %-10d %-10d %-14s%n",
                        truncate(rs.getString("distributor"), 30),
                        truncate(pub, 40),
                        rs.getInt("order_year"),
                        rs.getInt("order_week"),
                        tp != null ? tp.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No data for weekly price report.");
            }
        }
    }

    // ── 42. Monthly copies report ───────────────────────────────────────────
    static void monthlyCopiesReport() throws Exception {
        final String sql = "SELECT D.name AS distributor, P.title AS publication, "
                + "YEAR(DO.required_date) AS order_year, "
                + "MONTH(DO.required_date) AS order_month, "
                + "SUM(DO.quantity) AS total_copies "
                + "FROM DIST_ORDER DO "
                + "JOIN DISTRIBUTOR D ON DO.distributor_id = D.distributor_id "
                + "LEFT JOIN BOOK_EDITION BE ON DO.edition_id = BE.edition_id "
                + "LEFT JOIN ISSUE I ON DO.issue_id = I.issue_id "
                + "LEFT JOIN PUBLICATION P ON (BE.publication_id = P.publication_id OR I.publication_id = P.publication_id) "
                + "GROUP BY D.name, P.title, YEAR(DO.required_date), MONTH(DO.required_date) "
                + "ORDER BY D.name, P.title, order_year, order_month";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-30s %-40s %-10s %-11s %-14s%n",
                    "distributor", "publication", "order_year", "order_month", "total_copies");
            System.out.println("-".repeat(112));
            boolean found = false;
            while (rs.next()) {
                found = true;
                String pub = rs.getString("publication");
                System.out.printf("%-30s %-40s %-10d %-11d %-14s%n",
                        truncate(rs.getString("distributor"), 30),
                        truncate(pub, 40),
                        rs.getInt("order_year"),
                        rs.getInt("order_month"),
                        String.valueOf(rs.getLong("total_copies")));
            }
            if (!found) {
                System.out.println("No data for monthly copies report.");
            }
        }
    }

    // ── 43. Monthly price report ───────────────────────────────────────────
    static void monthlyPriceReport() throws Exception {
        final String sql = "SELECT D.name AS distributor, P.title AS publication, "
                + "YEAR(DO.required_date) AS order_year, "
                + "MONTH(DO.required_date) AS order_month, "
                + "SUM(DO.quantity * DO.price) AS total_price "
                + "FROM DIST_ORDER DO "
                + "JOIN DISTRIBUTOR D ON DO.distributor_id = D.distributor_id "
                + "LEFT JOIN BOOK_EDITION BE ON DO.edition_id = BE.edition_id "
                + "LEFT JOIN ISSUE I ON DO.issue_id = I.issue_id "
                + "LEFT JOIN PUBLICATION P ON (BE.publication_id = P.publication_id OR I.publication_id = P.publication_id) "
                + "GROUP BY D.name, P.title, YEAR(DO.required_date), MONTH(DO.required_date) "
                + "ORDER BY D.name, P.title, order_year, order_month";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-30s %-40s %-10s %-11s %-14s%n",
                    "distributor", "publication", "order_year", "order_month", "total_price");
            System.out.println("-".repeat(112));
            boolean found = false;
            while (rs.next()) {
                found = true;
                String pub = rs.getString("publication");
                BigDecimal tp = rs.getBigDecimal("total_price");
                System.out.printf("%-30s %-40s %-10d %-11d %-14s%n",
                        truncate(rs.getString("distributor"), 30),
                        truncate(pub, 40),
                        rs.getInt("order_year"),
                        rs.getInt("order_month"),
                        tp != null ? tp.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No data for monthly price report.");
            }
        }
    }

    // ── 44. Total revenue ───────────────────────────────────────────────────
    static void totalRevenue() throws Exception {
        final String sql = "SELECT COALESCE(SUM(billed_amount), 0) AS total_revenue FROM DIST_ORDER WHERE billed_amount IS NOT NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal tr = rs.getBigDecimal("total_revenue");
                System.out.println("total_revenue: " + (tr != null ? tr.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00"));
            }
        }
    }

    // ── 45. Total expenses ──────────────────────────────────────────────────
    static void totalExpenses() throws Exception {
        final String sql = "SELECT "
                + "(SELECT COALESCE(SUM(amount), 0) FROM STAFF_PAYMENT) AS total_staff_payments, "
                + "(SELECT COALESCE(SUM(shipping_cost), 0) FROM DIST_ORDER) AS total_shipping, "
                + "(SELECT COALESCE(SUM(amount), 0) FROM STAFF_PAYMENT) + "
                + "(SELECT COALESCE(SUM(shipping_cost), 0) FROM DIST_ORDER) AS total_expenses";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal staff = rs.getBigDecimal("total_staff_payments");
                BigDecimal ship = rs.getBigDecimal("total_shipping");
                BigDecimal exp = rs.getBigDecimal("total_expenses");
                System.out.println("total_staff_payments: "
                        + (staff != null ? staff.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00"));
                System.out.println("total_shipping: "
                        + (ship != null ? ship.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00"));
                System.out.println("total_expenses: "
                        + (exp != null ? exp.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00"));
            }
        }
    }

    // ── 46. Total distributor count ─────────────────────────────────────────
    static void totalDistributorCount() throws Exception {
        final String sql = "SELECT COUNT(*) AS total_distributors FROM DISTRIBUTOR";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                System.out.println("total_distributors: " + rs.getInt("total_distributors"));
            }
        }
    }

    // ── 47. Revenue by city ─────────────────────────────────────────────────
    static void revenueByCity() throws Exception {
        final String sql = "SELECT D.city, SUM(DP.amount) AS total_revenue "
                + "FROM DISTRIBUTOR D "
                + "JOIN DISTRIBUTOR_PAYMENT DP ON D.distributor_id = DP.distributor_id "
                + "GROUP BY D.city "
                + "ORDER BY total_revenue DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-30s %-18s%n", "city", "total_revenue");
            System.out.println("-".repeat(50));
            boolean found = false;
            while (rs.next()) {
                found = true;
                BigDecimal tr = rs.getBigDecimal("total_revenue");
                System.out.printf("%-30s %-18s%n",
                        truncate(rs.getString("city"), 30),
                        tr != null ? tr.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No distributor payment data.");
            }
        }
    }

    // ── 48. Revenue by distributor ────────────────────────────────────────
    static void revenueByDistributor() throws Exception {
        final String sql = "SELECT D.name, SUM(DP.amount) AS total_revenue "
                + "FROM DISTRIBUTOR D "
                + "JOIN DISTRIBUTOR_PAYMENT DP ON D.distributor_id = DP.distributor_id "
                + "GROUP BY D.distributor_id, D.name "
                + "ORDER BY total_revenue DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-40s %-18s%n", "name", "total_revenue");
            System.out.println("-".repeat(60));
            boolean found = false;
            while (rs.next()) {
                found = true;
                BigDecimal tr = rs.getBigDecimal("total_revenue");
                System.out.printf("%-40s %-18s%n",
                        truncate(rs.getString("name"), 40),
                        tr != null ? tr.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No distributor payment data.");
            }
        }
    }

    // ── 49. Staff payments by month ─────────────────────────────────────────
    static void staffPaymentsByMonth() throws Exception {
        final String sql = "SELECT DATE_FORMAT(issued_date, '%Y-%m') AS payment_month, "
                + "SUM(amount) AS total_paid "
                + "FROM STAFF_PAYMENT "
                + "GROUP BY payment_month "
                + "ORDER BY payment_month";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-12s %-18s%n", "payment_month", "total_paid");
            System.out.println("-".repeat(32));
            boolean found = false;
            while (rs.next()) {
                found = true;
                BigDecimal tp = rs.getBigDecimal("total_paid");
                System.out.printf("%-12s %-18s%n",
                        rs.getString("payment_month"),
                        tp != null ? tp.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No staff payment data.");
            }
        }
    }

    // ── 50. Staff payments by work type ─────────────────────────────────────
    static void staffPaymentsByWorkType() throws Exception {
        final String sql = "SELECT "
                + "CASE "
                + "WHEN contribution_reference LIKE '%Chapter%' THEN 'Book Authorship' "
                + "WHEN contribution_reference LIKE '%Article%' THEN 'Article Authorship' "
                + "WHEN contribution_reference LIKE '%Salary%'  THEN 'Editorial Work' "
                + "ELSE 'Other' "
                + "END AS work_type, "
                + "SUM(amount) AS total_paid "
                + "FROM STAFF_PAYMENT "
                + "GROUP BY CASE "
                + "WHEN contribution_reference LIKE '%Chapter%' THEN 'Book Authorship' "
                + "WHEN contribution_reference LIKE '%Article%' THEN 'Article Authorship' "
                + "WHEN contribution_reference LIKE '%Salary%'  THEN 'Editorial Work' "
                + "ELSE 'Other' END "
                + "ORDER BY 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-22s %-18s%n", "work_type", "total_paid");
            System.out.println("-".repeat(42));
            boolean found = false;
            while (rs.next()) {
                found = true;
                BigDecimal tp = rs.getBigDecimal("total_paid");
                System.out.printf("%-22s %-18s%n",
                        rs.getString("work_type"),
                        tp != null ? tp.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No staff payment data.");
            }
        }
    }

    static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
