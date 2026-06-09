package HotelSystem;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        AppContext.primaryStage = stage;
        AppContext.primaryStage.initStyle(StageStyle.UNDECORATED); // sirf ek baar
        DatabaseManager.loadUsers();
        LoginView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
