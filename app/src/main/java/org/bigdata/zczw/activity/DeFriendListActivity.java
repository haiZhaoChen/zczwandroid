package org.bigdata.zczw.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import org.bigdata.zczw.R;
import org.bigdata.zczw.utils.AppManager;

public class DeFriendListActivity extends AppCompatActivity {

    private EditText mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_friend_list);
        AppManager.getAppManager().addActivity(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayHomeAsUpEnabled(true);
        actionBar.hide();
    }
}
