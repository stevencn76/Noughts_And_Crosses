package net.ojava.noughtsandcrosses;

import static net.ojava.noughtsandcrosses.GameData.routs;

/**
 * Created by chenbao on 2016/9/30.
 */

public class SmartMachine {
    public static Cell nextCell(GameData gameData, int machineSign, int playerSign) {
        Cell tc = findPotentialThird(gameData, machineSign);
        if (tc != null)
            return tc;

        tc = findPotentialThird(gameData, playerSign);
        if (tc != null)
            return tc;

        tc = findPotentialSecond(gameData, machineSign);
        if (tc != null)
            return tc;

        tc = findPotentialFirst(gameData, machineSign);
        if (tc != null)
            return tc;

        tc = findFirstEmpty(gameData);
        if (tc != null)
            return tc;

        return null;
    }

    private static Cell findPotentialThird(GameData gameData, int sign) {
        for (int i = 0; i < routs.length; i++) {
            int count = 0;
            Cell emptyCell = null;
            for (int j = 0; j < routs[i].length; j++) {
                int v = gameData.getChess(routs[i][j].row, routs[i][j].col);
                if (v == sign)
                    count++;
                else if (v == 0) {
                    if (emptyCell == null)
                        emptyCell = routs[i][j];
                } else {
                    break;
                }
            }

            if (count == 2 && emptyCell != null)
                return emptyCell;
        }

        return null;
    }

    private static Cell findPotentialSecond(GameData gameData, int sign) {
        for (int i = 0; i < routs.length; i++) {
            int count = 0;
            int emptyCount = 0;
            Cell emptyCell = null;
            for (int j = 0; j < routs[i].length; j++) {
                int v = gameData.getChess(routs[i][j].row, routs[i][j].col);
                if (v == sign) {
                    count++;
                } else if (v == 0) {
                    emptyCount++;
                    if (emptyCell == null)
                        emptyCell = routs[i][j];
                } else {
                    break;
                }
            }

            if (count == 1 && emptyCount == 2)
                return emptyCell;
        }

        return null;
    }

    private static Cell findPotentialFirst(GameData gameData, int sign) {
        for (int i = 0; i < routs.length; i++) {
            int emptyCount = 0;
            for (int j = 0; j < routs[i].length; j++) {
                int v = gameData.getChess(routs[i][j].row, routs[i][j].col);
                if (v == 0) {
                    emptyCount++;
                } else {
                    break;
                }
            }

            if (emptyCount == 3) {
                return routs[i][1];
            }
        }

        return null;
    }

    private static Cell findFirstEmpty(GameData gameData) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameData.getChess(row, col) == 0) {
                    return new Cell(row, col);
                }
            }
        }

        return null;
    }
}
