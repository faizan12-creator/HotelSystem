
package HotelSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class BookingController {

    // ══════════════════════════════════════════════════════════════════
    //  ADMIN — BOOK ROOM
    // ══════════════════════════════════════════════════════════════════
    public static void adminBookRoom(String name, String phone, String email,
                                     String room, String ci, String co) {
        // Validate
        if (name.isEmpty() || phone.isEmpty() || room == null || ci.isEmpty() || co.isEmpty()) {
            AppContext.showAdminMsg("⚠  Fill all required (*) fields!", "#ffcc00"); return;
        }
        if (!phone.matches("\\d{10,13}")) {
            AppContext.showAdminMsg("⚠  Phone must be 10–13 digits!", "#ff4466"); return;
        }
        try {
            LocalDate ciD = LocalDate.parse(ci), coD = LocalDate.parse(co);
            if (!coD.isAfter(ciD))             { AppContext.showAdminMsg("⚠  Check-out must be after check-in!", "#ff4466"); return; }
            if (ciD.isBefore(LocalDate.now())) { AppContext.showAdminMsg("⚠  Check-in cannot be in the past!", "#ff4466"); return; }
        } catch (Exception e) {
            AppContext.showAdminMsg("⚠  Invalid date! Use YYYY-MM-DD", "#ff4466"); return;
        }
        String id = "BK-" + (AppContext.bookingCounter++);
        Booking b = new Booking(id, name, room, ci, co, AppContext.getPrice(room),
                phone, email, AppContext.currentUser);
        AppContext.bookingList.add(b);
        AppContext.updateStats();
        DatabaseManager.autoSave();
        AppContext.showAdminMsg("✔  Booking " + id + " confirmed for " + name + "!", "#9966ff");
    }

    // ══════════════════════════════════════════════════════════════════
    //  ADMIN — CHECK OUT
    // ══════════════════════════════════════════════════════════════════
    public static void adminCheckOut(Booking sel) {
        if (sel == null)                            { AppContext.showAdminMsg("⚠  Select a booking!", "#ffcc00"); return; }
        if (sel.getStatus().equals("Cancelled"))    { AppContext.showAdminMsg("⚠  Booking is cancelled!", "#ff4466"); return; }
        if (sel.getStatus().equals("Checked Out"))  { AppContext.showAdminMsg("⚠  Already checked out!", "#ff4466"); return; }
        sel.setStatus("Checked Out");
        if (AppContext.adminTable != null) AppContext.adminTable.refresh();
        AppContext.updateStats();
        DatabaseManager.autoSave();
        AppContext.showAdminMsg("🚪  " + sel.getGuestName() + " checked out.", "#44ddaa");
    }

    // ══════════════════════════════════════════════════════════════════
    //  ADMIN — CANCEL BOOKING
    // ══════════════════════════════════════════════════════════════════
    public static void adminCancelBooking(Booking sel) {
        if (sel == null)                          { AppContext.showAdminMsg("⚠  Select a booking!", "#ffcc00"); return; }
        if (sel.getStatus().equals("Cancelled"))  { AppContext.showAdminMsg("⚠  Already cancelled!", "#ff4466"); return; }
        sel.setStatus("Cancelled");
        if (AppContext.adminTable != null) AppContext.adminTable.refresh();
        AppContext.updateStats();
        DatabaseManager.autoSave();
        AppContext.showAdminMsg("❌  Booking " + sel.getBookingId() + " cancelled.", "#ff4466");
    }

    // ══════════════════════════════════════════════════════════════════
    //  USER — BOOK ROOM
    // ══════════════════════════════════════════════════════════════════
    public static void userBookRoom(String name, String phone, String email,
                                    String room, String ci, String co) {
        if (name.isEmpty() || phone.isEmpty() || room == null || ci.isEmpty() || co.isEmpty()) {
            AppContext.showUserMsg("⚠  Fill all required (*) fields!", "#ffcc00"); return;
        }
        if (!phone.matches("\\d{10,13}")) {
            AppContext.showUserMsg("⚠  Phone must be 10–13 digits!", "#ff4466"); return;
        }
        try {
            LocalDate ciD = LocalDate.parse(ci), coD = LocalDate.parse(co);
            if (!coD.isAfter(ciD))             { AppContext.showUserMsg("⚠  Check-out must be after check-in!", "#ff4466"); return; }
            if (ciD.isBefore(LocalDate.now())) { AppContext.showUserMsg("⚠  Check-in cannot be in the past!", "#ff4466"); return; }
        } catch (Exception e) {
            AppContext.showUserMsg("⚠  Invalid date! Use YYYY-MM-DD", "#ff4466"); return;
        }
        String id = "BK-" + (AppContext.bookingCounter++);
        Booking b = new Booking(id, name, room, ci, co, AppContext.getPrice(room),
                phone, email, AppContext.currentUser);
        AppContext.bookingList.add(b);
        refreshUserTable();
        DatabaseManager.autoSave();
        AppContext.showUserMsg("✅  Booking " + id + " confirmed! Rs " +
                String.format("%,.0f", b.getTotalPrice()) + " total.", "#44ccff");
    }

    // ══════════════════════════════════════════════════════════════════
    //  USER — CHECK OUT
    // ══════════════════════════════════════════════════════════════════
    public static void userCheckOut(Booking sel) {
        if (sel == null)                           { AppContext.showUserMsg("⚠  Select a booking!", "#ffcc00"); return; }
        if (sel.getStatus().equals("Cancelled"))   { AppContext.showUserMsg("⚠  Booking was cancelled.", "#ff4466"); return; }
        if (sel.getStatus().equals("Checked Out")) { AppContext.showUserMsg("⚠  Already checked out!", "#ff4466"); return; }
        sel.setStatus("Checked Out");
        refreshUserTable();
        DatabaseManager.autoSave();
        AppContext.showUserMsg("🚪  Checked out. Thank you, " + sel.getGuestName() + "!", "#44ddaa");
    }

    // ══════════════════════════════════════════════════════════════════
    //  PROCESS PAYMENT
    // ══════════════════════════════════════════════════════════════════
    public static void processPayment(Booking sel, String method) {
        sel.setPayment("Paid");
        sel.setPaymentMethod(method);
        if ("ADMIN".equals(AppContext.currentRole)) {
            if (AppContext.adminTable != null) AppContext.adminTable.refresh();
            AppContext.updateStats();
        } else {
            refreshUserTable();
        }
        DatabaseManager.autoSave();
        AppContext.showMsg("💳  Rs " + String.format("%,.0f", sel.getTotalPrice()) +
                " paid via " + method, "#ffcc00");
    }

    // ══════════════════════════════════════════════════════════════════
    //  FILTER HELPERS
    // ══════════════════════════════════════════════════════════════════
    public static ObservableList<Booking> filterAdmin(String q) {
        if (q == null || q.isEmpty()) return AppContext.bookingList;
        ObservableList<Booking> f = FXCollections.observableArrayList();
        for (Booking b : AppContext.bookingList)
            if (b.getGuestName().toLowerCase().contains(q.toLowerCase()) ||
                    b.getBookingId().toLowerCase().contains(q.toLowerCase()) ||
                    b.getPhone().contains(q)) f.add(b);
        return f;
    }

    public static ObservableList<Booking> filterUser(String q) {
        ObservableList<Booking> base = FXCollections.observableArrayList();
        for (Booking b : AppContext.bookingList)
            if (b.getBookedBy().equals(AppContext.currentUser)) base.add(b);
        if (q == null || q.isEmpty()) return base;
        ObservableList<Booking> f = FXCollections.observableArrayList();
        for (Booking b : base)
            if (b.getGuestName().toLowerCase().contains(q.toLowerCase()) ||
                    b.getBookingId().toLowerCase().contains(q.toLowerCase())) f.add(b);
        return f;
    }

    // ══════════════════════════════════════════════════════════════════
    //  REFRESH USER TABLE
    // ══════════════════════════════════════════════════════════════════
    public static void refreshUserTable() {
        if (AppContext.userTable == null) return;
        ObservableList<Booking> mine = FXCollections.observableArrayList();
        for (Booking b : AppContext.bookingList)
            if (b.getBookedBy().equals(AppContext.currentUser)) mine.add(b);
        AppContext.userTable.setItems(mine);
        AppContext.userTable.refresh();
    }
}