package HotelSystem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static HotelSystem.UIHelper.*;

public class RoomCalendarView {

    private static final String[] ROOM_TYPES = {"Standard", "Deluxe", "Suite", "Penthouse"};
    private static final String[] ROOM_COLORS = {"#9966ff", "#44aaff", "#44ddff", "#ffcc44"};
    private static final int DAYS_AHEAD = 30;

    public static void show() {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:#060614;-fx-border-color:#44aaff;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect:dropshadow(gaussian,#44aaff,24,0.3,0,0);");

        // Header
        VBox header = new VBox(4);
        header.setPadding(new Insets(24, 32, 16, 32));
        header.setStyle("-fx-background-color:#040f1a;-fx-background-radius:14 14 0 0;");
        header.getChildren().addAll(
                styled(lbl("🏠  ROOM AVAILABILITY CALENDAR", "Georgia", FontWeight.EXTRA_BOLD, 20, "#44bbff"),
                        "-fx-effect:dropshadow(gaussian,#0099cc,8,0.3,0,0);"),
                lbl("Next " + DAYS_AHEAD + " days  ·  " +
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) +
                        " onwards", "Georgia", 11, "#5599bb")
        );

        // Legend
        HBox legend = new HBox(20);
        legend.setPadding(new Insets(10, 32, 10, 32));
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setStyle("-fx-background-color:#051020;");
        legend.getChildren().addAll(
                legendBox("#ff4466", "Booked"),
                legendBox("#44ddaa", "Available"),
                legendBox("#ffcc00", "Checking Out"),
                legendBox("#9966ff", "Checked Out")
        );

        // Calendar Grid
        ScrollPane scroll = buildCalendarGrid();

        // Close button
        HBox footer = new HBox();
        footer.setPadding(new Insets(16, 32, 20, 32));
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setStyle("-fx-background-color:#040f1a;-fx-background-radius:0 0 14 14;");
        Button closeBtn = colorBtn("✕  CLOSE", "#1a0008", "#881133", "#ff4466", 12, false);
        closeBtn.setOnAction(e -> d.close());
        footer.getChildren().add(closeBtn);

        root.getChildren().addAll(header, legend, scroll, footer);

