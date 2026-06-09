package HotelSystem;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

import static HotelSystem.UIHelper.*;

public class AdminView extends BaseView {

    private static TextField guestNameField, phoneField, emailField,
            checkInField, checkOutField;
    private static ComboBox<String> roomTypeBox;

    public static void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#06060f;");
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        root.setCenter(buildMainPanel());
        AppContext.primaryStage.setMaximized(true);
        AppContext.primaryStage.setScene(new Scene(root, 1280, 780));
        AppContext.primaryStage.show();
    }

    // ── Header ────────────────────────────────────────────────────────
    private static HBox buildHeader() {
        HBox h = hbox(14, "linear-gradient(to right,#08081a,#100828,#08081a)", new Insets(14,28,14,28));
        h.setStyle(h.getStyle() + "-fx-border-color:#9966ff;-fx-border-width:0 0 2 0;");
        VBox titles = new VBox(2);
        titles.getChildren().addAll(
                styled(lbl("GRAND HORIZON  —  ADMIN DASHBOARD","Georgia",FontWeight.EXTRA_BOLD,20,"#d4aaff"),"-fx-effect:dropshadow(gaussian,#9966ff,8,0.3,0,0);"),
                lbl("Full Access  ·  Reservation Control  ·  Finance & Reports","Georgia",10,"#9977bb")
        );
        AppContext.msgLabel = lbl("Welcome, "+AppContext.currentUser,"Georgia",FontWeight.BOLD,12,"#bb88ff");
        Button logout = colorBtn("⬅  LOGOUT","#08081a","#443366","#9966ff",11,false);
        logout.setOnAction(e -> { AppContext.primaryStage.hide(); LoginView.show(); });
        Button close = xBtn(); close.setOnAction(e -> AppContext.primaryStage.close());
        h.getChildren().addAll(lbl("🏨",30,null,null), titles, spacer(),
                badge("🔐  ADMIN","#ffcc00","#1a1100","#886600"), AppContext.msgLabel, logout, close);
        return h;
    }

    // ── Sidebar ───────────────────────────────────────────────────────
    private static VBox buildSidebar() {
        VBox sb = new VBox(12); sb.setPadding(new Insets(18,16,18,16)); sb.setPrefWidth(320);
        sb.setStyle("-fx-background-color:linear-gradient(to bottom,#0a0a20,#070715);-fx-border-color:#332255;-fx-border-width:0 2 0 0;");

        // Stats
        AppContext.totalBookings = lbl("0","Georgia",FontWeight.EXTRA_BOLD,16,"#9966ff");
        AppContext.totalRevenue  = lbl("Rs 0","Georgia",FontWeight.EXTRA_BOLD,16,"#ffaa44");
        AppContext.confirmedLbl  = lbl("0","Georgia",FontWeight.EXTRA_BOLD,16,"#44ddaa");
        AppContext.pendingLbl    = lbl("0","Georgia",FontWeight.EXTRA_BOLD,16,"#ff5577");
        HBox r1 = row(statCard("BOOKINGS",AppContext.totalBookings,"#9966ff"), statCard("REVENUE",AppContext.totalRevenue,"#ffaa44"));
        HBox r2 = row(statCard("CONFIRMED",AppContext.confirmedLbl,"#44ddaa"), statCard("UNPAID",AppContext.pendingLbl,"#ff5577"));

        // Form
        guestNameField = adminField("Guest Full Name *"); phoneField = adminField("Phone (10-13 digits) *");
        emailField     = adminField("Email Address");     checkInField = adminField("Check-In (YYYY-MM-DD) *");
        checkOutField  = adminField("Check-Out (YYYY-MM-DD) *");
        roomTypeBox = new ComboBox<>(FXCollections.observableArrayList("Standard","Deluxe","Suite","Penthouse"));
        roomTypeBox.setPromptText("Select Room Type *"); styleAdminCombo(roomTypeBox);
        Label priceHint = lbl("Price: Select room type to see rate","Georgia",11,"#9977cc");
        roomTypeBox.setOnAction(e -> { String v=roomTypeBox.getValue(); if(v!=null) priceHint.setText("💰 Rs "+(int)AppContext.getPrice(v)+" / night  ·  "+v); });

        // Booking action buttons
        Button bookBtn = actionBtn("📋  BOOK ROOM","#1a0033","#7722cc","#aa55ff");
        Button coBtn   = actionBtn("🚪  CHECK OUT","#001a11","#116633","#44dd88");
        Button payBtn  = actionBtn("💳  PROCESS PAYMENT","#1a1100","#886600","#ffcc00");
        Button recBtn  = actionBtn("🧾  VIEW RECEIPT","#00111a","#115566","#44ccff");
        Button canBtn  = actionBtn("❌  CANCEL BOOKING","#1a0008","#881133","#ff4466");
        Button clrBtn  = actionBtn("🔄  CLEAR ALL","#0d0d0d","#555555","#bbbbbb");
        for (Button b : new Button[]{bookBtn,coBtn,payBtn,recBtn,canBtn,clrBtn}) b.setMaxWidth(Double.MAX_VALUE);

        bookBtn.setOnAction(e -> {
            BookingController.adminBookRoom(guestNameField.getText().trim(),phoneField.getText().trim(),
                    emailField.getText().trim(),roomTypeBox.getValue(),checkInField.getText().trim(),checkOutField.getText().trim());
            guestNameField.clear(); phoneField.clear(); emailField.clear();
            checkInField.clear(); checkOutField.clear(); roomTypeBox.setValue(null);
        });
        coBtn.setOnAction(e  -> BookingController.adminCheckOut(AppContext.adminTable.getSelectionModel().getSelectedItem()));
        payBtn.setOnAction(e -> {
            Booking sel = AppContext.adminTable.getSelectionModel().getSelectedItem();
            if (sel==null){AppContext.showAdminMsg("⚠  Select a booking!","#ffcc00");return;}
            if (sel.getPayment().equals("Paid")){AppContext.showAdminMsg("⚠  Already paid!","#44ddaa");return;}
            if (sel.getStatus().equals("Cancelled")){AppContext.showAdminMsg("⚠  Booking cancelled!","#ff4466");return;}
            PaymentView.show(sel);
        });
        recBtn.setOnAction(e -> {
            Booking sel = AppContext.adminTable.getSelectionModel().getSelectedItem();
            if (sel==null){AppContext.showAdminMsg("⚠  Select a booking!","#ffcc00");return;}
            ReceiptView.show(sel);
        });
        canBtn.setOnAction(e -> BookingController.adminCancelBooking(AppContext.adminTable.getSelectionModel().getSelectedItem()));
        clrBtn.setOnAction(e -> { AppContext.bookingList.clear(); AppContext.updateStats(); AppContext.showAdminMsg("All records cleared.","#aaaaaa"); });

        // ── NEW Feature Buttons ──────────────────────────────────────
        Button feedbackBtn = actionBtn("⭐  VIEW GUEST FEEDBACK",  "#140022","#661188","#cc44ff");
        Button serviceBtn  = actionBtn("🍽️  ROOM SERVICE ORDERS", "#001a0e","#116633","#44cc88");
        Button calendarBtn = actionBtn("🏠  ROOM AVAILABILITY",    "#001020","#115566","#44aaff");
        Button reportsBtn  = actionBtn("📊  REVENUE REPORTS",      "#1a1100","#886600","#ffcc00");
        for (Button b : new Button[]{feedbackBtn,serviceBtn,calendarBtn,reportsBtn}) b.setMaxWidth(Double.MAX_VALUE);

        feedbackBtn.setOnAction(e -> FeedbackView.showAdminView());
        serviceBtn.setOnAction(e  -> RoomServiceView.showAdminOrders());
        calendarBtn.setOnAction(e -> RoomCalendarView.show());
        reportsBtn.setOnAction(e  -> ReportsView.show());

        sb.getChildren().addAll(
                sectionLbl("📊  OVERVIEW","#9977bb"), r1, r2, new Separator(),
                sectionLbl("➕  NEW RESERVATION","#9977bb"),
                guestNameField, phoneField, emailField, roomTypeBox, priceHint,
                checkInField, checkOutField, bookBtn, new Separator(),
                coBtn, payBtn, recBtn, canBtn, new Separator(), clrBtn, new Separator(),
                sectionLbl("🔧  MANAGEMENT TOOLS","#9977bb"),
                feedbackBtn, serviceBtn, calendarBtn, reportsBtn, new Separator(),
                sectionLbl("🛏️  ROOM RATES","#9977bb"), buildRoomRatesBox()
        );

        ScrollPane sc = new ScrollPane(sb); sc.setFitToWidth(true);
        sc.setStyle("-fx-background-color:transparent;-fx-background:transparent;"); sc.setPrefWidth(320);
        VBox wrap = new VBox(sc); VBox.setVgrow(sc,Priority.ALWAYS); wrap.setPrefWidth(320);
        wrap.setStyle("-fx-background-color:linear-gradient(to bottom,#0a0a20,#070715);-fx-border-color:#332255;-fx-border-width:0 2 0 0;");
        return wrap;
    }

    // ── Main Panel ────────────────────────────────────────────────────
    private static VBox buildMainPanel() {
        VBox panel = new VBox(14); panel.setPadding(new Insets(22)); panel.setStyle("-fx-background-color:transparent;");

        HBox titleBar = new HBox(12); titleBar.setAlignment(Pos.CENTER_LEFT);
        Label title = sectionLbl("📋  ALL RESERVATIONS  (Admin View)","#bb88ff");
        title.setFont(javafx.scene.text.Font.font("Georgia",FontWeight.BOLD,14));
        TextField search = adminField("🔍  Search guest / booking ID / phone..."); search.setPrefWidth(280);
        search.textProperty().addListener((o,ov,nv) -> AppContext.adminTable.setItems(BookingController.filterAdmin(nv)));
        titleBar.getChildren().addAll(title, spacer(), search);

        Button saveBtn = actionBtn("💾  SAVE DATABASE","#000d1a","#115599","#44aaff");
        Button loadBtn = actionBtn("📂  LOAD DATABASE","#110d00","#775500","#ffaa33");
        saveBtn.setOnAction(e -> DatabaseManager.saveBookings());
        loadBtn.setOnAction(e -> { DatabaseManager.loadBookings(); AppContext.updateStats(); AppContext.showAdminMsg("Database loaded!","#44ddaa"); });
        HBox actionBar = new HBox(10, saveBtn, loadBtn); actionBar.setAlignment(Pos.CENTER_LEFT);

        AppContext.adminTable = new TableView<>(AppContext.bookingList);
        AppContext.adminTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(AppContext.adminTable, Priority.ALWAYS);
        AppContext.adminTable.setFixedCellSize(44);
        AppContext.adminTable.setStyle(
                "-fx-background-color:#080818;-fx-control-inner-background:#080818;-fx-control-inner-background-alt:#0b0b22;" +
                        "-fx-border-color:#332255;-fx-border-radius:10;-fx-background-radius:10;" +
                        "-fx-table-cell-border-color:#1a0044;-fx-font-family:'Georgia';-fx-font-size:12px;"
        );
        AppContext.adminTable.setRowFactory(tv -> {
            TableRow<Booking> row = new TableRow<>();
            row.selectedProperty().addListener((obs,ov,nv) -> row.setStyle(nv ? "-fx-background-color:#2a1a4a;" : ""));
            return row;
        });

        // Special columns
        TableColumn<Booking,String> numC = tCol("#",null,"#7755aa",42);
        numC.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String i,boolean e){super.updateItem(i,e);setText(e?null:String.valueOf(getIndex()+1));setStyle("-fx-text-fill:"+(isSelected()?"white":"#7755aa")+";-fx-alignment:CENTER;");}
            public void updateSelected(boolean s){super.updateSelected(s);if(!isEmpty())setStyle("-fx-text-fill:"+(s?"white":"#7755aa")+";-fx-alignment:CENTER;");}
        });
        TableColumn<Booking,String> stC = tCol("STATUS","status","#ffffff",105);
        stC.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String s,boolean e){super.updateItem(s,e);if(e||s==null){setText(null);setStyle("");return;}setText(s);String c=isSelected()?"white":s.equals("Confirmed")?"#aa77ff":s.equals("Checked Out")?"#44ddaa":"#ff4466";setStyle("-fx-text-fill:"+c+";-fx-font-weight:bold;-fx-alignment:CENTER;");}
            public void updateSelected(boolean sel){super.updateSelected(sel);if(!isEmpty())updateItem(getItem(),false);}
        });
        TableColumn<Booking,String> pyC = tCol("PAYMENT","payment","#ffffff",90);
        pyC.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String p,boolean e){super.updateItem(p,e);if(e||p==null){setText(null);setStyle("");return;}setText(p);String c=isSelected()?"white":p.equals("Paid")?"#44ddaa":"#ffcc00";setStyle("-fx-text-fill:"+c+";-fx-font-weight:bold;-fx-alignment:CENTER;");}
            public void updateSelected(boolean sel){super.updateSelected(sel);if(!isEmpty())updateItem(getItem(),false);}
        });

        AppContext.adminTable.getColumns().addAll(numC,
                tCol("BOOKING ID","bookingId","#9966ff",100), tCol("👤 GUEST","guestName","#d4aaff",0),
                tCol("📞 PHONE","phone","#bb99ff",115), tCol("BOOKED BY","bookedBy","#9977bb",90),
                tCol("🛏 ROOM","roomType","#bb99ff",100), tCol("CHECK IN","checkIn","#99aadd",105),
                tCol("CHECK OUT","checkOut","#99aadd",105), tColInt("NTS","nights","#bbbbdd",48),
                tColDbl("TOTAL(Rs)","totalPrice","#ffcc66",100), tCol("METHOD","paymentMethod","#99bbcc",100),
                stC, pyC
        );
        Label ph = lbl("No reservations found.\nUse the sidebar form to add bookings.","Georgia",14,"#7755aa");
        ph.setAlignment(Pos.CENTER); AppContext.adminTable.setPlaceholder(ph);
        AppContext.updateStats();

        panel.getChildren().addAll(titleBar, actionBar, AppContext.adminTable);
        return panel;
    }
}