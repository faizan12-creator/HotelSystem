
package HotelSystem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
        import javafx.scene.layout.*;
        import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UIHelper {

    // ── Labels ────────────────────────────────────────────────────────
    public static Label lbl(String t, double size, String family, String color) {
        Label l = new Label(t);
        l.setFont(family != null ? Font.font(family, size) : Font.font(size));
        if (color != null) l.setTextFill(Color.web(color));
        return l;
    }

    public static Label lbl(String t, String family, double size, String color) {
        Label l = new Label(t);
        l.setFont(Font.font(family, size));
        if (color != null) l.setTextFill(Color.web(color));
        return l;
    }

    public static Label lbl(String t, String family, FontWeight fw, double size, String color) {
        Label l = new Label(t);
        l.setFont(Font.font(family, fw, size));
        if (color != null) l.setTextFill(Color.web(color));
        return l;
    }

    public static Label styled(Label l, String css) {
        if (!css.isEmpty()) l.setStyle(l.getStyle() + css);
        return l;
    }

    public static Label sectionLbl(String t, String c) {
        return styled(lbl(t, "Georgia", FontWeight.BOLD, 12, c), "");
    }

    public static Label badge(String t, String tc, String bg, String bc) {
        Label l = lbl(t, "Georgia", FontWeight.BOLD, 11, tc);
        l.setStyle("-fx-background-color:" + bg + ";-fx-border-color:" + bc +
                ";-fx-border-width:1.5;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-padding:5 12;-fx-effect:dropshadow(gaussian," + tc + ",6,0.2,0,0);");
        return l;
    }

    // ── Separators & Spacers ─────────────────────────────────────────
    public static Separator sep(String color) {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:" + color + ";");
        return s;
    }

    public static Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    public static Label dashedLine() {
        Label l = lbl("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -",
                "Courier New", 10, "#332255");
        l.setPadding(new Insets(2, 0, 2, 0));
        return l;
    }

    // ── Layout Helpers ────────────────────────────────────────────────
    public static HBox hbox(double spacing, String bg, Insets pad) {
        HBox h = new HBox();
        h.setSpacing(spacing);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setStyle("-fx-background-color:" + bg + ";");
        h.setPadding(pad);
        return h;
    }

    public static HBox row(VBox... cards) {
        HBox h = new HBox(10, cards);
        h.setAlignment(Pos.CENTER);
        return h;
    }

    // ── Buttons ───────────────────────────────────────────────────────
    public static Button colorBtn(String text, String bg, String border, String glow,
                                  double fs, boolean full) {
        Button b = new Button(text);
        if (full) b.setMaxWidth(Double.MAX_VALUE);
        b.setFont(Font.font("Georgia", FontWeight.BOLD, fs));
        String base = "-fx-background-color:" + bg + ";-fx-border-color:" + border +
                ";-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;" +
                "-fx-text-fill:white;-fx-padding:12;-fx-cursor:hand;";
        String hov  = "-fx-background-color:" + border + ";-fx-border-color:" + glow +
                ";-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;" +
                "-fx-text-fill:white;-fx-padding:12;-fx-cursor:hand;" +
                "-fx-effect:dropshadow(gaussian," + glow + ",14,0.4,0,0);";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hov));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }

    public static Button actionBtn(String t, String bg, String border, String glow) {
        Button b = new Button(t);
        String base = "-fx-background-color:" + bg + ";-fx-border-color:" + border +
                ";-fx-border-width:1.5;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-text-fill:white;-fx-font-family:'Georgia';-fx-font-weight:bold;" +
                "-fx-font-size:12;-fx-padding:8 12;-fx-cursor:hand;";
        String ov   = "-fx-background-color:" + border + ";-fx-border-color:" + glow +
                ";-fx-border-width:1.5;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-text-fill:white;-fx-font-family:'Georgia';-fx-font-weight:bold;" +
                "-fx-font-size:12;-fx-padding:8 12;-fx-cursor:hand;" +
                "-fx-effect:dropshadow(gaussian," + glow + ",10,0.4,0,0);";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(ov));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }

    public static Button xBtn() {
        Button b = new Button("✕");
        String base = "-fx-background-color:transparent;-fx-text-fill:#cc4466;" +
                "-fx-font-size:20;-fx-font-weight:bold;-fx-cursor:hand;-fx-padding:0 12;";
        String hov  = "-fx-background-color:#cc4466;-fx-text-fill:white;" +
                "-fx-font-size:20;-fx-font-weight:bold;-fx-cursor:hand;-fx-padding:0 12;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hov));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }

    // ── TextFields ────────────────────────────────────────────────────
    public static TextField adminField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        String base = "-fx-background-color:#08081a;-fx-border-color:#332255;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#5544aa;-fx-padding:8 10;-fx-font-family:'Georgia';-fx-font-size:12;";
        String foc  = "-fx-background-color:#0b0b22;-fx-border-color:#9966ff;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#5544aa;-fx-padding:8 10;-fx-font-family:'Georgia';-fx-font-size:12;";
        f.setStyle(base);
        f.focusedProperty().addListener((o, ov, nv) -> f.setStyle(nv ? foc : base));
        return f;
    }

    public static TextField userField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        String base = "-fx-background-color:#040f1a;-fx-border-color:#0a3355;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#3a6688;-fx-padding:8 10;-fx-font-family:'Georgia';-fx-font-size:12;";
        String foc  = "-fx-background-color:#051525;-fx-border-color:#0088cc;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#3a6688;-fx-padding:8 10;-fx-font-family:'Georgia';-fx-font-size:12;";
        f.setStyle(base);
        f.focusedProperty().addListener((o, ov, nv) -> f.setStyle(nv ? foc : base));
        return f;
    }

    public static TextField loginField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        styleLoginField(f);
        return f;
    }

    public static TextField regField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        styleRegField(f);
        return f;
    }

    public static void styleLoginField(TextField f) {
        String base = "-fx-background-color:#08081a;-fx-border-color:#332255;-fx-border-width:1.5;" +
                "-fx-border-radius:8;-fx-background-radius:8;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#7755aa;-fx-padding:12 14;-fx-font-family:'Georgia';-fx-font-size:13;";
        String foc  = "-fx-background-color:#0b0b22;-fx-border-color:#9966ff;-fx-border-width:1.5;" +
                "-fx-border-radius:8;-fx-background-radius:8;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#7755aa;-fx-padding:12 14;-fx-font-family:'Georgia';-fx-font-size:13;";
        f.setStyle(base);
        f.focusedProperty().addListener((o, ov, nv) -> f.setStyle(nv ? foc : base));
    }

    public static void styleRegField(TextField f) {
        String base = "-fx-background-color:#030e08;-fx-border-color:#1a5533;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#3a7755;-fx-padding:10 12;-fx-font-family:'Georgia';-fx-font-size:12;";
        String foc  = "-fx-background-color:#041810;-fx-border-color:#44cc88;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#3a7755;-fx-padding:10 12;-fx-font-family:'Georgia';-fx-font-size:12;";
        f.setStyle(base);
        f.focusedProperty().addListener((o, ov, nv) -> f.setStyle(nv ? foc : base));
    }

    // ── ComboBox Styles ───────────────────────────────────────────────
    public static void styleAdminCombo(ComboBox<String> cb) {
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setStyle("-fx-background-color:#08081a;-fx-border-color:#332255;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#5544aa;-fx-font-family:'Georgia';-fx-font-size:12;");
    }

    public static void styleUserCombo(ComboBox<String> cb) {
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setStyle("-fx-background-color:#040f1a;-fx-border-color:#0a3355;-fx-border-width:1.5;" +
                "-fx-border-radius:6;-fx-background-radius:6;-fx-text-fill:white;" +
                "-fx-prompt-text-fill:#3a6688;-fx-font-family:'Georgia';-fx-font-size:12;");
    }

    // ── Stat Card ─────────────────────────────────────────────────────
    public static VBox statCard(String title, Label val, String color) {
        val.setTextFill(Color.web(color));
        val.setStyle("-fx-effect:dropshadow(gaussian," + color + ",4,0.15,0,0);");
        Label t = lbl(title, "Georgia", 9, "#8866aa");
        VBox card = new VBox(3, t, val);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setPrefWidth(130);
        card.setStyle("-fx-background-color:#08081a;-fx-border-color:" + color +
                ";-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;" +
                "-fx-effect:dropshadow(gaussian," + color + ",4,0.1,0,0);");
        return card;
    }

    // ── Table Columns ─────────────────────────────────────────────────
    public static <T> TableColumn<Booking, T> tCol(String title, String prop,
                                                   String color, double width) {
        TableColumn<Booking, T> col = new TableColumn<>(title);
        if (prop != null)
            col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
        col.setStyle("-fx-alignment:CENTER;-fx-text-fill:" + color + ";");
        if (width > 0) { col.setMinWidth(width); col.setMaxWidth(width); }
        return col;
    }

    public static TableColumn<Booking, Double> tColDbl(String title, String prop,
                                                       String color, double width) {
        TableColumn<Booking, Double> col = new TableColumn<>(title);
        col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
        col.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : String.format("%,.0f", v));
                setStyle("-fx-text-fill:" + color + ";-fx-alignment:CENTER;-fx-font-weight:bold;");
            }
        });
        if (width > 0) { col.setMinWidth(width); col.setMaxWidth(width); }
        return col;
    }

    public static TableColumn<Booking, Integer> tColInt(String title, String prop,
                                                        String color, double width) {
        TableColumn<Booking, Integer> col = new TableColumn<>(title);
        col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
        col.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(Integer v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : String.valueOf(v));
                setStyle("-fx-text-fill:" + color + ";-fx-alignment:CENTER;");
            }
        });
        if (width > 0) { col.setMinWidth(width); col.setMaxWidth(width); }
        return col;
    }

    // ── Room Rates Box ────────────────────────────────────────────────
    public static VBox buildRoomRatesBox() {
        VBox box = new VBox(6);
        String[][] rooms = {
                {"🛏 Standard",  "Rs 5,000/night",  "#9966ff"},
                {"🛏 Deluxe",    "Rs 9,000/night",  "#44aaff"},
                {"🛏 Suite",     "Rs 15,000/night", "#44ddaa"},
                {"🛏 Penthouse", "Rs 25,000/night", "#ffcc44"}
        };
        for (String[] r : rooms) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            Label n = lbl(r[0], "Georgia", 11, r[2]);
            n.setPrefWidth(90);
            row.getChildren().addAll(n, lbl(r[1], "Georgia", 11, "#9977bb"));
            box.getChildren().add(row);
        }
        return box;
    }

    // ── Receipt Row ───────────────────────────────────────────────────
    public static void addReceiptRow(javafx.scene.layout.VBox parent,
                                     String key, String val, String vc) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label k = lbl(key, "Georgia", 12, "#9977bb");
        k.setPrefWidth(145);
        row.getChildren().addAll(k, spacer(),
                lbl(val, "Georgia", FontWeight.BOLD, 12, vc));
        parent.getChildren().add(row);
    }

    // ── Payment RadioButton ───────────────────────────────────────────
    public static RadioButton payRB(String text, ToggleGroup g,
                                    String color, String bg) {
        RadioButton rb = new RadioButton(text);
        rb.setToggleGroup(g);
        rb.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        rb.setTextFill(Color.web(color));
        rb.setPadding(new Insets(8, 14, 8, 14));
        rb.setMaxWidth(Double.MAX_VALUE);
        rb.setStyle("-fx-cursor:hand;-fx-background-color:" + bg +
                ";-fx-border-color:" + color + "33;-fx-border-radius:6;-fx-background-radius:6;");
        return rb;
    }
}