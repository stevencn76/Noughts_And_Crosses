package net.ojava.noughtsandcrosses;

import android.util.Log;

import net.ojava.network.tcp.Global;
import net.ojava.network.tcp.TCPMessage;
import net.ojava.network.tcp.TCPNetClientListener;
import net.ojava.network.tcp.TCPRequest;
import net.ojava.network.tcp.TCPResponse;
import net.ojava.noughtsandcrosses.command.RivalOfflineMessage;
import net.ojava.noughtsandcrosses.command.StepCommand;

import java.util.HashSet;

/**
 * Created by chenbao on 2016/9/20.
 */
public class TCPNetClientListenerImpl implements TCPNetClientListener {
    private HashSet<INetProcessor> listeners = new HashSet<INetProcessor>();


    public void addNetProcessorListener(INetProcessor p) {
        listeners.add(p);
    }

    public void removeNetProcessorListener(INetProcessor p) {
        listeners.remove(p);
    }

    @Override
    public void onMessage(TCPMessage message) {
        if (message == null || message.getData() == null)
            return;

        if (message.getData() instanceof RivalOfflineMessage) {
            processRivalOffline((RivalOfflineMessage) message.getData());
        }
    }

    @Override
    public void onRequest(TCPRequest request, TCPResponse response) {
        if (request == null || request.getData() == null)
            return;

        if (request.getData() instanceof StepCommand) {
            StepCommand cmd = (StepCommand) request.getData();
            try {
                processRequestStepCommand(cmd);
                cmd.result = "ok";
            } catch (Exception e) {
                cmd.result = "Error:" + e.getMessage();
                Log.d(Global.LOG_KEY, "step command process error", e);
            }
            response.setData(cmd);
        }
    }

    @Override
    public void onClosed() {
        for (INetProcessor tl : listeners) {
            tl.netDisconnected();
        }
    }

    @Override
    public void onInterrupt(String ip, String port) {
        for (INetProcessor tl : listeners) {
            tl.netDisconnected();
        }
    }

    @Override
    public void onUnknowData(String msg) {

    }

    private void processRequestStepCommand(StepCommand cmd) {
        for (INetProcessor tl : listeners) {
            tl.stepCommandArrived(cmd);
        }
    }

    private void processRivalOffline(RivalOfflineMessage msg) {
        if (msg.rivalName != null && DataManager.getInstance().getRivalName() != null && msg.rivalName.equals(DataManager.getInstance().getRivalName())) {
            for (INetProcessor tl : listeners) {
                tl.rivalOffline();
            }
        }
    }
}
