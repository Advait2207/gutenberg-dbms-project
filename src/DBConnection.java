import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        // Read from environment variables
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String db   = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");

        // Basic validation (important for debugging)
        if (host == null || port == null || db == null || user == null || pass == null) {
            throw new Exception("Missing environment variables. Please set DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD.");
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;

        // Optional but safe (driver loading)
        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, user, pass);
    }
}