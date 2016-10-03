package net.ojava.noughtsandcrosses;

/**
 * Created by chenbao on 2016/9/29.
 */

public class GameData {
    public static Cell[][] routs = new Cell[8][3];

    static {
        routs[0][0] = new Cell(0, 0);
        routs[0][1] = new Cell(1, 1);
        routs[0][2] = new Cell(2, 2);
        routs[1][0] = new Cell(0, 2);
        routs[1][1] = new Cell(1, 1);
        routs[1][2] = new Cell(2, 0);

        routs[2][0] = new Cell(1, 0);
        routs[2][1] = new Cell(1, 1);
        routs[2][2] = new Cell(1, 2);
        routs[3][0] = new Cell(0, 1);
        routs[3][1] = new Cell(1, 1);
        routs[3][2] = new Cell(2, 1);

        routs[4][0] = new Cell(0, 0);
        routs[4][1] = new Cell(0, 1);
        routs[4][2] = new Cell(0, 2);
        routs[5][0] = new Cell(2, 0);
        routs[5][1] = new Cell(2, 1);
        routs[5][2] = new Cell(2, 2);

        routs[6][0] = new Cell(0, 0);
        routs[6][1] = new Cell(1, 0);
        routs[6][2] = new Cell(2, 0);
        routs[7][0] = new Cell(0, 2);
        routs[7][1] = new Cell(1, 2);
        routs[7][2] = new Cell(2, 2);
    }

    public static final int TURN_RIVAL = 1;
    public static final int TURN_PLAYER = 0;

    public static final int WIN_RIVAL = 2;
    public static final int WIN_PLAYER = 1;
    public static final int WIN_DRAW = 0;
    public static final int WIN_NONE = -1;

    public static final int NOUGHT = 1;
    public static final int CROSS = 2;
    public static final int NONE = 0;

    private int rivalSign = CROSS;
    private int playerSign = NOUGHT;

    private int data;
    private int turn;

    public GameData() {
        reset();
    }

    public void reset() {
        data = 0;
        turn = (int) (Math.random() * 2);

        if (turn == TURN_PLAYER) {
            playerSign = NOUGHT;
            rivalSign = CROSS;
        } else {
            playerSign = CROSS;
            rivalSign = NOUGHT;
        }
    }

    public void putChess(int row, int col, int chess) {
        switch (chess) {
            case NONE:
            case NOUGHT:
            case CROSS:
                int d1 = 3 << (2 * (col + row * 3));
                d1 = ~d1;
                data = data & d1;
                data = data | (chess << (2 * (col + row * 3)));
                break;
        }
    }

    public int getChess(int row, int col) {
        int d1 = 3 << (2 * (col + row * 3));
        return (data & d1) >>> (2 * (col + row * 3));
    }

    public void nextTurn() {
        turn++;
        if (turn >= 2)
            turn = 0;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getWin() {
        boolean hasSpace = false;
        for (int i = 0; i < routs.length; i++) {
            int chess = getChess(routs[i][0].row, routs[i][0].col);
            if (chess == 0) {
                hasSpace = true;
                continue;
            }

            int count = 1;
            for (int j = 1; j < 3; j++) {
                int tmp = getChess(routs[i][j].row, routs[i][j].col);
                if (chess == tmp) {
                    count++;
                }
                if (tmp == 0) {
                    hasSpace = true;
                }
            }

            if (count == 3) {
                if (chess == rivalSign) {
                    DataManager.getInstance().increaseLostCount();
                    return WIN_RIVAL;
                } else {
                    DataManager.getInstance().increaseWinCount();
                    return WIN_PLAYER;
                }
            }
        }

        if (!hasSpace) {
            DataManager.getInstance().increaseDrawCount();
            return WIN_DRAW;
        }

        return WIN_NONE;
    }

    public void setRivalSign(int rivalSign) {
        this.rivalSign = rivalSign;
    }

    public int getRivalSign() {
        return rivalSign;
    }

    public void setPlayerSign(int playerSign) {
        this.playerSign = playerSign;
    }

    public int getPlayerSign() {
        return playerSign;
    }
}
