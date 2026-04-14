import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ProductionService {

    @SuppressWarnings("resource")
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\nProduction Menu");
            System.out.println("1. Search publications");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try {
                        //searchPublications(null, null, null, null);
                        //searchPublications("Science", null, null, null);
                        searchPublications(null, null, "2024-01-01", "2024-12-31");
                    } catch (Exception e) {
                        System.out.println("Error occurred:");
                        e.printStackTrace();
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

    public static void searchPublications(String topic, String author, String start_date, String end_date) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("a.issue_id, ");
        sql.append("a.article_number, ");
        sql.append("a.title, ");
        sql.append("a.person_id, ");
        sql.append("a.topic, ");
        sql.append("a.date_written ");
        sql.append("FROM ARTICLE a ");
        sql.append("WHERE 1=1 ");

        if (topic != null) {
            sql.append("AND a.topic = ? ");
        }
        if (author != null) {
            sql.append("AND a.person_id = ? ");
        }
        if (start_date != null && end_date != null) {
            sql.append("AND a.date_written BETWEEN ? AND ? ");
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            if (topic != null) {
                ps.setString(idx++, topic);
            }
            if (author != null) {
                ps.setInt(idx++, Integer.parseInt(author));
            }
            if (start_date != null && end_date != null) {
                ps.setString(idx++, start_date);
                ps.setString(idx++, end_date);
            }

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("issue_id | article_no | title | person_id | topic | date");
                while (rs.next()) {
                    System.out.println(rs.getInt("issue_id") + " | " + rs.getInt("article_number") + " | "
                            + rs.getString("title") + " | " + rs.getInt("person_id") + " | "
                            + rs.getString("topic") + " | " + rs.getDate("date_written"));
                }
            }
        }
    }
}
