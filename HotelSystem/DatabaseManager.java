package HotelSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager {

    private static final String USERS_FILE    = "hotel_users.dat";
    private static final String BOOKINGS_FILE = "hotel_bookings.dat";

    // ══════════════════════════════════════════════════════════════════
    //  USER DATABASE
    // ══════════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public static void loadUsers() {
        File f = new File(USERS_FILE);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                AppContext.USERS = (HashMap<String, String[]>) ois.readObject();
                return;
            } catch (Exception ignored) {}
        }
        // Default accounts if no file exists
        AppContext.USERS.put("admin",   new String[]{"admin123", "ADMIN"});
        AppContext.USERS.put("manager", new String[]{"mgr456",   "ADMIN"});
        AppContext.USERS.put("staff",   new String[]{"staff789", "USER"});
        AppContext.USERS.put("guest",   new String[]{"guest000", "USER"});
        saveUsers();
    }

    public static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(new HashMap<>(AppContext.USERS));
        } catch (IOException ignored) {}
    }

    // ══════════════════════════════════════════════════════════════════
    //  BOOKINGS DATABASE
    // ══════════════════════════════════════════════════════════════════
    public static void autoSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(new ArrayList<>(AppContext.bookingList));
        } catch (IOException ignored) {}
    }

    @SuppressWarnings("unchecked")
    public static void loadBookings() {
        File f = new File(BOOKINGS_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            ArrayList<Booking> loaded = (ArrayList<Booking>) ois.readObject();
            AppContext.bookingList.setAll(loaded);
            if (!loaded.isEmpty()) {
                AppContext.bookingCounter = loaded.stream()
                        .mapToInt(b -> {
                            try { return Integer.parseInt(b.getBookingId().replace("BK-", "")); }
                            catch (Exception e) { return 1000; }
                        }).max().orElse(1000) + 1;
            }
        } catch (Exception e) {
            AppContext.showMsg("⚠  Could not load database.", "#ffcc00");
        }
    }

    public static void saveBookings() {
        if (!"ADMIN".equals(AppContext.currentRole)) {
            AppContext.showMsg("⚠  Admin access required!", "#ff4466");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(new ArrayList<>(AppContext.bookingList));
            AppContext.showAdminMsg("💾  " + AppContext.bookingList.size() + " bookings saved!", "#44aaff");
        } catch (IOException e) {
            AppContext.showAdminMsg("⚠  Save failed: " + e.getMessage(), "#ff4466");
        }
    }
}