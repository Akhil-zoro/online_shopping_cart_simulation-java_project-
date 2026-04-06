import java.sql.*;
import java.util.Scanner;

public class AddressManager {

    // 🔥 VIEW ALL ADDRESSES
    public static void viewAllAddresses() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM address WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, UserSession.getUserId());

            ResultSet rs = ps.executeQuery();

            System.out.println("\n📍 YOUR ADDRESSES:");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(
                    "ID: " + rs.getInt("id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("town") + " - " +
                    rs.getString("pincode")
                );
            }

            if (!found) {
                System.out.println("No addresses found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 ADD ADDRESS
    public static void addAddress() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Phone: ");
        String phone = sc.nextLine();
        // ✅ PHONE VALIDATION
if (!phone.matches("\\d{10}")) {
    System.out.println("❌ Phone number must be exactly 10 digits!");
    return;
}

        System.out.print("Street: ");
        String street = sc.nextLine();

        System.out.print("Town: ");
        String town = sc.nextLine();

        System.out.print("Pincode: ");
        String pincode = sc.nextLine();
        // ✅ PINCODE VALIDATION
if (!pincode.matches("\\d{6}")) {
    System.out.println("❌ Pincode must be exactly 6 digits!");
    return;
}

        if (name.isEmpty() || phone.isEmpty() || street.isEmpty()
                || town.isEmpty() || pincode.isEmpty()) {

            System.out.println("❌ All fields are required!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = """
                INSERT INTO address(user_id, name, phone, street, town, pincode)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, UserSession.getUserId());
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, street);
            ps.setString(5, town);
            ps.setString(6, pincode);

            ps.executeUpdate();

            System.out.println("✔ Address added!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 UPDATE ADDRESS
    public static void updateAddress() {

        viewAllAddresses();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Address ID to update: ");

        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("❌ Invalid input");
            return;
        }

        System.out.print("New Name: ");
        String name = sc.nextLine();

        System.out.print("New Phone: ");
        String phone = sc.nextLine();

        System.out.print("New Street: ");
        String street = sc.nextLine();

        System.out.print("New Town: ");
        String town = sc.nextLine();

        System.out.print("New Pincode: ");
        String pincode = sc.nextLine();

        if (name.isEmpty() || phone.isEmpty() || street.isEmpty()
                || town.isEmpty() || pincode.isEmpty()) {

            System.out.println("❌ All fields required!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = """
                UPDATE address 
                SET name=?, phone=?, street=?, town=?, pincode=?
                WHERE id=? AND user_id=?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, street);
            ps.setString(4, town);
            ps.setString(5, pincode);
            ps.setInt(6, id);
            ps.setInt(7, UserSession.getUserId());

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("✔ Address updated!");
            else
                System.out.println("❌ Invalid ID");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 NEW: GET PINCODE BY ADDRESS ID (FIXED)
    public static String getPincode(int addressId) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT pincode FROM address WHERE id=? AND user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, addressId);
            ps.setInt(2, UserSession.getUserId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("pincode");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔥 CHECK IF ADDRESS EXISTS
    public static boolean hasAddress() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT 1 FROM address WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, UserSession.getUserId());

            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 UPDATED SELECT ADDRESS (WITH VALIDATION + ADD OPTION)
    public static int selectAddress() {

        Scanner sc = new Scanner(System.in);

        while (true) {

            viewAllAddresses();

            System.out.print("Enter Address ID (0 = Add New, -1 = Cancel): ");

            int id;

            try {
                id = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("❌ Invalid input");
                continue;
            }

            if (id == -1) return -1;

            if (id == 0) {
                addAddress();
                continue;
            }

            // 🔥 VALIDATE ADDRESS
            try {
                Connection con = DBConnection.getConnection();

                String sql = "SELECT * FROM address WHERE id=? AND user_id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, UserSession.getUserId());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return id; // ✅ valid
                } else {
                    System.out.println("❌ Invalid Address ID!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 🔥 ADDRESS MENU
    public static void showAddressMenu() {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== ADDRESS ======");
            System.out.println("1. View Addresses");
            System.out.println("2. Add Address");
            System.out.println("3. Update Address");
            System.out.println("4. Back");
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    viewAllAddresses();
                    break;
                case 2:
                    addAddress();
                    break;
                case 3:
                    updateAddress();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}