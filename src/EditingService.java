import java.sql.*;
import java.util.Scanner;

public class EditingService {

    @SuppressWarnings("resource")
    static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== Editing & Publishing ===");
            System.out.println("-- Publication Management --");
            System.out.println("1.  Enter new publication");
            System.out.println("2.  Update publication information");
            System.out.println("3.  Assign editor to publication");
            System.out.println("4.  Remove editor from publication");
            System.out.println("5.  View publications by editor");
            System.out.println("-- Table of Contents Management --");
            System.out.println("6.  Add article to an issue");
            System.out.println("7.  Remove article from an issue");
            System.out.println("8.  Add chapter to a book edition");
            System.out.println("9.  Remove chapter from a book edition");
            System.out.println("10. List publications/issues with missing content");
            System.out.println("0.  Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:  enterNewPublication();           break;
                    case 2:  updatePublication();             break;
                    case 3:  assignEditor();                  break;
                    case 4:  removeEditor();                  break;
                    case 5:  viewPublicationsByEditor();      break;
                    case 6:  addArticleToIssue();             break;
                    case 7:  removeArticleFromIssue();        break;
                    case 8:  addChapterToEdition();           break;
                    case 9:  removeChapterFromEdition();      break;
                    case 10: listMissingContent();            break;
                    case 0:  inMenu = false;                  break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ── 1. Enter new publication ─────────────────────────────────────────────
    static void enterNewPublication() throws Exception {
        System.out.print("Publication ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Type (Book/Magazine/Journal): ");
        String type = scanner.nextLine();
        System.out.print("Periodicity (leave blank for Books): ");
        String periodicity = scanner.nextLine().trim();

        String sql = "INSERT INTO PUBLICATION (publication_id, title, type, periodicity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, type);
            ps.setString(4, periodicity.isEmpty() ? null : periodicity);
            ps.executeUpdate();
            System.out.println("Publication added successfully.");
        }
    }

    // ── 2. Update publication information ───────────────────────────────────
    static void updatePublication() throws Exception {
        System.out.print("Publication ID to update: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("New title (leave blank to keep): ");
        String title = scanner.nextLine().trim();
        System.out.print("New type (Book/Magazine/Journal, leave blank to keep): ");
        String type = scanner.nextLine().trim();
        System.out.print("New periodicity (leave blank to keep): ");
        String periodicity = scanner.nextLine().trim();

        StringBuilder sql = new StringBuilder("UPDATE PUBLICATION SET publication_id=publication_id");
        if (!title.isEmpty())       sql.append(", title=?");
        if (!type.isEmpty())        sql.append(", type=?");
        if (!periodicity.isEmpty()) sql.append(", periodicity=?");
        sql.append(" WHERE publication_id=?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!title.isEmpty())       ps.setString(idx++, title);
            if (!type.isEmpty())        ps.setString(idx++, type);
            if (!periodicity.isEmpty()) ps.setString(idx++, periodicity);
            ps.setInt(idx, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Publication updated." : "No publication found with that ID.");
        }
    }

    // ── 3. Assign editor to publication ─────────────────────────────────────
    static void assignEditor() throws Exception {
        System.out.print("Publication ID: ");
        int pubId = scanner.nextInt();
        System.out.print("Editor Person ID: ");
        int personId = scanner.nextInt(); scanner.nextLine();

        String sql = "INSERT INTO ASSIGNED_TO (publication_id, person_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pubId);
            ps.setInt(2, personId);
            ps.executeUpdate();
            System.out.println("Editor assigned successfully.");
        }
    }

    // ── 4. Remove editor from publication ───────────────────────────────────
    static void removeEditor() throws Exception {
        System.out.print("Publication ID: ");
        int pubId = scanner.nextInt();
        System.out.print("Editor Person ID: ");
        int personId = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM ASSIGNED_TO WHERE publication_id=? AND person_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pubId);
            ps.setInt(2, personId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Editor removed." : "Assignment not found.");
        }
    }

