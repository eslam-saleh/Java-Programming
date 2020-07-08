package GUI;

import GUI.ShipInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class AI {
    private int[][] board;
    private int numShips = 0, width, height, c = 2;
    private ArrayList<Integer> shipsSizes = new ArrayList<>(), shipsCount = new ArrayList<>();

    public AI(int boardDim, ObservableList<ShipInfo> receivedShips) {
        width = boardDim;
        height = boardDim;
        this.board = new int[boardDim][boardDim];
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                board[i][j] = 0;
            }
        }
        for (ShipInfo ship : receivedShips) {
            shipsSizes.add(ship.getShipSize());
            shipsCount.add(ship.getShipCount());
            numShips += ship.getShipCount();
        }
    }

    public int[][] createBoard() {
        int posW, posH;
        for (int i = 0; i < shipsCount.size(); i++) {
            do {
                posW = (int) (Math.random() * (width));
                posH = (int) (Math.random() * (height));
//                System.out.println("w = " + posW + "  h = " + posH);
            } while (board[posW][posH] != 0);
            board[posW][posH] = c;
            for (int j = 0; j < shipsCount.get(i); j++) {
                int dim = 0;
                for (int k = 0; k < shipsSizes.get(i); k++) {
                    boolean isOver = false;
                    dim = (int) (Math.random() * (4));
                    switch (dim) {
                        case 0:
                            if (posW - shipsSizes.get(i) < 0) {
                                --k;
                                continue;
                            }
                            for (int l = 1; l < shipsSizes.get(i); l++)
                                if (board[posW - l][posH] != 0) {
                                    isOver = true;
                                }
                            if (isOver) {
                                --k;
                                continue;
                            }
                            break;
                        case 1:
                            if (posH + shipsSizes.get(i) >= width) {
                                --k;
                                continue;
                            }
                            for (int l = 1; l < shipsSizes.get(i); l++)
                                if (board[posW][posH + l] != 0) {
                                    isOver = true;
                                }
                            if (isOver) {
                                --k;
                                continue;
                            }
                            break;
                        case 2:
                            if (posW + shipsSizes.get(i) >= width) {
                                --k;
                                continue;
                            }
                            for (int l = 1; l < shipsSizes.get(i); l++)
                                if (board[posW + l][posH] != 0) {
                                    isOver = true;
                                }
                            if (isOver) {
                                --k;
                                continue;
                            }
                            break;
                        case 3:
                            if (posH - shipsSizes.get(i) < 0) {
                                --k;
                                continue;
                            }
                            for (int l = 1; l < shipsSizes.get(i); l++)
                                if (board[posW][posH - l] != 0) {
                                    isOver = true;
                                }
                            if (isOver) {
                                --k;
                                continue;
                            }
                            break;
                    }
                }
				
                for (int k = 0; k < shipsSizes.get(i) - 1; k++) {
                    switch (dim) {
                        case 0:
                            board[--posW][posH] = c;
                            break;
                        case 1:
                            board[posW][++posH] = c;
                            break;
                        case 2:
                            board[++posW][posH] = c;
                            break;
                        case 3:
                            board[posW][--posH] = c;
                            break;
                    }
                }
				
				
                for (int k = 0; k < width; k++) {
                    for (int l = 0; l < width; l++) {
                        if (board[k][l] != 0)
                            continue;
                        if (k != width - 1 && k != 0 && l != width - 1 && l != 0)
                            if (check(board[k + 1][l]) || check(board[k][l + 1]) || check(board[k - 1][l]) || check(board[k][l - 1]) ||
                                    check(board[k + 1][l + 1]) || check(board[k + 1][l - 1]) || check(board[k - 1][l + 1]) || check(board[k - 1][l - 1]))
                                board[k][l] = 1;

                    }
                }
                for (int k = 0; k < width; k++) {
                    for (int l = 0; l < width; l++) {
                        if (k == 0)
                            if (board[k + 1][l] != 0 && board[k + 1][l] != 1) {
                                if (l > 0) board[k][l - 1] = 1;
                                board[k][l] = 1;
                                if (l < width - 1) board[k][l + 1] = 1;
                            }
                        if (l == 0)
                            if (board[k][l + 1] != 0 && board[k][l + 1] != 1) {
                                if (k > 0) board[k - 1][l] = 1;
                                board[k][l] = 1;
                                if (k < width - 1) board[k + 1][l] = 1;
                            }
                        if (k == width - 1)
                            if (board[k - 1][l] != 0 && board[k - 1][l] != 1) {
                                if (l > 0) board[k][l - 1] = 1;
                                board[k][l] = 1;
                                if (l < width - 1) board[k][l + 1] = 1;
                            }
                        if (l == width - 1)
                            if (board[k][l - 1] != 0 && board[k][l - 1] != 1) {
                                if (k > 0) board[k - 1][l] = 1;
                                board[k][l] = 1;
                                if (k < width - 1) board[k + 1][l] = 1;
                            }

                    }
                }

				
                do {
                    posW = (int) (Math.random() * (width));
                    posH = (int) (Math.random() * (height));
//                    System.out.println("w = " + posW + "  h = " + posH);
                } while (board[posW][posH] != 0);
                if (shipsCount.get(i) != j + 1) {
                    board[posW][posH] = ++c;
                } else {
//                    System.out.println("NOPE");
                    ++c;
                }

            }

        }
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                System.out.print(board[i][j] + " ");
//            }
//            System.out.println();
//        }
        return board;
    }

    public boolean check(int num) {
        return num != 0 && num != 1;
    }

}