import java.sql.*;
import java.util.Scanner;

public class ProductionService {

    @SuppressWarnings("resource")
    static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== Production ===");
            System.out.println("-- Book / Issue Management --");
            System.out.println("11. Enter new book edition");
            System.out.println("12. Enter new issue");
            System.out.println("13. Update book edition");
            System.out.println("14. Update issue");
            System.out.println("15. Delete book edition");
            System.out.println("16. Delete issue");
            System.out.println("-- Written Work Management --");
            System.out.println("17. Enter article");
            System.out.println("18. Update article metadata");
            System.out.println("19. Update article text");
            System.out.println("20. Enter chapter");
            System.out.println("21. Update chapter metadata");
            System.out.println("22. Update chapter text");
            System.out.println("-- Search & Retrieval --");
            System.out.println("23. Find works by topic");
            System.out.println("24. Find works by author");
            System.out.println("25. Find works by date range");
            System.out.println("-- Staff Payments --");
            System.out.println("26. Enter payment for author/editor");
            System.out.println("27. Record payment claimed");
            System.out.println("28. List unclaimed payments within time window");
            System.out.println("-- Issue Comparison --");
            System.out.println("29. Compare two issues");
            System.out.println("0.  Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 11: enterNewBookEdition();          break;
                    case 12: enterNewIssue();                break;
                    case 13: updateBookEdition();            break;
                    case 14: updateIssue();                  break;
                    case 15: deleteBookEdition();            break;
                    case 16: deleteIssue();                  break;
                    case 17: enterArticle();                 break;
                    case 18: updateArticleMetadata();        break;
                    case 19: updateArticleText();            break;
                    case 20: enterChapter();                 break;
                    case 21: updateChapterMetadata();        break;
                    case 22: updateChapterText();            break;
                    case 23: findByTopic();                  break;
                    case 24: findByAuthor();                 break;
                    case 25: findByDateRange();              break;
                    case 26: enterPayment();                 break;
                    case 27: recordPaymentClaimed();         break;
                    case 28: listUnclaimedPayments();        break;
                    case 29: compareIssues();                break;
                    case 0:  inMenu = false;                 break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ── 11. Enter new book edition ───────────────────────────────────────────
    static void enterNewBookEdition() throws Exception {
        System.out.print("Edition ID: ");
        int editionId = scanner.nextInt();
        System.out.print("Publication ID: ");
        int pubId = scanner.nextInt();
        System.out.print("Edition Number: ");
        int edNum = scanner.nextInt(); scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Publication Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        String sql = "INSERT INTO BOOK_EDITION (edition_id, publication_id, edition_number, ISBN, publication_date) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, editionId);
            ps.setInt(2, pubId);
            ps.setInt(3, edNum);
            ps.setString(4, isbn);
            ps.setDate(5, java.sql.Date.valueOf(date));
            ps.executeUpdate();
            System.out.println("Book edition added.");
        }
    }

    // ── 12. Enter new issue ──────────────────────────────────────────────────
    static void enterNewIssue() throws Exception {
        System.out.print("Issue ID: ");
        int issueId = scanner.nextInt();
        System.out.print("Publication ID: ");
        int pubId = scanner.nextInt(); scanner.nextLine();
        System.out.print("Issue Title: ");
        String title = scanner.nextLine();
        System.out.print("Publication Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        String sql = "INSERT INTO ISSUE (issue_id, publication_id, issue_title, publication_date) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.setInt(2, pubId);
            ps.setString(3, title);
            ps.setDate(4, java.sql.Date.valueOf(date));
            ps.executeUpdate();
            System.out.println("Issue added.");
        }
    }

