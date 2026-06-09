package HotelSystem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static HotelSystem.UIHelper.*;

public class RegisterView {

    public static void show() {
        Stage s = new Stage();
        s.initStyle(StageStyle.UNDECORATED);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:#06060f;");

        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 48, 40, 48));
        card.setMaxWidth(440);
        card.setStyle("-fx-background-color:#04120a;-fx-border-color:#44cc88;" +
                "-fx-border-width:1.5;-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect:dropshadow(gaussian,#44cc88,28,0.25,0,0);");

        TextField     fnF = regField("👤  Full Name *");
        TextField     unF = regField("🔑  Username *");
        TextField     phF = regField("📞  Phone *");
        TextField     emF = regField("✉   Email (optional)");
        PasswordField p1  = new PasswordField(); p1.setPromptText("🔒  Password *");  styleRegField(p1);
        PasswordField p2  = new PasswordField(); p2.setPromptText("🔒  Confirm Password *"); styleRegField(p2);

        Label err  = lbl("", "Georgia", 11, "#ff4466"); err.setWrapText(true);
        Label note = lbl("ℹ  All new registrations get Guest (User) access.",
                "Georgia", 10, "#77aa88"); note.setWrapText(true);

        Button createBtn = colorBtn("✅   CREATE ACCOUNT", "#001a0e", "#116633", "#44cc88", 14, true);
        Button backBtn   = new Button("⬅   BACK TO LOGIN");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setStyle("-fx-background-color:transparent;-fx-border-color:#225544;" +
                "-fx-border-width:1;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-text-fill:#44aa77;-fx-font-family:'Georgia';-fx-padding:8;-fx-cursor:hand;");
        backBtn.setOnAction(e -> { s.close(); LoginView.show(); });

        createBtn.setOnAction(e -> {
            String error = AuthController.register(
                    fnF.getText().trim(), unF.getText().trim(),
                    p1.getText(), p2.getText(), phF.getText().trim()
            );
            if (error != null) { err.setText(error); return; }
            showSuccessPopup(s, unF.getText().trim(), fnF.getText().trim());
        });

        card.getChildren().addAll(
                lbl("📝", 48, null, null),
                styled(lbl("CREATE ACCOUNT", "Georgia", FontWeight.EXTRA_BOLD, 24, "#44cc88"),
                        "-fx-effect:dropshadow(gaussian,#44cc88,10,0.4,0,0);"),
                lbl("Register as a Guest Customer", "Georgia", 11, "#77bb99"),
                sep("#1a5533"),
                fnF, unF, p1, p2, phF, emF, note, err, createBtn, backBtn
        );

        ScrollPane sc = new ScrollPane(card);
        sc.setFitToWidth(true);
        sc.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        root.getChildren().add(sc);

        s.setScene(new Scene(root, 500, 700));
        s.centerOnScreen();
        s.show();
    }

    private static void showSuccessPopup(Stage parent, String username, String fullName) {
        Stage p = new Stage();
        p.initModality(Modality.APPLICATION_MODAL);
        p.initStyle(StageStyle.UNDECORATED);

        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.setStyle("-fx-background-color:#04120a;-fx-border-color:#44cc88;" +
                "-fx-border-width:2;-fx-border-radius:16;-fx-background-radius:16;" +
                "-fx-effect:dropshadow(gaussian,#44cc88,20,0.3,0,0);");

        Label info = lbl("Welcome, " + fullName + "!\nUsername: " + username +
                "\n\nYou can now login.", "Georgia", 13, "#88ddaa");
        info.setAlignment(Pos.CENTER);

        Button ok = colorBtn("🔓  GO TO LOGIN", "#115533", "#44cc88", "#44cc88", 13, false);
        ok.setOnAction(e -> { p.close(); parent.close(); LoginView.show(); });

        box.getChildren().addAll(
                lbl("✅", 52, null, null),
                styled(lbl("Account Created!", "Georgia", FontWeight.EXTRA_BOLD, 22, "#44cc88"), ""),
                info, ok
        );

        p.setScene(new Scene(box, 360, 300));
        p.centerOnScreen();
        p.show();
    }
}