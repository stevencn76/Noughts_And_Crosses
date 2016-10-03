package net.ojava.noughtsandcrosses;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenbao on 2016/9/30.
 */

public class DataManager {
    private static DataManager instance;

    public static final String NAC_PREFERENCES = "NacPreferences";

    public static final String WIN_COUNT = "WIN_COUNT";
    public static final String DRAW_COUNT = "DRAW_COUNT";
    public static final String LOST_COUNT = "LOST_COUNT";

    public static final String LOGIN_NAME = "LOGIN_NAME";
    public static final String LOGIN_PWD = "LOGIN_PWD";
    public static final String LOGIN_SAVE = "LOGIN_SAVE";
    public static final String SERVER_IP = "SERVER_IP";
    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String RIVAL_NAME = "RIVAL_NAME";


    private int winCount;
    private int drawCount;
    private int lostCount;
    private String loginName;
    private String loginPwd;
    private boolean saveUser;
    private String serverIp;

    private String serverPort;

    private String rivalName;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }

        return instance;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void increaseDrawCount() {
        this.drawCount++;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public boolean isSaveUser() {
        return saveUser;
    }

    public void setSaveUser(boolean saveUser) {
        this.saveUser = saveUser;
    }

    public int getLostCount() {
        return lostCount;
    }

    public void increaseLostCount() {
        this.lostCount++;
    }

    public int getWinCount() {
        return winCount;
    }

    public void increaseWinCount() {
        this.winCount++;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAC_PREFERENCES, Context.MODE_PRIVATE);

        winCount = sp.getInt(WIN_COUNT, 0);
        drawCount = sp.getInt(DRAW_COUNT, 0);
        lostCount = sp.getInt(LOST_COUNT, 0);
        loginName = sp.getString(LOGIN_NAME, null);
        loginPwd = sp.getString(LOGIN_PWD, null);
        saveUser = sp.getBoolean(LOGIN_SAVE, true);
        serverIp = sp.getString(SERVER_IP, "127.0.0.1");
        serverPort = sp.getString(SERVER_PORT, "7031");
        rivalName = sp.getString(RIVAL_NAME, "");
    }

    public void save(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAC_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(WIN_COUNT, winCount);
        editor.putInt(DRAW_COUNT, drawCount);
        editor.putInt(LOST_COUNT, lostCount);
        editor.putString(LOGIN_NAME, loginName);
        editor.putString(LOGIN_PWD, loginPwd);
        editor.putBoolean(LOGIN_SAVE, saveUser);
        editor.putString(SERVER_IP, serverIp);
        editor.putString(SERVER_PORT, serverPort);
        editor.putString(RIVAL_NAME, rivalName);

        editor.commit();
    }

    public void setRivalName(String rivalName) {
        this.rivalName = rivalName;
    }

    public String getRivalName() {
        return rivalName;
    }
}
