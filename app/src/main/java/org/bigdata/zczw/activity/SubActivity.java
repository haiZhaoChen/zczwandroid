package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.bigdata.zczw.R;
import org.bigdata.zczw.utils.AppManager;

import io.rong.imlib.model.Conversation;

/*
*
* */

public class SubActivity extends AppCompatActivity {

    private String targetId;
    private String targetIds;
    private String type;
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        Intent intent = getIntent();

        targetId = intent.getData().getQueryParameter("targetId");
        targetIds = intent.getData().getQueryParameter("targetIds");
        type = intent.getData().getQueryParameter("type");

        if (targetId != null) {
            mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
        } else if (targetIds != null)
            mConversationType = Conversation.ConversationType.valueOf(intent.getData().getQueryParameter("type").toUpperCase());

        if(type.equals("group")) {
            getSupportActionBar().setTitle(R.string.de_actionbar_sub_group);
        }else if(type.equals("private")){
            getSupportActionBar().setTitle(R.string.de_actionbar_sub_private);
        }else if(type.equals("discussion")){
            getSupportActionBar().setTitle(R.string.de_actionbar_sub_discussion);
        }else if(type.equals("system")){
            getSupportActionBar().setTitle(R.string.de_actionbar_sub_system);
        }else {
            getSupportActionBar().setTitle(R.string.de_actionbar_sub_defult);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
