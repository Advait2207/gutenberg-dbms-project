import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.Scanner;

public class DistributionService {

    @SuppressWarnings("resource")
    static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== Distribution ===");
            System.out.println("-- Distributor Management --");
            System.out.println("30. Enter new distributor");
            System.out.println("31. Update distributor");
            System.out.println("32. Delete distributor");
            System.out.println("-- Orders and Billing --");
            System.out.println("33. Create order");
            System.out.println("34. Bill distributor (order)");
            System.out.println("-- Payments --");
            System.out.println("35. Record distributor payment");
            System.out.println("36. Allocate payment to order");
            System.out.println("-- Financial Integrity --");
            System.out.println("37. View outstanding balances");
            System.out.println("38. Identify billing mismatch");
            System.out.println("-- Filtering --");
            System.out.println("39. List distributors by type and city");
            System.out.println("0.  Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 30:  enterNewDistributor();           break;
                    case 31:  updateDistributor();              break;
                    case 32:  deleteDistributor();              break;
                    case 33:  createOrder();                   break;
                    case 34:  billDistributor();               break;
                    case 35:  recordDistributorPayment();      break;
                    case 36:  allocatePaymentToOrder();        break;
                    case 37:  viewOutstandingBalances();       break;
                    case 38:  identifyBillingMismatch();       break;
                    case 39:  listDistributorsByTypeAndCity();   break;
                    case 0:   inMenu = false;                  break;
                    default:  System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ── 30. Enter new distributor ────────────────────────────────────────────
    static void enterNewDistributor() throws Exception {
        System.out.print("Distributor ID: ");
        int distributorId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Category (Bookstore/Wholesale/Library): ");
        String category = scanner.nextLine().trim();
        System.out.print("Street: ");
        String street = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("State: ");
        String state = scanner.nextLine();
        System.out.print("ZIP: ");
        String zip = scanner.nextLine();
        System.out.print("Country: ");
        String country = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Contact person: ");
        String contactPerson = scanner.nextLine();

        String sql = "INSERT INTO DISTRIBUTOR (distributor_id, name, category, street, city, state, zip, country, phone, contact_person) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, distributorId);
            ps.setString(2, name);
            ps.setString(3, category);
            ps.setString(4, street);
            ps.setString(5, city);
            ps.setString(6, state);
            ps.setString(7, zip);
            ps.setString(8, country);
            ps.setString(9, phone);
            ps.setString(10, contactPerson);
            ps.executeUpdate();
            System.out.println("Distributor added successfully.");
        }
    }

    // ── 31. Update distributor ─────────────────────────────────────────────
    static void updateDistributor() throws Exception {
        System.out.print("Distributor ID: ");
        int distributorId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter new values (press Enter to skip each field).");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Category (Bookstore/Wholesale/Library): ");
        String category = scanner.nextLine();
        System.out.print("Street: ");
        String street = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("State: ");
        String state = scanner.nextLine();
        System.out.print("ZIP: ");
        String zip = scanner.nextLine();
        System.out.print("Country: ");
        String country = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Contact person: ");
        String contactPerson = scanner.nextLine();

        StringBuilder sql = new StringBuilder("UPDATE DISTRIBUTOR SET distributor_id=distributor_id");
        if (!name.trim().isEmpty())           sql.append(", name=?");
        if (!category.trim().isEmpty())       sql.append(", category=?");
        if (!street.trim().isEmpty())         sql.append(", street=?");
        if (!city.trim().isEmpty())           sql.append(", city=?");
        if (!state.trim().isEmpty())          sql.append(", state=?");
        if (!zip.trim().isEmpty())            sql.append(", zip=?");
        if (!country.trim().isEmpty())        sql.append(", country=?");
        if (!phone.trim().isEmpty())          sql.append(", phone=?");
        if (!contactPerson.trim().isEmpty())  sql.append(", contact_person=?");
        sql.append(" WHERE distributor_id=?");

