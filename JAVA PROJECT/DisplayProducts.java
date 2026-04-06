import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DisplayProducts {

    public static void displayAllProducts() {
        try {
            Connection con = DBConnection.getConnection();
 
            String sql = "SELECT * FROM product";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("------------------------------------------------");
            System.out.println("ID   NAME           CATEGORY        PRICE  STOCK");
            System.out.println("------------------------------------------------");

            while (rs.next()) { 
                System.out.printf(
                    "%-4d %-14s %-15s %-6d %-5d%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("price"),
                    rs.getInt("stock")
                );
            }

            System.out.println("------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
