import java.util.Scanner;

public class AdminMenu {

    public static void showMenu() {

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== ADMIN MENU ======");
            System.out.println("1. Add Product");
            System.out.println("2. Delete Product");
            System.out.println("3. Update Product"); // 🔥 NEW
            System.out.println("4. Reset System");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    AdminProduct.addProduct();
                    break;
                case 2:
                    AdminProduct.deleteProduct();
                    break;
                case 3:
                    AdminProduct.updateProduct();
                    break;

                case 4:
                    AdminReset.resetSystem();
                    break;

                case 5:
                    return;
                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 4);
    }
}