    // ── 13. Update book edition ──────────────────────────────────────────────
    static void updateBookEdition() throws Exception {
        System.out.print("Edition ID to update: ");
        int editionId = scanner.nextInt(); scanner.nextLine();
        System.out.print("New ISBN (blank to keep): ");
        String isbn = scanner.nextLine().trim();
        System.out.print("New Publication Date (YYYY-MM-DD, blank to keep): ");
        String date = scanner.nextLine().trim();

        StringBuilder sql = new StringBuilder("UPDATE BOOK_EDITION SET edition_id=edition_id");
        if (!isbn.isEmpty()) sql.append(", ISBN=?");
        if (!date.isEmpty()) sql.append(", publication_date=?");
        sql.append(" WHERE edition_id=?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!isbn.isEmpty()) ps.setString(idx++, isbn);
            if (!date.isEmpty()) ps.setDate(idx++, java.sql.Date.valueOf(date));
            ps.setInt(idx, editionId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Book edition updated." : "Edition not found.");
        }
    }

    // ── 14. Update issue ─────────────────────────────────────────────────────
    static void updateIssue() throws Exception {
        System.out.print("Issue ID to update: ");
        int issueId = scanner.nextInt(); scanner.nextLine();
        System.out.print("New title (blank to keep): ");
        String title = scanner.nextLine().trim();
        System.out.print("New publication date (YYYY-MM-DD, blank to keep): ");
        String date = scanner.nextLine().trim();

        StringBuilder sql = new StringBuilder("UPDATE ISSUE SET issue_id=issue_id");
        if (!title.isEmpty()) sql.append(", issue_title=?");
        if (!date.isEmpty())  sql.append(", publication_date=?");
        sql.append(" WHERE issue_id=?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!title.isEmpty()) ps.setString(idx++, title);
            if (!date.isEmpty())  ps.setDate(idx++, java.sql.Date.valueOf(date));
            ps.setInt(idx, issueId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Issue updated." : "Issue not found.");
        }
    }

    // ── 15. Delete book edition ──────────────────────────────────────────────
    static void deleteBookEdition() throws Exception {
        System.out.print("Edition ID to delete: ");
        int editionId = scanner.nextInt(); scanner.nextLine();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM BOOK_EDITION WHERE edition_id=?")) {
            ps.setInt(1, editionId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Book edition deleted." : "Edition not found.");
        }
    }

    // ── 16. Delete issue ─────────────────────────────────────────────────────
    static void deleteIssue() throws Exception {
        System.out.print("Issue ID to delete: ");
        int issueId = scanner.nextInt(); scanner.nextLine();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM ISSUE WHERE issue_id=?")) {
            ps.setInt(1, issueId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Issue deleted." : "Issue not found.");
        }
    }

    // ── 17. Enter article ────────────────────────────────────────────────────
    static void enterArticle() throws Exception {
        System.out.print("Issue ID: ");
        int issueId = scanner.nextInt();
        System.out.print("Article Number: ");
        int articleNo = scanner.nextInt(); scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author Person ID: ");
        int personId = scanner.nextInt(); scanner.nextLine();
        System.out.print("Topic: ");
        String topic = scanner.nextLine();
        System.out.print("Date Written (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Full Text: ");
        String text = scanner.nextLine();

        String sql = "INSERT INTO ARTICLE (issue_id, article_number, title, person_id, topic, date_written, full_text) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.setInt(2, articleNo);
            ps.setString(3, title);
            ps.setInt(4, personId);
            ps.setString(5, topic);
            ps.setDate(6, java.sql.Date.valueOf(date));
            ps.setString(7, text);
            ps.executeUpdate();
            System.out.println("Article added.");
        }
    }

    // ── 18. Update article metadata ──────────────────────────────────────────
    static void updateArticleMetadata() throws Exception {
        System.out.print("Issue ID: ");
        int issueId = scanner.nextInt();
        System.out.print("Article Number: ");
        int articleNo = scanner.nextInt(); scanner.nextLine();
        System.out.print("New title (blank to keep): ");
        String title = scanner.nextLine().trim();
        System.out.print("New author Person ID (0 to keep): ");
        int personId = scanner.nextInt(); scanner.nextLine();
        System.out.print("New topic (blank to keep): ");
        String topic = scanner.nextLine().trim();
        System.out.print("New date (YYYY-MM-DD, blank to keep): ");
        String date = scanner.nextLine().trim();

        StringBuilder sql = new StringBuilder("UPDATE ARTICLE SET issue_id=issue_id");
        if (!title.isEmpty())  sql.append(", title=?");
        if (personId != 0)     sql.append(", person_id=?");
        if (!topic.isEmpty())  sql.append(", topic=?");
        if (!date.isEmpty())   sql.append(", date_written=?");
        sql.append(" WHERE issue_id=? AND article_number=?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!title.isEmpty())  ps.setString(idx++, title);
            if (personId != 0)     ps.setInt(idx++, personId);
            if (!topic.isEmpty())  ps.setString(idx++, topic);
            if (!date.isEmpty())   ps.setDate(idx++, java.sql.Date.valueOf(date));
            ps.setInt(idx++, issueId);
            ps.setInt(idx, articleNo);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Article metadata updated." : "Article not found.");
        }
    }

    // ── 19. Update article text ──────────────────────────────────────────────
    static void updateArticleText() throws Exception {
        System.out.print("Issue ID: ");
        int issueId = scanner.nextInt();
        System.out.print("Article Number: ");
        int articleNo = scanner.nextInt(); scanner.nextLine();
        System.out.print("New full text: ");
        String text = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE ARTICLE SET full_text=? WHERE issue_id=? AND article_number=?")) {
            ps.setString(1, text);
            ps.setInt(2, issueId);
            ps.setInt(3, articleNo);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Article text updated." : "Article not found.");
        }
    }

