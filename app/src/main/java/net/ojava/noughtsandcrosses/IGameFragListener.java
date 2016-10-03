package net.ojava.noughtsandcrosses;

/**
 * Created by chenbao on 2016/10/2.
 */

public interface IGameFragListener {
    public void chessPlaced();

    public void doStopGame();

    public void gameEnd(int win);
}