        if (sql.toString().equals("UPDATE DISTRIBUTOR SET distributor_id=distributor_id WHERE distributor_id=?")) {
            System.out.println("No fields to update.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!name.trim().isEmpty())           ps.setString(idx++, name.trim());
            if (!category.trim().isEmpty())       ps.setString(idx++, category.trim());
            if (!street.trim().isEmpty())         ps.setString(idx++, street.trim());
            if (!city.trim().isEmpty())           ps.setString(idx++, city.trim());
            if (!state.trim().isEmpty())          ps.setString(idx++, state.trim());
            if (!zip.trim().isEmpty())            ps.setString(idx++, zip.trim());
            if (!country.trim().isEmpty())        ps.setString(idx++, country.trim());
            if (!phone.trim().isEmpty())          ps.setString(idx++, phone.trim());
            if (!contactPerson.trim().isEmpty())  ps.setString(idx++, contactPerson.trim());
            ps.setInt(idx, distributorId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Distributor updated successfully." : "No distributor found with that ID.");
        }
    }

    // ── 32. Delete distributor ───────────────────────────────────────────────
    static void deleteDistributor() throws Exception {
        System.out.print("Distributor ID: ");
        int distributorId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String countSql = "SELECT COUNT(*) FROM DIST_ORDER WHERE distributor_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                ps.setInt(1, distributorId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Could not verify orders for this distributor.");
                        return;
                    }
                    int n = rs.getInt(1);
                    if (n > 0) {
                        System.out.println("Cannot delete: distributor has existing orders");
                        return;
                    }
                }
            }
            String delSql = "DELETE FROM DISTRIBUTOR WHERE distributor_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(delSql)) {
                ps.setInt(1, distributorId);
                int rows = ps.executeUpdate();
                System.out.println(rows > 0 ? "Distributor deleted successfully." : "No distributor found with that ID.");
            }
        }
    }

    // ── 33. Create order ─────────────────────────────────────────────────────
    static void createOrder() throws Exception {
        System.out.print("Order ID: ");
        int orderId = scanner.nextInt();
        System.out.print("Distributor ID: ");
        int distributorId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Order for (1) Book Edition or (2) Issue? ");
        int kind = scanner.nextInt();
        scanner.nextLine();

        Integer editionId = null;
        Integer issueId = null;
        if (kind == 1) {
            System.out.print("Edition ID: ");
            editionId = scanner.nextInt();
            scanner.nextLine();
        } else if (kind == 2) {
            System.out.print("Issue ID: ");
            issueId = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid choice: must be 1 or 2.");
            return;
        }

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Price: ");
        BigDecimal price = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Shipping cost: ");
        BigDecimal shippingCost = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Required date (YYYY-MM-DD): ");
        String requiredDate = scanner.nextLine().trim();

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }
        if (price.compareTo(BigDecimal.ZERO) < 0 || shippingCost.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Price and shipping cost must be zero or greater.");
            return;
        }

        String sql = "INSERT INTO DIST_ORDER (order_id, distributor_id, edition_id, issue_id, quantity, price, shipping_cost, required_date, billed_amount) VALUES (?,?,?,?,?,?,?,?,NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, distributorId);
            if (editionId == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, editionId);
            }
            if (issueId == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, issueId);
            }
            ps.setInt(5, quantity);
            ps.setBigDecimal(6, price);
            ps.setBigDecimal(7, shippingCost);
            ps.setDate(8, java.sql.Date.valueOf(requiredDate));
            ps.executeUpdate();
            System.out.println("Order created successfully.");
        }
    }

    // ── 34. Bill distributor (transaction) ─────────────────────────────────
    static void billDistributor() {
        Connection conn = null;
        try {
            System.out.print("Order ID: ");
            int orderId = scanner.nextInt();
            System.out.print("Payment ID: ");
            int paymentId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Payment date (YYYY-MM-DD): ");
            String paymentDate = scanner.nextLine().trim();

            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // TRANSACTION START
            BigDecimal billed;
            int distributorId;

            String sel = "SELECT quantity, price, shipping_cost, distributor_id FROM DIST_ORDER WHERE order_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sel)) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new Exception("Order not found.");
                    }
                    int qty = rs.getInt("quantity");
                    BigDecimal price = rs.getBigDecimal("price");
                    BigDecimal ship = rs.getBigDecimal("shipping_cost");
                    distributorId = rs.getInt("distributor_id");
                    billed = price.multiply(BigDecimal.valueOf(qty)).add(ship);
                }
            }

            String upd = "UPDATE DIST_ORDER SET billed_amount = ? WHERE order_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(upd)) {
                ps.setBigDecimal(1, billed);
                ps.setInt(2, orderId);
                if (ps.executeUpdate() == 0) {
                    throw new Exception("Could not update order.");
                }
            }

            String ins = "INSERT INTO DISTRIBUTOR_PAYMENT (payment_id, distributor_id, amount, payment_date) VALUES (?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(ins)) {
                ps.setInt(1, paymentId);
                ps.setInt(2, distributorId);
                ps.setBigDecimal(3, billed);
                ps.setDate(4, java.sql.Date.valueOf(paymentDate));
                ps.executeUpdate();
            }
            // TRANSACTION END

            conn.commit();
            System.out.println("Order billed and payment recorded successfully");
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    // keep flow simple
                }
            }
            System.out.println("Billing failed - transaction rolled back");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    // keep flow simple
                }
            }
        }
    }

    // ── 35. Record distributor payment ───────────────────────────────────────
    static void recordDistributorPayment() throws Exception {
        System.out.print("Payment ID: ");
        int paymentId = scanner.nextInt();
        System.out.print("Distributor ID: ");
        int distributorId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Payment date (YYYY-MM-DD): ");
        String paymentDate = scanner.nextLine().trim();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        String sql = "INSERT INTO DISTRIBUTOR_PAYMENT (payment_id, distributor_id, amount, payment_date) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.setInt(2, distributorId);
            ps.setBigDecimal(3, amount);
            ps.setDate(4, java.sql.Date.valueOf(paymentDate));
            ps.executeUpdate();
            System.out.println("Distributor payment recorded successfully.");
        }
    }

    // ── 36. Allocate payment to order ────────────────────────────────────────
    static void allocatePaymentToOrder() throws Exception {
        System.out.print("Payment ID: ");
        int paymentId = scanner.nextInt();
        System.out.print("Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Allocated amount: ");
        BigDecimal allocated = new BigDecimal(scanner.nextLine().trim());

        if (allocated.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Allocated amount must be greater than 0.");
            return;
        }

        String sql = "INSERT INTO PAYMENT_ALLOCATION (payment_id, order_id, allocated_amount) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.setInt(2, orderId);
            ps.setBigDecimal(3, allocated);
            ps.executeUpdate();
            System.out.println("Payment allocation recorded successfully.");
        }
    }

    // ── 37. View outstanding balances ─────────────────────────────────────────
    static void viewOutstandingBalances() throws Exception {
        final String sql = "SELECT d.distributor_id, d.name, "
                + "COALESCE(SUM(o.billed_amount), 0) AS total_billed, "
                + "COALESCE((SELECT SUM(pa.allocated_amount) FROM PAYMENT_ALLOCATION pa "
                + "JOIN DIST_ORDER o2 ON pa.order_id = o2.order_id WHERE o2.distributor_id = d.distributor_id), 0) AS total_paid, "
                + "COALESCE(SUM(o.billed_amount), 0) - "
                + "COALESCE((SELECT SUM(pa.allocated_amount) FROM PAYMENT_ALLOCATION pa "
                + "JOIN DIST_ORDER o2 ON pa.order_id = o2.order_id WHERE o2.distributor_id = d.distributor_id), 0) AS outstanding_balance "
                + "FROM DISTRIBUTOR d "
                + "LEFT JOIN DIST_ORDER o ON d.distributor_id = o.distributor_id "
                + "GROUP BY d.distributor_id, d.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-16s %-30s %-18s %-18s %-22s%n",
                    "distributor_id", "name", "total_billed", "total_paid", "outstanding_balance");
            System.out.println("-".repeat(110));
            boolean found = false;
            while (rs.next()) {
                found = true;
                BigDecimal tb = rs.getBigDecimal("total_billed");
                BigDecimal tp = rs.getBigDecimal("total_paid");
                BigDecimal ob = rs.getBigDecimal("outstanding_balance");
                System.out.printf("%-16d %-30s %-18s %-18s %-22s%n",
                        rs.getInt("distributor_id"),
                        rs.getString("name"),
                        tb != null ? tb.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00",
                        tp != null ? tp.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00",
                        ob != null ? ob.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No distributors found.");
            }
        }
    }

    // ── 38. Identify billing mismatch ─────────────────────────────────────────
    static void identifyBillingMismatch() throws Exception {
        final String sql = "SELECT D.distributor_id, D.name, "
                + "COALESCE(SUM(DO.billed_amount), 0) AS total_billed, "
                + "COALESCE(SUM(PA.allocated_amount), 0) AS total_paid "
                + "FROM DISTRIBUTOR D "
                + "LEFT JOIN DIST_ORDER DO ON D.distributor_id = DO.distributor_id "
                + "LEFT JOIN PAYMENT_ALLOCATION PA ON DO.order_id = PA.order_id "
                + "GROUP BY D.distributor_id, D.name "
                + "HAVING total_billed != total_paid OR total_paid IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-16s %-30s %-18s %-18s%n",
                    "distributor_id", "name", "total_billed", "total_paid");
            System.out.println("-".repeat(88));
            boolean found = false;
            while (rs.next()) {
                found = true;
                BigDecimal tb = rs.getBigDecimal("total_billed");
                BigDecimal tp = rs.getBigDecimal("total_paid");
                System.out.printf("%-16d %-30s %-18s %-18s%n",
                        rs.getInt("distributor_id"),
                        rs.getString("name"),
                        tb != null ? tb.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00",
                        tp != null ? tp.setScale(2, RoundingMode.HALF_UP).toPlainString() : "0.00");
            }
            if (!found) {
                System.out.println("No billing mismatches found.");
            }
        }
    }

    // ── 39. List distributors by type and city ───────────────────────────────
    static void listDistributorsByTypeAndCity() throws Exception {
        System.out.print("Category (Bookstore/Wholesale/Library): ");
        String category = scanner.nextLine().trim();
        System.out.print("City: ");
        String city = scanner.nextLine().trim();

        String sql = "SELECT * FROM DISTRIBUTOR WHERE category = ? AND city = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, city);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-6s %-25s %-12s %-20s %-12s %-8s %-8s %-12s %-15s %-20s%n",
                        "id", "name", "category", "street", "city", "state", "zip", "country", "phone", "contact");
                System.out.println("-".repeat(140));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-6d %-25s %-12s %-20s %-12s %-8s %-8s %-12s %-15s %-20s%n",
                            rs.getInt("distributor_id"),
                            truncate(rs.getString("name"), 25),
                            truncate(rs.getString("category"), 12),
                            truncate(rs.getString("street"), 20),
                            truncate(rs.getString("city"), 12),
                            truncate(rs.getString("state"), 8),
                            truncate(rs.getString("zip"), 8),
                            truncate(rs.getString("country"), 12),
                            truncate(rs.getString("phone"), 15),
                            truncate(rs.getString("contact_person"), 20));
                }
                if (!found) {
                    System.out.println("No distributors match that category and city.");
                }
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
