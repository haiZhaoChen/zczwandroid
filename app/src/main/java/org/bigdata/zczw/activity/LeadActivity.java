package org.bigdata.zczw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.LeadViewPagerAdapter;
import org.bigdata.zczw.fragment.LeadImageFragment;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/*
* 新特性
* */

public class LeadActivity extends AppCompatActivity {
    private ViewPager leadView;
    private List<LeadImageFragment> list;
    private ImageView[] icons;

    private  boolean isFirstRun = false;
    private SharedPreferences.Editor editor;

    private String versionName;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);

        str = SPUtil.getString(this,SPUtil.VERSION);
        //版本显示代码
        versionName = null;
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        if (!Utils.judgeNetConnected(LeadActivity.this)) {
            setNetworkMethod();
            Toast.makeText(getApplicationContext(), "当前网络不可用！", Toast.LENGTH_SHORT).show();
        }else{
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(versionName) && versionName.equals(str)) {
                startActivity(new Intent(LeadActivity.this, StartActivity.class));
                LeadActivity.this.finish();
            }else {
                init();
                initBottomIcons();
                if (!TextUtils.isEmpty(versionName)) {
                    SPUtil.put(this,SPUtil.VERSION,versionName);
                }
            }
        }
    }

    //打开设置网络界面
    public  void setNetworkMethod(){
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(LeadActivity.this);
        builder.setMessage("网络连接不可用,是否进行设置?");
        builder.setTitle("网络设置提示：");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS );
                startActivityForResult(intent, 66);
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(versionName) && versionName.equals(str)) {
            startActivity(new Intent(LeadActivity.this, StartActivity.class));
            LeadActivity.this.finish();
        }else {
            init();
            initBottomIcons();
            if (!TextUtils.isEmpty(versionName)) {
                SPUtil.put(this,SPUtil.VERSION,versionName);
            }
        }
    }

    private void init() {
        leadView = (ViewPager) findViewById(R.id.viewpager_lead);

        list = new ArrayList<>();
        for(int i = 0;i < 4;i++){
            LeadImageFragment fragment = new LeadImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("value", i);
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        LeadViewPagerAdapter adapter = new LeadViewPagerAdapter(getSupportFragmentManager(),list);
        leadView.setAdapter(adapter);

        leadView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //滑动后选择了某一个页面
            //参数代表着滑动的界面所对应的下标
            @Override
            public void onPageSelected(int position) {
                //实现底部圆点的切换
                for (int i = 0; i < 4; i++) {
                    //将所有的圆点都修改成白色的
                    icons[i].setImageResource(R.drawable.icon01);
                }
                //设置所选中的图片所对应的圆点是灰色
                icons[position].setImageResource(R.drawable.icon02);
            }
            //滑动了ViewPager
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            //滑动状态的改变
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void initBottomIcons() {
        // TODO Auto-generated method stub
        LinearLayout bottomIcons =
                (LinearLayout) findViewById(R.id.bottomIcoms);
        icons = new ImageView[4];
        //进行赋值
        for(int i = 0;i<bottomIcons.getChildCount();i++){
            icons[i] = (ImageView) bottomIcons.getChildAt(i);
            //设置标签，区分每一个ImageView
            icons[i].setTag(i);
            icons[i].setOnClickListener(new View.OnClickListener() {
                //参数代表着被点击的对象
                //对每一个点击的对象都设置了Tag
                //点击的时候，获取tag，让viewPager进行跳转
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //点击某一个底部原点，进行跳转
                    //viewPager进行跳转
                    int index = (Integer) v.getTag();
                    leadView.setCurrentItem(index);
                }
            });
        }
        //设置第一原点默认选中状态
        icons[0].setImageResource(R.drawable.icon02);
    }
}
