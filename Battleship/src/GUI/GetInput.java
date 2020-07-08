package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class GetInput {
    @FXML Pane window;
    @FXML TextField boardDim;
    @FXML ChoiceBox<Integer> shipsSizes;
    @FXML TextField shipCount;
    @FXML TextField seconds;
    @FXML Button dimSet;
    @FXML Button addShip;
    @FXML Button remShip;
    @FXML Button startGame;
    @FXML Button reset;
    @FXML TableView<ShipInfo> ships;
    @FXML TableColumn<ShipInfo, Integer> tShipSize;
    @FXML TableColumn<ShipInfo, Integer> tShipCount;
    ServerSocket serverSocket;
    Socket socket;
    int dim = 0;
    int boardSize = 0, filledSize = 0;
    public ObservableList<ShipInfo> list;

    private boolean isPVP = false;
    private boolean isPVC = false;
    private boolean isCVC = false;

    public void setPVP(boolean PVP) {
        isPVP = PVP;
    }

    public void setPVC(boolean PVC) {
        isPVC = PVC;
    }

    public void setCVC(boolean CVC) {
        isCVC = CVC;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @FXML
    public void initialize() {
        ships.setDisable(true);
        shipsSizes.setDisable(true);
        shipCount.setDisable(true);
        addShip.setDisable(true);
        remShip.setDisable(true);
        tShipSize.setCellValueFactory(new PropertyValueFactory<>("shipSize"));
        tShipCount.setCellValueFactory(new PropertyValueFactory<>("shipCount"));
        shipsSizes.getItems().add(2);
        shipsSizes.getItems().add(3);
        shipsSizes.getItems().add(4);
        shipsSizes.getItems().add(5);
        shipsSizes.getSelectionModel().selectFirst();
    }

    public void setDim(ActionEvent event) {
        if (!boardDim.getText().equals("")) {
            int temp=0;
            try {
                temp = Integer.parseInt(boardDim.getText());
            } catch (Exception ex) {
                return;
            }
            if (temp >= 5 && temp <= 30) {
                dim = temp;
                boardDim.setDisable(true);
                dimSet.setDisable(true);
                ships.setDisable(false);
                shipsSizes.setDisable(false);
                shipCount.setDisable(false);
                addShip.setDisable(false);
                remShip.setDisable(false);
                boardSize = dim * dim;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Board dimension must be between 5 and 30", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
            }
        }
    }

    public void addShip(ActionEvent event) {
        Integer tf1;
        String tf2;
        int size, count;
        tf1 = shipsSizes.getSelectionModel().getSelectedItem();
        tf2 = shipCount.getText();
        try {
            size = tf1;
            count = Integer.parseInt(tf2);
        } catch (Exception ex) {
            return;
        }
        if (size > dim || size < 1) {
            return;
        }
        int index = -1;
        for (int i = 0; i < ships.getItems().size(); i++) {
            if (ships.getItems().get(i).getShipSize() == size) {
                int oldCount = ships.getItems().get(i).getShipCount();
                filledSize -= size * oldCount;
                filledSize += size * count;
                if (filledSize > boardSize) {
                    filledSize -= size * count;
                    filledSize += size * oldCount;
                    return;
                }
                ships.getItems().get(i).setShipCount(count);
                index = i;
                break;
            }
        }
        if (index == -1) {
            filledSize += size * count;
            if (filledSize > boardSize) {
                filledSize -= size * count;
                return;
            }
            ships.getItems().add(new ShipInfo(size, count));
        }
        shipsSizes.requestFocus();
    }

    public void remShip(ActionEvent event) {
        int index = ships.getSelectionModel().getSelectedIndex();
        if (index == -1)
            return;
        ShipInfo ship = ships.getItems().get(index);
        filledSize -= ship.getShipSize() * ship.getShipCount();
        ships.getItems().remove(index);
    }

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        if (ships.getItems().size() < 1) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Must be at least 1 ship in the table", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }
        list = FXCollections.observableArrayList();
        list.addAll(ships.getItems());
        if (isPVP) {
            PairPlayers.sendData(list, dim);
        }
        FXMLLoader loader = new FXMLLoader();
        Scene scene;
        if (isPVC || isPVP) {
            loader.setLocation(getClass().getResource("PlaceShips.fxml"));
            Parent root = loader.load();
            scene = new Scene(root);

            PlaceShips controller = loader.getController();
            controller.setPVP(isPVP);
            controller.setHost(true);
            controller.setPVC(isPVC);
            controller.setCVC(isCVC);
            controller.initData(ships.getItems(), dim);
            controller.setServerSocket(serverSocket);
            controller.setSocket(socket);

            Stage nextWindow = new Stage();
            nextWindow.setTitle("Battleship");
            if (isPVP)
                nextWindow.setTitle("Host Player");
            nextWindow.setScene(scene);
            nextWindow.setResizable(false);
            nextWindow.show();
        }
        else if(isCVC) {
            loader.setLocation(getClass().getResource("PlayBoard.fxml"));
            Parent root = loader.load();
            scene = new Scene(root);

            int totalShips = 0;
            for (ShipInfo ship : ships.getItems()) {
                totalShips += ship.getShipCount();
            }

            PlayBoard controller = loader.getController();
            controller.setPVP(isPVP);
            controller.setPVC(isPVC);
            controller.setCVC(isCVC);
            controller.initData(dim, totalShips, ships.getItems());

            Stage nextWindow = new Stage();
            nextWindow.setTitle("Battleship");
            nextWindow.setScene(scene);
            nextWindow.setResizable(false);
            nextWindow.show();
        }


        Stage currentWindow = (Stage) startGame.getScene().getWindow();
        currentWindow.close();
    }

    public void backToMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE, "Back to menu ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Parent root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
            Scene scene = new Scene(root);
            Stage nextWindow = new Stage();
            nextWindow.setTitle("Battleship");
            nextWindow.setScene(scene);
            nextWindow.setResizable(false);
            nextWindow.show();
            Stage currentWindow = (Stage) window.getScene().getWindow();
            currentWindow.close();
        }
    }

    public void reset() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Reset all fields ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Reset");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            boardDim.setText("");
            shipsSizes.getSelectionModel().selectFirst();
            shipCount.setText("");
            ships.getItems().clear();
            boardDim.setDisable(false);
            dimSet.setDisable(false);
            ships.setDisable(true);
            shipsSizes.setDisable(true);
            shipCount.setDisable(true);
            addShip.setDisable(true);
            remShip.setDisable(true);
            boardDim.requestFocus();
        }
    }
}