    // ── 5. View publications assigned to a specific editor ───────────────────
    static void viewPublicationsByEditor() throws Exception {
        System.out.print("Editor Person ID: ");
        int personId = scanner.nextInt(); scanner.nextLine();

        String sql = "SELECT p.publication_id, p.title, p.type, p.periodicity " +
                     "FROM PUBLICATION p JOIN ASSIGNED_TO a ON p.publication_id = a.publication_id " +
                     "WHERE a.person_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-6s %-40s %-10s %-10s%n", "ID", "Title", "Type", "Periodicity");
                System.out.println("-".repeat(70));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-6d %-40s %-10s %-10s%n",
                        rs.getInt("publication_id"), rs.getString("title"),
                        rs.getString("type"), rs.getString("periodicity"));
                }
                if (!found) System.out.println("No publications found for this editor.");
            }
        }
    }

    // ── 6. Add article to an issue ───────────────────────────────────────────
    static void addArticleToIssue() throws Exception {
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
        System.out.print("Date written (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Full text: ");
        String fullText = scanner.nextLine();

        String sql = "INSERT INTO ARTICLE (issue_id, article_number, title, person_id, topic, date_written, full_text) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.setInt(2, articleNo);
            ps.setString(3, title);
            ps.setInt(4, personId);
            ps.setString(5, topic);
            ps.setDate(6, java.sql.Date.valueOf(date));
            ps.setString(7, fullText);
            ps.executeUpdate();
            System.out.println("Article added successfully.");
        }
    }

    // ── 7. Remove article from an issue ─────────────────────────────────────
    static void removeArticleFromIssue() throws Exception {
        System.out.print("Issue ID: ");
        int issueId = scanner.nextInt();
        System.out.print("Article Number: ");
        int articleNo = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM ARTICLE WHERE issue_id=? AND article_number=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            ps.setInt(2, articleNo);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Article removed." : "Article not found.");
        }
    }

    // ── 8. Add chapter to a book edition ────────────────────────────────────
    static void addChapterToEdition() throws Exception {
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
        System.out.print("Date written (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Full text: ");
        String fullText = scanner.nextLine();

        String sql = "INSERT INTO CHAPTER (edition_id, chapter_number, title, person_id, topic, date_written, full_text) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, editionId);
            ps.setInt(2, chapterNo);
            ps.setString(3, title);
            ps.setInt(4, personId);
            ps.setString(5, topic);
            ps.setDate(6, java.sql.Date.valueOf(date));
            ps.setString(7, fullText);
            ps.executeUpdate();
            System.out.println("Chapter added successfully.");
        }
    }

    // ── 9. Remove chapter from a book edition ───────────────────────────────
    static void removeChapterFromEdition() throws Exception {
        System.out.print("Edition ID: ");
        int editionId = scanner.nextInt();
        System.out.print("Chapter Number: ");
        int chapterNo = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM CHAPTER WHERE edition_id=? AND chapter_number=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, editionId);
            ps.setInt(2, chapterNo);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Chapter removed." : "Chapter not found.");
        }
    }

    // ── 10. List publications/issues with missing content ───────────────────
    static void listMissingContent() throws Exception {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("\n-- Issues with no articles --");
            String sql1 = "SELECT i.issue_id, i.issue_title, p.title AS publication " +
                          "FROM ISSUE i JOIN PUBLICATION p ON i.publication_id = p.publication_id " +
                          "WHERE NOT EXISTS (SELECT 1 FROM ARTICLE a WHERE a.issue_id = i.issue_id)";
            try (ResultSet rs = stmt.executeQuery(sql1)) {
                System.out.printf("%-10s %-40s %-30s%n", "Issue ID", "Issue Title", "Publication");
                System.out.println("-".repeat(82));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-10d %-40s %-30s%n",
                        rs.getInt("issue_id"), rs.getString("issue_title"),
                        rs.getString("publication"));
                }
                if (!found) System.out.println("All issues have articles.");
            }

            System.out.println("\n-- Book editions with no chapters --");
            String sql2 = "SELECT be.edition_id, be.edition_number, p.title AS publication " +
                          "FROM BOOK_EDITION be JOIN PUBLICATION p ON be.publication_id = p.publication_id " +
                          "WHERE NOT EXISTS (SELECT 1 FROM CHAPTER c WHERE c.edition_id = be.edition_id)";
            try (ResultSet rs = stmt.executeQuery(sql2)) {
                System.out.printf("%-12s %-10s %-40s%n", "Edition ID", "Edition #", "Publication");
                System.out.println("-".repeat(64));
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.printf("%-12d %-10d %-40s%n",
                        rs.getInt("edition_id"), rs.getInt("edition_number"),
                        rs.getString("publication"));
                }
                if (!found) System.out.println("All book editions have chapters.");
            }
        }
    }
}
