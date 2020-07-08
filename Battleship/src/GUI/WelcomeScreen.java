package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;

public class WelcomeScreen {
    @FXML Button exit;

    public void pvp() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PairPlayers.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage nextWindow = new Stage();
        nextWindow.setTitle("Game Options");
        nextWindow.setScene(scene);
        nextWindow.setResizable(false);

        nextWindow.show();
        Stage currentWindow = (Stage) exit.getScene().getWindow();
        currentWindow.close();
    }

    public void pvc() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GetInput.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        GetInput controller = loader.getController();
        controller.setPVC(true);

        Stage nextWindow = new Stage();
        nextWindow.setTitle("Battleship");
        nextWindow.setScene(scene);
        nextWindow.setResizable(false);

        nextWindow.show();
        Stage currentWindow = (Stage) exit.getScene().getWindow();
        currentWindow.close();
    }

    public void cvc() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GetInput.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        GetInput controller = loader.getController();
        controller.setCVC(true);

        Stage nextWindow = new Stage();
        nextWindow.setTitle("Game Options");
        nextWindow.setScene(scene);
        nextWindow.setResizable(false);

        nextWindow.show();
        Stage currentWindow = (Stage) exit.getScene().getWindow();
        currentWindow.close();
    }

    public void load() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PlayBoard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        PlayBoard controller = loader.getController();
        controller.setPVC(true);
        if (!controller.loadFile())
            return;

        Stage nextWindow = new Stage();
        nextWindow.setTitle("Battleship");
        nextWindow.setScene(scene);
        nextWindow.setResizable(false);

        nextWindow.show();
        Stage currentWindow = (Stage) exit.getScene().getWindow();
        currentWindow.close();
    }

    public void exit() {
        exitWindow();
    }

    private void exitWindow() {
        Stage currentWindow = (Stage) exit.getScene().getWindow();
        currentWindow.close();
    }
}
