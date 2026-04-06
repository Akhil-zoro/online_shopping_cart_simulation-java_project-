import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== WELCOME ======");
            System.out.println("1. Customer Login");
            System.out.println("2. Register");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            String input = sc.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Enter a number.");
                continue; // 🔥 prevents crash
            }

            switch (choice) {
                case 1:
                    Auth.login();
                    break;
                case 2:
                    Auth.register();
                    break;
                case 3:
                    AdminAuth.login();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    System.exit(0); // 🔥 clean exit
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}