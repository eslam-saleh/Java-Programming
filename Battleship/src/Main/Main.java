package Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static String fxmlFile = "/GUI/BattleshipCover.fxml";

    @Override
    public void start(Stage nextWindow) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        nextWindow.setTitle("Battleship");
        nextWindow.setScene(new Scene(root, 1000, 600));
        nextWindow.setResizable(false);
        nextWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
