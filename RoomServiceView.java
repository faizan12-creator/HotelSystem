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

import java.util.LinkedHashMap;
import java.util.Map;

import static HotelSystem.UIHelper.*;

public class RoomServiceView {

    // Menu: name -> price
    private static final Map<String, Double> MENU = new LinkedHashMap<>() {{
        put("☕ Tea / Coffee",         150.0);
        put("🥐 Breakfast Platter",   850.0);
        put("🍳 Full English Breakfast",1200.0);
        put("🥗 Caesar Salad",         650.0);
        put("🍔 Club Sandwich",         750.0);
        put("🍕 Margherita Pizza",     1400.0);
        put("🍜 Pasta Alfredo",        1100.0);
        put("🍗 Grilled Chicken",      1600.0);
        put("🥩 Beef Steak",           2200.0);
        put("🍰 Dessert Platter",       900.0);
        put("🧃 Fresh Juice",           350.0);
        put("🍶 Mineral Water",         100.0);
    }};

    public static void show() {
        // Must have an active booking
        Booking activeBooking = null;
        for (Booking b : AppContext.bookingList)
            if (b.getBookedBy().equals(AppContext.currentUser)
                    && b.getStatus().equals("Confirmed")) {
                activeBooking = b;
                break;
            }

        if (activeBooking == null) {
            AppContext.showUserMsg("⚠  You need a confirmed booking to order room service!", "#ffcc00");
            return;
        }
        final Booking booking = activeBooking;

        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:#03100a;-fx-border-color:#44cc88;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect:dropshadow(gaussian,#44cc88,24,0.3,0,0);");

        // Header
        VBox header = new VBox(4);
        header.setPadding(new Insets(24, 32, 20, 32));
        header.setStyle("-fx-background-color:#041810;-fx-background-radius:14 14 0 0;");
        header.getChildren().addAll(
                styled(lbl("🍽️  ROOM SERVICE", "Georgia", FontWeight.EXTRA_BOLD, 22, "#44cc88"),
                        "-fx-effect:dropshadow(gaussian,#44cc88,8,0.3,0,0);"),
                lbl("Booking: " + booking.getBookingId() + "  |  Room: " + booking.getRoomType(),
                        "Georgia", 11, "#77bb99")
        );

        // Body
        VBox body = new VBox(0);
        body.setPadding(new Insets(16, 32, 16, 32));

        Label menuTitle = sectionLbl("📋  SELECT ITEMS", "#44cc88");
        menuTitle.setPadding(new Insets(0, 0, 8, 0));

        // Menu items with spinners
        Map<String, Spinner<Integer>> spinners = new LinkedHashMap<>();
        VBox menuBox = new VBox(6);

        Label totalLabel = styled(
                lbl("Rs 0", "Georgia", FontWeight.EXTRA_BOLD, 22, "#44cc88"),
                "-fx-effect:dropshadow(gaussian,#44cc88,6,0.2,0,0);"
        );

        Runnable updateTotal = () -> {
            double total = 0;
            for (Map.Entry<String, Spinner<Integer>> entry : spinners.entrySet())
                total += MENU.get(entry.getKey()) * entry.getValue().getValue();
            totalLabel.setText("Rs " + String.format("%,.0f", total));
        };

        for (Map.Entry<String, Double> item : MENU.entrySet()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6, 10, 6, 10));
            row.setStyle("-fx-border-color:#1a5533;-fx-border-width:0 0 1 0;");

            Label nameL = lbl(item.getKey(), "Georgia", 13, "#aaddcc");
            nameL.setPrefWidth(200);
            Label priceL = lbl("Rs " + String.format("%,.0f", item.getValue()), "Georgia", 12, "#77bb99");
            priceL.setPrefWidth(90);

            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            spinner.setPrefWidth(70);
            spinner.setStyle("-fx-background-color:#041810;-fx-border-color:#1a5533;" +
                    "-fx-border-radius:4;-fx-font-family:'Georgia';-fx-font-size:12;");
            spinner.valueProperty().addListener((o, ov, nv) -> updateTotal.run());

