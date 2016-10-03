package net.ojava.noughtsandcrosses;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by chenbao on 2016/9/19.
 */
public class RegisterActivity extends AppCompatActivity {
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private EditText nameEditText;
    private EditText pwdEditText;
    private EditText confirmPwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        pwdEditText = (EditText) findViewById(R.id.pwdEditText);
        confirmPwdEditText = (EditText) findViewById(R.id.confirmPwdEditText);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onRegisterBtnClicked(View view) {
        StrictMode.setThreadPolicy(policy);

        String name = nameEditText.getText().toString();
        String pwd = pwdEditText.getText().toString();
        String confirm = confirmPwdEditText.getText().toString();

        if (name != null)
            name = name.trim();

        if (name == null || name.length() == 0) {
            Toast.makeText(this, "Please input name", Toast.LENGTH_LONG).show();
            return;
        }

        if (pwd == null || pwd.length() == 0) {
            Toast.makeText(this, "Please input password", Toast.LENGTH_LONG).show();
            return;
        }

        if (confirm == null || confirm.length() == 0) {
            Toast.makeText(this, "Please input password", Toast.LENGTH_LONG).show();
            return;
        }

        if (!pwd.equals(confirm)) {
            Toast.makeText(this, "Password does not match with confirm password", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (!NetworkUtil.getInstance().isConnected()) {
                NetworkUtil.getInstance().connect();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Connect to server failed", Toast.LENGTH_LONG).show();
            Log.d("nac", "connect error", e);
            return;
        }

        try {
            NetworkUtil.getInstance().register(name, pwd);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("nac", "connect error", e);
            return;
        }

        Toast.makeText(this, "Register Successfully", Toast.LENGTH_LONG).show();

        Intent i = new Intent();
        i.putExtra("name", name);
        i.putExtra("password", pwd);
        setResult(RESULT_OK, i);

        finish();
    }

    public void onCancelBtnClicked(View view) {
        setResult(RESULT_CANCELED);

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
}
