package net.ojava.noughtsandcrosses;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by chenbao on 2016/9/19.
 */
public class LoginActivity extends AppCompatActivity {
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private EditText nameEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);

        String name = null;
        String pwd = null;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                name = (String) extras.get("name");
                pwd = (String) extras.get("password");
            }
        } else {
            name = (String) savedInstanceState.getSerializable("name");
            pwd = (String) savedInstanceState.getSerializable("password");
        }
        if (name != null && pwd != null) {
            nameEditText.setText(name);
            passwordEditText.setText(pwd);
        } else {
            rememberCheckBox.setSelected(DataManager.getInstance().isSaveUser());
            if (DataManager.getInstance().isSaveUser()) {
                nameEditText.setText(DataManager.getInstance().getLoginName() == null ? "" : DataManager.getInstance().getLoginName());
                passwordEditText.setText(DataManager.getInstance().getLoginPwd() == null ? "" : DataManager.getInstance().getLoginPwd());
            } else {
                nameEditText.setText("");
                passwordEditText.setText("");
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, IntroductionActivity.class);
        startActivity(i);

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

    public void onLoginBtnClicked(View view) {
        StrictMode.setThreadPolicy(policy);

        try {
            if (!NetworkUtil.getInstance().isConnected()) {
                NetworkUtil.getInstance().connect();
            }

            String name = this.nameEditText.getText().toString();
            String pwd = this.passwordEditText.getText().toString();
            boolean remember = this.rememberCheckBox.isChecked();

            NetworkUtil.getInstance().login(name, pwd);

            DataManager.getInstance().setLoginName(name);
            DataManager.getInstance().setLoginPwd(pwd);
            DataManager.getInstance().setSaveUser(remember);
        } catch (Exception e) {
            Toast.makeText(this, "Connect to server failed", Toast.LENGTH_LONG).show();
            Log.d("nac", "connect error", e);
            return;
        }
    }

    public void onRegisterBtnClicked(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        StrictMode.setThreadPolicy(policy);
        NetworkUtil.getInstance().disconnect();

        super.onStop();
    }
}
