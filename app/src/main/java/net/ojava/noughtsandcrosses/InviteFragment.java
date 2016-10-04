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
import android.widget.EditText;
import android.widget.Toast;

import net.ojava.noughtsandcrosses.command.PkData;

import java.util.HashSet;

/**
 * Created by chenbao on 2016/10/2.
 */

public class InviteFragment extends Fragment {
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private EditText nameEditText;

    private Button inviteBtn;

    private HashSet<IInviteFragListener> listeners = new HashSet<IInviteFragListener>();

    public void addInviteFragListener(IInviteFragListener listener) {
        listeners.add(listener);
    }

    public void removeInviteFragListener(IInviteFragListener listener) {
        listeners.remove(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_fragment, container, false);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);

        inviteBtn = (Button) (view.findViewById(R.id.inviteBtn));

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doInvite();
            }
        });

        nameEditText.setText(DataManager.getInstance().getRivalName());

        return view;
    }

    private void doInvite() {
        String name = this.nameEditText.getText().toString();

        if (name != null)
            name = name.trim();

        if (name == null || name.length() == 0) {
            Toast.makeText(getActivity(), "Please input name firstly", Toast.LENGTH_LONG).show();
            return;
        }

        DataManager.getInstance().setRivalName(name);
        DataManager.getInstance().save(getActivity());

        StrictMode.setThreadPolicy(policy);

        try {
            if (!NetworkUtil.getInstance().isConnected()) {
                return;
            }

            PkData pkData = NetworkUtil.getInstance().invite(name);

            for (IInviteFragListener tl : listeners) {
                tl.inviteSucceed(pkData);
            }
        } catch (Exception e) {
            Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("nac", "connect error", e);
            return;
        }
    }
}

