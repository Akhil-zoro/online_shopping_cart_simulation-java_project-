import java.sql.*;
import java.util.Scanner;

public class RemoveFromCart {

    public static void remove() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter Product ID to remove: ");
        int productId = sc.nextInt();

        System.out.print("Enter quantity to remove: ");
        int removeQty = sc.nextInt();

        if (removeQty <= 0) {
            System.out.println("❌ Quantity must be greater than zero.");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // ✅ Fetch only current user's cart item
            String fetchSql = "SELECT quantity FROM cart WHERE product_id = ? AND user_id = ?";
            PreparedStatement ps1 = con.prepareStatement(fetchSql);
            ps1.setInt(1, productId);
            ps1.setInt(2, UserSession.getUserId());
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Product not found in your cart.");
                return;
            }

            int currentQty = rs.getInt("quantity");

            if (removeQty > currentQty) {
                System.out.println("❌ Cannot remove more than existing quantity.");
                System.out.println("Current quantity in cart: " + currentQty);
                return;
            }

            if (removeQty == currentQty) {
                // ✅ Delete only user's row
                String deleteSql = "DELETE FROM cart WHERE product_id = ? AND user_id = ?";
                PreparedStatement ps2 = con.prepareStatement(deleteSql);
                ps2.setInt(1, productId);
                ps2.setInt(2, UserSession.getUserId());
                ps2.executeUpdate();

                System.out.println("✔ Item completely removed from cart.");
            } else {
                // ✅ Update only user's row
                String updateSql =
                    "UPDATE cart SET quantity = quantity - ? WHERE product_id = ? AND user_id = ?";
                PreparedStatement ps3 = con.prepareStatement(updateSql);
                ps3.setInt(1, removeQty);
                ps3.setInt(2, productId);
                ps3.setInt(3, UserSession.getUserId());
                ps3.executeUpdate();

                System.out.println("✔ Removed " + removeQty + " item(s) from cart.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}