package net.ojava.noughtsandcrosses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by chenbao on 2016/9/19.
 */
public class ScoreActivity extends AppCompatActivity {
    private TextView wonCountTextView;
    private TextView lostCountTextView;
    private TextView drawnCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scores);

        wonCountTextView = (TextView) findViewById(R.id.wonCountTextView);
        lostCountTextView = (TextView) findViewById(R.id.lostCountTextView);
        drawnCountTextView = (TextView) findViewById(R.id.drawnCountTextView);

        wonCountTextView.setText(String.valueOf(DataManager.getInstance().getWinCount()));
        lostCountTextView.setText(String.valueOf(DataManager.getInstance().getLostCount()));
        drawnCountTextView.setText(String.valueOf(DataManager.getInstance().getDrawCount()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
