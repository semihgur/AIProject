
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class State {

    enum Player {
        Red, Blue
    }
    String[][] boardState; //GEÇİCİ OLARAK PRİVATE KALDIRDIM MAİNDE MOVE KULLANMADNA DEĞİŞTİRİP DEBUG İÇİN
    List<State> childList = new ArrayList<>();
    String winner = "N";
    Player turn = Player.Blue;
    int boardSize;
    int moves;

    public State(int size) {//Birden fazla parametre alan state constructoru o anki boardı oluşturmak için
        createBoard(size);
        resetBoard();
    }

    public State(State st) {
        this.boardState = st.boardState;
        this.winner = st.winner;
        this.boardSize = st.boardSize;
    }

    public void resetBoard() {  //CLEAR BOARDDAN İNİTİALA ÇEVİRDİM TAŞLARI DA BURDA KOYUYOR
        try {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    boardState[i][j] = " -";
                }
            }
            createStones(boardSize);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createStones(int size) { // DEĞİŞMEDİ

        if (size % 2 == 0) {
            int stoneCount = (size / 2) - 1;//8-3 6-2 4-1  (N+1)*2 = boardSize

            for (int i = 0; i < stoneCount; i++) {
                for (int j = 0; j < stoneCount; j++) {
                    boardState[i][j] = " B";
                }
            }

            for (int i = size - 1; i >= size - stoneCount; i--) {
                for (int j = size - 1; j >= size - stoneCount; j--) {
                    boardState[i][j] = " R";
                }
            }
        } else {
            int stoneCount = (size - 1) / 2;
            for (int i = 0; i < stoneCount; i++) {
                for (int j = 0; j < --stoneCount; j++) {
                    boardState[i][j] = " B";
                }
            }
            for (int i = size - 1; i >= size - stoneCount; i--) {
                for (int j = 0; j >= size - (--stoneCount); j--) {
                    boardState[i][j] = " R";
                }
            }
        }

    }

    public void sizeValidity(int size) { // SIZE KURALLARA UYMUYORSA DEFAULT OLARAK 8 YAPIYOR UYUYORSA PARAMETREYI KULLANIYOR
        if (size < 5) {
            this.boardSize = 8;
        } else {
            this.boardSize = size;
        }
    }

    public void createBoard(int size) {
        sizeValidity(size);
        this.boardState = new String[boardSize][boardSize];

    }

    public String winCondition() {
        String redWin = "R", blueWin = "B";
        winner = "N";

        for (int i = 0; i < (boardSize / 2) - 1; i++) {
            for (int j = 0; j < (boardSize / 2) - 1; j++) {
                //HEPSİ REDE EŞİTSE İFE GİRMEZ REDWİN TRUE OLARAK KALIR
                if (boardState[i][j] != " R") {
                    redWin = "N";
                    break;
                }
            }
        }
        for (int i = boardSize - 1; (boardSize / 2) < i; i--) {
            for (int j = boardSize - 1; (boardSize / 2) < j; j--) {
                //HEPSİ BLUE EŞİTSE İFE GİRMEZ BLUEWİN TRUE OLARAK KALIR
                if (boardState[i][j] != " B") {
                    blueWin = "N";
                    break;
                }
            }
        }
        // BURA DAHA TEMİZ YAZILABİLİR AMA KAFAM ÇALIŞMADIĞI İÇİN BÖYLE YAPTIM
        if (redWin == "R") {
            winner = "R";
        }
        if (blueWin == "B") {
            winner = "B";
        }
        return winner;
    }

    public void swap(int a, int b, int i, int j) {//0 1 2 3 4
        String temp = boardState[a][b];           //1
        boardState[a][b] = boardState[i][j];      //2
        boardState[i][j] = temp;                  //3
    }

    public void move(String direction, int stoneLocationX, int stoneLocationY, Player p) {
        if (p == Player.Red) {
            switch (direction.toLowerCase()) {
                case "up" -> {
                    if (" -".equals(boardState[stoneLocationX - 1][stoneLocationY])) {
                        swap(stoneLocationX - 1, stoneLocationY, stoneLocationX, stoneLocationY);
                    } else if ((boardState[stoneLocationX - 1][stoneLocationY] != " -")) {
                        swap(stoneLocationX - 2, stoneLocationY, stoneLocationX, stoneLocationY);
                    } else {
                        break;//DUVARA ÇARPMA DURUMUNDA INDEX OUT OF BOUND YIYECEĞIZ ONU HANDELLAMAK LAZIM ... HALETTİM EMRAN 
                    }
                }
                case "left" -> {
                    if (boardState[stoneLocationX][stoneLocationY - 1] == " -") {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY - 1);
                    } else if ((boardState[stoneLocationX][stoneLocationY - 1] != " -")) {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY - 2);
                    } else {
                        break;//DUVARA ÇARPMA DURUMUNDA INDEX OUT OF BOUND YIYECEĞIZ ONU HANDELLAMAK LAZIM  ... halletimt emran
                    }
                }
                case "right" -> {
                    if (boardState[stoneLocationX][stoneLocationY + 1] == " -") {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY + 1);
                    } else if ((boardState[stoneLocationX][stoneLocationY + 1] != " -")) {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY + 2);
                    } else {
                        break;//DUVARA ÇARPMA DURUMUNDA INDEX OUT OF BOUND YIYECEĞIZ ONU HANDELLAMAK LAZIM
                    }
                }
                default -> {
                    System.out.println("There's no input like that.");
                    break;
                }
            }
        } else if (p == Player.Blue) {
            switch (direction.toLowerCase()) {
                case "right" -> {
                    if (boardState[stoneLocationX][stoneLocationY + 1] == " -") {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY + 1);
                    } else if ((boardState[stoneLocationX][stoneLocationY + 1] != " -")) {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY + 2);
                    } else {
                        break;//DUVARA ÇARPMA DURUMUNDA INDEX OUT OF BOUND YIYECEĞIZ ONU HANDELLAMAK LAZIM
                    }
                }
                case "left" -> {
                    if (boardState[stoneLocationX][stoneLocationY - 1] == " -") {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY - 1);
                    } else if ((boardState[stoneLocationX][stoneLocationY - 1] != " -")) {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX, stoneLocationY - 2);
                    } else {
                        break;//DUVARA ÇARPMA DURUMUNDA INDEX OUT OF BOUND YIYECEĞIZ ONU HANDELLAMAK LAZIM
                    }

                }
                case "down" -> {
                    if (boardState[stoneLocationX + 1][stoneLocationY] == " -") {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX + 1, stoneLocationY);
                    } else if ((boardState[stoneLocationX + 1][stoneLocationY] != " -")) {
                        swap(stoneLocationX, stoneLocationY, stoneLocationX + 2, stoneLocationY);
                    }
                }
                default -> {
                    System.out.println("There's no input like that.");
                    break;
                }
            }

        }

    }

    public void play(State st) {
        Scanner sc = new Scanner(System.in);

        int x;
        int y;

        if (turn == Player.Blue) {
            System.out.println(turn + "'s turn.");
            do {
                x = -1;
                y = -1;
                while (y < 0 || y > boardSize || x < 0 || x > boardSize) {
                    System.out.print("x: ");
                    x = sc.nextInt();
                    System.out.print("y: ");
                    y = sc.nextInt();
                }
            } while (!boardState[x][y].equals(" B"));

            System.out.print("Direction: ");
            String direction = sc.next();
            System.out.println("\n");
            move(direction, y, x, turn);
            turn = Player.Red;
        } else {
            System.out.println(turn + "'s turn.");
            x = -1;
            y = -1;
            do {
                x = -1;
                y = -1;
                while (y < 0 || y > boardSize || x < 0 || x > boardSize) {
                    System.out.print("x: ");
                    x = sc.nextInt();
                    System.out.print("y: ");
                    y = sc.nextInt();
                }
            } while (!boardState[x][y].equals(" R"));

            System.out.print("Direction: ");
            String direction = sc.next();
            System.out.println("\n");
            move(direction, y, x, turn);
            turn = Player.Blue;
        }

    }

    public boolean isInWinZone(int stoneX, int stoneY, Player turn) {
        if (Player.Blue == turn) {
            if ((stoneX < (boardSize / 2) - 1 && stoneX > 0) && (stoneY < (boardSize / 2) - 1 && stoneY > 0)) {
                return true;
            }
        }
        if (Player.Red == turn) {
            if ((stoneX < boardSize - 1 && stoneX > (boardSize / 2)) && (stoneY < boardSize - 1 && stoneY > (boardSize / 2))) {
                return true;
            }
        }
        return false;
    }

    public void isMoveable(int stoneX, int stoneY, String direction) {
        if (isInWinZone(stoneX, stoneY, turn)) {
            switch (direction.toLowerCase()) {
                case "up":
                    break;
                case "down":
                    break;
                case "left":
                    break;
                case "right":
                    break;
            }
        }
        if (turn == Player.Red) {
            switch (direction.toLowerCase()) {
                case "up":
                    break;
                case "left":
                    break;
            }
        } else {
            switch (direction.toLowerCase()) {
                case "down":
                    break;
                case "right":
                    break;
            }
        }

    }

    public void printBoard() {
        try {
            System.out.println("-------------------------------------------");
            System.out.print("  X");
            for(int i = 0; i < boardSize;i++){
                System.out.print(i+" ");
            }
            System.out.println("");
            for (int i = 0; i < boardSize; i++) {
                    System.out.print("Y"+i);
                for (int j = 0; j < boardSize; j++) {
                    System.out.print(boardState[i][j] + "");
                }
                System.out.println("");
            }
            System.out.println("-------------------------------------------");
        } catch (Exception e) {
            throw e;
        }

    }
}
