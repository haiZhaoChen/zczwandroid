package org.bigdata.zczw.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.utils.AppManager;
/*
* 忘记密码
* */
public class ForgetPawActivity extends AppCompatActivity {
    private TextView hint;
    private int REQUEST_CODE_CALL_PHONE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forger_paw);

        AppManager.getAppManager().addActivity(this);
        hint = (TextView) findViewById(R.id.act_forgetpwd_phone);

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(ForgetPawActivity.this, Manifest.permission.CALL_PHONE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ForgetPawActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
            }
        }
        String  tv_phone_str = "如密码遗忘，请拨打<font color='blue'>15632301743</font>获取帮助。";
        hint.setText(Html.fromHtml(tv_phone_str));
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneStr = "15632301743";

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneStr.substring(0, 11)));
                //开启系统拨号器
                startActivity(intent);
            }
        });
    }
}
