import java.sql.*;

public class OrderHistory {

    public static void showHistory() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = """
                SELECT * FROM order_history
                WHERE user_id = ?
                ORDER BY order_group_id DESC, order_date DESC
                """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, UserSession.getUserId());
            ResultSet rs = ps.executeQuery();

            int currentOrder = -1;
            boolean found = false;

            while (rs.next()) {
                found = true;

                int orderId = rs.getInt("order_group_id");

                if (orderId != currentOrder) {
                    currentOrder = orderId;

                    System.out.println("\n==============================");
                    System.out.println("Order ID: " + orderId);
                    System.out.println("------------------------------");
                }

                System.out.println(
                    rs.getString("product_name") +
                    "  x" + rs.getInt("quantity") +
                    "  ₹" + rs.getInt("total_price")
                );
            }

            if (!found) {
                System.out.println("No previous orders found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}