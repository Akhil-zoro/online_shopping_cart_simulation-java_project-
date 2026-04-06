import java.sql.*;

public class ProductByCategory {

    public static void show(String category) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = """
                SELECT 
                    p.id,
                    p.name,
                    p.price,
                    p.stock,
                    IFNULL(SUM(c.quantity), 0) AS reserved_qty,
                    MIN(TIMESTAMPDIFF(SECOND, NOW(), 
                        c.reserved_at + INTERVAL ? MINUTE)) AS time_left
                FROM product p
                LEFT JOIN cart c 
                    ON p.id = c.product_id
                    AND c.reserved_at > NOW() - INTERVAL ? MINUTE
                WHERE p.category = ?
                GROUP BY p.id, p.name, p.price, p.stock
                ORDER BY p.id
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ReservationConfig.RESERVATION_MINUTES);
            ps.setInt(2, ReservationConfig.RESERVATION_MINUTES);
            ps.setString(3, category);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- " + category.toUpperCase() + " PRODUCTS ---");

            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");
                int reserved = rs.getInt("reserved_qty");
                int timeLeft = rs.getInt("time_left");

                int available = stock - reserved;

                String status;

                // ✅ AMAZON STYLE DISPLAY
                if (available <= 0) {
                    status = "❌ Out of Stock";
                } 
                else if (available <= 5) {
                    status = "⚠ Only few left";
                } 
                else {
                    status = "✔ In Stock";
                }

                // 🔥 KEEP RESERVATION FEATURE
                if (reserved > 0 && available > 0) {

                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;

                    status += " | ⏳ " + reserved + " reserved (" 
                            + minutes + "m " + seconds + "s)";
                }

                System.out.println(
                    id + " | " + name +
                    " | ₹" + price +
                    " | " + status
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}