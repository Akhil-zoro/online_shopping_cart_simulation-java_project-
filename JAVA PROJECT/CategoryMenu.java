import java.sql.*;
import java.util.*;

public class CategoryMenu {

    public static void showMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n====== MAIN MENU ======");
                System.out.println("1. Browse Categories");
                System.out.println("2. Search Product");
                System.out.println("3. View Cart");
                System.out.println("4. Account");
                System.out.println("5. Exit");

                if (UserSession.isAdmin()) {
                    System.out.println("6. Admin Reset");
                }

                System.out.print("Enter your choice: ");

                String input = sc.nextLine();

                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (Exception e) {
                    System.out.println("❌ Invalid input");
                    continue;
                }

                switch (choice) {

                    case 1:
                        showCategories(sc);
                        break;

                    case 2:
                        searchProduct(sc);
                        break;

                    case 3:
                        ViewCart.showCart();
                        break;

                    case 4:
    if (!UserSession.isLoggedIn()) {

        System.out.println("\n====== ACCOUNT ======");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Back");
        System.out.print("Choose option: ");

        String input2 = sc.nextLine();

        int opt;
        try {
            opt = Integer.parseInt(input2);
        } catch (Exception e) {
            System.out.println("❌ Invalid input");
            break;
        }

        switch (opt) {
            case 1:
                Auth.login();
                break;
            case 2:
                Auth.register();
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice");
        }

    } else {
        AccountManager.showAccount();
    }
    break;

                    case 5:
                        System.out.println("Thank you for shopping!");
                        System.exit(0);

                    case 6:
                        if (UserSession.isAdmin()) {
                            AdminReset.resetSystem();
                        } else {
                            System.out.println("Invalid choice");
                        }
                        break;

                    default:
                        System.out.println("Invalid choice");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void showCategories(Scanner sc) {

        while (true) {
            try {
                Connection con = DBConnection.getConnection();

                String sql = "SELECT DISTINCT category FROM product ORDER BY category";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                List<String> categories = new ArrayList<>();

                System.out.println("\n====== CATEGORIES ======");

                int index = 1;
                while (rs.next()) {
                    String cat = rs.getString("category");
                    categories.add(cat);
                    System.out.println(index + ". " + cat);
                    index++;
                }

                System.out.println(index + ". Back");
                System.out.print("Choose category: ");

                String input = sc.nextLine();

                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (Exception e) {
                    System.out.println("❌ Invalid input");
                    continue;
                }

                if (choice >= 1 && choice <= categories.size()) {
                    String selected = categories.get(choice - 1);
                    ProductByCategory.show(selected);
                    postProductOptions(sc);
                } else if (choice == index) {
                    return;
                } else {
                    System.out.println("❌ Invalid choice");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void searchProduct(Scanner sc) {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("\nEnter product name to search: ");
            String keyword = sc.nextLine();

            String sql = "SELECT * FROM product WHERE LOWER(name) LIKE LOWER(?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n====== SEARCH RESULTS ======");

            boolean found = false;

            while (rs.next()) {
                found = true;

                int stock = rs.getInt("stock");

                String status;
                if (stock <= 0) {
                    status = "❌ Out of Stock";
                } else if (stock <= 5) {
                    status = "⚠ Only few left";
                } else {
                    status = "✔ In Stock";
                }

                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | ₹" +
                                rs.getInt("price") + " | " +
                                status
                );
            }

            if (!found) {
                System.out.println("No products found.");
                return;
            }

            System.out.println("\n1. Add to Cart");
            System.out.println("2. Back");
            System.out.print("Choose option: ");

            int opt = Integer.parseInt(sc.nextLine());

            if (opt == 1) {
                AddToCart.add();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void postProductOptions(Scanner sc) {
        System.out.println("\n1. Add to Cart");
        System.out.println("2. Add to Favorites ❤️");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose option: ");

        int opt = Integer.parseInt(sc.nextLine());

        if (opt == 1) {
            AddToCart.add();
        } else if (opt == 2) {
            Favorites.add();
        }
    }
}