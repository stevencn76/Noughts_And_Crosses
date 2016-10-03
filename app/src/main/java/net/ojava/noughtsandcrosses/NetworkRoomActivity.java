package net.ojava.noughtsandcrosses;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import net.ojava.network.tcp.Global;
import net.ojava.noughtsandcrosses.command.PkData;
import net.ojava.noughtsandcrosses.command.StepCommand;

/**
 * Created by chenbao on 2016/10/2.
 */

public class NetworkRoomActivity extends AppCompatActivity implements ILoginFragListener, INetProcessor, IInviteFragListener, IGameFragListener, IGameOverFragListener {
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private LoginFragment loginFragment;
    private InviteFragment inviteFragment;
    private GameFragment gameFragment;
    private GameOverFragment gameOverFragment;

    private Fragment curFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.networkroom);

        loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.loginFragment);
        inviteFragment = (InviteFragment) getSupportFragmentManager().findFragmentById(R.id.inviteFragment);
        gameFragment = (GameFragment) getSupportFragmentManager().findFragmentById(R.id.gameFragment);
        gameOverFragment = (GameOverFragment) getSupportFragmentManager().findFragmentById(R.id.gameOverFragment);

        loginFragment.addLoginFragListener(this);
        inviteFragment.addInviteFragListener(this);
        gameFragment.addGameFragListener(this);
        gameOverFragment.addGameOverFragListener(this);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.show(loginFragment);
        tx.hide(inviteFragment);
        tx.hide(gameFragment);
        tx.hide(gameOverFragment);
        tx.commit();

        curFragment = loginFragment;

        NetworkUtil.getInstance().getNetListener().addNetProcessorListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        StrictMode.setThreadPolicy(policy);

        super.onBackPressed();

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkUtil.getInstance().getNetListener().removeNetProcessorListener(this);

        new Thread() {
            public void run() {
                NetworkUtil.getInstance().disconnect();
            }
        }.start();
    }

    private void switchTo(Fragment fragment) {
        if (curFragment != fragment) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.hide(curFragment);
            tx.show(fragment);
            tx.commit();
            curFragment = fragment;
        }
    }

    @Override
    public void loginFinished(String name, String password) {
        switchTo(inviteFragment);
    }

    @Override
    public void netDisconnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NetworkRoomActivity.this, "Network disconnected", Toast.LENGTH_LONG).show();

                switchTo(loginFragment);
            }
        });
    }

    @Override
    public void rivalOffline() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NetworkRoomActivity.this, "Rival has left", Toast.LENGTH_LONG).show();

                switchTo(inviteFragment);
            }
        });
    }

    @Override
    public void stepCommandArrived(StepCommand cmd) {
        final PkData pkData = cmd.pkData;

        if (DataManager.getInstance().getLoginName().equals(pkData.name1)) {
            DataManager.getInstance().setRivalName(pkData.name2);
        } else {
            DataManager.getInstance().setRivalName(pkData.name1);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchTo(gameFragment);

                gameFragment.refresh(pkData);
            }
        });
    }

    @Override
    public void inviteSucceed(PkData pkData) {
        switchTo(gameFragment);

        gameFragment.refresh(pkData);
    }

    @Override
    public void chessPlaced() {
        StrictMode.setThreadPolicy(policy);

        try {
            NetworkUtil.getInstance().placeChess(gameFragment.getGameData());
        } catch (Exception e) {
            Toast.makeText(this, "error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(Global.LOG_KEY, "place chess error", e);
        }
    }

    @Override
    public void doStopGame() {
        onBackPressed();
//        switchTo(inviteFragment);
    }

    @Override
    public void gameEnd(final int win) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchTo(gameOverFragment);
                gameOverFragment.setResult(win);
            }
        });
    }

    @Override
    public void processReplay(PkData pkData) {
        switchTo(gameFragment);

        gameFragment.refresh(pkData);
    }

    @Override
    public void processQuitGame() {
        onBackPressed();
    }
}
