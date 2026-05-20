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

public class UserView {

    private static TextField uNameField, uPhoneField, uEmailField, uCiField, uCoField;
    private static ComboBox<String> uRoomBox;

    public static void show() {
        BorderPane root = new BorderPane(); root.setStyle("-fx-background-color:#030d16;");
        root.setTop(buildHeader());
        HBox main = new HBox(0);
        VBox right = buildBookingsPanel(); HBox.setHgrow(right, Priority.ALWAYS);
        main.getChildren().addAll(buildFormPanel(), right);
        root.setCenter(main);
        AppContext.primaryStage.setMaximized(true);
        AppContext.primaryStage.setScene(new Scene(root, 1280, 780));
        AppContext.primaryStage.show();
    }

    // ── Header ────────────────────────────────────────────────────────
    private static HBox buildHeader() {
        HBox h = hbox(14,"linear-gradient(to right,#030d16,#061e30,#030d16)",new Insets(14,28,14,28));
        h.setStyle(h.getStyle()+"-fx-border-color:#0088cc;-fx-border-width:0 0 2 0;");
        VBox titles = new VBox(2);
        titles.getChildren().addAll(
                styled(lbl("GRAND HORIZON  —  GUEST PORTAL","Georgia",FontWeight.EXTRA_BOLD,20,"#aaddff"),"-fx-effect:dropshadow(gaussian,#0099cc,8,0.3,0,0);"),
                lbl("Book Rooms  ·  Room Service  ·  Feedback  ·  Payments","Georgia",10,"#5599bb")
        );
        String[] ui = AppContext.USERS.get(AppContext.currentUser);
        String dn = (ui!=null&&ui.length>2) ? ui[2] : AppContext.currentUser;
        AppContext.uMsgLabel = lbl("Welcome,  "+dn+" 👋","Georgia",FontWeight.BOLD,13,"#33aadd");
        Button logout = colorBtn("⬅  LOGOUT","#040f1a","#0055aa","#44aaff",11,false);
        logout.setOnAction(e -> { AppContext.primaryStage.hide(); LoginView.show(); });
        Button close = xBtn(); close.setOnAction(e -> AppContext.primaryStage.close());
        h.getChildren().addAll(lbl("🏨",30,null,null), titles, spacer(),
                badge("👤  GUEST USER","#44ccff","#031520","#0077aa"), AppContext.uMsgLabel, logout, close);
        return h;
    }

