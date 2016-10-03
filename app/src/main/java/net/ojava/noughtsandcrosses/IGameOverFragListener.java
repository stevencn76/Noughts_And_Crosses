package net.ojava.noughtsandcrosses;

import net.ojava.noughtsandcrosses.command.PkData;

/**
 * Created by chenbao on 2016/10/2.
 */

public interface IGameOverFragListener {
    public void processReplay(PkData pkData);

    public void processQuitGame();
}
