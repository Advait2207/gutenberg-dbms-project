import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class EditingService {

    @SuppressWarnings("resource")
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\nEditing & Publishing Menu");
            System.out.println("1. View publications by editor");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try {
                        viewPublicationsByEditor(4);
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

    public static void viewPublicationsByEditor(int person_id) throws Exception {
        String sql = "SELECT issue_id, article_number, title, topic, date_written FROM ARTICLE WHERE person_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, person_id);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("issue_id | article_no | title | topic | date");
                while (rs.next()) {
                    System.out.println(rs.getInt("issue_id") + " | " + rs.getInt("article_number") + " | "
                            + rs.getString("title") + " | " + rs.getString("topic") + " | "
                            + rs.getDate("date_written"));
                }
            }
        }
    }
}
