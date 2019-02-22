package org.bigdata.zczw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Num;
import org.bigdata.zczw.utils.AppManager;

/*
* 我的消息页面 不用了
* */

public class TrendMsgActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout aboutMe,zan,comment;
    private TextView atNum,comNum,zanNum;
    private ImageView atImg,comImg,zanImg;

    private Num num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_msg);
        getSupportActionBar().setTitle("我的消息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        init();
    }

    private void init() {
        aboutMe = (RelativeLayout) findViewById(R.id.rl_about_me_trend_msg_act);
        zan = (RelativeLayout) findViewById(R.id.rl_zan_trend_msg_act);
        comment = (RelativeLayout) findViewById(R.id.rl_pinglun_trend_msg_act);

        atNum = (TextView) findViewById(R.id.txt_at_num);
        comNum = (TextView) findViewById(R.id.txt_comment_num);
        zanNum = (TextView) findViewById(R.id.txt_zan_num);

        atImg = (ImageView) findViewById(R.id.img_at_to_new);
        comImg = (ImageView) findViewById(R.id.img_com_to_new);
        zanImg = (ImageView) findViewById(R.id.img_zan_to_new);

        aboutMe.setOnClickListener(this);
        zan.setOnClickListener(this);
        comment.setOnClickListener(this);

        if (App.NUM1 > 0) {
            atNum.setVisibility(View.VISIBLE);
            atImg.setVisibility(View.GONE);
            atNum.setText( App.NUM1+"");
        }
        if (App.NUM3 > 0) {
            zanNum.setVisibility(View.VISIBLE);
            zanImg.setVisibility(View.GONE);
            zanNum.setText(App.NUM3+"");
        }
        if (App.NUM2 > 0) {
            comNum.setVisibility(View.VISIBLE);
            comImg.setVisibility(View.GONE);
            comNum.setText(App.NUM2+"");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_about_me_trend_msg_act:
                atNum.setVisibility(View.GONE);
                atImg.setVisibility(View.VISIBLE);
                Intent intent = new Intent(TrendMsgActivity.this,TrendActivity.class);
                intent.putExtra("type","about");
                App.NUM1 = 0;
                startActivity(intent);
                break;
            case R.id.rl_zan_trend_msg_act:
                zanNum.setVisibility(View.GONE);
                zanImg.setVisibility(View.VISIBLE);
                App.NUM3 = 0;
                Intent intent1 = new Intent(TrendMsgActivity.this,TrendActivity.class);
                intent1.putExtra("type","zan");
                startActivity(intent1);
                break;
            case R.id.rl_pinglun_trend_msg_act:
                comNum.setVisibility(View.GONE);
                comImg.setVisibility(View.VISIBLE);
                App.NUM2 = 0;
                Intent intent2 = new Intent(TrendMsgActivity.this,TrendComActivity.class);
                intent2.putExtra("type","ping");
                startActivity(intent2);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
