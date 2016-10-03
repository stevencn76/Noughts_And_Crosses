package net.ojava.noughtsandcrosses;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chenbao on 2016/10/3.
 */

public class HelpExpandAdapter extends BaseExpandableListAdapter {
    private Activity context;

    private String[] helpGroups = new String[]{
            "How to play as single",
            "How to play on network",
            "How to view score history"
    };

    private String[] helpItems = new String[]{
            "After enter the main menu, click 'single player', a game view would be shown as you play with the machine.",
            "After enter the main menu, click 'two player', a login view would be shown to let you login or register to play with other guy on the network.",
            "After enter the main menu, click the option menu which is on right top corner. Choose 'scores' to show the history."
    };

    public HelpExpandAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return helpGroups.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return helpGroups[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return helpItems[i];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private TextView getTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(36, 0, 0, 0);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = getTextView();
        textView.setTextColor(Color.BLUE);
        textView.setText(getGroup(i).toString());
        ll.addView(textView);
        ll.setPadding(100, 10, 10, 10);
        return ll;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(14, 12, 0, 0);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = getTextView();
        textView.setText(getChild(i, i1).toString());
        textView.setLayoutParams(params);
        ll.addView(textView);
        return ll;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
