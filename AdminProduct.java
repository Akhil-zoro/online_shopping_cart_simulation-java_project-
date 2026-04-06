import java.sql.*;
import java.util.Scanner;

public class AdminProduct {

    public static void addProduct() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        System.out.print("Enter Price: ");
        int price = sc.nextInt();

        System.out.print("Enter Stock: ");
        int stock = sc.nextInt();

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO product(name, category, price, stock, original_stock) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setInt(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, stock); // original_stock = initial stock

            ps.executeUpdate();

            System.out.println("✔ Product added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Product ID to delete: ");
        int id = sc.nextInt();

        try {
            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM product WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✔ Product deleted successfully!");
            } else {
                System.out.println("❌ Product not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct() {
    Scanner sc = new Scanner(System.in);

    try {
        Connection con = DBConnection.getConnection();

        System.out.print("Enter Product ID to update: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.println("1. Update Price");
        System.out.println("2. Add Stock");
        System.out.println("3. Change Name");
        System.out.print("Choose option: ");

        int choice = Integer.parseInt(sc.nextLine());

        switch (choice) {

            case 1:
                System.out.print("Enter new price: ");
                int newPrice = Integer.parseInt(sc.nextLine());

                String priceSql = "UPDATE product SET price = ? WHERE id = ?";
                PreparedStatement ps1 = con.prepareStatement(priceSql);
                ps1.setInt(1, newPrice);
                ps1.setInt(2, id);
                ps1.executeUpdate();

                System.out.println("✔ Price updated!");
                break;

            case 2:
                System.out.print("Enter quantity to add: ");
                int addStock = Integer.parseInt(sc.nextLine());

                String stockSql =
                    "UPDATE product SET stock = stock + ?, original_stock = original_stock + ? WHERE id = ?";
                PreparedStatement ps2 = con.prepareStatement(stockSql);
                ps2.setInt(1, addStock);
                ps2.setInt(2, addStock);
                ps2.setInt(3, id);
                ps2.executeUpdate();

                System.out.println("✔ Stock updated!");
                break;

            case 3:
                System.out.print("Enter new name: ");
                String newName = sc.nextLine();

                String nameSql = "UPDATE product SET name = ? WHERE id = ?";
                PreparedStatement ps3 = con.prepareStatement(nameSql);
                ps3.setString(1, newName);
                ps3.setInt(2, id);
                ps3.executeUpdate();

                System.out.println("✔ Name updated!");
                break;

            default:
                System.out.println("Invalid option");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}