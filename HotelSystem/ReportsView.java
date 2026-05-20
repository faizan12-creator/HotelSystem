package HotelSystem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

        import static HotelSystem.UIHelper.*;

public class ReportsView {

    public static void show() {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:#06060f;-fx-border-color:#ffcc00;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect:dropshadow(gaussian,#ffcc00,24,0.3,0,0);");

        // ── Header ─────────────────────────────────────────────────────
        VBox header = new VBox(4);
        header.setPadding(new Insets(24, 32, 16, 32));
        header.setStyle("-fx-background-color:#0d0d00;-fx-background-radius:14 14 0 0;");
        header.getChildren().addAll(
                styled(lbl("📊  ADMIN REVENUE REPORTS", "Georgia", FontWeight.EXTRA_BOLD, 22, "#ffcc00"),
                        "-fx-effect:dropshadow(gaussian,#ffcc00,8,0.3,0,0);"),
                lbl("Booking statistics, revenue breakdown and room performance",
                        "Georgia", 11, "#cc9900")
        );

        // ── Summary Cards ──────────────────────────────────────────────
        HBox summaryRow = buildSummaryCards();
        summaryRow.setPadding(new Insets(16, 32, 8, 32));
        summaryRow.setStyle("-fx-background-color:#0a0a00;");

        // ── Tab Pane for different charts ──────────────────────────────
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setStyle("-fx-background-color:#08080f;-fx-tab-min-height:36;" +
                "-fx-font-family:'Georgia';-fx-font-size:12;");

        Tab revenueTab  = new Tab("💰  Revenue by Room");
        Tab bookingsTab = new Tab("📋  Bookings Overview");
        Tab paymentTab  = new Tab("💳  Payment Methods");
        Tab monthlyTab  = new Tab("📅  Monthly Trend");

        revenueTab.setContent(buildRevenueChart());
        bookingsTab.setContent(buildBookingsOverview());
        paymentTab.setContent(buildPaymentChart());
        monthlyTab.setContent(buildMonthlyTrend());

        tabs.getTabs().addAll(revenueTab, bookingsTab, paymentTab, monthlyTab);
        VBox.setVgrow(tabs, Priority.ALWAYS);

        // ── Footer ─────────────────────────────────────────────────────
        HBox footer = new HBox();
        footer.setPadding(new Insets(14, 32, 20, 32));
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setStyle("-fx-background-color:#0d0d00;-fx-background-radius:0 0 14 14;");
        Button closeBtn = colorBtn("✕  CLOSE", "#1a0008", "#881133", "#ff4466", 12, false);
        closeBtn.setOnAction(e -> d.close());
        footer.getChildren().add(closeBtn);

        root.getChildren().addAll(header, summaryRow, tabs, footer);

        Scene sc = new Scene(root, 860, 660);
        sc.setFill(Color.TRANSPARENT);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }

    // ══════════════════════════════════════════════════════════════════
    //  SUMMARY CARDS
    // ══════════════════════════════════════════════════════════════════
    private static HBox buildSummaryCards() {
        long total     = AppContext.bookingList.size();
        long confirmed = AppContext.bookingList.stream().filter(b -> b.getStatus().equals("Confirmed")).count();
        long checkedOut= AppContext.bookingList.stream().filter(b -> b.getStatus().equals("Checked Out")).count();
        long cancelled = AppContext.bookingList.stream().filter(b -> b.getStatus().equals("Cancelled")).count();
        double totalRev= AppContext.bookingList.stream().filter(b -> b.getPayment().equals("Paid")).mapToDouble(Booking::getTotalPrice).sum();
        double pendRev = AppContext.bookingList.stream().filter(b -> b.getPayment().equals("Pending") && !b.getStatus().equals("Cancelled")).mapToDouble(Booking::getTotalPrice).sum();

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(
                summaryCard("TOTAL\nBOOKINGS",  String.valueOf(total),                        "#9966ff"),
                summaryCard("CONFIRMED",         String.valueOf(confirmed),                     "#44ddaa"),
                summaryCard("CHECKED OUT",       String.valueOf(checkedOut),                    "#44aaff"),
                summaryCard("CANCELLED",         String.valueOf(cancelled),                     "#ff4466"),
                summaryCard("REVENUE\nCOLLECTED","Rs "+String.format("%,.0f",totalRev),         "#ffcc00"),
                summaryCard("PENDING\nREVENUE",  "Rs "+String.format("%,.0f",pendRev),          "#ffaa33")
        );
        return row;
    }

