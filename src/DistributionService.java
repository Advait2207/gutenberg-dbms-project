import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

public class DistributionService {

    @SuppressWarnings("resource")
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\nDistribution Menu");
            System.out.println("1. Show outstanding balance");
            System.out.println("2. Create order");
            System.out.println("3. Allocate payment");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try {
                        getOutstandingBalance();
                    } catch (Exception e) {
                        System.out.println("Feature not implemented yet");
                    }
                    break;
                case 2:
                    try {
                        createOrder(8, 1, 1, null, 2, 10.00, 5.00, "2026-12-01");
                    } catch (Exception e) {
                        System.out.println("Feature not implemented yet");
                    }
                    break;
                case 3:
                    allocatePayment(8, 20.00);
                    break;
                case 4:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void getOutstandingBalance() throws Exception {
        
        
        final String sql = "SELECT\n"
                + "d.distributor_id,\n"
                + "d.name,\n"
                + "COALESCE(o.total_billed, 0) - COALESCE(p.total_paid, 0) AS outstanding_balance\n"
                + "FROM DISTRIBUTOR d\n"
                + "LEFT JOIN (\n"
                + "SELECT distributor_id, SUM(billed_amount) AS total_billed\n"
                + "FROM DIST_ORDER\n"
                + "GROUP BY distributor_id\n"
                + ") o ON d.distributor_id = o.distributor_id\n"
                + "LEFT JOIN (\n"
                + "SELECT o.distributor_id, SUM(pa.allocated_amount) AS total_paid\n"
                + "FROM PAYMENT_ALLOCATION pa\n"
                + "JOIN DIST_ORDER o ON pa.order_id = o.order_id\n"
                + "GROUP BY o.distributor_id\n"
                + ") p ON d.distributor_id = p.distributor_id;";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("distributor_id | name | outstanding_balance");
            while (rs.next()) {
                System.out.println(rs.getInt("distributor_id") + " | " + rs.getString("name") + " | "
                        + rs.getBigDecimal("outstanding_balance"));
            }
        }
    }

    public static void createOrder(int order_id, int distributor_id, Integer edition_id, Integer issue_id, int quantity,
            double price, double shipping_cost, String required_date) throws Exception {
        if (edition_id == null && issue_id == null) {
            throw new Exception("Either edition_id or issue_id must be provided");
        }

        if (edition_id != null && issue_id != null) {
            throw new Exception("Only one of edition_id or issue_id should be provided");
        }

        final String sql = "INSERT INTO DIST_ORDER (order_id, distributor_id, edition_id, issue_id, quantity, price, "
                + "shipping_cost, required_date, billed_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        double billed_amount = (quantity * price) + shipping_cost;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order_id);
            ps.setInt(2, distributor_id);
            if (edition_id == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, edition_id);
            }
            if (issue_id == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, issue_id);
            }
            ps.setInt(5, quantity);
            ps.setDouble(6, price);
            ps.setDouble(7, shipping_cost);
            ps.setString(8, required_date);
            ps.setDouble(9, billed_amount);
            ps.executeUpdate();
            System.out.println("Order inserted successfully");
        }
    }
    
    public static void allocatePayment(int order_id, double amount) {

        if (amount <= 0) {
            System.out.println("Invalid payment amount");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COALESCE(MAX(payment_id), 0) + 1 FROM DISTRIBUTOR_PAYMENT");
            if (!rs.next()) {
                throw new Exception("Could not generate payment_id");
            }
            int payment_id = rs.getInt(1);
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;

            ps = conn.prepareStatement("SELECT distributor_id FROM DIST_ORDER WHERE order_id = ?");
            ps.setInt(1, order_id);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new Exception("Order not found");
            }
            int distributor_id = rs.getInt("distributor_id");
            rs.close();
            rs = null;
            ps.close();
            ps = null;

            ps = conn.prepareStatement(
                    "INSERT INTO DISTRIBUTOR_PAYMENT (payment_id, distributor_id, amount, payment_date) VALUES (?, ?, ?, CURRENT_DATE)");
            ps.setInt(1, payment_id);
            ps.setInt(2, distributor_id);
            ps.setDouble(3, amount);
            ps.executeUpdate();
            ps.close();
            ps = null;

            ps = conn.prepareStatement(
                    "INSERT INTO PAYMENT_ALLOCATION (payment_id, order_id, allocated_amount) VALUES (?, ?, ?)");
            ps.setInt(1, payment_id);
            ps.setInt(2, order_id);
            ps.setDouble(3, amount);
            ps.executeUpdate();

            conn.commit();
            System.out.println("Payment allocated successfully");

        } catch (Exception e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackException) {
                    // keep flow simple
                }
            }

            System.out.println("Error allocating payment");

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
            }

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
