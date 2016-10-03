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
import android.widget.ImageView;
import android.widget.TextView;

import net.ojava.network.tcp.Global;
import net.ojava.noughtsandcrosses.command.PkData;

import java.util.HashSet;

import static net.ojava.noughtsandcrosses.GameData.WIN_NONE;

/**
 * Created by chenbao on 2016/10/2.
 */

public class GameFragment extends Fragment implements ChessViewListener {
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private ChessView chessView;
    private GameData gameData;

    private ImageView machineHandImageView;
    private ImageView playerHandImageView;

    private ImageView machineSignImageView;
    private ImageView playerSignImageView;

    private TextView rivalNameTextView;
    private TextView playerNameTextView;

    private Button stopBtn;

    private HashSet<IGameFragListener> listeners = new HashSet<IGameFragListener>();

    public void addGameFragListener(IGameFragListener listener) {
        listeners.add(listener);
    }

    public void removeGameFragListener(IGameFragListener listener) {
        listeners.remove(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);


        machineHandImageView = (ImageView) view.findViewById(R.id.machineHandImageView);
        playerHandImageView = (ImageView) view.findViewById(R.id.playerHandImageView);

        machineHandImageView.setVisibility(ImageView.GONE);
        playerHandImageView.setVisibility(ImageView.GONE);

        machineSignImageView = (ImageView) view.findViewById(R.id.machineSignImageView);
        playerSignImageView = (ImageView) view.findViewById(R.id.playerSignImageView);

        rivalNameTextView = (TextView) view.findViewById(R.id.rivalNameTextView);
        playerNameTextView = (TextView) view.findViewById(R.id.playerTextView);

        chessView = (ChessView) view.findViewById(R.id.chessview);
        chessView.addListener(this);

        gameData = new GameData();
        chessView.setGameData(gameData);

        stopBtn = (Button) view.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStop();
            }
        });

        return view;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void refresh(final PkData pkData) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                rivalNameTextView.setText(DataManager.getInstance().getRivalName());
                playerNameTextView.setText(DataManager.getInstance().getLoginName());

                gameData.setRivalSign(pkData.signIds.get(DataManager.getInstance().getRivalName()));
                gameData.setPlayerSign(pkData.signIds.get(DataManager.getInstance().getLoginName()));
                if (pkData.nextTurnId == pkData.turnIds.get(DataManager.getInstance().getRivalName())) {
                    gameData.setTurn(GameData.TURN_RIVAL);
                } else {
                    gameData.setTurn(GameData.TURN_PLAYER);
                }
                gameData.setData(pkData.gameData);
                Log.i(Global.LOG_KEY, "cur turn: " + gameData.getTurn());

                raiseHand(true);
                chessView.invalidate();

                int win = gameData.getWin();
                if (win != WIN_NONE) {
                    showWin(win);
                }
            }
        });
    }

    private void raiseHand(boolean raising) {
        if (!raising) {
            machineHandImageView.setVisibility(ImageView.GONE);
            playerHandImageView.setVisibility(ImageView.GONE);
            return;
        }

        if (gameData.getTurn() == 0) {
            machineHandImageView.setVisibility(ImageView.GONE);
            playerHandImageView.setVisibility(ImageView.VISIBLE);
        } else {
            machineHandImageView.setVisibility(ImageView.VISIBLE);
            playerHandImageView.setVisibility(ImageView.GONE);
        }
    }

    @Override
    public void cellClicked(int row, int col) {
        if (gameData.getTurn() == GameData.TURN_PLAYER && gameData.getChess(row, col) == 0) {
            gameData.putChess(row, col, gameData.getPlayerSign());
            gameData.nextTurn();

            for (IGameFragListener tl : listeners) {
                tl.chessPlaced();
            }

            raiseHand(true);
            chessView.invalidate();

            int win = gameData.getWin();
            if (win != WIN_NONE) {
                showWin(win);
            }
        }
    }

    private void showWin(int win) {
        raiseHand(false);

        for (IGameFragListener tl : listeners) {
            tl.gameEnd(win);
        }
    }

    private void doStop() {

        for (IGameFragListener tl : listeners) {
            tl.doStopGame();
        }
    }
}

