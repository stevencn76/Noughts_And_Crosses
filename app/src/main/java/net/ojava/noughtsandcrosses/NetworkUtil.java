package net.ojava.noughtsandcrosses;

import android.util.Log;

import net.ojava.network.tcp.TCPNetClient;
import net.ojava.network.tcp.TCPRequest;
import net.ojava.network.tcp.TCPResponse;
import net.ojava.noughtsandcrosses.command.InviteCommand;
import net.ojava.noughtsandcrosses.command.LoginCommand;
import net.ojava.noughtsandcrosses.command.PkData;
import net.ojava.noughtsandcrosses.command.RegisterCommand;
import net.ojava.noughtsandcrosses.command.StepCommand;

/**
 * Created by chenbao on 2016/9/20.
 */
public class NetworkUtil {
    private static NetworkUtil instance;
    private TCPNetClient tcpClient;

    private TCPNetClientListenerImpl netListener = new TCPNetClientListenerImpl();

    private NetworkUtil() {

    }

    public static NetworkUtil getInstance() {
        if (instance == null) {
            instance = new NetworkUtil();

            instance.tcpClient = new TCPNetClient();
            instance.tcpClient.addListener(instance.netListener);
        }

        return instance;
    }

    public TCPNetClientListenerImpl getNetListener() {
        return netListener;
    }

    public boolean isConnected() {
        return tcpClient.isConnected();
    }

    public void connect() throws Exception {
        try {
            tcpClient.connect(DataManager.getInstance().getServerIp(), Integer.parseInt(DataManager.getInstance().getServerPort()));
//            tcpClient.connect("10.72.176.155", 7031);
        } catch (Exception e) {
            Log.d("nac", "connect error", e);
        }
    }

    public void disconnect() {
        try {
            tcpClient.close();
        } catch (Exception e) {
            Log.d("nac", "connect error", e);
        }
    }

    public void register(String name, String pwd) throws Exception {
        TCPRequest request = new TCPRequest();
        RegisterCommand cmd = new RegisterCommand();
        cmd.name = name;
        cmd.password = pwd;
        request.setData(cmd);

        TCPResponse res = tcpClient.sendRequest(request);
        if (res.getData() instanceof RegisterCommand) {
            cmd = (RegisterCommand) res.getData();
            if (cmd.result != null && cmd.result.equalsIgnoreCase("ok"))
                return;

            throw new Exception(cmd.result == null ? "no result" : cmd.result);
        }
        throw new Exception("Response does not match");
    }

    public void login(String name, String pwd) throws Exception {
        TCPRequest request = new TCPRequest();
        LoginCommand cmd = new LoginCommand();
        cmd.name = name;
        cmd.password = pwd;
        request.setData(cmd);

        TCPResponse res = tcpClient.sendRequest(request);
        if (res.getData() instanceof LoginCommand) {
            cmd = (LoginCommand) res.getData();
            if (cmd.result != null && cmd.result.equalsIgnoreCase("ok"))
                return;

            throw new Exception(cmd.result == null ? "no result" : cmd.result);
        }
        throw new Exception("Response does not match");
    }

    public PkData invite(String name) throws Exception {
        TCPRequest request = new TCPRequest();
        InviteCommand cmd = new InviteCommand();
        cmd.accepterName = name;
        cmd.inviterName = DataManager.getInstance().getLoginName();
        request.setData(cmd);

        TCPResponse res = tcpClient.sendRequest(request);
        if (res.getData() instanceof InviteCommand) {
            cmd = (InviteCommand) res.getData();
            if (cmd.result != null && cmd.result.equalsIgnoreCase("ok")) {
                return cmd.pkData;
            }

            throw new Exception("Invite failed: " + cmd.result);
        }
        throw new Exception("Response does not match");
    }

    public void placeChess(GameData gameData) throws Exception {
        TCPRequest request = new TCPRequest();
        StepCommand cmd = new StepCommand();
        cmd.pkData = new PkData();
        cmd.pkData.name1 = DataManager.getInstance().getLoginName();
        cmd.pkData.name2 = DataManager.getInstance().getRivalName();
        cmd.pkData.gameData = gameData.getData();
        cmd.pkData.signIds.put(cmd.pkData.name1, gameData.getPlayerSign());
        cmd.pkData.signIds.put(cmd.pkData.name2, gameData.getRivalSign());
        cmd.pkData.turnIds.put(cmd.pkData.name1, 0);
        cmd.pkData.turnIds.put(cmd.pkData.name2, 1);
        cmd.pkData.nextTurnId = 1;
        request.setData(cmd);

        TCPResponse res = tcpClient.sendRequest(request);
        if (res.getData() instanceof StepCommand) {
            cmd = (StepCommand) res.getData();
            if (cmd.result != null && cmd.result.equalsIgnoreCase("ok")) {
                return;
            }

            throw new Exception(cmd.result);
        }
        throw new Exception("Response does not match");
    }
}
