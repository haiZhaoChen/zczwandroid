package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.ui.LoadingDialog;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.DateUtils;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.imkit.RongIM;

/*
* 修改密码
* */

public class ChangeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText pwd_now;
    private EditText pwd_update;
    private EditText pwd_update_ok;
    private Button pwd_ok;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        getSupportActionBar().setTitle("密码设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        initView();
    }

    private void initView() {
        pwd_now = (EditText) findViewById(R.id.pwd_now);
        pwd_update = (EditText) findViewById(R.id.pwd_update);
        pwd_update_ok = (EditText) findViewById(R.id.pwd_update_ok);
        pwd_ok = (Button) findViewById(R.id.pwd_ok);
//^[A-Za-z0-9]+$
        pwd_ok.setOnClickListener(this);
        pwd_update.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isRightPwd(pwd_update.getText().toString())) {
                    pwd_update_ok.setEnabled(true);
                    pwd_update_ok.setHint("再次确认密码");
                } else {
                    pwd_update_ok.setHint("");
                    pwd_update_ok.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pwd_ok:
                if (pwd_now == null || "".equals(pwd_now.getText().toString())) {
                    WinToast.toast(this, "请输入当前密码");
                    return;
                } else if (pwd_update == null
                        || !isRightPwd(pwd_update.getText().toString())) {
                    WinToast.toast(this, "密码格式不正确");
                    return;
                } else if (pwd_update_ok == null
                        || !(pwd_update_ok.getText().toString()).equals(pwd_update.getText().toString())) {
                    WinToast.toast(this, "两次输入密码不匹配");
                    return;
                }
                if (mDialog == null) {
                    mDialog = new LoadingDialog(this);
                    mDialog.show();
                } else {
                    mDialog.show();
                }
                String passWord_now = DateUtils.string2MD5(pwd_now.getText().toString().trim());
                String password_update = DateUtils.string2MD5(pwd_update.getText().toString().trim());
                String userName = SPUtil.getString(ChangeActivity.this, App.USER_PHONE);
                ServerUtils.changePwd(userName, passWord_now, password_update, changePwd);
                break;
        }
    }

    RequestCallBack<String> changePwd = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch(demoApiJSON.getStatus()){
                case 200://密码修改成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    WinToast.toast(getApplicationContext(),"密码修改成功");
                    RongIM.getInstance().disconnect();
                    startActivity(new Intent(ChangeActivity.this,LoginActivity.class));
                    finish();
                    break;
                case 400://客户端错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    WinToast.toast(getApplicationContext(),demoApiJSON.getMsg());
                    break;
                case 444://登陆过期跳转到登录界面
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    WinToast.toast(getApplicationContext(),"登陆过期");
                    startActivity(new Intent(ChangeActivity.this,LoginActivity.class));
                    break;
                case 500://服务器错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    WinToast.toast(getApplicationContext(),demoApiJSON.getMsg());
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    /**
     * 验证密码格式，6——10位数字或字母或数字与字母组合
     *
     * @param mobiles
     * @return
     */
    private static boolean isRightPwd(String mobiles) {
        Pattern p = Pattern.compile("^[0-9a-zA-Z]{6,16}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
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