        Scene sc = new Scene(root, 780, 620);
        sc.setFill(Color.TRANSPARENT);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }

    private static ScrollPane buildCalendarGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(12, 32, 12, 32));
        grid.setStyle("-fx-background-color:#060614;");

        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM");

        // Build booking map: "RoomType_Date" -> status
        Map<String, String> bookingMap = buildBookingMap(today);

        // Column headers — dates
        Label cornerLbl = lbl("ROOM \\ DATE", "Georgia", FontWeight.BOLD, 10, "#5599bb");
        cornerLbl.setPrefWidth(90);
        cornerLbl.setPrefHeight(36);
        cornerLbl.setAlignment(Pos.CENTER);
        cornerLbl.setStyle("-fx-background-color:#051020;-fx-border-color:#0a3355;" +
                "-fx-border-width:0.5;-fx-padding:4;");
        grid.add(cornerLbl, 0, 0);

        for (int day = 0; day < DAYS_AHEAD; day++) {
            LocalDate date = today.plusDays(day);
            boolean isToday = date.equals(today);
            boolean isWeekend = date.getDayOfWeek().getValue() >= 6;

            Label dateLbl = new Label(date.format(fmt));
            dateLbl.setFont(javafx.scene.text.Font.font("Georgia",
                    isToday ? FontWeight.BOLD : FontWeight.NORMAL, 10));
            dateLbl.setPrefWidth(56);
            dateLbl.setPrefHeight(36);
            dateLbl.setAlignment(Pos.CENTER);
            dateLbl.setTextFill(Color.web(isToday ? "#44ccff" : isWeekend ? "#9966ff" : "#5599bb"));
            dateLbl.setStyle("-fx-background-color:" + (isToday ? "#051830" : "#040f1a") +
                    ";-fx-border-color:#0a3355;-fx-border-width:0.5;-fx-padding:4;");
            grid.add(dateLbl, day + 1, 0);
        }

        // Rows — room types
        for (int r = 0; r < ROOM_TYPES.length; r++) {
            String roomType = ROOM_TYPES[r];
            String roomColor = ROOM_COLORS[r];

            // Room label
            Label roomLbl = new Label(roomType);
            roomLbl.setFont(javafx.scene.text.Font.font("Georgia", FontWeight.BOLD, 11));
            roomLbl.setPrefWidth(90);
            roomLbl.setPrefHeight(40);
            roomLbl.setAlignment(Pos.CENTER);
            roomLbl.setTextFill(Color.web(roomColor));
            roomLbl.setStyle("-fx-background-color:#051020;-fx-border-color:#0a3355;" +
                    "-fx-border-width:0.5;-fx-padding:4;");
            grid.add(roomLbl, 0, r + 1);

            // Day cells
            for (int day = 0; day < DAYS_AHEAD; day++) {
                LocalDate date = today.plusDays(day);
                String key = roomType + "_" + date.toString();
                String status = bookingMap.getOrDefault(key, "Available");

                String bg, tc, text;
                switch (status) {
                    case "Booked"       -> { bg = "#3a0011"; tc = "#ff4466"; text = "●"; }
                    case "Checking Out" -> { bg = "#2a1a00"; tc = "#ffcc00"; text = "◐"; }
                    case "Checked Out"  -> { bg = "#1a0033"; tc = "#9966ff"; text = "○"; }
                    default             -> { bg = "#003322"; tc = "#44ddaa"; text = "✓"; }
                }

                Label cell = new Label(text);
                cell.setFont(javafx.scene.text.Font.font(14));
                cell.setPrefWidth(56);
                cell.setPrefHeight(40);
                cell.setAlignment(Pos.CENTER);
                cell.setTextFill(Color.web(tc));
                cell.setStyle("-fx-background-color:" + bg + ";-fx-border-color:#0a1a1a;" +
                        "-fx-border-width:0.5;");

                // Tooltip with guest name if booked
                String ttKey = roomType + "_" + date.toString() + "_guest";
                if (bookingMap.containsKey(ttKey)) {
                    Tooltip tt = new Tooltip(bookingMap.get(ttKey));
                    tt.setStyle("-fx-font-family:'Georgia';-fx-font-size:11;");
                    Tooltip.install(cell, tt);
                }

                grid.add(cell, day + 1, r + 1);
            }
        }

        ScrollPane sp = new ScrollPane(grid);
        sp.setFitToHeight(true);
        sp.setPrefHeight(420);
        sp.setStyle("-fx-background-color:#060614;-fx-background:transparent;");
        return sp;
    }

    private static Map<String, String> buildBookingMap(LocalDate today) {
        Map<String, String> map = new HashMap<>();
        LocalDate endDate = today.plusDays(DAYS_AHEAD);

        for (Booking b : AppContext.bookingList) {
            if (b.getStatus().equals("Cancelled")) continue;
            try {
                LocalDate ci = LocalDate.parse(b.getCheckIn());
                LocalDate co = LocalDate.parse(b.getCheckOut());
                LocalDate cur = ci;
                while (!cur.isAfter(co) && !cur.isAfter(endDate)) {
                    if (!cur.isBefore(today)) {
                        String key = b.getRoomType() + "_" + cur.toString();
                        String status = cur.equals(co)
                                ? (b.getStatus().equals("Checked Out") ? "Checked Out" : "Checking Out")
                                : "Booked";
                        map.put(key, status);
                        map.put(key + "_guest", b.getGuestName() + "\n" + b.getBookingId());
                    }
                    cur = cur.plusDays(1);
                }
            } catch (Exception ignored) {}
        }
        return map;
    }

    private static HBox legendBox(String color, String label) {
        HBox box = new HBox(6);
        box.setAlignment(Pos.CENTER_LEFT);
        Label dot = new Label("■");
        dot.setFont(javafx.scene.text.Font.font(14));
        dot.setTextFill(Color.web(color));
        Label txt = lbl(label, "Georgia", 11, "#7799bb");
        box.getChildren().addAll(dot, txt);
        return box;
    }
}