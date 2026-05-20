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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static HotelSystem.UIHelper.*;

public class ReceiptView {

    public static void show(Booking b) {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:#0a0a22;-fx-border-color:#44ccff;" +
                "-fx-border-width:2;-fx-border-radius:14;-fx-background-radius:14;" +
                "-fx-effect:dropshadow(gaussian,#44ccff,22,0.25,0,0);");
        root.setPrefWidth(460);

        // ── Header ────────────────────────────────────────────────────
        VBox hdr = new VBox(5);
        hdr.setAlignment(Pos.CENTER);
        hdr.setPadding(new Insets(28, 28, 20, 28));
        hdr.setStyle("-fx-background-color:#080818;-fx-background-radius:12 12 0 0;");
        hdr.getChildren().addAll(
                styled(lbl("🏨  GRAND HORIZON", "Georgia", FontWeight.EXTRA_BOLD, 24, "#d4aaff"),
                        "-fx-effect:dropshadow(gaussian,#9966ff,10,0.4,0,0);"),
                lbl("Hotel & Suites — Official Receipt", "Georgia", 11, "#9977bb"),
                new Separator(),
                lbl("Receipt No:  RCP-" + b.getBookingId().replace("BK-", ""),
                        "Georgia", FontWeight.BOLD, 13, "#44ccff"),
                lbl("Issued:  " + LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")),
                        "Georgia", 10, "#9977bb")
        );

        // ── Body ──────────────────────────────────────────────────────
        VBox body = new VBox(8);
        body.setPadding(new Insets(16, 28, 16, 28));

        addReceiptRow(body, "Booking ID",    b.getBookingId(),   "#44ccff");
        addReceiptRow(body, "Guest Name",    b.getGuestName(),   "#d4aaff");
        addReceiptRow(body, "Phone",         b.getPhone(),       "#bb99ff");
        if (!b.getEmail().isEmpty())
            addReceiptRow(body, "Email",     b.getEmail(),       "#bb99ff");
        addReceiptRow(body, "Room Type",     b.getRoomType(),    "#44aaff");
        addReceiptRow(body, "Check-In",      b.getCheckIn(),     "#99aadd");
        addReceiptRow(body, "Check-Out",     b.getCheckOut(),    "#99aadd");
        addReceiptRow(body, "No. of Nights", b.getNights() + "", "#bbbbdd");

        body.getChildren().add(dashedLine());

        double ppn = b.getTotalPrice() / Math.max(1, b.getNights());
        addReceiptRow(body, "Rate / Night",
                "Rs " + String.format("%,.0f", ppn), "#ffcc66");

        // Total row
        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.setPadding(new Insets(10, 0, 10, 0));
        totalRow.setStyle("-fx-background-color:#080812;-fx-border-color:#221144;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-padding:8;");
        totalRow.getChildren().addAll(
                lbl("TOTAL AMOUNT", "Georgia", FontWeight.EXTRA_BOLD, 15, "#ffcc00"),
                spacer(),
                styled(lbl("Rs " + String.format("%,.0f", b.getTotalPrice()),
                                "Georgia", FontWeight.EXTRA_BOLD, 22, "#ffcc00"),
                        "-fx-effect:dropshadow(gaussian,#ffcc00,8,0.3,0,0);")
        );
        body.getChildren().add(totalRow);

        addReceiptRow(body, "Payment Method", b.getPaymentMethod(), "#44ccff");

        // Payment status badge
        boolean paid = b.getPayment().equals("Paid");
        String bc = paid ? "#44ddaa" : "#ffcc00";
        HBox statusRow = new HBox();
        statusRow.setAlignment(Pos.CENTER_LEFT);
        Label statusBadge = lbl(paid ? "✅  PAID" : "⏳  PENDING",
                "Georgia", FontWeight.BOLD, 13, bc);
        statusBadge.setStyle("-fx-background-color:#08081a;-fx-border-color:" + bc +
                ";-fx-border-width:1.5;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-padding:5 12;-fx-effect:dropshadow(gaussian," + bc + ",6,0.3,0,0);");
        statusRow.getChildren().addAll(
                lbl("Payment Status", "Georgia", 12, "#9977bb"), spacer(), statusBadge
        );
        body.getChildren().add(statusRow);

        body.getChildren().add(dashedLine());
        addReceiptRow(body, "Booked By", b.getBookedBy(), "#9977bb");
        addReceiptRow(body, "Booked At", b.getBookedAt(), "#7766aa");

        // ── Footer ────────────────────────────────────────────────────
        VBox footer = new VBox(8);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(16, 28, 24, 28));
        footer.setStyle("-fx-background-color:#080818;-fx-background-radius:0 0 12 12;");

        Button closeBtn = new Button("✕  CLOSE");
        closeBtn.setStyle("-fx-background-color:#1a0008;-fx-border-color:#881133;" +
                "-fx-border-width:1.5;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-text-fill:#ff4466;-fx-font-family:'Georgia';-fx-font-weight:bold;" +
                "-fx-padding:8 20;-fx-cursor:hand;");
        closeBtn.setOnAction(e -> d.close());

        HBox btnRow = new HBox(closeBtn);
        btnRow.setAlignment(Pos.CENTER);

        footer.getChildren().addAll(
                styled(lbl("Thank you for choosing Grand Horizon!",
                                "Georgia", FontWeight.BOLD, 14, "#9966ff"),
                        "-fx-effect:dropshadow(gaussian,#9966ff,6,0.2,0,0);"),
                lbl("📍 Grand Horizon Hotel, Islamabad, Pakistan", "Georgia", 10, "#9977bb"),
                lbl("📞 +92-51-1234567  |  ✉  info@grandhorizon.pk", "Georgia", 10, "#9977bb"),
                btnRow
        );

        root.getChildren().addAll(hdr, dashedLine(), body, footer);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        Scene sc = new Scene(sp, 460, 700);
        sc.setFill(Color.TRANSPARENT);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }
}