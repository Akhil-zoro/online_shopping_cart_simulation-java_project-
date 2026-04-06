import java.util.Scanner;

public class AdminAuth {

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin123";

    public static void login() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Admin Email: ");
        String email = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("✔ Admin login successful!");
            AdminMenu.showMenu();
        } else {
            System.out.println("❌ Invalid admin credentials");
        }
    }
}