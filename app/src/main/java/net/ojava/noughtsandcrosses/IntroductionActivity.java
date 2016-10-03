package net.ojava.noughtsandcrosses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by chenbao on 2016/9/19.
 */
public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction);

        DataManager.getInstance().load(this);
    }

    public void onSinglePlayerBtnClicked(View view) {
        Intent i = new Intent(this, SingleRoomActivity.class);
        startActivity(i);
    }

    public void onTwoPlayerBtnClicked(View view) {
        Intent i = new Intent(this, NetworkRoomActivity.class);
        startActivity(i);
    }

    public void onExitBtnClicked(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.introduction_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.scoreMenuItem:
                i = new Intent(this, ScoreActivity.class);
                startActivity(i);
                return true;
            case R.id.aboutMenuItem:
                i = new Intent(this, AboutUsActivity.class);
                startActivity(i);
                return true;
            case R.id.helpMenuItem:
                i = new Intent(this, HelpActivity.class);
                startActivity(i);
                return true;
            case R.id.settingMenuItem:
                i = new Intent(this, SettingActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DataManager.getInstance().save(this);
    }
}
