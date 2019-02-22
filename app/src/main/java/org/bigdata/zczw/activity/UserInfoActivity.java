package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.utils.SPUtil;

/*
* 好友信息详情
* */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeLayout;

    private RelativeLayout rela_Portrait;
    private ImageView imageView;
    private TextView userName;
    private TextView unit;
//    private TextView level;
    private TextView post;
//    private TextView type;

    @Override
    protected void onStart() {
        super.onStart();
        String img = SPUtil.getString(this, App.IMAGE_POSITION, "");
        if (!TextUtils.isEmpty(img)&& img.length()>5) {
            Picasso.with(this).load(img).into(imageView);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        initDate();
    }

    private void initView() {
        getSupportActionBar().setTitle("个人详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        userName = (TextView) findViewById(R.id.userInfoname);
        unit = (TextView) findViewById(R.id.userInfounit);
//        level = (TextView) findViewById(R.id.userInfolevel);
        post = (TextView) findViewById(R.id.userInfopost);
        rela_Portrait = (RelativeLayout) findViewById(R.id.rela);
        imageView = (ImageView) findViewById(R.id.async_image);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_personal_detail);
    }

    private void initDate() {
        rela_Portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, PortraitActivity.class);
                startActivity(intent);
            }
        });

        userName.setText(SPUtil.getString(this,App.USER_NAME));
        unit.setText(SPUtil.getString(this,App.UNITSNAME));
//        level.setText(SPUtil.getString(this,App.POSITION_NAME));
        post.setText(SPUtil.getString(this,App.JOBSNAME));

        relativeLayout.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_personal_detail:
                Intent intent = new Intent(UserInfoActivity.this,DetailActivity.class);
                intent.putExtra("id",SPUtil.getString(this,App.USER_ID));
                intent.putExtra("name",SPUtil.getString(this,App.USER_NAME));
                startActivity(intent);
                break;
        }
    }
}
