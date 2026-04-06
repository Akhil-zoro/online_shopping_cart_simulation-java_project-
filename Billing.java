import java.sql.*;
import java.util.Scanner;

public class Billing {

    static final String[] ALLOWED_PINCODES = {"533001","533002","533003","533004","533005","533006"};

    public static void checkout() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DBConnection.getConnection();

            // 🔥 1. ADDRESS CHECK
            if (!AddressManager.hasAddress()) {
                System.out.println("❌ No address found.");
                AddressManager.addAddress();
            }

            // 🔥 2. SELECT ADDRESS
            int addressId;

            while (true) {

                addressId = AddressManager.selectAddress();

                if (addressId == -1) {
                    System.out.println("❌ Invalid Address!");
                    System.out.println("1. Try Again");
                    System.out.println("2. Add New Address");
                    System.out.println("3. Cancel");

                    String opt = sc.nextLine();

                    if (opt.equals("2")) {
                        AddressManager.addAddress();
                    } else if (opt.equals("3")) {
                        return;
                    }

                } else {
                    break; // ✅ valid
                }
            }

            // 🔥 3. GET PINCODE OF SELECTED ADDRESS
            String userPin = AddressManager.getPincode(addressId);

            boolean allowed = false;
            for (String pin : ALLOWED_PINCODES) {
                if (pin.equals(userPin)) {
                    allowed = true;
                    break;
                }
            }

            if (!allowed) {
                System.out.println("❌ Delivery not available in your area!");
                return;
            }

            // 🔥 4. FETCH CART + ORDER SUMMARY
            String sql = """
                SELECT p.name, p.price, c.quantity
                FROM product p
                JOIN cart c ON p.id = c.product_id
                WHERE c.user_id = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, UserSession.getUserId());
            ResultSet rs = ps.executeQuery();

            int subtotal = 0;

            System.out.println("\n=========== ORDER SUMMARY ===========");
            System.out.println("\n📦 Items:");
            System.out.println("------------------------------------");

            boolean empty = true;

            while (rs.next()) {
                empty = false;

                String name = rs.getString("name");
                int price = rs.getInt("price");
                int qty = rs.getInt("quantity");

                int total = price * qty;
                subtotal += total;

                System.out.println(name + " | ₹" + price + " x " + qty + " = ₹" + total);
            }

            if (empty) {
                System.out.println("❌ Cart is empty!");
                return;
            }

            // 🔥 BILL CALCULATION
            double discount = (subtotal >= 50000) ? subtotal * 0.10 : 0;
            double gst = (subtotal - discount) * 0.05;
            double finalAmount = subtotal - discount + gst;

            System.out.println("------------------------------------");
            System.out.println("Subtotal : ₹" + subtotal);
            System.out.println("Discount : ₹" + discount);
            System.out.println("GST      : ₹" + gst);
            System.out.println("TOTAL    : ₹" + finalAmount);

            System.out.println("\n------------------------------------");
            System.out.println("1. Confirm Order");
            System.out.println("2. Cancel");
            System.out.print("Choose option: ");

            int confirm;
            try {
                confirm = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("❌ Invalid input");
                return;
            }

            if (confirm != 1) {
                System.out.println("❌ Order cancelled.");
                return;
            }

            // 🔥 FINAL PROCESS
            processOrder(con);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processOrder(Connection con) throws SQLException {

        con.setAutoCommit(false);

        int orderGroupId = (int) (System.currentTimeMillis() / 1000);

        try {
            String fetchCart = """
                SELECT p.id, p.name, p.price, c.quantity
                FROM product p
                JOIN cart c ON p.id = c.product_id
                WHERE c.user_id = ?
            """;

            PreparedStatement ps1 = con.prepareStatement(fetchCart);
            ps1.setInt(1, UserSession.getUserId());
            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                int qty = rs.getInt("quantity");
                int total = rs.getInt("price") * qty;

                // ✅ FIXED SQL
                String orderSql =
                    "INSERT INTO order_history(order_group_id, user_id, product_name, quantity, total_price) VALUES (?,?,?,?,?)";

                PreparedStatement ps2 = con.prepareStatement(orderSql);
                ps2.setInt(1, orderGroupId);
                ps2.setInt(2, UserSession.getUserId());
                ps2.setString(3, name);
                ps2.setInt(4, qty);
                ps2.setInt(5, total);
                ps2.executeUpdate();

                // ✅ STOCK UPDATE
                String stockSql =
                    "UPDATE product SET stock = stock - ? WHERE id = ? AND stock >= ?";

                PreparedStatement ps3 = con.prepareStatement(stockSql);
                ps3.setInt(1, qty);
                ps3.setInt(2, productId);
                ps3.setInt(3, qty);

                int updated = ps3.executeUpdate();

                if (updated == 0) {
                    throw new SQLException("Insufficient stock during checkout");
                }
            }

            // ✅ CLEAR CART
            PreparedStatement clear = con.prepareStatement(
                "DELETE FROM cart WHERE user_id = ?");
            clear.setInt(1, UserSession.getUserId());
            clear.executeUpdate();

            con.commit();
            con.setAutoCommit(true);

            System.out.println("\n✔ Order placed successfully!");
            System.out.println("Order ID: " + orderGroupId);

        } catch (Exception e) {
            con.rollback();
            con.setAutoCommit(true);
            System.out.println("❌ Transaction failed. Rolled back.");
        }
    }
}