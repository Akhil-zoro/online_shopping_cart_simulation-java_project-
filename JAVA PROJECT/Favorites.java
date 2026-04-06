import java.sql.*;
import java.util.Scanner;

public class Favorites {

    public static void add() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Product ID: ");
        int productId = sc.nextInt();

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT IGNORE INTO favorites(user_id, product_id) VALUES (?, ?)");
            ps.setInt(1, UserSession.getUserId());
            ps.setInt(2, productId);
            ps.executeUpdate();

            System.out.println("❤️ Added to favorites!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewFavorites() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("""
                SELECT p.id, p.name, p.price
                FROM product p
                JOIN favorites f ON p.id = f.product_id
                WHERE f.user_id = ?
            """);

            ps.setInt(1, UserSession.getUserId());
            ResultSet rs = ps.executeQuery();

            System.out.println("\n❤️ FAVORITES ❤️");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("name") + " | Rs." +
                    rs.getInt("price"));
            }

            if (!found) return;

            System.out.println("\n1. Add to Cart");
            System.out.println("2. Remove");
            System.out.println("3. Back");

            int choice = Integer.parseInt(sc.nextLine());

            if (choice == 1) AddToCart.add();
            else if (choice == 2) {
                System.out.print("Enter Product ID: ");
                int pid = Integer.parseInt(sc.nextLine());

                PreparedStatement ps2 = con.prepareStatement(
                    "DELETE FROM favorites WHERE user_id=? AND product_id=?");
                ps2.setInt(1, UserSession.getUserId());
                ps2.setInt(2, pid);
                ps2.executeUpdate();

                System.out.println("❌ Removed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void view() {
    viewFavorites(); // or your actual method
}

}