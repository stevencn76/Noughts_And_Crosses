package net.ojava.noughtsandcrosses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

/**
 * Created by chenbao on 2016/10/2.
 */

public class LoginFragment extends Fragment {
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private static final int REGISTER_CODE = 1;

    private EditText nameEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;

    private Button loginBtn;
    private Button registerBtn;

    private HashSet<ILoginFragListener> listeners = new HashSet<ILoginFragListener>();

    public void addLoginFragListener(ILoginFragListener listener) {
        listeners.add(listener);
    }

    public void removeLoginFragListener(ILoginFragListener listener) {
        listeners.remove(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        rememberCheckBox = (CheckBox) view.findViewById(R.id.rememberCheckBox);

        loginBtn = (Button) (view.findViewById(R.id.loginBtn));
        registerBtn = (Button) (view.findViewById(R.id.registerBtn));

        rememberCheckBox.setSelected(DataManager.getInstance().isSaveUser());
        if (DataManager.getInstance().isSaveUser()) {
            nameEditText.setText(DataManager.getInstance().getLoginName() == null ? "" : DataManager.getInstance().getLoginName());
            passwordEditText.setText(DataManager.getInstance().getLoginPwd() == null ? "" : DataManager.getInstance().getLoginPwd());
        } else {
            nameEditText.setText("");
            passwordEditText.setText("");
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });

        return view;
    }

    private void doLogin() {
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

            DataManager.getInstance().save(getActivity());

            for (ILoginFragListener tl : listeners) {
                tl.loginFinished(name, pwd);
            }
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "Connect to server failed", Toast.LENGTH_LONG).show();
            Log.d("nac", "connect error", e);
            return;
        }
    }

    private void doRegister() {
        Intent i = new Intent(getActivity(), RegisterActivity.class);
        startActivityForResult(i, REGISTER_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_CODE && resultCode == Activity.RESULT_OK) {
            nameEditText.setText(data.getStringExtra("name"));
            passwordEditText.setText(data.getStringExtra("password"));
        }
    }
}

