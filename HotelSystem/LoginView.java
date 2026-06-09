package HotelSystem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;

import static HotelSystem.UIHelper.*;

public class LoginView {

    public static void show() {

        // ── Full-screen root (same as AdminView/UserView) ─────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:#06060f;");

        // ── Top-right X button ─────────────────────────────────────────
        Button closeBtn = xBtn();
        closeBtn.setOnAction(e -> AppContext.primaryStage.close());
        StackPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(closeBtn, new Insets(14, 20, 0, 0));

        // ── Login card ─────────────────────────────────────────────────
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 48, 40, 48));
        card.setMaxWidth(420);
        card.setStyle(
                "-fx-background-color:#0a0a22;-fx-border-color:#9966ff;" +
                        "-fx-border-width:1.5;-fx-border-radius:16;-fx-background-radius:16;" +
                        "-fx-effect:dropshadow(gaussian,#9966ff,28,0.25,0,0);"
        );

        Label icon = lbl("🏨", 52, null, null);
        Label name = styled(
                lbl("GRAND HORIZON", "Georgia", FontWeight.EXTRA_BOLD, 26, "#d4aaff"),
                "-fx-effect:dropshadow(gaussian,#9966ff,10,0.4,0,0);"
        );
        Label sub = lbl("Hotel & Suites  ·  Staff Portal", "Georgia", 12, "#9977bb");
        Separator sep = sep("#443366");

        TextField     uField = loginField("👤  Username");
        PasswordField pField = new PasswordField();
        pField.setPromptText("🔒  Password");
        styleLoginField(pField);

        Label err = lbl("", "Georgia", 11, "#ff4466");

        // ── Demo credentials hint ──────────────────────────────────────
        VBox hint = new VBox(4);
        hint.setStyle(
                "-fx-background-color:#0d0d1a;-fx-border-color:#443366;" +
                        "-fx-border-radius:8;-fx-background-radius:8;-fx-padding:10;"
        );
        hint.getChildren().addAll(
                styled(lbl("Demo Credentials", "Georgia", FontWeight.BOLD, 11, "#9977cc"), ""),
                lbl("Admin  →  admin / admin123   |   manager / mgr456", "Georgia", 10, "#8866aa"),
                lbl("User   →  staff / staff789   |   guest / guest000", "Georgia", 10, "#8866aa")
        );

        // ── Buttons ────────────────────────────────────────────────────
        Button loginBtn  = colorBtn("🔓   SIGN IN",                "#1a0033","#7722cc","#aa55ff",14,true);
        Button regBtn    = colorBtn("📝   NEW CUSTOMER? REGISTER", "#001a11","#115533","#44cc88",12,true);
        Button exitBtn   = new Button("✕  EXIT");
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setStyle(
                "-fx-background-color:transparent;-fx-border-color:#663355;" +
                        "-fx-border-width:1;-fx-border-radius:6;-fx-background-radius:6;" +
                        "-fx-text-fill:#cc6699;-fx-font-family:'Georgia';-fx-padding:6;-fx-cursor:hand;"
        );

        regBtn.setOnAction(e  -> { AppContext.primaryStage.hide(); RegisterView.show(); });
        exitBtn.setOnAction(e -> AppContext.primaryStage.close());

        Runnable doLogin = () -> {
            String error = AuthController.login(uField.getText().trim(), pField.getText());
            if (error != null) {
                err.setText(error);
                pField.clear();
                return;
            }
            AuthController.launchMainApp();
        };
        loginBtn.setOnAction(e -> doLogin.run());
        pField.setOnAction(e   -> doLogin.run());

        card.getChildren().addAll(
                icon, name, sub, sep,
                uField, pField, err,
                loginBtn, regBtn, exitBtn,
                hint
        );

        // ── Put card + close button into root ──────────────────────────
        root.getChildren().addAll(card, closeBtn);

        // ── Use primaryStage (full screen like AdminView) ─────────────
        AppContext.primaryStage.setScene(new Scene(root, 1400, 840));
        AppContext.primaryStage.setMaximized(true);
        AppContext.primaryStage.show();
    }
}