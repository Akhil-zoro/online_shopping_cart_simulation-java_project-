public class UserSession {

    private static int userId = -1;
    private static String name = "";
    private static String email = "";
    private static String role = "customer"; // 🔥 NEW

    public static void setUser(int id, String userName, String userEmail) {
        userId = id;
        name = userName;
        email = userEmail;
    }

    // 🔥 NEW METHOD
    public static void setRole(String userRole) {
        role = userRole;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getName() {
        return name;
    }

    public static String getEmail() {
        return email;
    }

    public static String getRole() {
        return role;
    }

    // 🔥 NEW METHOD
    public static boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public static boolean isLoggedIn() {
        return userId != -1;
    }

    public static void logout() {
        userId = -1;
        name = "";
        email = "";
        role = "customer"; // reset
    }
}