package GUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PlaceShips {
    @FXML Pane window;
    @FXML GridPane board = new GridPane();
    @FXML Button startGame;
    @FXML TableView<ShipInfo> ships;
    @FXML TableColumn<ShipInfo, Integer> tShipSize;
    @FXML TableColumn<ShipInfo, Integer> tShipCount;
    int boardDim = 0;
    int cellSize = 120;
    int totalShips = 0;
    int currentShips = 0;
    ArrayList<Ship> allShips;
    boolean[] allPlaced;
    int[][] shipsPlaces;
    int[][] shipsPlacesOpponent;
    ObservableList<ShipInfo> list;
    ServerSocket serverSocket;
    Socket socket;

    private boolean received = false;
    private boolean sent = false;
    private boolean isPVP = false;
    private boolean isHost = false;
    private boolean isPVC = false;
    private boolean isCVC = false;

    public void setPVP(boolean PVP) {
        isPVP = PVP;
    }

    public void setHost(boolean host) {
        isHost = host;
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

    public void initData(ObservableList<ShipInfo> list, int dim) throws IOException {
        ships.setItems(list);
        boardDim = dim;
        this.list = FXCollections.observableArrayList();
        for (ShipInfo ship : list) {
            this.list.add(new ShipInfo(ship.getShipSize(), ship.getShipCount()));
        }

        shipsPlaces = new int[boardDim][boardDim];
        for (ShipInfo shipInfo : list) {
            totalShips += shipInfo.getShipCount();
        }
        allShips = new ArrayList<>();
        allPlaced = new boolean[totalShips];


        window.getChildren().addAll(board);
        board.setLayoutX(10);
        board.setLayoutY(10);
        while (boardDim * cellSize > 500) {
            cellSize -= 2;
        }
        board.setPrefSize(boardDim * cellSize, boardDim * cellSize);
        board.setHgap(2);
        board.setVgap(2);
        board.setStyle("-fx-background-color: #89fc41; -fx-grid-lines-visible: true");
        for (int row = 0; row < boardDim; row++) {
            for (int col = 0; col < boardDim; col++) {
                Rectangle rec = new Rectangle();
                rec.setWidth(cellSize);
                rec.setHeight(cellSize);
                rec.setFill(Color.rgb(6, 21, 5));
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                board.getChildren().addAll(rec);
            }
        }
        tShipSize.setCellValueFactory(new PropertyValueFactory<>("shipSize"));
        tShipCount.setCellValueFactory(new PropertyValueFactory<>("shipCount"));

        if (isPVP) {
            shipsPlacesOpponent = new int[boardDim][boardDim];
            Network con = new Network();
            con.start();
        }
    }

    public void addShip(ActionEvent event) {
        ShipInfo boat = ships.getSelectionModel().getSelectedItem();
        if (boat == null)
            return;
        if (boat.getShipCount() < 1)
            return;
        boat.setShipCount(boat.getShipCount() - 1);
        currentShips += 1;
        int boatCellSize = cellSize;
        int strokeSize = (int) (cellSize * 0.1);

        Rectangle rec = new Rectangle();
        rec.setWidth(boatCellSize);
        rec.setHeight(boatCellSize * boat.getShipSize() + 2 * boat.getShipSize() - 2);
        rec.setStroke(Color.rgb(180, 21, 5));
        rec.setStrokeWidth(strokeSize);
        rec.setStrokeType(StrokeType.INSIDE);
        rec.setFill(Color.rgb(0, 0, 0));
        rec.setArcHeight(cellSize * 2);
        rec.setArcWidth(cellSize * 2);
        rec.setLayoutX(750 - cellSize);
        rec.setLayoutY(30);

        Ship ship = new Ship();
        ship.id = currentShips;
        ship.x = 650;
        ship.y = 30;
        ship.size = boat.getShipSize();
        ship.boat = rec;

        setBoatListeners(rec, boat.getShipSize());
        allShips.add(ship);
        window.getChildren().add(rec);
    }

    private boolean rotatable = true, dragged = false;
    private void setBoatListeners(Rectangle boat, int boatSize) {
        boat.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown())
                    return;
                for (int i = 0; i < currentShips; i++) {
                    if (allShips.get(i).boat.equals(boat)) {
                        allPlaced[allShips.get(i).id - 1] = false;
                    }
                }
            }
        });
        boat.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rotatable = false;
                dragged = true;
                boat.setLayoutX(mouseEvent.getSceneX() - cellSize / 2.0);
                boat.setLayoutY(mouseEvent.getSceneY() - cellSize / 2.0);
            }
        });
        boat.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (dragged) {
                    int mX = (int) mouseEvent.getSceneX();
                    int mY = (int) mouseEvent.getSceneY();
                    if (mX >= 10 && mX <= boardDim * cellSize + 2 * (boardDim - 1) && mY >= 10 && mY <= boardDim * cellSize + 2 * (boardDim - 1)) {
                        int col = (mX - 10) / (cellSize + 2);
                        int row = (mY - 10) / (cellSize + 2);
                        if (col * (cellSize + 2) + boat.getWidth() <= boardDim * cellSize + 2 * (boardDim - 1)
                            && row * (cellSize + 2) + boat.getHeight()<= boardDim * cellSize + 2 * (boardDim - 1)) {
                            boat.setLayoutX(col * cellSize + 10 + 2 * col);
                            boat.setLayoutY(row * cellSize + 10 + 2 * row);

                            for (int i = 0; i < currentShips; i++) {
                                if (allShips.get(i).boat.equals(boat)) {
                                    allPlaced[allShips.get(i).id - 1] = true;
                                    allShips.get(i).x = col;
                                    allShips.get(i).y = row;
                                }
                            }
                        }
                    }
                }
            }
        });
        boat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (rotatable)
                    rotate(boat);
                else
                    rotatable = true;
                dragged = false;
            }
        });
    }

    private void rotate(Rectangle boat) {
        double temp = boat.getWidth();
        boat.setWidth(boat.getHeight());
        boat.setHeight(temp);
    }

    public void startGame(ActionEvent event) throws IOException {
        if (currentShips < totalShips) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Place all ships on the board, please", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
            return;
        }
        for (int i = 0; i < totalShips; i++) {
            if (!allPlaced[i]) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Place all ships on the board, please", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
                return;
            }
        }
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                shipsPlaces[i][j] = 0;
            }
        }
        for (Ship ship : allShips) {
            int row = ship.y;
            int col = ship.x;
            if (ship.boat.getWidth() > ship.boat.getHeight()) {
                for (int i = 0; i < ship.size; i++) {
                    if (shipsPlaces[row][col + i] > 1) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "Don't place ships over each other, please", ButtonType.OK);
                        alert.setTitle("Error");
                        alert.showAndWait();
                        return;
                    }
                    else if (shipsPlaces[row][col + i] == 1) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "Don't place adjacent ships, please", ButtonType.OK);
                        alert.setTitle("Error");
                        alert.showAndWait();
                        return;
                    }
                    else
                        shipsPlaces[row][col + i] = ship.id + 1;
                }
                int left = col - 1, right = col + ship.size, top = row - 1, bottom = row + 1;
                if (left < 0)
                    left++;
                if (right >= boardDim)
                    right--;
                if (top < 0)
                    top++;
                if (bottom >= boardDim)
                    bottom--;
                for (int i = top; i <= bottom; i++) {
                    for (int j = left; j <= right; j++) {
                        if (shipsPlaces[i][j] == 0)
                            shipsPlaces[i][j] = 1;
                    }
                }
            }
            else if (ship.boat.getHeight() > ship.boat.getWidth()) {
                for (int i = 0; i < ship.size; i++) {
                    if (shipsPlaces[row + i][col] > 1) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "Don't place ships over each other, please", ButtonType.OK);
                        alert.setTitle("Error");
                        alert.showAndWait();
                        return;
                    }
                    else if (shipsPlaces[row + i][col] == 1) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "Don't place adjacent ships, please", ButtonType.OK);
                        alert.setTitle("Error");
                        alert.showAndWait();
                        return;
                    }
                    else
                        shipsPlaces[row + i][col] = ship.id + 1;
                }
                int left = col - 1, right = col + 1, top = row - 1, bottom = row + ship.size;
                if (left < 0)
                    left++;
                if (right >= boardDim)
                    right--;
                if (top < 0)
                    top++;
                if (bottom >= boardDim)
                    bottom--;
                for (int i = top; i <= bottom; i++) {
                    for (int j = left; j <= right; j++) {
                        if (shipsPlaces[i][j] == 0)
                            shipsPlaces[i][j] = 1;
                    }
                }
            }
        }

        if (isPVP) {
            startGame.setVisible(false);
            sendBoard();
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PlayBoard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        PlayBoard controller = loader.getController();
        controller.setPVP(isPVP);
        controller.setHost(isHost);
        controller.setPVC(isPVC);
        controller.setCVC(isCVC);
        controller.initData(shipsPlaces, boardDim, totalShips, list);

        Stage nextWindow = new Stage();
        nextWindow.setTitle("Battleship");
        nextWindow.setScene(scene);
        nextWindow.setResizable(false);

        nextWindow.show();
        Stage currentWindow = (Stage) window.getScene().getWindow();
        currentWindow.close();
    }

    private void sendBoard() throws IOException {
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                data.append(shipsPlaces[i][j]).append(" ");
            }
        }
        writer.println(data);
        sent = true;
    }

    private void receiveBoard() throws IOException {
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String[] receivedData = reader.readLine().split(" ");

        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                shipsPlacesOpponent[i][j] = Integer.parseInt(receivedData[i * boardDim + j]);
            }
        }
        received = true;
    }

    public void backToMenu() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE, "Back to menu ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            sent = false;
            received = false;
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

    static class Ship {
        int size;
        int x;
        int y;
        int id;
        Rectangle boat;
    }

    private class Network extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while (!received)
                    receiveBoard();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!received || !sent) {
                System.out.print("");
            }
            Platform.runLater(() -> {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("PlayBoard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert root != null;
                Scene scene = new Scene(root);

                PlayBoard controller = loader.getController();
                controller.setPVP(isPVP);
                controller.setHost(isHost);
                controller.setPVC(isPVC);
                controller.setCVC(isCVC);
                if (isHost) {
                    controller.setServerSocket(serverSocket);
                }
                controller.setSocket(socket);
                try {
                    controller.initData(shipsPlaces, shipsPlacesOpponent, boardDim, totalShips);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage nextWindow = new Stage();
                if (isHost)
                    nextWindow.setTitle("Host Player");
                else
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
