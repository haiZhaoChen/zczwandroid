package org.bigdata.zczw.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.MsgTag;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.utils.AppManager;

import java.util.ArrayList;
import java.util.List;

public class MsgTimeTypeActivity extends AppCompatActivity implements View.OnClickListener{


    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private RadioGroup radioGroup4;
    private RadioButton gonggongBtn;
    private RadioButton shoufeiBtn;
    private RadioButton yanghuBtn;
    private RadioButton jidianBtn;
    private RadioButton xindiaoBtn;
    private RadioButton oneWeakBtn;
    private RadioButton oneMonthBtn;
    private RadioButton threeMonthBtn;
    private RadioButton sixMonthBtn;
    private RadioButton oneYearBtn;

    private String addType;
    private int addTimeType;

    private List<User> userList;
    private String userIds ;
    private String userNames,topic ;
    private Record record;

    private ArrayList<MsgTag> tagList;

    private TextView nextTextBtn;
    private ArrayList<Long> timeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_msg_leader);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setLogo(R.drawable.empty_logo);// actionbar 添加logo
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);

        initView();
        initData();

    }


    private void initData(){

        userIds = "";
        userNames = "";
        userList = new ArrayList<>();
        timeList = new ArrayList<>();
        Long oneDay = 24*3600*1000L;
        timeList.add(oneDay);
        timeList.add(oneDay*7);
        timeList.add(oneDay*30);
        timeList.add(oneDay*90);
        timeList.add(oneDay*180);
        timeList.add(oneDay*365);

        tagList = (ArrayList<MsgTag>) getIntent().getSerializableExtra("tag");

        if (getIntent().hasExtra("record")) {
            record = (Record) getIntent().getSerializableExtra("record");
        }
        if (getIntent().hasExtra("topic")) {
            topic = getIntent().getStringExtra("topic");
        }


    }



    private void initView(){

         radioGroup1 = (RadioGroup)findViewById(R.id.rg_tag_type_leader1);
         radioGroup2 = (RadioGroup)findViewById(R.id.rg_tag_type_leader11);
         radioGroup3 = (RadioGroup)findViewById(R.id.rg_tag_type_leader2);
         radioGroup4 = (RadioGroup)findViewById(R.id.rg_tag_type_leader22);
         gonggongBtn = (RadioButton) findViewById(R.id.rb_tag_gg);
         shoufeiBtn = (RadioButton) findViewById(R.id.rb_tag_sf);
         yanghuBtn = (RadioButton) findViewById(R.id.rb_tag_yh);
         jidianBtn = (RadioButton) findViewById(R.id.rb_tag_jd);
         xindiaoBtn = (RadioButton) findViewById(R.id.rb_tag_xd);
         oneWeakBtn = (RadioButton) findViewById(R.id.rb_tag_yizhou);
         oneMonthBtn = (RadioButton) findViewById(R.id.rb_tag_yue);
         threeMonthBtn = (RadioButton) findViewById(R.id.rb_tag_sanyue);
         sixMonthBtn = (RadioButton) findViewById(R.id.rb_tag_liuyue);
         oneYearBtn = (RadioButton) findViewById(R.id.rb_tag_year);

        nextTextBtn = (TextView)findViewById(R.id.txt_next_msg_tag_act);
        nextTextBtn.setOnClickListener(this);

         radioGroup1.setOnCheckedChangeListener(new radioGroup1Listener());
         radioGroup2.setOnCheckedChangeListener(new radioGroup2Listener());
         radioGroup3.setOnCheckedChangeListener(new radioGroup3Listener());
         radioGroup4.setOnCheckedChangeListener(new radioGroup4Listener());


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_next_msg_tag_act:

                Intent intentAll;
                intentAll = new Intent(MsgTimeTypeActivity.this,AddMessageActivity.class);
                intentAll.putExtra("tag",tagList);
                if (getIntent().hasExtra("topic")) {
                    intentAll.putExtra("topic",topic);
                }

                intentAll.putExtra("type","0");
                intentAll.putExtra("publicScope","10");

                Long increeseTime = timeList.get(addTimeType);
                intentAll.putExtra("increaseTime",increeseTime+"");
                intentAll.putExtra("increaseType",addType);
                startActivity(intentAll);
                finish();
                break;
        }

    }

    private class radioGroup1Listener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {


            switch (checkedId){
                case R.id.rb_tag_gg:
                    if (gonggongBtn.isChecked())
                        radioGroup2.clearCheck();
                    addType = "1";
                    break;
                case R.id.rb_tag_sf:
                    if (shoufeiBtn.isChecked())
                        radioGroup2.clearCheck();
                    addType = "2";
                    break;
                case R.id.rb_tag_yh:
                    if (yanghuBtn.isChecked())
                        radioGroup2.clearCheck();
                    addType = "3";
                    break;
            }


        }
    }
    private class radioGroup2Listener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId){
                case R.id.rb_tag_jd:
                    if (jidianBtn.isChecked())
                        radioGroup1.clearCheck();
                    addType = "4";
                    break;
                case R.id.rb_tag_xd:
                    if (xindiaoBtn.isChecked())
                        radioGroup1.clearCheck();
                    addType = "5";
                    break;

            }



        }
    }
    private class radioGroup3Listener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId){
                case R.id.rb_tag_yizhou:
                    if (oneWeakBtn.isChecked())
                        radioGroup4.clearCheck();
                    addTimeType =0;
                    break;
                case R.id.rb_tag_yue:
                    if (oneMonthBtn.isChecked())
                        radioGroup4.clearCheck();
                    addTimeType =1;
                    break;
                case R.id.rb_tag_sanyue:
                    if (threeMonthBtn.isChecked())
                        radioGroup4.clearCheck();
                    addTimeType =2;
                    break;
            }


        }
    }

    private class radioGroup4Listener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId){
                case R.id.rb_tag_liuyue:
                    if (sixMonthBtn.isChecked())
                        radioGroup3.clearCheck();
                    addTimeType =3;
                    break;
                case R.id.rb_tag_year:
                    if (oneYearBtn.isChecked())
                        radioGroup3.clearCheck();
                    addTimeType =4;
                    break;

            }


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
