package HotelSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AppContext {

    // ── Primary Stage ─────────────────────────────────────────────────
    public static Stage primaryStage;

    // ── Session ───────────────────────────────────────────────────────
    public static String currentUser = "";
    public static String currentRole = "";

    // ── User Store ────────────────────────────────────────────────────
    public static Map<String, String[]> USERS = new HashMap<>();

    // ── Data Lists ────────────────────────────────────────────────────
    public static ObservableList<Booking>          bookingList       = FXCollections.observableArrayList();
    public static ObservableList<Feedback>         feedbackList      = FXCollections.observableArrayList();
    public static ObservableList<RoomServiceOrder> serviceOrders     = FXCollections.observableArrayList();

    // ── Counters ──────────────────────────────────────────────────────
    public static int bookingCounter      = 1001;
    public static int feedbackCounter     = 1;
    public static int serviceOrderCounter = 1;

    // ── Admin UI References (for stat updates & messages) ─────────────
    public static Label msgLabel;
    public static Label totalBookings;
    public static Label totalRevenue;
    public static Label confirmedLbl;
    public static Label pendingLbl;
    public static TableView<Booking> adminTable;

    // ── User UI References ────────────────────────────────────────────
    public static Label uMsgLabel;
    public static TableView<Booking> userTable;

    // ── Room Price Helper ─────────────────────────────────────────────
    public static double getPrice(String type) {
        return switch (type) {
            case "Deluxe"    -> 9000;
            case "Suite"     -> 15000;
            case "Penthouse" -> 25000;
            default          -> 5000;
        };
    }

    // ── Message Helpers ───────────────────────────────────────────────
    public static void showMsg(String msg, String color) {
        if ("ADMIN".equals(currentRole)) showAdminMsg(msg, color);
        else                             showUserMsg(msg, color);
    }

    public static void showAdminMsg(String msg, String color) {
        if (msgLabel != null) {
            msgLabel.setText(msg);
            msgLabel.setStyle(msgLabel.getStyle() + "-fx-text-fill:" + color + ";");
            javafx.scene.paint.Color c = javafx.scene.paint.Color.web(color);
            msgLabel.setTextFill(c);
        }
    }

    public static void showUserMsg(String msg, String color) {
        if (uMsgLabel != null) {
            uMsgLabel.setText(msg);
            uMsgLabel.setTextFill(javafx.scene.paint.Color.web(color));
        }
    }

    // ── Stat Update ───────────────────────────────────────────────────
    public static void updateStats() {
        if (totalBookings == null) return;
        totalBookings.setText(String.valueOf(bookingList.size()));
        double rev = bookingList.stream()
                .filter(b -> b.getPayment().equals("Paid"))
                .mapToDouble(Booking::getTotalPrice).sum();
        totalRevenue.setText("Rs " + String.format("%,.0f", rev));
        long conf    = bookingList.stream().filter(b -> b.getStatus().equals("Confirmed")).count();
        long pending = bookingList.stream().filter(b -> b.getPayment().equals("Pending")).count();
        confirmedLbl.setText(String.valueOf(conf));
        pendingLbl.setText(String.valueOf(pending));
    }
}