    // ── Form Panel (left) ─────────────────────────────────────────────
    private static VBox buildFormPanel() {
        VBox panel = new VBox(14); panel.setPadding(new Insets(22,20,22,22)); panel.setPrefWidth(400);
        panel.setStyle("-fx-background-color:linear-gradient(to bottom,#04111e,#030d18);-fx-border-color:#0a3355;-fx-border-width:0 2 0 0;");

        uNameField  = userField("Full Name *"); uPhoneField = userField("Phone Number (10-13 digits) *");
        uEmailField = userField("Email Address (optional)"); uCiField = userField("Check-In Date (YYYY-MM-DD) *");
        uCoField    = userField("Check-Out Date (YYYY-MM-DD) *");
        uRoomBox = new ComboBox<>(FXCollections.observableArrayList("Standard","Deluxe","Suite","Penthouse"));
        uRoomBox.setPromptText("Select Room Type *"); styleUserCombo(uRoomBox);

        VBox priceCard = new VBox(4); priceCard.setAlignment(Pos.CENTER);
        priceCard.setStyle("-fx-background-color:#041220;-fx-border-color:#0a3355;-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;-fx-padding:10;");
        Label pLabel = lbl("Select a room type to see rate","Georgia",FontWeight.BOLD,13,"#5599bb");
        Label pTag   = styled(lbl("","Georgia",FontWeight.EXTRA_BOLD,20,"#44ccff"),"-fx-effect:dropshadow(gaussian,#0099cc,8,0.3,0,0);");
        priceCard.getChildren().addAll(pLabel, pTag);
        uRoomBox.setOnAction(e -> { String v=uRoomBox.getValue(); if(v!=null){pLabel.setText(v+" Room");pLabel.setTextFill(Color.web("#44aacc"));pTag.setText("Rs "+String.format("%,.0f",AppContext.getPrice(v))+" / night");}});

        Button bookBtn = colorBtn("📋   CONFIRM BOOKING","#031a2e","#0066aa","#44aaff",14,true);
        bookBtn.setOnAction(e -> {
            BookingController.userBookRoom(uNameField.getText().trim(),uPhoneField.getText().trim(),
                    uEmailField.getText().trim(),uRoomBox.getValue(),uCiField.getText().trim(),uCoField.getText().trim());
            uNameField.clear(); uPhoneField.clear(); uEmailField.clear();
            uCiField.clear(); uCoField.clear(); uRoomBox.setValue(null);
        });

        // ── NEW Feature Buttons ──────────────────────────────────────
        Button feedbackBtn = colorBtn("⭐  SUBMIT FEEDBACK",    "#140022","#661188","#cc44ff",12,true);
        Button serviceBtn  = colorBtn("🍽️  ORDER ROOM SERVICE","#001a0e","#116633","#44cc88",12,true);
        Button calendarBtn = colorBtn("🏠  ROOM AVAILABILITY",  "#001020","#115566","#44aaff",12,true);

        feedbackBtn.setOnAction(e -> FeedbackView.showSubmit());
        serviceBtn.setOnAction(e  -> RoomServiceView.show());
        calendarBtn.setOnAction(e -> RoomCalendarView.show());

        // Rates card
        VBox ratesCard = new VBox(0);
        ratesCard.setStyle("-fx-background-color:#041220;-fx-border-color:#0a3355;-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;-fx-padding:4;");
        for (String[] r : new String[][]{{"🛏 Standard","Rs 5,000/night","#6699cc"},{"🛏 Deluxe","Rs 9,000/night","#44aaff"},{"🛏 Suite","Rs 15,000/night","#44ddff"},{"🛏 Penthouse","Rs 25,000/night","#00eeff"}}) {
            HBox rr = new HBox(8); rr.setAlignment(Pos.CENTER_LEFT); rr.setPadding(new Insets(8,12,8,12));
            rr.setStyle("-fx-border-color:#0a2a40;-fx-border-width:0 0 1 0;");
            Label rn = lbl(r[0],"Georgia",FontWeight.BOLD,12,r[2]); rn.setPrefWidth(100);
            rr.getChildren().addAll(rn, spacer(), lbl(r[1],"Georgia",12,"#5599bb"));
            ratesCard.getChildren().add(rr);
        }

        panel.getChildren().addAll(
                styled(lbl("🛏️  BOOK A ROOM","Georgia",FontWeight.EXTRA_BOLD,16,"#44bbff"),"-fx-effect:dropshadow(gaussian,#0099cc,6,0.3,0,0);"),
                lbl("Fill in your details below to make a reservation","Georgia",10,"#5599bb"),
                sep("#0a3355"), sectionLbl("👤  GUEST INFORMATION","#4488aa"),
                uNameField, uPhoneField, uEmailField, sep("#0a3355"),
                sectionLbl("🏷️  ROOM & DATES","#4488aa"),
                uRoomBox, priceCard, uCiField, uCoField, sep("#0a3355"),
                bookBtn, sep("#0a3355"),
                sectionLbl("✨  GUEST SERVICES","#4488aa"),
                feedbackBtn, serviceBtn, calendarBtn, sep("#0a3355"),
                sectionLbl("💰  ROOM RATES","#4488aa"), ratesCard
        );

        ScrollPane sc = new ScrollPane(panel); sc.setFitToWidth(true);
        sc.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        VBox wrap = new VBox(sc); VBox.setVgrow(sc,Priority.ALWAYS); wrap.setPrefWidth(400);
        wrap.setStyle("-fx-background-color:linear-gradient(to bottom,#04111e,#030d18);-fx-border-color:#0a3355;-fx-border-width:0 2 0 0;");
        return wrap;
    }

