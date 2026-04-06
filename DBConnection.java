import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver"); // 🔥 IMPORTANT

        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/onlineshop?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "Akhil@1709"
        );
    }
}