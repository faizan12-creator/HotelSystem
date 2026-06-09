package HotelSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class FeedbackView {

    // ══════════════════════════════════════════════════════════════════
    //  GUEST — Submit Feedback
    // ══════════════════════════════════════════════════════════════════
    public static void showSubmit() {
        // Guest must have a confirmed booking
        ObservableList<Booking> myBookings = FXCollections.observableArrayList();
        for (Booking b : AppContext.bookingList)
            if (b.getBookedBy().equals(AppContext.currentUser)) myBookings.add(b);

        if (myBookings.isEmpty()) {
            AppContext.showUserMsg("⚠  You need a booking before submitting feedback!", "#ffcc00");
            return;
        }

        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(16);
        root.setPadding(new Insets(36, 40, 36, 40));
        root.setMaxWidth(480);
        root.setStyle("-fx-background-color:#0a0814;-fx-border-color:#9966ff;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect:dropshadow(gaussian,#9966ff,24,0.3,0,0);");

        // Header
        Label header = styled(
                lbl("⭐  GUEST FEEDBACK", "Georgia", FontWeight.EXTRA_BOLD, 22, "#d4aaff"),
                "-fx-effect:dropshadow(gaussian,#9966ff,8,0.3,0,0);"
        );
        Label subLbl = lbl("Share your experience with us", "Georgia", 11, "#9977bb");

        // Booking selector
        Label bookLbl = sectionLbl("📋  Select Your Booking", "#9977bb");
        ComboBox<String> bookingBox = new ComboBox<>();
        for (Booking b : myBookings)
            bookingBox.getItems().add(b.getBookingId() + "  —  " + b.getRoomType()
                    + "  (" + b.getCheckIn() + " to " + b.getCheckOut() + ")");
        bookingBox.setPromptText("Select booking *");
        styleAdminCombo(bookingBox);

        // Star rating
        Label ratingLbl = sectionLbl("⭐  Rating", "#9977bb");
        HBox starBox = new HBox(8);
        starBox.setAlignment(Pos.CENTER_LEFT);
        final int[] selectedRating = {0};
        Label[] stars = new Label[5];
        for (int i = 0; i < 5; i++) {
            final int starNum = i + 1;
            Label star = new Label("☆");
            star.setFont(javafx.scene.text.Font.font(32));
            star.setTextFill(Color.web("#443355"));
            star.setStyle("-fx-cursor:hand;");
            star.setOnMouseEntered(e -> {
                for (int j = 0; j < starNum; j++) stars[j].setTextFill(Color.web("#ffcc00"));
                for (int j = starNum; j < 5; j++) stars[j].setTextFill(Color.web("#443355"));
            });
            star.setOnMouseExited(e -> updateStars(stars, selectedRating[0]));
            star.setOnMouseClicked(e -> {
                selectedRating[0] = starNum;
                updateStars(stars, starNum);
            });
            stars[i] = star;
            starBox.getChildren().add(star);
        }
        Label ratingValue = lbl("Click to rate", "Georgia", 12, "#9977bb");
        starBox.getChildren().add(ratingValue);

        // Update rating label when star clicked
        for (int i = 0; i < 5; i++) {
            final int starNum = i + 1;
            stars[i].setOnMouseClicked(e -> {
                selectedRating[0] = starNum;
                updateStars(stars, starNum);
                String[] desc = {"", "Poor", "Fair", "Good", "Very Good", "Excellent"};
                ratingValue.setText(starNum + "/5 — " + desc[starNum]);
                ratingValue.setTextFill(Color.web("#ffcc00"));
            });
        }

        // Comment
        Label commentLbl = sectionLbl("💬  Your Comment", "#9977bb");
        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Tell us about your stay...");
        commentArea.setPrefRowCount(4);
        commentArea.setWrapText(true);
        commentArea.setStyle("-fx-background-color:#08081a;-fx-border-color:#332255;" +
                "-fx-border-width:1.5;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-text-fill:white;-fx-prompt-text-fill:#5544aa;" +
                "-fx-font-family:'Georgia';-fx-font-size:12;-fx-control-inner-background:#08081a;");

        Label errLbl = lbl("", "Georgia", 11, "#ff4466");

        // Buttons
        Button submitBtn = colorBtn("⭐  SUBMIT FEEDBACK", "#1a0033", "#7722cc", "#aa55ff", 13, true);
        Button closeBtn  = colorBtn("✕  CANCEL", "#1a0008", "#881133", "#ff4466", 12, true);

        submitBtn.setOnAction(e -> {
            if (bookingBox.getValue() == null) { errLbl.setText("⚠  Select a booking!"); return; }
            if (selectedRating[0] == 0)        { errLbl.setText("⚠  Please give a star rating!"); return; }
            if (commentArea.getText().trim().isEmpty()) { errLbl.setText("⚠  Please write a comment!"); return; }

            // Get selected booking
            int idx = bookingBox.getSelectionModel().getSelectedIndex();
            Booking b = myBookings.get(idx);

            String fid = "FB-" + (AppContext.feedbackCounter++);
            Feedback fb = new Feedback(fid, b.getBookingId(), b.getGuestName(),
                    selectedRating[0], commentArea.getText().trim(), AppContext.currentUser);
            AppContext.feedbackList.add(fb);
            AppContext.showUserMsg("⭐  Thank you for your feedback!", "#44ccff");
            d.close();
        });
        closeBtn.setOnAction(e -> d.close());

        root.getChildren().addAll(
                header, subLbl, sep("#332255"),
                bookLbl, bookingBox,
                ratingLbl, starBox,
                commentLbl, commentArea,
                errLbl, submitBtn, closeBtn
        );

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        Scene sc = new Scene(sp, 500, 620);
        sc.setFill(Color.TRANSPARENT);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }

    // ══════════════════════════════════════════════════════════════════
    //  ADMIN — View All Feedback
    // ══════════════════════════════════════════════════════════════════
    public static void showAdminView() {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        d.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(14);
        root.setPadding(new Insets(28, 32, 28, 32));
        root.setPrefWidth(720);
        root.setStyle("-fx-background-color:#0a0814;-fx-border-color:#9966ff;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;");

        // Header
        HBox hdr = new HBox(12);
        hdr.setAlignment(Pos.CENTER_LEFT);
        Label title = styled(
                lbl("⭐  ALL GUEST FEEDBACK", "Georgia", FontWeight.EXTRA_BOLD, 20, "#d4aaff"),
                "-fx-effect:dropshadow(gaussian,#9966ff,8,0.3,0,0);"
        );

        // Average rating
        double avg = AppContext.feedbackList.stream()
                .mapToInt(Feedback::getRating).average().orElse(0);
        Label avgLbl = lbl(String.format("Avg: %.1f ⭐  (%d reviews)",
                avg, AppContext.feedbackList.size()), "Georgia", 12, "#ffcc00");

        hdr.getChildren().addAll(title, spacer(), avgLbl);

        // Table
        TableView<Feedback> table = new TableView<>(AppContext.feedbackList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(380);
        table.setStyle("-fx-background-color:#080818;-fx-control-inner-background:#080818;" +
                "-fx-control-inner-background-alt:#0b0b22;-fx-border-color:#332255;" +
                "-fx-border-radius:10;-fx-background-radius:10;" +
                "-fx-font-family:'Georgia';-fx-font-size:12px;");

        // Columns
        TableColumn<Feedback, String> idCol = new TableColumn<>("FEEDBACK ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFeedbackId()));
        styleCol(idCol, "#9966ff", 90);

        TableColumn<Feedback, String> bkCol = new TableColumn<>("BOOKING ID");
        bkCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBookingId()));
        styleCol(bkCol, "#bb99ff", 90);

        TableColumn<Feedback, String> nameCol = new TableColumn<>("👤 GUEST");
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getGuestName()));
        styleCol(nameCol, "#d4aaff", 0);

        TableColumn<Feedback, String> starCol = new TableColumn<>("RATING");
        starCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStars()));
        starCol.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); return; }
                setText(s);
                int len = s.replaceAll("[^⭐]","").length();
                String color = len >= 4 ? "#44ddaa" : len == 3 ? "#ffcc00" : "#ff5577";
                setStyle("-fx-text-fill:" + color + ";-fx-alignment:CENTER;-fx-font-size:14;");
            }
        });
        starCol.setMinWidth(110); starCol.setMaxWidth(110);

        TableColumn<Feedback, String> commentCol = new TableColumn<>("💬 COMMENT");
        commentCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getComment()));
        styleCol(commentCol, "#aaaacc", 0);

        TableColumn<Feedback, String> dateCol = new TableColumn<>("DATE");
        dateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSubmittedAt()));
        styleCol(dateCol, "#9977bb", 130);

        table.getColumns().addAll(idCol, bkCol, nameCol, starCol, commentCol, dateCol);
        Label ph = lbl("No feedback yet.", "Georgia", 13, "#7755aa");
        ph.setAlignment(Pos.CENTER);
        table.setPlaceholder(ph);

        Button closeBtn = colorBtn("✕  CLOSE", "#1a0008", "#881133", "#ff4466", 12, false);
        closeBtn.setOnAction(e -> d.close());
        HBox footer = new HBox(closeBtn);
        footer.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(hdr, sep("#332255"), table, footer);
        Scene sc = new Scene(root, 740, 520);
        sc.setFill(Color.TRANSPARENT);
        d.setScene(sc);
        d.centerOnScreen();
        d.show();
    }

    // ── Star helper ───────────────────────────────────────────────────
    private static void updateStars(Label[] stars, int count) {
        for (int i = 0; i < 5; i++)
            stars[i].setTextFill(Color.web(i < count ? "#ffcc00" : "#443355"));
    }

    private static void styleCol(TableColumn<Feedback, String> col, String color, double w) {
        col.setStyle("-fx-alignment:CENTER;");
        col.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); setStyle(""); return; }
                setText(s);
                setStyle("-fx-text-fill:" + (isSelected() ? "white" : color) + ";-fx-alignment:CENTER;");
            }
            public void updateSelected(boolean sel) {
                super.updateSelected(sel);
                if (!isEmpty()) setStyle("-fx-text-fill:" + (sel ? "white" : color) + ";-fx-alignment:CENTER;");
            }
        });
        if (w > 0) { col.setMinWidth(w); col.setMaxWidth(w); }
    }
}