            spinners.put(item.getKey(), spinner);
            row.getChildren().addAll(nameL, spacer(), priceL, spinner);
            menuBox.getChildren().add(row);
        }

        ScrollPane menuScroll = new ScrollPane(menuBox);
        menuScroll.setFitToWidth(true);
        menuScroll.setPrefHeight(280);
        menuScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;");

        // Notes
        Label notesLbl = sectionLbl("📝  Special Instructions", "#44cc88");
        notesLbl.setPadding(new Insets(8, 0, 4, 0));
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Any special requests (e.g. no onions, extra spicy)...");
        notesArea.setPrefRowCount(2);
        notesArea.setWrapText(true);
        notesArea.setStyle("-fx-background-color:#041810;-fx-control-inner-background:#041810;" +
                "-fx-border-color:#1a5533;-fx-border-width:1.5;-fx-border-radius:6;" +
                "-fx-text-fill:white;-fx-prompt-text-fill:#336644;" +
                "-fx-font-family:'Georgia';-fx-font-size:12;");

        body.getChildren().addAll(menuTitle, menuScroll, notesLbl, notesArea);

        // Footer
        VBox footer = new VBox(10);
        footer.setPadding(new Insets(16, 32, 24, 32));
        footer.setStyle("-fx-background-color:#041810;-fx-background-radius:0 0 14 14;");

        HBox totalRow = new HBox(10);
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.getChildren().addAll(
                lbl("TOTAL:", "Georgia", FontWeight.BOLD, 14, "#77bb99"),
                spacer(), totalLabel
        );

        Label errLbl = lbl("", "Georgia", 11, "#ff4466");

        Button orderBtn  = colorBtn("🍽️  PLACE ORDER", "#001a0e", "#116633", "#44cc88", 13, true);
        Button closeBtn2 = colorBtn("✕  CANCEL",       "#1a0008", "#881133", "#ff4466", 12, true);

        orderBtn.setOnAction(e -> {
            // Build order summary
            StringBuilder items = new StringBuilder();
            double total = 0;
            for (Map.Entry<String, Spinner<Integer>> entry : spinners.entrySet()) {
                int qty = entry.getValue().getValue();
                if (qty > 0) {
                    items.append(entry.getKey()).append(" x").append(qty).append(", ");
                    total += MENU.get(entry.getKey()) * qty;
                }
            }
            if (items.isEmpty()) { errLbl.setText("⚠  Please select at least one item!"); return; }

            String itemsStr = items.substring(0, items.length() - 2);
            if (!notesArea.getText().trim().isEmpty())
                itemsStr += " | Note: " + notesArea.getText().trim();

            String oid = "RS-" + (AppContext.serviceOrderCounter++);
            RoomServiceOrder order = new RoomServiceOrder(
                    oid, booking.getBookingId(), booking.getGuestName(),
                    itemsStr, total, AppContext.currentUser
            );
            AppContext.serviceOrders.add(order);
            AppContext.showUserMsg("🍽️  Order " + oid + " placed! Rs " +
                    String.format("%,.0f", total) + " will be charged.", "#44cc88");
            d.close();
        });
        closeBtn2.setOnAction(e -> d.close());

        footer.getChildren().addAll(totalRow, errLbl, orderBtn, closeBtn2);
        root.getChildren().addAll(header, body, footer);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        Scene sc = new Scene(sp, 520, 680);
        sc.setFill(Color.TRANSPARENT);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }

    // ══════════════════════════════════════════════════════════════════
    //  ADMIN — View All Orders
    // ══════════════════════════════════════════════════════════════════
    public static void showAdminOrders() {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(14);
        root.setPadding(new Insets(28, 32, 28, 32));
        root.setPrefWidth(820);
        root.setStyle("-fx-background-color:#03100a;-fx-border-color:#44cc88;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;");

        HBox hdr = new HBox(12);
        hdr.setAlignment(Pos.CENTER_LEFT);
        Label title = styled(
                lbl("🍽️  ROOM SERVICE ORDERS", "Georgia", FontWeight.EXTRA_BOLD, 20, "#44cc88"),
                "-fx-effect:dropshadow(gaussian,#44cc88,8,0.3,0,0);"
        );
        long pending = AppContext.serviceOrders.stream()
                .filter(o -> o.getStatus().equals("Pending")).count();
        Label statLbl = lbl("Pending: " + pending, "Georgia", 12, "#ffcc00");
        hdr.getChildren().addAll(title, spacer(), statLbl);

        TableView<RoomServiceOrder> table = new TableView<>(AppContext.serviceOrders);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(360);
        table.setStyle("-fx-background-color:#041810;-fx-control-inner-background:#041810;" +
                "-fx-control-inner-background-alt:#051f12;-fx-border-color:#1a5533;" +
                "-fx-border-radius:10;-fx-background-radius:10;" +
                "-fx-font-family:'Georgia';-fx-font-size:12px;");

        addSvcCol(table, "ORDER ID",   "orderId",   "#44cc88", 90);
        addSvcCol(table, "BOOKING ID", "bookingId", "#77bb99", 90);
        addSvcCol(table, "👤 GUEST",   "guestName", "#aaddcc", 0);
        addSvcCol(table, "ITEMS",      "items",     "#ccddbb", 0);
        addSvcDblCol(table);
        addSvcStatusCol(table);
        addSvcCol(table, "TIME",       "orderedAt", "#77aa88", 115);

        // Status update buttons
        Button deliveredBtn = actionBtn("✅  MARK DELIVERED", "#001a0e", "#116633", "#44cc88");
        deliveredBtn.setOnAction(e -> {
            RoomServiceOrder sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            sel.setStatus("Delivered");
            table.refresh();
        });

        HBox btnRow = new HBox(10, deliveredBtn, spacer(),
                colorBtn("✕  CLOSE", "#1a0008", "#881133", "#ff4466", 12, false));
        ((Button)btnRow.getChildren().get(2)).setOnAction(e -> d.close());
        btnRow.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(hdr, sep("#1a5533"), table, btnRow);
        Scene sc = new Scene(root, 840, 500);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }

    private static void addSvcCol(TableView<RoomServiceOrder> table,
                                  String title, String prop, String color, double w) {
        TableColumn<RoomServiceOrder, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
        col.setStyle("-fx-alignment:CENTER;");
        col.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String s, boolean e) {
                super.updateItem(s, e);
                if (e || s == null) { setText(null); return; }
                setText(s);
                setStyle("-fx-text-fill:" + (isSelected() ? "white" : color) + ";-fx-alignment:CENTER;");
            }
        });
        if (w > 0) { col.setMinWidth(w); col.setMaxWidth(w); }
        table.getColumns().add(col);
    }

    private static void addSvcDblCol(TableView<RoomServiceOrder> table) {
        TableColumn<RoomServiceOrder, Double> col = new TableColumn<>("TOTAL(Rs)");
        col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("totalCost"));
        col.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(Double v, boolean e) {
                super.updateItem(v, e);
                if (e || v == null) { setText(null); return; }
                setText(String.format("%,.0f", v));
                setStyle("-fx-text-fill:" + (isSelected() ? "white" : "#ffcc44") +
                        ";-fx-alignment:CENTER;-fx-font-weight:bold;");
            }
        });
        col.setMinWidth(95); col.setMaxWidth(95);
        table.getColumns().add(col);
    }

    private static void addSvcStatusCol(TableView<RoomServiceOrder> table) {
        TableColumn<RoomServiceOrder, String> col = new TableColumn<>("STATUS");
        col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        col.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String s, boolean e) {
                super.updateItem(s, e);
                if (e || s == null) { setText(null); return; }
                setText(s);
                String c = s.equals("Delivered") ? "#44ddaa" : "#ffcc00";
                setStyle("-fx-text-fill:" + c + ";-fx-alignment:CENTER;-fx-font-weight:bold;");
            }
        });
        col.setMinWidth(90); col.setMaxWidth(90);
        table.getColumns().add(col);
    }
}