package net.ojava.noughtsandcrosses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by chenbao on 2016/10/3.
 */

public class SettingActivity extends AppCompatActivity {
    private EditText serverIpEditText;
    private EditText serverPortEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        serverIpEditText = (EditText) findViewById(R.id.serverIpEditText);
        serverIpEditText.setText(DataManager.getInstance().getServerIp());

        serverPortEditText = (EditText) findViewById(R.id.serverPortEditText);
        serverPortEditText.setText(DataManager.getInstance().getServerPort());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        String ip = serverIpEditText.getText().toString();
        String port = serverPortEditText.getText().toString();
        DataManager.getInstance().setServerIp(ip);
        DataManager.getInstance().setServerPort(port);
        DataManager.getInstance().save(this);

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
