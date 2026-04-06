import java.sql.*;
import java.util.Scanner;

public class Auth {

    public static void login() {
        Scanner sc = new Scanner(System.in);

        
        System.out.print("Email: ");
        String email = sc.next();
        if (!email.endsWith("@gmail.com")) {
    System.out.println("❌ Invalid email format!");
    return;
}

        System.out.print("Password: ");
        String password = sc.next();

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT user_id, name, email, role FROM users WHERE email=? AND password=?";    
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String role = rs.getString("role");

                UserSession.setUser(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email")
                );
                UserSession.setRole(rs.getString("role")); // 🔥 IMPORTANT
                System.out.println("✔ Login successful!");
                sc.nextLine();
                CategoryMenu.showMenu();
            } else {
                System.out.println("❌ Invalid credentials");
            }

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 show real error
        }
    }

    public static void register() {
        Scanner sc = new Scanner(System.in);

         // 🔥 clear buffer

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.next();
if (!email.matches("^[A-Za-z0-9+_.-]+@gmail\\.com$")) {
    System.out.println("❌ Invalid email format!");
    return;
}
        System.out.print("Password: ");
        String password = sc.next();

        // ✅ Basic validation
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("❌ Invalid email format");
            return;
        }

        if (password.length() < 4) {
            System.out.println("❌ Password must be at least 4 characters");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            ps.executeUpdate();

            System.out.println("✔ Registration successful! Please login.");

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                System.out.println("❌ Email already registered!");
            } else {
                System.out.println("❌ Database error:");
                e.printStackTrace(); // 🔥 exact issue
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkPassword(String password) {

    try {
        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, UserSession.getUserId());
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        return rs.next();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}

    public static void updatePassword(String newPassword) {

    try {
        Connection con = DBConnection.getConnection();

        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, newPassword);
        ps.setInt(2, UserSession.getUserId());

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}