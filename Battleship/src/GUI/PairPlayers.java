package GUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class PairPlayers {
    @FXML Pane window;
    @FXML Button host;
    @FXML Button player;
    @FXML Label waiting;

    static boolean canStart = false;
    private boolean isHost = false;
    private ServerSocket serverSocket;
    static Socket socket;
    private int dim = 0;
    private ObservableList<ShipInfo> list = FXCollections.observableArrayList();
    private Connection con = new Connection();

    public void createGame() throws IOException {
        waiting.setVisible(false);
        serverSocket = new ServerSocket(9991);

        isHost = true;
        canStart = true;
        con.start();
    }

    public void joinGame() throws IOException {
        waiting.setVisible(false);
        try {
            socket = new Socket("localhost", 9991);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.NONE, "No Host Found", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        canStart = true;
        con.start();
    }

    static void sendData(ObservableList<ShipInfo> ships, int dim) throws IOException {
        canStart = true;
        OutputStream output = socket.getOutputStream();

        PrintWriter writer = new PrintWriter(output, true);

        StringBuilder data = new StringBuilder();
        data.append(dim).append(" ");
        data.append(ships.size()).append(" ");
        for (ShipInfo ship : ships) {
            data.append(ship.getShipSize()).append(" ");
            data.append(ship.getShipCount()).append(" ");
        }
        writer.println(data);
    }

    void receiveData() throws IOException {
        canStart = true;
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String[] data = reader.readLine().split(" ");
        dim = Integer.parseInt(data[0]);
        int listSize = Integer.parseInt(data[1]);
        for (int i = 2; i < listSize * 2 + 2; i++) {
            int shipSize = Integer.parseInt(data[i++]);
            int shipCount = Integer.parseInt(data[i]);
            list.add(new ShipInfo(shipSize, shipCount));
        }
    }

    public void backToMenu() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE, "Back to menu ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            canStart = false;
            if (socket != null)
                socket.close();
            if (serverSocket != null)
                serverSocket.close();
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

    private class Connection extends Thread {
        @Override
        public void run() {
            super.run();
            if (isHost) {
                Platform.runLater(() -> {
                    waiting.setText("Waiting for the Opponent");
                    waiting.setVisible(true);
                    waiting.setAlignment(Pos.CENTER);
                    host.setVisible(false);
                    player.setVisible(false);
                });
                try {
                    socket = serverSocket.accept();
                } catch (Exception ex) {
//                    Platform.runLater(() -> {
//                        (new Alert(Alert.AlertType.NONE, "No Player Found", ButtonType.OK)).showAndWait();
//                    });
                }
                if (canStart) {
                    Platform.runLater(() -> {
                        host.setVisible(false);
                        host.setVisible(false);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("GetInput.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new Scene(root);

                        GetInput controller = loader.getController();
                        controller.setPVP(true);
                        controller.setServerSocket(serverSocket);
                        controller.setSocket(socket);

                        Stage nextWindow = new Stage();
                        nextWindow.setTitle("Battleship");
                        nextWindow.setScene(scene);
                        nextWindow.setResizable(false);
                        nextWindow.show();

                        Stage currentWindow = (Stage) window.getScene().getWindow();
                        currentWindow.close();
                    });
                }
            }
            else {
                Platform.runLater(() -> {
                    waiting.setText("\t  Connected...\n" +
                                    "Waiting for the Opponent");
                    waiting.setVisible(true);
                    waiting.setAlignment(Pos.CENTER);
                    host.setVisible(false);
                    player.setVisible(false);
                });
                try {
                    receiveData();
                } catch (Exception ex) {
//                    e.printStackTrace();
                }
                if (canStart) {
                    Platform.runLater(() -> {
                        FXMLLoader loader = new FXMLLoader();
                        Scene scene;
                        loader.setLocation(getClass().getResource("PlaceShips.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        scene = new Scene(root);

                        PlaceShips controller = loader.getController();
                        controller.setPVP(true);
                        try {
                            controller.initData(list, dim);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        controller.setPVP(true);
                        controller.setHost(false);
                        controller.setSocket(socket);

                        Stage nextWindow = new Stage();
                        nextWindow.setTitle("Player");
                        nextWindow.setScene(scene);
                        nextWindow.setResizable(false);
                        nextWindow.show();

                        Stage currentWindow = (Stage) window.getScene().getWindow();
                        currentWindow.close();
                    });
                }
            }
        }
    }
}