    // ── Bookings Panel (right) ────────────────────────────────────────
    private static VBox buildBookingsPanel() {
        VBox panel = new VBox(14); panel.setPadding(new Insets(22)); panel.setStyle("-fx-background-color:transparent;");

        HBox tb = new HBox(12); tb.setAlignment(Pos.CENTER_LEFT);
        Label pt = styled(lbl("📄  MY RESERVATIONS","Georgia",FontWeight.BOLD,16,"#44bbff"),"-fx-effect:dropshadow(gaussian,#0099cc,4,0.2,0,0);");
        TextField sf = userField("🔍  Search by name / ID..."); sf.setPrefWidth(220);
        sf.textProperty().addListener((o,ov,nv) -> AppContext.userTable.setItems(BookingController.filterUser(nv)));
        tb.getChildren().addAll(pt, spacer(), sf);

        AppContext.userTable = new TableView<>();
        AppContext.userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(AppContext.userTable, Priority.ALWAYS);
        AppContext.userTable.setFixedCellSize(44);
        AppContext.userTable.setStyle(
                "-fx-background-color:#040f1a;-fx-control-inner-background:#040f1a;-fx-control-inner-background-alt:#051525;" +
                        "-fx-border-color:#0a3355;-fx-border-radius:10;-fx-background-radius:10;" +
                        "-fx-table-cell-border-color:#0a2244;-fx-font-family:'Georgia';-fx-font-size:12px;"
        );
        AppContext.userTable.setRowFactory(tv -> {
            TableRow<Booking> row = new TableRow<>();
            row.selectedProperty().addListener((obs,ov,nv) -> row.setStyle(nv ? "-fx-background-color:#0d3a5c;" : ""));
            return row;
        });

        TableColumn<Booking,String> numC = tCol("#",null,"#336688",42);
        numC.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String i,boolean e){super.updateItem(i,e);setText(e?null:String.valueOf(getIndex()+1));setStyle("-fx-text-fill:"+(isSelected()?"white":"#336688")+";-fx-alignment:CENTER;");}
            public void updateSelected(boolean s){super.updateSelected(s);if(!isEmpty())setStyle("-fx-text-fill:"+(s?"white":"#336688")+";-fx-alignment:CENTER;");}
        });
        TableColumn<Booking,String> stC = tCol("STATUS","status","#ffffff",105);
        stC.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String s,boolean e){super.updateItem(s,e);if(e||s==null){setText(null);setStyle("");return;}setText(s);String c=isSelected()?"white":s.equals("Confirmed")?"#44aaff":s.equals("Checked Out")?"#44ddaa":"#ff4466";setStyle("-fx-text-fill:"+c+";-fx-font-weight:bold;-fx-alignment:CENTER;");}
            public void updateSelected(boolean sel){super.updateSelected(sel);if(!isEmpty())updateItem(getItem(),false);}
        });
        TableColumn<Booking,String> pyC = tCol("PAYMENT","payment","#ffffff",90);
        pyC.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String p,boolean e){super.updateItem(p,e);if(e||p==null){setText(null);setStyle("");return;}setText(p);String c=isSelected()?"white":p.equals("Paid")?"#44ddaa":"#ffcc00";setStyle("-fx-text-fill:"+c+";-fx-font-weight:bold;-fx-alignment:CENTER;");}
            public void updateSelected(boolean sel){super.updateSelected(sel);if(!isEmpty())updateItem(getItem(),false);}
        });

        AppContext.userTable.getColumns().addAll(numC,
                tCol("BOOKING ID","bookingId","#44aaff",100), tCol("👤 GUEST","guestName","#aaddff",0),
                tCol("📞 PHONE","phone","#9999bb",115), tCol("🛏 ROOM","roomType","#44bbff",100),
                tCol("CHECK IN","checkIn","#7799bb",105), tCol("CHECK OUT","checkOut","#7799bb",105),
                tColInt("NTS","nights","#9999bb",48), tColDbl("TOTAL(Rs)","totalPrice","#ffcc44",105),
                tCol("METHOD","paymentMethod","#6699aa",110), stC, pyC
        );
        Label ph = lbl("You have no reservations yet.\nUse the booking form on the left!","Georgia",14,"#336688");
        ph.setAlignment(Pos.CENTER); AppContext.userTable.setPlaceholder(ph);
        BookingController.refreshUserTable();

        Button coBtn  = actionBtn("🚪  CHECK OUT","#011520","#116655","#44ddaa");
        Button payBtn = actionBtn("💳  PROCESS PAYMENT","#150e00","#885500","#ffaa33");
        Button recBtn = actionBtn("🧾  VIEW RECEIPT","#001218","#115566","#44ccff");
        for (Button b : new Button[]{coBtn,payBtn,recBtn}) b.setPrefHeight(40);

        coBtn.setOnAction(e  -> BookingController.userCheckOut(AppContext.userTable.getSelectionModel().getSelectedItem()));
        payBtn.setOnAction(e -> {
            Booking sel = AppContext.userTable.getSelectionModel().getSelectedItem();
            if (sel==null){AppContext.showUserMsg("⚠  Select a booking!","#ffcc00");return;}
            if (sel.getPayment().equals("Paid")){AppContext.showUserMsg("⚠  Already paid!","#44ddaa");return;}
            if (sel.getStatus().equals("Cancelled")){AppContext.showUserMsg("⚠  Booking cancelled!","#ff4466");return;}
            PaymentView.show(sel);
        });
        recBtn.setOnAction(e -> {
            Booking sel = AppContext.userTable.getSelectionModel().getSelectedItem();
            if (sel==null){AppContext.showUserMsg("⚠  Select a booking!","#ffcc00");return;}
            ReceiptView.show(sel);
        });

        HBox ab = new HBox(12, coBtn, payBtn, recBtn); ab.setAlignment(Pos.CENTER_LEFT); ab.setPadding(new Insets(10,0,0,0));
        Label instr = lbl("ℹ  Select a booking, then use the buttons to Check Out, Pay, or View Receipt.","Georgia",10,"#5599bb");
        instr.setWrapText(true);
        panel.getChildren().addAll(tb, AppContext.userTable, ab, instr);
        return panel;
    }
}