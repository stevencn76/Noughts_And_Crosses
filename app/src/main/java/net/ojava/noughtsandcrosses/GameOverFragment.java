package net.ojava.noughtsandcrosses;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.ojava.noughtsandcrosses.command.PkData;

import java.util.HashSet;

/**
 * Created by chenbao on 2016/10/3.
 */

public class GameOverFragment extends Fragment {
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private TextView resultTextView;
    private Button replayBtn;
    private Button stopBtn;

    private HashSet<IGameOverFragListener> listeners = new HashSet<IGameOverFragListener>();

    public void addGameOverFragListener(IGameOverFragListener listener) {
        listeners.add(listener);
    }

    public void removeGameOverFragListener(IGameOverFragListener listener) {
        listeners.remove(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gameover_fragment, container, false);


        resultTextView = (TextView) view.findViewById(R.id.resultTextView);

        replayBtn = (Button) view.findViewById(R.id.replayBtn);
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doReplay();
            }
        });

        stopBtn = (Button) view.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStop();
            }
        });

        return view;
    }

    public void setResult(int win) {
        String msg = "";
        switch (win) {
            case GameData.WIN_PLAYER:
                msg = "Congratulations, you won.";
                break;
            case GameData.WIN_RIVAL:
                msg = "Sorry, you lost.";
                break;
            case GameData.WIN_DRAW:
                msg = "You have a draw.";
                break;
        }
        resultTextView.setText(msg);
    }

    private void doReplay() {
        StrictMode.setThreadPolicy(policy);

        try {
            if (!NetworkUtil.getInstance().isConnected()) {
                return;
            }

            PkData pkData = NetworkUtil.getInstance().invite(DataManager.getInstance().getRivalName());

            for (IGameOverFragListener tl : listeners) {
                tl.processReplay(pkData);
            }
        } catch (Exception e) {
            Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("nac", "connect error", e);
            return;
        }
    }

    private void doStop() {
        for (IGameOverFragListener tl : listeners) {
            tl.processQuitGame();
        }
    }
}
