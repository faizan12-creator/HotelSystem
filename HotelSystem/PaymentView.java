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

import static HotelSystem.UIHelper.*;

public class PaymentView {

    public static void show(Booking sel) {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(16);
        root.setPadding(new Insets(36, 40, 36, 40));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color:#0d0d00;-fx-border-color:#886600;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect:dropshadow(gaussian,#ffcc00,24,0.3,0,0);");

        // ── Header ────────────────────────────────────────────────────
        Label header = styled(
                lbl("💳  PROCESS PAYMENT", "Georgia", FontWeight.EXTRA_BOLD, 20, "#ffcc00"),
                "-fx-effect:dropshadow(gaussian,#ffcc00,6,0.2,0,0);"
        );

        // ── Summary ───────────────────────────────────────────────────
        VBox summary = new VBox(8);
        summary.setStyle("-fx-background-color:#08080a;-fx-border-color:#332200;" +
                "-fx-border-radius:8;-fx-background-radius:8;-fx-padding:16;");
        addReceiptRow(summary, "Booking ID", sel.getBookingId(),           "#ffcc00");
        addReceiptRow(summary, "Guest Name", sel.getGuestName(),           "#ffddaa");
        addReceiptRow(summary, "Room Type",  sel.getRoomType(),            "#ffaa55");
        addReceiptRow(summary, "No. Nights", sel.getNights() + " nights",  "#ccaa66");

        HBox amtRow = new HBox();
        amtRow.setAlignment(Pos.CENTER_LEFT);
        Label ak = lbl("AMOUNT DUE", "Georgia", FontWeight.BOLD, 13, "#cc9900");
        Label av = styled(
                lbl("Rs " + String.format("%,.0f", sel.getTotalPrice()),
                        "Georgia", FontWeight.EXTRA_BOLD, 24, "#ffcc00"),
                "-fx-effect:dropshadow(gaussian,#ffcc00,10,0.4,0,0);"
        );
        amtRow.getChildren().addAll(ak, spacer(), av);
        summary.getChildren().addAll(sep("#332200"), amtRow);

        // ── Payment methods ───────────────────────────────────────────
        Label methodTitle = lbl("💰  Select Payment Method", "Georgia", FontWeight.BOLD, 14, "#cc9900");
        ToggleGroup mg = new ToggleGroup();
        RadioButton cashB   = payRB("💵  Cash Payment",           mg, "#44ddaa", "#003322");
        RadioButton cardB   = payRB("💳  Credit / Debit Card",    mg, "#44aaff", "#001a33");
        RadioButton onlineB = payRB("📱  Online Transfer / IBFT", mg, "#aa55ff", "#1a0033");
        RadioButton easyB   = payRB("📲  EasyPaisa",              mg, "#ff8800", "#2a1000");
        RadioButton jazzB   = payRB("📲  JazzCash",               mg, "#ff4400", "#2a0800");
        cashB.setSelected(true);

        TextField cardF = userField("Enter 16-digit Card Number");
        cardF.setVisible(false);
        cardF.setManaged(false);
        cardB.selectedProperty().addListener((o, ov, nv) -> {
            cardF.setVisible(nv);
            cardF.setManaged(nv);
        });

        VBox methodBox = new VBox(10, cashB, cardB, onlineB, easyB, jazzB, cardF);

        // ── Confirm / Cancel buttons ──────────────────────────────────
        Button conf = colorBtn("✔  CONFIRM PAYMENT", "#1a1000", "#886600", "#ffcc00", 13, true);
        Button canc = new Button("✕  CANCEL");
        canc.setMaxWidth(Double.MAX_VALUE);
        canc.setStyle("-fx-background-color:#1a0008;-fx-border-color:#881133;" +
                "-fx-border-width:1;-fx-border-radius:8;-fx-background-radius:8;" +
                "-fx-text-fill:#ff4466;-fx-padding:10;-fx-cursor:hand;");

        conf.setOnAction(e -> {
            if (cardB.isSelected() && !cardF.getText().matches("\\d{16}")) {
                AppContext.showMsg("⚠  Enter valid 16-digit card number!", "#ff4466");
                return;
            }
            String method = ((RadioButton) mg.getSelectedToggle())
                    .getText().replaceAll("[💵💳📱📲]\\s+", "").trim();
            BookingController.processPayment(sel, method);
            d.close();
            ReceiptView.show(sel);
        });
        canc.setOnAction(e -> d.close());

        root.getChildren().addAll(
                header, summary, methodTitle, methodBox, sep("#332200"), conf, canc
        );

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        Scene sc = new Scene(sp, 460, 620);
        sc.setFill(Color.TRANSPARENT);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }
}