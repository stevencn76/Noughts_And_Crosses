package net.ojava.noughtsandcrosses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import static net.ojava.noughtsandcrosses.GameData.WIN_NONE;

/**
 * Created by chenbao on 2016/9/19.
 */
public class SingleRoomActivity extends AppCompatActivity implements ChessViewListener {
    private ChessView chessView;
    private GameData gameData;

    private ImageView machineHandImageView;
    private ImageView playerHandImageView;

    private ImageView machineSignImageView;
    private ImageView playerSignImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleroom);

        machineHandImageView = (ImageView) findViewById(R.id.machineHandImageView);
        playerHandImageView = (ImageView) findViewById(R.id.playerHandImageView);
        machineHandImageView.setVisibility(ImageView.GONE);
        playerHandImageView.setVisibility(ImageView.GONE);

        machineSignImageView = (ImageView) findViewById(R.id.machineSignImageView);
        playerSignImageView = (ImageView) findViewById(R.id.playerSignImageView);

        chessView = (ChessView) findViewById(R.id.chessview);
        chessView.addListener(this);

        gameData = new GameData();
        chessView.setGameData(gameData);

        startGame();
    }

    public void startGame() {
        gameData.reset();

        raiseHand(true);
        if (gameData.getTurn() == GameData.TURN_RIVAL) {
            machineStep();
        }

        if (gameData.getRivalSign() == GameData.NOUGHT) {
            machineSignImageView.setImageResource(R.mipmap.nought);
            playerSignImageView.setImageResource(R.mipmap.cross);
        } else {
            machineSignImageView.setImageResource(R.mipmap.cross);
            playerSignImageView.setImageResource(R.mipmap.nought);
        }
        chessView.invalidate();
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
            raiseHand(true);

            chessView.invalidate();

            int win = gameData.getWin();
            if (win == WIN_NONE)
                machineStep();
            else
                showWin(win);
        }
    }

    private void machineStep() {
        if (gameData.getTurn() == GameData.TURN_RIVAL) {
            Cell tc = SmartMachine.nextCell(gameData, gameData.getRivalSign(), gameData.getPlayerSign());
            if (tc != null) {
                gameData.putChess(tc.row, tc.col, gameData.getRivalSign());

                gameData.nextTurn();
                raiseHand(true);
            }
            int win = gameData.getWin();
            if (win != WIN_NONE)
                showWin(win);
        }
    }

    private void showWin(int win) {
        String msg = null;
        switch (win) {
            case GameData.WIN_DRAW:
                msg = "You have a draw, play again?";
                break;
            case GameData.WIN_PLAYER:
                msg = "You won, play again?";
                break;
            case GameData.WIN_RIVAL:
                msg = "You lost, play again?";
                break;
        }

        raiseHand(false);

        if (msg != null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Game Over");
            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startGame();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    public void quitGame(View view) {
        finish();
    }
}
