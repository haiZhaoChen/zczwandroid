package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import io.rong.imkit.RongIM;

/*
* 设置
* */

public class SetActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout tr_banben;
    private RelativeLayout tr_mima;
    private RelativeLayout tr_exitdenglu;
    private RelativeLayout tr_exitall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setTitle("设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        tr_banben = (RelativeLayout) findViewById(R.id.user_btn_banben);
        tr_mima = (RelativeLayout) findViewById(R.id.user_btn_pass);
        tr_exitdenglu = (RelativeLayout) findViewById(R.id.user_btn_exit);
        tr_exitall = (RelativeLayout) findViewById(R.id.user_btn_out);

        tr_banben.setOnClickListener(this);
        tr_mima.setOnClickListener(this);
        tr_exitdenglu.setOnClickListener(this);
        tr_exitall.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //版本更新
            case R.id.user_btn_banben:
                startActivity(new Intent(SetActivity.this, AppInfoActivity.class));
                break;
            //密码设置
            case R.id.user_btn_pass:
                startActivity(new Intent(SetActivity.this, ChangeActivity.class));
                break;
            //退出登录
            case R.id.user_btn_exit:
                String url = DemoApi.HOST+DemoApi.DEMO_LOGOUT;
                ServerUtils.getServerDatasGet(url,logout);
                SPUtil.remove(SetActivity.this, App.ZW_TOKEN);//清空token
                SPUtil.remove(SetActivity.this, App.USER_TOKEN);//清空token
                SPUtil.remove(SetActivity.this, App.IMAGE_POSITION);
                SPUtil.remove(SetActivity.this, App.USER_NAME);
                startActivity(new Intent(SetActivity.this, LoginActivity.class));
                AppManager.getAppManager().finishAllActivity();
                finish();
                break;
            //退出软件
            case R.id.user_btn_out:
//                ServerUtils.getServerDatasGet(url, logout);
                RongIM.getInstance().logout();
                //结束所有的
                AppManager.getAppManager().finishAllActivity();
                break;
        }
    }

    private RequestCallBack<String> logout = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

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
