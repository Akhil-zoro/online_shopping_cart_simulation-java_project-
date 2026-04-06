import java.util.Scanner;

public class AccountManager {

    public static void showAccount() {

        if (!UserSession.isLoggedIn()) {
            System.out.println("❌ Please login first!");
            return;
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== ACCOUNT ======");
            System.out.println("Name  : " + UserSession.getName());
            System.out.println("Email : " + UserSession.getEmail());

            System.out.println("\n1. Order History");
            System.out.println("2. View Favorites");
            System.out.println("3. Manage Address");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");
            System.out.println("6. Back");

            System.out.print("Choose option: ");
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
                    OrderHistory.showHistory();
                    break;

                case 2:
                    Favorites.view(); // 🔥 must match your file
                    break;

                case 3:
                    AddressManager.showAddressMenu(); // 🔥 must match your file
                    break;

                case 4:
                    changePassword(sc);
                    break;

                case 5:
                    UserSession.logout();
                    System.out.println("✔ Logged out successfully!");
                    return;

                case 6:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void changePassword(Scanner sc) {

        try {
            System.out.print("Enter current password: ");
            String current = sc.nextLine();

            if (!Auth.checkPassword(current)) {
                System.out.println("❌ Incorrect password!");
                return;
            }

            System.out.print("Enter new password: ");
            String newPass = sc.nextLine();

            System.out.print("Confirm new password: ");
            String confirm = sc.nextLine();

            if (!newPass.equals(confirm)) {
                System.out.println("❌ Password mismatch!");
                return;
            }

            if (newPass.length() < 4) {
                System.out.println("❌ Password too short!");
                return;
            }

            Auth.updatePassword(newPass);

            System.out.println("✔ Password updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}