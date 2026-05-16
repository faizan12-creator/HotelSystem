
package HotelSystem;

public class AuthController {

    // ── Login Validation ──────────────────────────────────────────────
    public static String login(String username, String password) {
        String[] creds = AppContext.USERS.get(username);
        if (creds == null || !creds[0].equals(password)) {
            return "⚠  Invalid username or password!";
        }
        AppContext.currentUser = username;
        AppContext.currentRole = creds[1];
        return null; // null = success
    }

    // ── Register Validation ───────────────────────────────────────────
    public static String register(String fullName, String username, String password,
                                  String confirm, String phone) {
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || phone.isEmpty())
            return "⚠  Fill all required fields!";
        if (username.contains(" "))
            return "⚠  Username cannot contain spaces!";
        if (username.length() < 4)
            return "⚠  Username min 4 characters!";
        if (password.length() < 6)
            return "⚠  Password min 6 characters!";
        if (!password.equals(confirm))
            return "⚠  Passwords do not match!";
        if (!phone.matches("\\d{10,13}"))
            return "⚠  Phone must be 10–13 digits!";
        if (AppContext.USERS.containsKey(username))
            return "⚠  Username already exists!";

        // Save new user
        AppContext.USERS.put(username, new String[]{password, "USER", fullName, phone});
        DatabaseManager.saveUsers();
        return null; // null = success
    }

    // ── Launch after login ────────────────────────────────────────────
    public static void launchMainApp() {
        DatabaseManager.loadBookings();
        if ("ADMIN".equals(AppContext.currentRole)) {
            AdminView.show();
        } else {
            UserView.show();
        }
    }
}