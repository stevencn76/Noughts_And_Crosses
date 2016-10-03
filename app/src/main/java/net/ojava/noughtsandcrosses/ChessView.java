package net.ojava.noughtsandcrosses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenbao on 2016/9/29.
 */

public class ChessView extends View {
    private final int paintColor = Color.BLACK;
    private Paint drawPaint;

    private int startX = 5;
    private int startY = 5;
    private int space = 20;

    private Set<ChessViewListener> listeners = new HashSet<ChessViewListener>();

    private GameData gameData = new GameData();

    public ChessView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gameData.putChess(0, 0, 1);
        gameData.putChess(1, 1, 2);
        setupPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("nac", "action: " + event.getAction() + " UP: " + MotionEvent.ACTION_UP);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            if (x >= startX && x <= this.getWidth() - startX && y >= startY && y <= this.getHeight() - startY) {
                int spanX = (this.getWidth() - (2 * startX)) / 3;
                int spanY = (this.getHeight() - (2 * startY)) / 3;

                int row = (y - startY) / spanY;
                int col = (x - startX) / spanX;

                for (ChessViewListener tl : listeners) {
                    tl.cellClicked(row, col);
                }
            }
        }

        return true;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void addListener(ChessViewListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ChessViewListener listener) {
        listeners.remove(listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int spanX = (canvas.getWidth() - (2 * startX)) / 3;
        int spanY = (canvas.getHeight() - (2 * startY)) / 3;

        drawPaint.setColor(Color.BLACK);
        canvas.drawLine(startX, startY + spanY, canvas.getWidth() - startX, startY + spanY, drawPaint);
        canvas.drawLine(startX, startY + spanY * 2, canvas.getWidth() - startX, startY + spanY * 2, drawPaint);

        canvas.drawLine(startX + spanX, startY, startX + spanX, canvas.getHeight() - startY, drawPaint);
        canvas.drawLine(startX + spanX * 2, startY, startX + spanX * 2, canvas.getHeight() - startY, drawPaint);

        if (gameData != null) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    switch (gameData.getChess(row, col)) {
                        case GameData.NOUGHT:
                            drawPaint.setColor(Color.GREEN);
                            canvas.drawCircle(startX + (col * spanX) + spanX / 2, startY + (row * spanY) + spanY / 2, (spanX > spanY ? spanY : spanX) / 2 - space, drawPaint);
                            break;
                        case GameData.CROSS:
                            drawPaint.setColor(Color.RED);
                            canvas.drawLine(startX + (col * spanX) + space, startY + (row * spanY) + space, startX + (col + 1) * spanX - space, startY + (row + 1) * spanY - space, drawPaint);
                            canvas.drawLine(startX + (col + 1) * spanX - space, startY + (row * spanY) + space, startX + col * spanX + space, startY + (row + 1) * spanY - space, drawPaint);
                            break;
                    }
                }
            }
        }
    }
}
