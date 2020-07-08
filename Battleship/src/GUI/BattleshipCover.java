
package GUI;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.stage.Stage;

        import java.io.IOException;

public class BattleshipCover {
    @FXML Button welcome;
    @FXML
    public void welcome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene scene = new Scene(root);
        Stage nextWindow = new Stage();
        nextWindow.setTitle("Battleship");
        nextWindow.setScene(scene);
        nextWindow.setResizable(false);
        nextWindow.show();
        Stage currentWindow = (Stage) welcome.getScene().getWindow();
        currentWindow.close();
    }

}
