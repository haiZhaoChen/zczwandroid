package org.bigdata.zczw.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.LoadingDialog;
import org.bigdata.zczw.utils.Utils;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
/*
* 个人详情
*
* */
public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeLayout;
    private ImageView mPersonalImg;
    private TextView mPersonalName;
    private TextView mPersonalUnits;
//    private TextView mPersonalPossition;
    private TextView mPersonalJobs;
    private RelativeLayout mSendMessage;
    private RelativeLayout mSendVoip;
    private String friendid;
    private LoadingDialog mDialog;

    private int type,tag;

    private User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        initView();
        initDate();
    }

    private void initView() {
        getSupportActionBar().setTitle("个人详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        mPersonalName = (TextView) findViewById(R.id.personal_name);
//        mPersonalPossition = (TextView) findViewById(R.id.personal_position);
        mPersonalJobs = (TextView) findViewById(R.id.personal_jobs);
        mPersonalUnits = (TextView) findViewById(R.id.personal_units);

        mPersonalImg = (ImageView) findViewById(R.id.personal_portrait);
        mSendMessage = (RelativeLayout) findViewById(R.id.send_message);
        mSendVoip = (RelativeLayout) findViewById(R.id.send_voip);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_personal_detail);
        mDialog = new LoadingDialog(this);
    }

    private void initDate() {
        mSendMessage.setOnClickListener(this);
        mSendVoip.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        mPersonalImg.setOnClickListener(this);

        if (getIntent().hasExtra("tag")) {
            tag = getIntent().getIntExtra("tag",0);
            type = getIntent().getIntExtra("type",0);
        }

        if (getIntent().hasExtra("PERSONAL")) {
            friendid = getIntent().getStringExtra("PERSONAL");

            userInfo = Singleton.getInstance().getFriendById(friendid);

            if (userInfo != null) {
                if(!(TextUtils.isEmpty(userInfo.getImagePosition())||userInfo.getImagePosition().equals("null"))){
                    Picasso.with(this).load(userInfo.getImagePosition()).into(mPersonalImg);
                }
                mPersonalName.setText(userInfo.getUsername().toString());
//                mPersonalPossition.setText(userInfo.getPositionName());
                mPersonalUnits.setText(userInfo.getUnitsName());
                mPersonalJobs.setText(userInfo.getJobsName());
            }
        }
        //"userId", "啊明", Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")
        UserInfo userInfos;

        if (!TextUtils.isEmpty(userInfo.getImagePosition()) && !userInfo.getImagePosition().contains("null")) {
            userInfos = new UserInfo(userInfo.getUserid() + "", userInfo.getUsername()+"-"+userInfo.getUnitsName(), Uri.parse(userInfo.getImagePosition()));
        } else {
            userInfo.setImagePosition("");
            userInfos = new UserInfo(userInfo.getUserid() + "", userInfo.getUsername()+"-"+userInfo.getUnitsName(), Uri.parse(userInfo.getImagePosition()));
        }
        RongIM.getInstance().refreshUserInfoCache(userInfos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message:
                if (RongIM.getInstance() != null) {
                    if (tag == 1) {
                        if (type == 1) {
                            RongIM.getInstance().startPrivateChat(PersonalActivity.this, friendid, userInfo.getUsername()+"-"+userInfo.getUnitsName());
                            finish();
                        }else {
                            finish();
                        }
                    }else {
                        RongIM.getInstance().startPrivateChat(PersonalActivity.this, friendid, userInfo.getUsername()+"-"+userInfo.getUnitsName());
                        finish();
                    }
                }
                break;
            case R.id.send_voip:
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.CALL_PHONE);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(PersonalActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }
                }
                dialog();
                break;
            case R.id.rl_personal_detail:
                Intent intent = new Intent(PersonalActivity.this,DetailActivity.class);
                intent.putExtra("id",userInfo.getUserid()+"");
                intent.putExtra("name",userInfo.getUsername());
                startActivity(intent);
                break;
            case R.id.personal_portrait:
                if (userInfo != null) {
                    if(!(TextUtils.isEmpty(userInfo.getImagePosition())||userInfo.getImagePosition().equals("null"))){
                        Intent intent1 = new Intent(PersonalActivity.this, ImagePagerActivity.class);
                        ArrayList<String> urls = new ArrayList<>();
                        urls.add(userInfo.getImagePosition());
                        intent1.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                        intent1.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                        intent1.putExtra("type", 0);
                        startActivity(intent1);
                    }
                    else {
                        Utils.showToast(PersonalActivity.this,"暂未设置头像");
                    }
                }else {
                    Utils.showToast(PersonalActivity.this,"暂未设置头像");
                }
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

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
        builder.setMessage("联系人："+ userInfo.getUsername()+"\n联系电话："+userInfo.getUserphone());
        builder.setTitle("是否拨打电话联系此人？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneStr = userInfo.getUserphone();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneStr.substring(0, 11)));
                //开启系统拨号器
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
