package HotelSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OOP Concepts:
 *   - INTERFACE      : implements Saveable
 *   - THREADING      : background Thread for autoSave
 *   - SYNCHRONIZATION: synchronized blocks prevent file corruption
 *   - ENCAPSULATION  : file paths as private constants
 */
public class DatabaseManager implements Saveable {

    private static final String USERS_FILE    = "hotel_users.dat";
    private static final String BOOKINGS_FILE = "hotel_bookings.dat";

    // Thread-safe counter — AtomicInteger (no sync needed)
    private static final AtomicInteger activeSaves = new AtomicInteger(0);

    // ── Saveable Interface @Override methods ──────────────────────────
    @Override
    public void save() { saveBookings(); }

    @Override
    public void load() { loadBookings(); }

    // ══════════════════════════════════════════════════════════════════
    //  USER DATABASE
    // ══════════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public static void loadUsers() {
        File f = new File(USERS_FILE);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(f))) {
                AppContext.USERS = (HashMap<String, String[]>) ois.readObject();
                return;
            } catch (Exception ignored) {}
        }
        AppContext.USERS.put("admin",   new String[]{"admin123", "ADMIN"});
        AppContext.USERS.put("manager", new String[]{"mgr456",   "ADMIN"});
        AppContext.USERS.put("staff",   new String[]{"staff789", "USER"});
        AppContext.USERS.put("guest",   new String[]{"guest000", "USER"});
        saveUsers();
    }

    public static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(USERS_FILE))) {
            oos.writeObject(new HashMap<>(AppContext.USERS));
        } catch (IOException ignored) {}
    }

    // ══════════════════════════════════════════════════════════════════
    //  AUTO SAVE — Background Thread + Synchronization
    // ══════════════════════════════════════════════════════════════════
    public static void autoSave() {

        // Thread-safe snapshot of list
        ArrayList<Booking> snapshot;
        synchronized (AppContext.bookingList) {
            snapshot = new ArrayList<>(AppContext.bookingList);
        }

        activeSaves.incrementAndGet();

        Thread saveThread = new Thread(() -> {
            try {
                synchronized (DatabaseManager.class) {
                    try (ObjectOutputStream oos = new ObjectOutputStream(
                            new FileOutputStream(BOOKINGS_FILE))) {
                        oos.writeObject(snapshot);
                    }
                }
            } catch (IOException ignored) {
            } finally {
                activeSaves.decrementAndGet();
            }
        });

        saveThread.setName("BookingSaveThread-" + activeSaves.get());
        saveThread.setDaemon(true);
        saveThread.start();
    }

    // ══════════════════════════════════════════════════════════════════
    //  LOAD BOOKINGS
    // ══════════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    public static void loadBookings() {
        File f = new File(BOOKINGS_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(f))) {
            ArrayList<Booking> loaded = (ArrayList<Booking>) ois.readObject();
            AppContext.bookingList.setAll(loaded);
            if (!loaded.isEmpty()) {
                AppContext.bookingCounter = loaded.stream()
                        .mapToInt(b -> {
                            try { return Integer.parseInt(
                                    b.getBookingId().replace("BK-", "")); }
                            catch (Exception e) { return 1000; }
                        }).max().orElse(1000) + 1;
            }
        } catch (Exception e) {
            AppContext.showMsg("⚠  Could not load database.", "#ffcc00");
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  MANUAL SAVE (Admin button)
    // ══════════════════════════════════════════════════════════════════
    public static void saveBookings() {
        if (!"ADMIN".equals(AppContext.currentRole)) {
            AppContext.showMsg("⚠  Admin access required!", "#ff4466");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(new ArrayList<>(AppContext.bookingList));
            AppContext.showAdminMsg(
                    "💾  " + AppContext.bookingList.size() + " bookings saved!",
                    "#44aaff");
        } catch (IOException e) {
            AppContext.showAdminMsg(
                    "⚠  Save failed: " + e.getMessage(), "#ff4466");
        }
    }

    public static int getActiveSaves() { return activeSaves.get(); }
}