    private static VBox summaryCard(String title, String value, String color) {
        Label tLbl = lbl(title, "Georgia", 9, "#888866");
        tLbl.setWrapText(true);
        tLbl.setAlignment(Pos.CENTER);
        Label vLbl = lbl(value, "Georgia", FontWeight.EXTRA_BOLD, 15, color);
        vLbl.setWrapText(true);
        vLbl.setAlignment(Pos.CENTER);
        vLbl.setStyle("-fx-effect:dropshadow(gaussian," + color + ",4,0.15,0,0);");
        VBox card = new VBox(4, tLbl, vLbl);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10, 8, 10, 8));
        card.setPrefWidth(130);
        card.setStyle("-fx-background-color:#0a0a00;-fx-border-color:" + color +
                ";-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;");
        return card;
    }

    // ══════════════════════════════════════════════════════════════════
    //  TAB 1 — Revenue by Room Type (Bar Chart)
    // ══════════════════════════════════════════════════════════════════
    private static ScrollPane buildRevenueChart() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(20, 32, 20, 32));
        box.setStyle("-fx-background-color:#08080f;");

        Label title = sectionLbl("💰  Revenue Collected by Room Type", "#ffcc00");

        String[] rooms  = {"Standard", "Deluxe", "Suite", "Penthouse"};
        String[] colors = {"#9966ff",  "#44aaff", "#44ddff", "#ffcc44"};

        double[] revenues = new double[4];
        int[]    counts   = new int[4];
        for (Booking b : AppContext.bookingList) {
            if (!b.getPayment().equals("Paid")) continue;
            for (int i = 0; i < rooms.length; i++) {
                if (b.getRoomType().equals(rooms[i])) {
                    revenues[i] += b.getTotalPrice();
                    counts[i]++;
                }
            }
        }

        double maxRev = Arrays.stream(revenues).max().orElse(1);
        Canvas canvas = new Canvas(760, 260);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#08080f"));
        gc.fillRect(0, 0, 760, 260);

        int barW = 100, gap = 60, startX = 60;
        for (int i = 0; i < rooms.length; i++) {
            double barH = maxRev > 0 ? (revenues[i] / maxRev) * 200 : 0;
            double x = startX + i * (barW + gap);
            double y = 220 - barH;

            // Bar
            gc.setFill(Color.web(colors[i] + "88"));
            gc.fillRoundRect(x, y, barW, barH, 8, 8);
            gc.setStroke(Color.web(colors[i]));
            gc.setLineWidth(1.5);
            gc.strokeRoundRect(x, y, barW, barH, 8, 8);

            // Value on top
            gc.setFill(Color.web(colors[i]));
            gc.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
            String valStr = "Rs " + String.format("%,.0f", revenues[i]);
            gc.fillText(valStr, x + barW/2.0 - 30, y - 8);

            // Room label
            gc.setFill(Color.web("#aaaacc"));
            gc.setFont(Font.font("Georgia", 12));
            gc.fillText(rooms[i], x + barW/2.0 - 24, 240);

            // Count
            gc.setFill(Color.web("#666688"));
            gc.setFont(Font.font("Georgia", 10));
            gc.fillText(counts[i] + " bookings", x + barW/2.0 - 28, 255);
        }

        // Baseline
        gc.setStroke(Color.web("#332255"));
        gc.setLineWidth(1);
        gc.strokeLine(40, 222, 740, 222);

        box.getChildren().addAll(title, canvas);
        box.getChildren().add(buildRevenueTable(rooms, colors, revenues, counts));

        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:#08080f;");
        return sp;
    }

    private static GridPane buildRevenueTable(String[] rooms, String[] colors,
                                              double[] revenues, int[] counts) {
        GridPane grid = new GridPane();
        grid.setHgap(20); grid.setVgap(8);
        grid.setPadding(new Insets(8, 0, 0, 0));
        String[] headers = {"Room Type", "Bookings", "Revenue", "Avg/Booking"};
        String[] hColors = {"#9977bb", "#9977bb", "#9977bb", "#9977bb"};
        for (int c = 0; c < headers.length; c++) {
            Label h = lbl(headers[c], "Georgia", FontWeight.BOLD, 11, hColors[c]);
            grid.add(h, c, 0);
        }
        for (int i = 0; i < rooms.length; i++) {
            double avg = counts[i] > 0 ? revenues[i] / counts[i] : 0;
            grid.add(lbl(rooms[i], "Georgia", FontWeight.BOLD, 12, colors[i]), 0, i+1);
            grid.add(lbl(String.valueOf(counts[i]), "Georgia", 12, "#aaaacc"), 1, i+1);
            grid.add(lbl("Rs "+String.format("%,.0f",revenues[i]), "Georgia", 12, "#ffcc66"), 2, i+1);
            grid.add(lbl("Rs "+String.format("%,.0f",avg), "Georgia", 12, "#99bbcc"), 3, i+1);
        }
        return grid;
    }

    // ══════════════════════════════════════════════════════════════════
    //  TAB 2 — Bookings Overview
    // ══════════════════════════════════════════════════════════════════
    private static ScrollPane buildBookingsOverview() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(20, 32, 20, 32));
        box.setStyle("-fx-background-color:#08080f;");

        Label title = sectionLbl("📋  Bookings Status Overview", "#44ddaa");

        long confirmed  = AppContext.bookingList.stream().filter(b -> b.getStatus().equals("Confirmed")).count();
        long checkedOut = AppContext.bookingList.stream().filter(b -> b.getStatus().equals("Checked Out")).count();
        long cancelled  = AppContext.bookingList.stream().filter(b -> b.getStatus().equals("Cancelled")).count();
        long total      = AppContext.bookingList.size();

        Canvas canvas = new Canvas(700, 240);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#08080f"));
        gc.fillRect(0, 0, 700, 240);

        // Horizontal bar chart
        String[] labels  = {"Confirmed",  "Checked Out", "Cancelled"};
        long[]   values  = {confirmed,    checkedOut,    cancelled};
        String[] bColors = {"#44ddaa",    "#44aaff",     "#ff4466"};
        double maxVal = total > 0 ? total : 1;

        for (int i = 0; i < labels.length; i++) {
            double barW = (values[i] / maxVal) * 520;
            double y = 30 + i * 65;

            gc.setFill(Color.web("#0a0a1a"));
            gc.fillRoundRect(140, y, 520, 40, 6, 6);
            gc.setFill(Color.web(bColors[i] + "99"));
            gc.fillRoundRect(140, y, barW, 40, 6, 6);
            gc.setStroke(Color.web(bColors[i]));
            gc.setLineWidth(1);
            gc.strokeRoundRect(140, y, barW > 0 ? barW : 2, 40, 6, 6);

            gc.setFill(Color.web(bColors[i]));
            gc.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
            gc.fillText(labels[i], 10, y + 25);

            gc.setFill(Color.web("#ffffff"));
            gc.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
            gc.fillText(String.valueOf(values[i]), 148 + barW, y + 26);

            String pct = total > 0 ? String.format("%.1f%%", (values[i] * 100.0 / total)) : "0%";
            gc.setFill(Color.web("#888899"));
            gc.setFont(Font.font("Georgia", 11));
            gc.fillText(pct, 660, y + 26);
        }

        // Total
        gc.setFill(Color.web("#9977bb"));
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        gc.fillText("Total Bookings: " + total, 10, 220);

        box.getChildren().addAll(title, canvas);

        // Recent bookings list
        box.getChildren().add(sectionLbl("🕐  Recent 5 Bookings", "#9977bb"));
        int count = 0;
        for (int i = AppContext.bookingList.size()-1; i >= 0 && count < 5; i--, count++) {
            Booking b = AppContext.bookingList.get(i);
            HBox row = new HBox(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(8, 12, 8, 12));
            row.setStyle("-fx-background-color:#0d0d22;-fx-border-color:#221144;" +
                    "-fx-border-radius:6;-fx-background-radius:6;");
            String sc = b.getStatus().equals("Confirmed") ? "#aa77ff"
                    : b.getStatus().equals("Checked Out") ? "#44ddaa" : "#ff4466";
            row.getChildren().addAll(
                    lbl(b.getBookingId(), "Georgia", FontWeight.BOLD, 12, "#9966ff"),
                    lbl(b.getGuestName(), "Georgia", 12, "#d4aaff"),
                    spacer(),
                    lbl(b.getRoomType(), "Georgia", 12, "#bb99ff"),
                    lbl(b.getCheckIn() + " → " + b.getCheckOut(), "Georgia", 11, "#7799bb"),
                    lbl("Rs "+String.format("%,.0f",b.getTotalPrice()), "Georgia", FontWeight.BOLD, 12, "#ffcc66"),
                    lbl(b.getStatus(), "Georgia", FontWeight.BOLD, 11, sc)
            );
            box.getChildren().add(row);
        }
        if (AppContext.bookingList.isEmpty()) {
            box.getChildren().add(lbl("No bookings yet.", "Georgia", 12, "#555577"));
        }

        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:#08080f;");
        return sp;
    }

    // ══════════════════════════════════════════════════════════════════
    //  TAB 3 — Payment Methods (Pie-style)
    // ══════════════════════════════════════════════════════════════════
    private static ScrollPane buildPaymentChart() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(20, 32, 20, 32));
        box.setStyle("-fx-background-color:#08080f;");

        Label title = sectionLbl("💳  Payment Method Breakdown", "#44ccff");

        Map<String, Long> methodCount = new LinkedHashMap<>();
        Map<String, Double> methodRev = new LinkedHashMap<>();
        for (Booking b : AppContext.bookingList) {
            if (!b.getPayment().equals("Paid")) continue;
            String m = b.getPaymentMethod();
            methodCount.merge(m, 1L, Long::sum);
            methodRev.merge(m, b.getTotalPrice(), Double::sum);
        }

        String[] methColors = {"#44ddaa","#44aaff","#aa55ff","#ff8800","#ff4400","#ffcc00"};
        int colorIdx = 0;

        Canvas canvas = new Canvas(700, 280);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#08080f"));
        gc.fillRect(0, 0, 700, 280);

        if (methodCount.isEmpty()) {
            gc.setFill(Color.web("#555577"));
            gc.setFont(Font.font("Georgia", 14));
            gc.fillText("No paid bookings yet.", 280, 140);
        } else {
            // Draw arc chart
            double total = methodCount.values().stream().mapToLong(v -> v).sum();
            double startAngle = 0;
            double cx = 140, cy = 140, radius = 110;
            List<String> methods = new ArrayList<>(methodCount.keySet());

            for (int i = 0; i < methods.size(); i++) {
                String m = methods.get(i);
                String c = methColors[i % methColors.length];
                double sweep = (methodCount.get(m) / total) * 360;
                gc.setFill(Color.web(c + "aa"));
                gc.fillArc(cx-radius, cy-radius, radius*2, radius*2, startAngle, sweep,
                        javafx.scene.shape.ArcType.ROUND);
                gc.setStroke(Color.web("#08080f"));
                gc.setLineWidth(2);
                gc.strokeArc(cx-radius, cy-radius, radius*2, radius*2, startAngle, sweep,
                        javafx.scene.shape.ArcType.ROUND);
                startAngle += sweep;
            }

            // Legend & table on right
            double ly = 30;
            for (int i = 0; i < methods.size(); i++) {
                String m = methods.get(i);
                String c = methColors[i % methColors.length];
                gc.setFill(Color.web(c));
                gc.fillRoundRect(310, ly, 14, 14, 3, 3);
                gc.setFont(Font.font("Georgia", 12));
                gc.fillText(m, 332, ly + 12);
                gc.setFill(Color.web("#aaaacc"));
                gc.fillText(methodCount.get(m) + " bookings", 332, ly + 28);
                gc.setFill(Color.web("#ffcc66"));
                gc.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
                gc.fillText("Rs " + String.format("%,.0f", methodRev.get(m)), 332, ly + 44);
                ly += 65;
            }
        }

        box.getChildren().addAll(title, canvas);

        // Paid vs Pending
        long paidCount    = AppContext.bookingList.stream().filter(b -> b.getPayment().equals("Paid")).count();
        long pendingCount = AppContext.bookingList.stream().filter(b -> b.getPayment().equals("Pending")).count();
        double paidRev    = AppContext.bookingList.stream().filter(b -> b.getPayment().equals("Paid")).mapToDouble(Booking::getTotalPrice).sum();
        double pendRev    = AppContext.bookingList.stream().filter(b -> b.getPayment().equals("Pending") && !b.getStatus().equals("Cancelled")).mapToDouble(Booking::getTotalPrice).sum();

        HBox pvRow = new HBox(16);
        pvRow.getChildren().addAll(
                infoCard("✅  PAID",    paidCount + " bookings\nRs " + String.format("%,.0f",paidRev),    "#44ddaa"),
                infoCard("⏳  PENDING", pendingCount + " bookings\nRs " + String.format("%,.0f",pendRev), "#ffcc00")
        );
        box.getChildren().addAll(sectionLbl("📊  Paid vs Pending", "#9977bb"), pvRow);

        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:#08080f;");
        return sp;
    }

    // ══════════════════════════════════════════════════════════════════
    //  TAB 4 — Monthly Revenue Trend
    // ══════════════════════════════════════════════════════════════════
    private static ScrollPane buildMonthlyTrend() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(20, 32, 20, 32));
        box.setStyle("-fx-background-color:#08080f;");

        Label title = sectionLbl("📅  Monthly Revenue Trend (Paid Bookings)", "#ffaa33");

        // Group revenue by month
        Map<String, Double> monthly = new TreeMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        for (Booking b : AppContext.bookingList) {
            if (!b.getPayment().equals("Paid")) continue;
            try {
                String month = LocalDate.parse(b.getCheckIn()).format(fmt);
                monthly.merge(month, b.getTotalPrice(), Double::sum);
            } catch (Exception ignored) {}
        }

        Canvas canvas = new Canvas(760, 260);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#08080f"));
        gc.fillRect(0, 0, 760, 260);

        if (monthly.isEmpty()) {
            gc.setFill(Color.web("#555577"));
            gc.setFont(Font.font("Georgia", 14));
            gc.fillText("No paid booking data available.", 240, 130);
        } else {
            List<Map.Entry<String, Double>> entries = new ArrayList<>(monthly.entrySet());
            double maxRev = entries.stream().mapToDouble(Map.Entry::getValue).max().orElse(1);
            int n = entries.size();
            double barW = Math.min(80, 680.0 / n - 10);
            double startX = 60;

            // Grid lines
            gc.setStroke(Color.web("#1a1a2a"));
            gc.setLineWidth(0.5);
            for (int g = 1; g <= 4; g++) {
                double y = 220 - (g * 0.25 * 200);
                gc.strokeLine(40, y, 740, y);
                gc.setFill(Color.web("#555577"));
                gc.setFont(Font.font("Georgia", 9));
                gc.fillText("Rs "+String.format("%,.0f", maxRev * g * 0.25), 2, y + 4);
            }

            for (int i = 0; i < n; i++) {
                Map.Entry<String, Double> entry = entries.get(i);
                double barH = (entry.getValue() / maxRev) * 200;
                double x = startX + i * (barW + 10);
                double y = 220 - barH;

                gc.setFill(Color.web("#ff880066"));
                gc.fillRoundRect(x, y, barW, barH, 6, 6);
                gc.setStroke(Color.web("#ffaa33"));
                gc.setLineWidth(1.5);
                gc.strokeRoundRect(x, y, barW, barH, 6, 6);

                gc.setFill(Color.web("#ffcc66"));
                gc.setFont(Font.font("Georgia", FontWeight.BOLD, 9));
                String valStr = "Rs " + String.format("%,.0f", entry.getValue());
                gc.fillText(valStr, x + barW/2 - 20, y - 6);

                gc.setFill(Color.web("#9977bb"));
                gc.setFont(Font.font("Georgia", 9));
                String monthLabel = entry.getKey().substring(5);
                gc.fillText(entry.getKey().substring(2, 4) + "-" + monthLabel, x + barW/2 - 14, 238);
            }
        }

        // Baseline
        gc.setStroke(Color.web("#332255"));
        gc.setLineWidth(1);
        gc.strokeLine(40, 222, 740, 222);

        // Summary stats
        double totalRev = monthly.values().stream().mapToDouble(v -> v).sum();
        double avgRev   = monthly.isEmpty() ? 0 : totalRev / monthly.size();
        Optional<Map.Entry<String, Double>> bestMonth = monthly.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
                infoCard("📊  TOTAL REVENUE",   "Rs " + String.format("%,.0f", totalRev), "#ffcc00"),
                infoCard("📅  MONTHLY AVG",     "Rs " + String.format("%,.0f", avgRev),   "#ffaa33"),
                infoCard("🏆  BEST MONTH",      bestMonth.map(e -> e.getKey() + "\nRs " +
                        String.format("%,.0f", e.getValue())).orElse("N/A"),              "#44ddaa")
        );

        box.getChildren().addAll(title, canvas, sectionLbl("📈  Summary", "#9977bb"), statsRow);

        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:#08080f;");
        return sp;
    }

    // ── Info Card helper ──────────────────────────────────────────────
    private static VBox infoCard(String title, String value, String color) {
        Label t = lbl(title, "Georgia", FontWeight.BOLD, 12, color);
        Label v = lbl(value, "Georgia", 12, "#ccccdd");
        v.setWrapText(true);
        VBox card = new VBox(6, t, v);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color:#0d0d22;-fx-border-color:" + color +
                ";-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;");
        return card;
    }
}