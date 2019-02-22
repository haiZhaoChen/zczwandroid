package org.bigdata.zczw.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupActivity;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.entity.MsgTag;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;
/*
* 发动态公开范围
* */
public class MsgPowerActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlAll,rlSelect,rlList;
    private ImageView imageView;

    private List<User> userList;
    private String userIds ;
    private String userNames,topic ;
    private Record record;

    private ArrayList<MsgTag> tagList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_power);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setLogo(R.drawable.empty_logo);// actionbar 添加logo
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);

        initView();
        initData();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.img_back_act);
        rlAll = (RelativeLayout) findViewById(R.id.rl_all_act);
        rlSelect = (RelativeLayout) findViewById(R.id.rl_my_unit_act);
        rlList = (RelativeLayout) findViewById(R.id.rl_select_list_act);
        Animation translate = AnimationUtils.loadAnimation(MsgPowerActivity.this, R.anim.down_to_up);
        Animation translate1 = AnimationUtils.loadAnimation(MsgPowerActivity.this, R.anim.down_to_up1);
        Animation translate2 = AnimationUtils.loadAnimation(MsgPowerActivity.this, R.anim.down_to_up2);
        rlAll.startAnimation(translate);
        rlSelect.startAnimation(translate1);
        rlList.startAnimation(translate2);
        imageView.setOnClickListener(this);
    }

    private void initData() {
        userIds = "";
        userNames = "";
        userList = new ArrayList<>();

        tagList = (ArrayList<MsgTag>) getIntent().getSerializableExtra("tag");

        if (getIntent().hasExtra("record")) {
            record = (Record) getIntent().getSerializableExtra("record");
        }
        if (getIntent().hasExtra("topic")) {
            topic = getIntent().getStringExtra("topic");
        }

        rlAll.setOnClickListener(this);
        rlSelect.setOnClickListener(this);
        rlList.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_all_act://公开
                Intent intentAll;
                if (getIntent().hasExtra("share")) {
                    intentAll = new Intent(MsgPowerActivity.this,AddShareActivity.class);
                    intentAll.putExtra("record",record);
                }else {
                    intentAll = new Intent(MsgPowerActivity.this,AddMessageActivity.class);
                    intentAll.putExtra("tag",tagList);
                }
                if (getIntent().hasExtra("topic")) {
                    intentAll.putExtra("topic",topic);
                }

                intentAll.putExtra("type","0");
                intentAll.putExtra("publicScope","10");
                startActivity(intentAll);
                finish();
                break;
            case R.id.rl_my_unit_act://本部门
                String group = SPUtil.getString(MsgPowerActivity.this, App.UNITSNAME);
                userList = Singleton.getInstance().getUserByGroup(group);

                if (userList != null) {
                    for (int i = 0; i < userList.size(); i++) {
                        userIds = userIds+userList.get(i).getUserid()+"/";
                        userNames = userNames+userList.get(i).getUsername()+"、";
                    }
                }
                Intent intent;
                if (getIntent().hasExtra("share")) {
                    intent = new Intent(MsgPowerActivity.this,AddShareActivity.class);
                    intent.putExtra("record",record);
                }else {
                    intent = new Intent(MsgPowerActivity.this,AddMessageActivity.class);
                    intent.putExtra("tag",tagList);
                }

                if (getIntent().hasExtra("topic")) {
                    intent.putExtra("topic",topic);
                }
                intent.putExtra("type","1");
                intent.putExtra("ids",userIds);
                intent.putExtra("names",userNames);
                intent.putExtra("publicScope","5");
                startActivity(intent);
                finish();
                break;
            case R.id.rl_select_list_act://选择
                Intent intentSel = new Intent(MsgPowerActivity.this,SendGroupActivity.class);
                intentSel.putExtra("type","addMsg");
                intentSel.putExtra("publicScope","1");
                if (getIntent().hasExtra("share")) {
                    intentSel.putExtra("share",1);
                    intentSel.putExtra("record",record);
                }else {
                    intentSel.putExtra("tag",tagList);
                }
                if (getIntent().hasExtra("topic")) {
                    intentSel.putExtra("topic",topic);
                }
                startActivity(intentSel);
                finish();
                break;
            case R.id.img_back_act:
                finish();
                break;
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
