import java.sql.*;
import java.util.Scanner;

public class ViewCart {

    public static void showCart() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = """
                SELECT 
                    p.id,
                    p.name,
                    p.price,
                    c.quantity,
                    TIMESTAMPDIFF(SECOND, NOW(), 
                        c.reserved_at + INTERVAL ? MINUTE) AS time_left
                FROM product p
                JOIN cart c ON p.id = c.product_id
                WHERE c.user_id = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ReservationConfig.RESERVATION_MINUTES);
            ps.setInt(2, UserSession.getUserId());

            ResultSet rs = ps.executeQuery();

            int grandTotal = 0;
            boolean empty = true;
            boolean hasActiveItems = false; // 🔥 IMPORTANT

            System.out.println("\n========= YOUR CART =========");
            System.out.println("ID | NAME | PRICE | QTY | TOTAL | STATUS");

            while (rs.next()) {
                empty = false;

                int id = rs.getInt("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int qty = rs.getInt("quantity");
                int timeLeft = rs.getInt("time_left");

                int total = price * qty;
                grandTotal += total;

                if (timeLeft > 0) {
                    hasActiveItems = true; // 🔥 track active items

                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;

                    System.out.println(
                        id + " | " + name + " | Rs." + price +
                        " | " + qty + " | Rs." + total +
                        " | ⏳ Expires in " + minutes + "m " + seconds + "s"
                    );
                } else {
                    System.out.println(
                        id + " | " + name + " | Rs." + price +
                        " | " + qty + " | Rs." + total +
                        " | ❌ Reservation expired"
                    );
                }
            }

            if (empty) {
                System.out.println("❌ Cart is empty!");
                return;
            }

            System.out.println("-----------------------------------------");
            System.out.println("Subtotal: Rs." + grandTotal);

            // 🔥 NEW LOGIC
            if (!hasActiveItems) {
                System.out.println("\n❌ All reservations expired.");
                System.out.println("1. Back to Menu");

                Scanner sc = new Scanner(System.in);
                sc.nextLine(); // wait for input
                return;
            }

            cartOptions();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cartOptions() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n1. Checkout");
        System.out.println("2. Remove Item");
        System.out.println("3. Back to Menu");
        System.out.print("Choose option: ");

        String input = sc.nextLine();

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("❌ Invalid input");
            return;
        }

        switch (choice) {
            case 1:
                Billing.checkout();
                break;
            case 2:
                RemoveFromCart.remove();
                break;
            case 3:
                return; // 🔥 don't call menu again
            default:
                System.out.println("Invalid option");
        }
    }
}