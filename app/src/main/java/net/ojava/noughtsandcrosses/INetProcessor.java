package net.ojava.noughtsandcrosses;

import net.ojava.noughtsandcrosses.command.StepCommand;

/**
 * Created by chenbao on 2016/10/2.
 */

public interface INetProcessor {
    public void netDisconnected();

    public void rivalOffline();

    public void stepCommandArrived(StepCommand cmd);
}
