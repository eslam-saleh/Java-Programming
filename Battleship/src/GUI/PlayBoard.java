package GUI;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;

public class PlayBoard {
    @FXML private Pane window;
    @FXML private GridPane board1 = new GridPane();
    @FXML private GridPane board2 = new GridPane();
    @FXML private Label turn;
    @FXML private Label turn1;
    @FXML private Label turn2;
    @FXML private Button save;
    @FXML private Button nextMove;
    private int boardDim = 0;
    private int cellSize = 120;
    private int totalShips = 0;
    private boolean p1Turn = true;
    private boolean gameOver = false;
    private boolean youWin = false;
    private boolean isPVP = false;
    private boolean isHost = false;
    private boolean isPVC = false;
    private boolean isCVC = false;
    private int[][] shipsPlaces1;
    private int[][] shipsPlaces2;
    private AiMove aiP1, aiP2;
    ServerSocket serverSocket;
    Socket socket;

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

    public void initData(int[][] board, int[][] opponentBoard, int dim, int totalShips) throws IOException {
        boardDim = dim;
        shipsPlaces1 = new int[boardDim][boardDim];
        shipsPlaces2 = new int[boardDim][boardDim];
        this.totalShips = totalShips;
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                shipsPlaces1[i][j] = board[i][j];
            }
        }
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                shipsPlaces2[i][j] = opponentBoard[i][j];
            }
        }
        initBoard();
        if (!isHost) {
            p1Turn = false;
            board2.setDisable(true);
            turn.setText("Opponent Turn");
            turn.setAlignment(Pos.CENTER);

//            InputStream input = socket.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//            String[] data = reader.readLine().split(" ");
//            int row = Integer.parseInt(data[0]);
//            int col = Integer.parseInt(data[1]);
//
//            opponentMove(row, col);
        }
        Network dataReceiver = new Network();
        dataReceiver.start();
    }

    public void initData(int[][] board, int dim, int totalShips, ObservableList<ShipInfo> shipsData) {
        boardDim = dim;
        shipsPlaces1 = new int[boardDim][boardDim];
        shipsPlaces2 = new int[boardDim][boardDim];
        this.totalShips = totalShips;
        if (isPVP || isPVC) {
            for (int i = 0; i < boardDim; i++) {
                for (int j = 0; j < boardDim; j++) {
                    shipsPlaces1[i][j] = board[i][j];
                }
            }
        }
        if (isPVC) {
            AI ai = new AI(boardDim, shipsData);
            shipsPlaces2 = ai.createBoard();
        }
        initBoard();
    }

    public void initData(int dim, int totalShips, ObservableList<ShipInfo> shipsData) {
        boardDim = dim;
        shipsPlaces1 = new int[boardDim][boardDim];
        shipsPlaces2 = new int[boardDim][boardDim];
        this.totalShips = totalShips;
        if (isCVC) {
            AI ai1 = new AI(boardDim, shipsData);
            AI ai2 = new AI(boardDim, shipsData);
            shipsPlaces1 = ai1.createBoard();
            shipsPlaces2 = ai2.createBoard();
        }
        initBoard();
    }

    private void initBoard() {
        while (boardDim * cellSize > 500) {
            cellSize -= 2;
        }
        window.getChildren().addAll(board1, board2);
        boolean temp = p1Turn;
        p1Turn = true;
        createBoard(board1, false);
        p1Turn = !p1Turn;
        createBoard(board2, !isCVC);
        p1Turn = temp;
        board2.setLayoutX(1090 - boardDim * cellSize - 2 * (boardDim - 1));
        if (!isPVC)
            save.setVisible(false);
        if (isPVP) {
            if (isHost)
                turn.setText("Your Turn");
            else
                turn.setText("Opponent Turn");
        }
        if (isPVC) {
            aiP2 = new AiMove();
            turn.setText("Your Turn");
        }
        if (isCVC) {
            aiP1 = new AiMove();
            aiP2 = new AiMove();
            turn.setText("Ai 1 Turn");
            turn1.setText("Ai 1 Board");
            turn2.setText("Ai 2 Board");
        }
        else
            nextMove.setVisible(false);
        if (isPVP && !isHost) {
            p1Turn = false;
            board2.setDisable(true);
        }
        turn.setAlignment(Pos.CENTER);
        turn1.setAlignment(Pos.CENTER_LEFT);
        turn2.setAlignment(Pos.CENTER_RIGHT);
    }

    private void createBoard(GridPane board, boolean serListeners) {
        board.setLayoutX(10);
        board.setLayoutY(50);
        board.setPrefSize(boardDim * cellSize, boardDim * cellSize);
        board.setHgap(2);
        board.setVgap(2);
        board.setStyle("-fx-background-color: #89fc41; -fx-grid-lines-visible: true");
        for (int row = 0; row < boardDim; row++) {
            for (int col = 0; col < boardDim; col++) {
                Rectangle rec = new Rectangle();
                rec.setWidth(cellSize);
                rec.setHeight(cellSize);
                if (p1Turn) {
                    if (shipsPlaces1[row][col] >= 0)
                        rec.setFill(Color.rgb(6, 21, 5));
                    else if (shipsPlaces1[row][col] == -1)
                        rec.setFill(Color.rgb(4, 12, 236));
                    else if (shipsPlaces1[row][col] < -500)
                        rec.setFill(Color.rgb(136, 12, 4));
                    else
                        rec.setFill(Color.rgb(220, 110, 0));
                }
                else {
                    if (shipsPlaces2[row][col] >= 0)
                        rec.setFill(Color.rgb(6, 21, 5));
                    else if (shipsPlaces2[row][col] == -1)
                        rec.setFill(Color.rgb(4, 12, 236));
                    else if (shipsPlaces2[row][col] < -500)
                        rec.setFill(Color.rgb(136, 12, 4));
                    else
                        rec.setFill(Color.rgb(220, 110, 0));
                }
                int finalCol = col;
                int finalRow = row;
                if (serListeners) {
                    rec.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (!mouseEvent.isPrimaryButtonDown())
                                return;
                            if (p1Turn && !gameOver){
                                if (shipsPlaces2[finalRow][finalCol] == 0 || shipsPlaces2[finalRow][finalCol] == 1) {
                                    shipsPlaces2[finalRow][finalCol] = -1;
                                    rec.setFill(Color.rgb(4, 12, 236));
                                }
                                else if (shipsPlaces2[finalRow][finalCol] > 1) {
                                    int id = shipsPlaces2[finalRow][finalCol];
                                    shipsPlaces2[finalRow][finalCol] = -shipsPlaces2[finalRow][finalCol];
                                    boolean inRow = false, inCol = false;
                                    for (int i = 0; i < boardDim; i++) {
                                        if (shipsPlaces2[finalRow][i] == id) {
                                            inRow = true;
                                            break;
                                        }
                                    }
                                    for (int i = 0; i < boardDim; i++) {
                                        if (shipsPlaces2[i][finalCol] == id) {
                                            inCol = true;
                                            break;
                                        }
                                    }
                                    /// ship destroyed
                                    if (!inRow && !inCol) {
                                        for (int i = 0; i < boardDim; i++) {
                                            if (shipsPlaces2[finalRow][i] == -id) {
                                                shipsPlaces2[finalRow][i] = -id * 10000;
                                                ((Rectangle) (board2.getChildren().get(finalRow * boardDim + i))).setFill(Color.rgb(136, 12, 4));
                                            }
                                        }
                                        for (int i = 0; i < boardDim; i++) {
                                            if (shipsPlaces2[i][finalCol] == -id) {
                                                shipsPlaces2[i][finalCol] = -id * 10000;
                                                ((Rectangle) (board2.getChildren().get(i * boardDim + finalCol))).setFill(Color.rgb(136, 12, 4));
                                            }
                                        }
                                    }
                                    else
                                        rec.setFill(Color.rgb(220, 110, 0));
                                }
                                checkVictory();
                                p1Turn = !p1Turn;
                                board2.setDisable(true);
                                rec.setDisable(true);

                                if (!p1Turn && isPVC)
                                    aiP2.aiTurn(shipsPlaces1);

                                if (isPVP) {
                                    if (!youWin) {
                                        turn.setText("Opponent Turn");
                                        turn.setAlignment(Pos.CENTER);
                                    }
                                    OutputStream output = null;
                                    try {
                                        output = socket.getOutputStream();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    PrintWriter writer = new PrintWriter(output, true);
                                    String data = finalRow + " " + finalCol;
                                    if (gameOver)
                                        data += " 1";
                                    else
                                        data += " 0";
                                    writer.println(data);
                                }
                            }
                        }
                    });
                }
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                board.getChildren().addAll(rec);
            }
        }
    }

    public void nextMove(ActionEvent event) {
        if (p1Turn) {
            aiP1.aiTurn(shipsPlaces2);
            turn.setText("Ai 2 Turn");
        }
        else {
            aiP2.aiTurn(shipsPlaces1);
            turn.setText("Ai 1 Turn");
        }
        turn.setAlignment(Pos.CENTER);
        if (gameOver)
            nextMove.setDisable(true);
    }

    class AiMove {
        int firstX = -1, firstY = -1;
        int lastX = -1, lastY = -1, dir = 0;
        boolean isShip = false;

        public void aiTurn(int[][] shipsPlaces) {
            if (gameOver)
                return;
            int row = 0, col = 0;
            if (lastX == -1 || !isShip) {
                row = (int) (Math.random() * (boardDim));
                col = (int) (Math.random() * (boardDim));
                while (shipsPlaces[row][col] < 0) {
                    row = (int) (Math.random() * (boardDim));
                    col = (int) (Math.random() * (boardDim));
                }
            } else {
                if (dir == 0 && isShip) {
                    dir = (int) (Math.random() * 4) + 1;
                }
                if (!isValid(shipsPlaces)) {
                    dir = (dir + 1) % 4 + 1;
                    lastX = firstX;
                    lastY = firstY;
                }
                int c = 0;
                while (!isValid(shipsPlaces) && c++ < 4)
                    dir = dir % 4 + 1;
                if (c == 4) {
                    dir = 0;
                    isShip = false;
                    row = (int) (Math.random() * (boardDim));
                    col = (int) (Math.random() * (boardDim));
                    while (shipsPlaces[row][col] < 0) {
                        row = (int) (Math.random() * (boardDim));
                        col = (int) (Math.random() * (boardDim));
                    }
                }
                switch (dir) {
                    case 1 -> {
                        row = lastY;
                        col = lastX + 1;
                    }
                    case 2 -> {
                        row = lastY + 1;
                        col = lastX;
                    }
                    case 3 -> {
                        row = lastY;
                        col = lastX - 1;
                    }
                    case 4 -> {
                        row = lastY - 1;
                        col = lastX;
                    }
                }
            }
            Rectangle rec;
            if (p1Turn)
                rec = (Rectangle) (board2.getChildren().get(row * boardDim + col));
            else
                rec = (Rectangle) (board1.getChildren().get(row * boardDim + col));
            if (shipsPlaces[row][col] == 0 || shipsPlaces[row][col] == 1) {
                shipsPlaces[row][col] = -1;
                rec.setFill(Color.rgb(4, 12, 236));
                if (isShip) {
                    if (dir != 0) {
                        dir = (dir + 1) % 4 + 1;
                    }
                    lastX = firstX;
                    lastY = firstY;
                    int c = 0;
                    while (!isValid(shipsPlaces) && c++ < 4)
                        dir = dir % 4 + 1;
                    if (c == 4) {
                        dir = 0;
                        isShip = false;
                    }
                    checkVictory();
                    p1Turn = !p1Turn;
                    if (isPVC)
                        board2.setDisable(false);
                    return;
                } else {
                    isShip = false;
                    dir = 0;
                }
            } else if (shipsPlaces[row][col] > 1) {
                if (!isShip) {
                    firstX = col;
                    firstY = row;
                    isShip = true;
                }
                int id = shipsPlaces[row][col];
                shipsPlaces[row][col] = -shipsPlaces[row][col];
                boolean inRow = false, inCol = false;
                for (int i = 0; i < boardDim; i++) {
                    if (shipsPlaces[row][i] == id) {
                        inRow = true;
                        break;
                    }
                }
                for (int i = 0; i < boardDim; i++) {
                    if (shipsPlaces[i][col] == id) {
                        inCol = true;
                        break;
                    }
                }
                /// ship destroyed
                if (!inRow && !inCol) {
                    for (int i = 0; i < boardDim; i++) {
                        if (shipsPlaces[row][i] == -id) {
                            shipsPlaces[row][i] = -id * 10000;
                            if (p1Turn)
                                ((Rectangle) (board2.getChildren().get(row * boardDim + i))).setFill(Color.rgb(136, 12, 4));
                            else
                                ((Rectangle) (board1.getChildren().get(row * boardDim + i))).setFill(Color.rgb(136, 12, 4));
                        }
                    }
                    for (int i = 0; i < boardDim; i++) {
                        if (shipsPlaces[i][col] == -id) {
                            shipsPlaces[i][col] = -id * 10000;
                            if (p1Turn)
                                ((Rectangle) (board2.getChildren().get(i * boardDim + col))).setFill(Color.rgb(136, 12, 4));
                            else
                                ((Rectangle) (board1.getChildren().get(i * boardDim + col))).setFill(Color.rgb(136, 12, 4));
                        }
                    }
                    isShip = false;
                    dir = 0;
                } else
                    rec.setFill(Color.rgb(220, 110, 0));
            }
            lastX = col;
            lastY = row;
            checkVictory();
            p1Turn = !p1Turn;
            if (isPVP || isPVC)
                board2.setDisable(false);
        }

        private boolean isValid(int[][] shipsPlaces) {
            switch (dir) {
                case 1 -> {
                    if (lastX + 1 < boardDim) {
                        return shipsPlaces[lastY][lastX + 1] >= 0;
                    }
                }
                case 2 -> {
                    if (lastY + 1 < boardDim)
                        return shipsPlaces[lastY + 1][lastX] >= 0;
                }
                case 3 -> {
                    if (lastX - 1 >= 0)
                        return shipsPlaces[lastY][lastX - 1] >= 0;
                }
                case 4 -> {
                    if (lastY - 1 >= 0)
                        return shipsPlaces[lastY - 1][lastX] >= 0;
                }
            }
            return false;
        }
    }

    private void checkVictory() {
        int gameResult = getWinner();
        if (gameResult == 1) {
            /// Player 1 Wins
            Alert alert;
            if (isCVC)
                alert = new Alert(Alert.AlertType.NONE, "AI 1 Wins", ButtonType.OK);
            else
                alert = new Alert(Alert.AlertType.NONE, "You Win", ButtonType.OK);
            alert.showAndWait();
            board1.setDisable(true);
            board2.setDisable(true);
            gameOver = true;
            youWin = true;
        }
        else if (gameResult == 2) {
            /// Player 2 Wins
            Alert alert;
            if (isCVC)
                alert = new Alert(Alert.AlertType.NONE, "AI 2 Wins", ButtonType.OK);
            else
                alert = new Alert(Alert.AlertType.NONE, "You lose", ButtonType.OK);
            alert.showAndWait();
            board1.setDisable(true);
            board2.setDisable(true);
            gameOver = true;
        }
        else return;
        if (isPVC)
            save.setVisible(false);
    }

    private int getWinner() {
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                if (p1Turn) {
                    if (shipsPlaces2[i][j] > 1) {
                        return 0;
                    }
                }
                else {
                    if (shipsPlaces1[i][j] > 1) {
                        return 0;
                    }
                }
            }
        }
        if (p1Turn)
            return 1;
        else
            return 2;
    }

    public void opponentMove(int row, int col) {
        Rectangle rec = (Rectangle) (board1.getChildren().get(row * boardDim + col));
        if (shipsPlaces1[row][col] == 0 || shipsPlaces1[row][col] == 1) {
            shipsPlaces1[row][col] = -1;
            rec.setFill(Color.rgb(4, 12, 236));
        }
        else if (shipsPlaces1[row][col] > 1) {
            int id = shipsPlaces1[row][col];
            shipsPlaces1[row][col] = -id;
            boolean inRow = false, inCol = false;
            for (int i = 0; i < boardDim; i++) {
                if (shipsPlaces1[row][i] == id) {
                    inRow = true;
                    break;
                }
            }
            for (int i = 0; i < boardDim; i++) {
                if (shipsPlaces1[i][col] == id) {
                    inCol = true;
                    break;
                }
            }
            /// ship destroyed
            if (!inRow && !inCol) {
                for (int i = 0; i < boardDim; i++) {
                    if (shipsPlaces1[row][i] == -id) {
                        shipsPlaces1[row][i] = -id * 10000;
                        ((Rectangle) (board1.getChildren().get(row * boardDim + i))).setFill(Color.rgb(136, 12, 4));
                    }
                }
                for (int i = 0; i < boardDim; i++) {
                    if (shipsPlaces1[i][col] == -id) {
                        shipsPlaces1[i][col] = -id * 10000;
                        ((Rectangle) (board1.getChildren().get(i * boardDim + col))).setFill(Color.rgb(136, 12, 4));
                    }
                }
            } else
                rec.setFill(Color.rgb(220, 110, 0));
        }
        Platform.runLater(() -> {
            turn.setText("Your Turn");
            turn.setAlignment(Pos.CENTER);
        });
        p1Turn = !p1Turn;
        board2.setDisable(false);
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

    public void saveFile() throws IOException {
        String fileName = "";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("BSGF Files", "*.bsgf");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            fileName = selectedFile.getName();
        }
        else {
            return;
        }
        new File(fileName).createNewFile();
        Files.write(Paths.get(fileName), (boardDim + "\n" + cellSize + "\n" + totalShips + "\n").getBytes(), StandardOpenOption.APPEND);
        if (p1Turn) Files.write(Paths.get(fileName), ("1" + "\n").getBytes(), StandardOpenOption.APPEND);
        else Files.write(Paths.get(fileName), ("0" + "\n").getBytes(), StandardOpenOption.APPEND);
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                Files.write(Paths.get(fileName), ("" + shipsPlaces1[i][j] + " ").getBytes(), StandardOpenOption.APPEND);
            }
            Files.write(Paths.get(fileName), ("\n").getBytes(), StandardOpenOption.APPEND);
        }
        Files.write(Paths.get(fileName), ("*" + "\n").getBytes(), StandardOpenOption.APPEND);
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                Files.write(Paths.get(fileName), ("" + shipsPlaces2[i][j] + " ").getBytes(), StandardOpenOption.APPEND);
            }
            Files.write(Paths.get(fileName), ("\n").getBytes(), StandardOpenOption.APPEND);
        }
    }

    public boolean loadFile() throws IOException {
        String fileName = "";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("BSGF Files", "*.bsgf");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileName = selectedFile.getName();
        }
        else {
            return false;
        }
        String txt = new String(Files.readAllBytes(Paths.get(fileName)));
        String[] parts = txt.split("\n");
        boardDim = Integer.parseInt(parts[0].replaceAll(" ", ""));
        cellSize = Integer.parseInt(parts[1].replaceAll(" ", ""));
        totalShips = Integer.parseInt(parts[2].replaceAll(" ", ""));
        int check = Integer.parseInt(parts[3].replaceAll(" ", ""));
        p1Turn = check != 0;
        shipsPlaces1 = new int[boardDim][boardDim];
        shipsPlaces2 = new int[boardDim][boardDim];
        for (int i = 4; i < 4 + boardDim; i++) {
            String[] parts2 = parts[i].split(" ");
            for (int j = 0; j < boardDim; j++) {
                shipsPlaces1[i - 4][j] = Integer.parseInt(parts2[j]);
            }
        }
        for (int i = 5 + boardDim; i < boardDim * 2 + 5; i++) {
            String[] parts2 = parts[i].split(" ");
            for (int j = 0; j < boardDim; j++) {
                shipsPlaces2[i - 5 - boardDim][j] = Integer.parseInt(parts2[j]);
            }
        }

        initBoard();
        return true;
    }

    private class Network extends Thread {
        @Override
        public void run() {
            super.run();
            while (!gameOver) {
                InputStream input = null;
                try {
                    input = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert input != null;
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String[] opponentData = null;
                try {
                    opponentData = reader.readLine().split(" ");
                } catch (Exception ignored) {}

                int opponentRow = -1, opponentCol = -1;
                try {
                    assert opponentData != null;
                    opponentRow = Integer.parseInt(opponentData[0]);
                    opponentCol = Integer.parseInt(opponentData[1]);
                    gameOver = opponentData[2].equals("1");
                    opponentMove(opponentRow, opponentCol);
                } catch (Exception ignored) {}
            }
            if (!youWin) {
                while (!window.isFocused())
                    System.out.print("");
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.NONE, "You lose", ButtonType.OK);
                    alert.showAndWait();
                });
            }
            turn.setVisible(false);
            board1.setDisable(true);
            board2.setDisable(true);
        }
    }

}