    // ── 20. Enter chapter ────────────────────────────────────────────────────
    static void enterChapter() throws Exception {
        System.out.print("Edition ID: ");
        int editionId = scanner.nextInt();
        System.out.print("Chapter Number: ");
        int chapterNo = scanner.nextInt(); scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author Person ID: ");
        int personId = scanner.nextInt(); scanner.nextLine();
        System.out.print("Topic: ");
        String topic = scanner.nextLine();
        System.out.print("Date Written (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Full Text: ");
        String text = scanner.nextLine();

        String sql = "INSERT INTO CHAPTER (edition_id, chapter_number, title, person_id, topic, date_written, full_text) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, editionId);
            ps.setInt(2, chapterNo);
            ps.setString(3, title);
            ps.setInt(4, personId);
            ps.setString(5, topic);
            ps.setDate(6, java.sql.Date.valueOf(date));
            ps.setString(7, text);
            ps.executeUpdate();
            System.out.println("Chapter added.");
        }
    }

    // ── 21. Update chapter metadata ──────────────────────────────────────────
    static void updateChapterMetadata() throws Exception {
        System.out.print("Edition ID: ");
        int editionId = scanner.nextInt();
        System.out.print("Chapter Number: ");
        int chapterNo = scanner.nextInt(); scanner.nextLine();
        System.out.print("New title (blank to keep): ");
        String title = scanner.nextLine().trim();
        System.out.print("New author Person ID (0 to keep): ");
        int personId = scanner.nextInt(); scanner.nextLine();
        System.out.print("New topic (blank to keep): ");
        String topic = scanner.nextLine().trim();
        System.out.print("New date (YYYY-MM-DD, blank to keep): ");
        String date = scanner.nextLine().trim();

        StringBuilder sql = new StringBuilder("UPDATE CHAPTER SET edition_id=edition_id");
        if (!title.isEmpty())  sql.append(", title=?");
        if (personId != 0)     sql.append(", person_id=?");
        if (!topic.isEmpty())  sql.append(", topic=?");
        if (!date.isEmpty())   sql.append(", date_written=?");
        sql.append(" WHERE edition_id=? AND chapter_number=?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!title.isEmpty())  ps.setString(idx++, title);
            if (personId != 0)     ps.setInt(idx++, personId);
            if (!topic.isEmpty())  ps.setString(idx++, topic);
            if (!date.isEmpty())   ps.setDate(idx++, java.sql.Date.valueOf(date));
            ps.setInt(idx++, editionId);
            ps.setInt(idx, chapterNo);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Chapter metadata updated." : "Chapter not found.");
        }
    }

    // ── 22. Update chapter text ──────────────────────────────────────────────
    static void updateChapterText() throws Exception {
        System.out.print("Edition ID: ");
        int editionId = scanner.nextInt();
        System.out.print("Chapter Number: ");
        int chapterNo = scanner.nextInt(); scanner.nextLine();
        System.out.print("New full text: ");
        String text = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE CHAPTER SET full_text=? WHERE edition_id=? AND chapter_number=?")) {
            ps.setString(1, text);
            ps.setInt(2, editionId);
            ps.setInt(3, chapterNo);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Chapter text updated." : "Chapter not found.");
        }
    }

    // ── 23. Find works by topic ──────────────────────────────────────────────
    static void findByTopic() throws Exception {
        System.out.print("Topic: ");
        String topic = scanner.nextLine();

        String sql = "SELECT 'Article' AS type, a.title, p.name AS author, a.topic, a.date_written " +
                     "FROM ARTICLE a JOIN PERSON p ON a.person_id=p.person_id WHERE a.topic LIKE ? " +
                     "UNION ALL " +
                     "SELECT 'Chapter' AS type, c.title, p.name AS author, c.topic, c.date_written " +
                     "FROM CHAPTER c JOIN PERSON p ON c.person_id=p.person_id WHERE c.topic LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + topic + "%");
            ps.setString(2, "%" + topic + "%");
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-8s %-40s %-20s %-25s %-12s%n", "Type", "Title", "Author", "Topic", "Date");
                System.out.println("-".repeat(108));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-8s %-40s %-20s %-25s %-12s%n",
                        rs.getString("type"), rs.getString("title"),
                        rs.getString("author"), rs.getString("topic"),
                        rs.getDate("date_written"));
                }
                if (!found) System.out.println("No works found for topic: " + topic);
            }
        }
    }

    // ── 24. Find works by author ─────────────────────────────────────────────
    static void findByAuthor() throws Exception {
        System.out.print("Author Person ID: ");
        int personId = scanner.nextInt(); scanner.nextLine();

        String sql = "SELECT 'Article' AS type, a.title, a.topic, a.date_written " +
                     "FROM ARTICLE a WHERE a.person_id=? " +
                     "UNION ALL " +
                     "SELECT 'Chapter' AS type, c.title, c.topic, c.date_written " +
                     "FROM CHAPTER c WHERE c.person_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personId);
            ps.setInt(2, personId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-8s %-40s %-25s %-12s%n", "Type", "Title", "Topic", "Date");
                System.out.println("-".repeat(88));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-8s %-40s %-25s %-12s%n",
                        rs.getString("type"), rs.getString("title"),
                        rs.getString("topic"), rs.getDate("date_written"));
                }
                if (!found) System.out.println("No works found for author ID: " + personId);
            }
        }
    }

    // ── 25. Find works by date range ─────────────────────────────────────────
    static void findByDateRange() throws Exception {
        System.out.print("Start date (YYYY-MM-DD): ");
        String start = scanner.nextLine();
        System.out.print("End date (YYYY-MM-DD): ");
        String end = scanner.nextLine();

        String sql = "SELECT 'Article' AS type, a.title, p.name AS author, a.topic, a.date_written " +
                     "FROM ARTICLE a JOIN PERSON p ON a.person_id=p.person_id " +
                     "WHERE a.date_written BETWEEN ? AND ? " +
                     "UNION ALL " +
                     "SELECT 'Chapter' AS type, c.title, p.name AS author, c.topic, c.date_written " +
                     "FROM CHAPTER c JOIN PERSON p ON c.person_id=p.person_id " +
                     "WHERE c.date_written BETWEEN ? AND ? " +
                     "ORDER BY date_written";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            ps.setDate(3, java.sql.Date.valueOf(start));
            ps.setDate(4, java.sql.Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-8s %-40s %-20s %-25s %-12s%n", "Type", "Title", "Author", "Topic", "Date");
                System.out.println("-".repeat(108));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-8s %-40s %-20s %-25s %-12s%n",
                        rs.getString("type"), rs.getString("title"),
                        rs.getString("author"), rs.getString("topic"),
                        rs.getDate("date_written"));
                }
                if (!found) System.out.println("No works found in that date range.");
            }
        }
    }

    // ── 26. Enter payment for author/editor ──────────────────────────────────
    static void enterPayment() throws Exception {
        System.out.print("Payment ID: ");
        int paymentId = scanner.nextInt();
        System.out.print("Person ID: ");
        int personId = scanner.nextInt(); scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble(); scanner.nextLine();
        System.out.print("Issued Date (YYYY-MM-DD): ");
        String issuedDate = scanner.nextLine();
        System.out.print("Contribution Reference: ");
        String ref = scanner.nextLine();

        String sql = "INSERT INTO STAFF_PAYMENT (payment_id, person_id, amount, issued_date, contribution_reference) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.setInt(2, personId);
            ps.setDouble(3, amount);
            ps.setDate(4, java.sql.Date.valueOf(issuedDate));
            ps.setString(5, ref);
            ps.executeUpdate();
            System.out.println("Payment entered.");
        }
    }

    // ── 27. Record payment claimed ───────────────────────────────────────────
    static void recordPaymentClaimed() throws Exception {
        System.out.print("Payment ID: ");
        int paymentId = scanner.nextInt(); scanner.nextLine();
        System.out.print("Claimed Date (YYYY-MM-DD): ");
        String claimedDate = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE STAFF_PAYMENT SET claimed_date=? WHERE payment_id=?")) {
            ps.setDate(1, java.sql.Date.valueOf(claimedDate));
            ps.setInt(2, paymentId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Payment marked as claimed." : "Payment not found.");
        }
    }

    // ── 28. List unclaimed payments within time window ───────────────────────
    static void listUnclaimedPayments() throws Exception {
        System.out.print("Start date (YYYY-MM-DD): ");
        String start = scanner.nextLine();
        System.out.print("End date (YYYY-MM-DD): ");
        String end = scanner.nextLine();

        String sql = "SELECT sp.payment_id, p.name, sp.amount, sp.issued_date, sp.contribution_reference " +
                     "FROM STAFF_PAYMENT sp JOIN PERSON p ON sp.person_id=p.person_id " +
                     "WHERE sp.claimed_date IS NULL AND sp.issued_date BETWEEN ? AND ? " +
                     "ORDER BY sp.issued_date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-10s %-20s %-10s %-12s %-30s%n",
                    "Pay ID", "Person", "Amount", "Issued", "Reference");
                System.out.println("-".repeat(85));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-10d %-20s %-10.2f %-12s %-30s%n",
                        rs.getInt("payment_id"), rs.getString("name"),
                        rs.getDouble("amount"), rs.getDate("issued_date"),
                        rs.getString("contribution_reference"));
                }
                if (!found) System.out.println("No unclaimed payments in that window.");
            }
        }
    }

    // ── 29. Compare two issues ───────────────────────────────────────────────
    static void compareIssues() throws Exception {
        System.out.print("First Issue ID: ");
        int issue1 = scanner.nextInt();
        System.out.print("Second Issue ID: ");
        int issue2 = scanner.nextInt(); scanner.nextLine();

        String sql = "SELECT a.issue_id, a.article_number, a.title, p.name AS author, a.topic, a.date_written " +
                     "FROM ARTICLE a JOIN PERSON p ON a.person_id=p.person_id " +
                     "WHERE a.issue_id=? OR a.issue_id=? ORDER BY a.issue_id, a.article_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, issue1);
            ps.setInt(2, issue2);
            try (ResultSet rs = ps.executeQuery()) {
                int lastIssue = -1;
                while (rs.next()) {
                    int currentIssue = rs.getInt("issue_id");
                    if (currentIssue != lastIssue) {
                        System.out.println("\n--- Issue " + currentIssue + " ---");
                        System.out.printf("%-5s %-35s %-20s %-25s %-12s%n",
                            "Art#", "Title", "Author", "Topic", "Date");
                        System.out.println("-".repeat(100));
                        lastIssue = currentIssue;
                    }
                    System.out.printf("%-5d %-35s %-20s %-25s %-12s%n",
                        rs.getInt("article_number"), rs.getString("title"),
                        rs.getString("author"), rs.getString("topic"),
                        rs.getDate("date_written"));
                }
            }
        }
    }
}
