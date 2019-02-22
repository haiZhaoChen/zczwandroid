package org.bigdata.zczw.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.ThemeAdapter;
import org.bigdata.zczw.entity.TCom;
import org.bigdata.zczw.entity.Theme;
import org.bigdata.zczw.entity.ThemeBean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/*
* 主题详情
* */
public class ThemeActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private EditText editText;
    private TextView sent;

    private View convertView;
    private TextView title,date,content,num;
    private WebView webView;

    private ThemeAdapter adapter;

    private String id;
    private String comment;
    private ArrayList<TCom> tComs;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        getSupportActionBar().setTitle("主题消息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        listView = (ListView) findViewById(R.id.list_theme_act);
        editText = (EditText) findViewById(R.id.edit_msg_theme_act);
        sent = (TextView) findViewById(R.id.txt_send_msg_theme_act);

        convertView = LayoutInflater.from(this).inflate(R.layout.item_theme_header,null);
        title = (TextView) convertView.findViewById(R.id.txt_title_theme_header);
        date = (TextView) convertView.findViewById(R.id.txt_time_theme_header);
        content = (TextView) convertView.findViewById(R.id.txt_content_theme_header);
        num = (TextView) convertView.findViewById(R.id.txt_num_theme_header);

        webView = (WebView) convertView.findViewById(R.id.web_theme_header);

        settings = webView.getSettings();

        //支持Js
        settings.setJavaScriptEnabled(true);
        //页面支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(false);//将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true);// 打开页面时， 自适应屏幕自适应屏幕

        initData();
    }

    private void initData() {
        id = getIntent().getStringExtra("msg");
        if (!TextUtils.isEmpty(id)) {
            ServerUtils.getTheme(id,callBack);
        }

        sent.setOnClickListener(this);
    }

    private RequestCallBack<String> callBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            ThemeBean themeBean = JsonUtils.jsonToPojo(json,ThemeBean.class);
            if (themeBean != null) {
                if (themeBean.getStatus() == 200) {
                    Theme theme =  themeBean.getData();

                    title.setText(theme.getTitle());

                    String content = theme.getContent();
//                    String s = "<!doctype html>\n" +
//                            "<html>\n" +
//                            "<head>\n" +
//                            "<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />\n" +
//                            "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no'/>\n" +
//                            "<meta name='format-detection' content='telephone=no'>\n" +
//                            "<meta name='apple-mobile-web-app-capable' content='yes' />\n" +
//                            "<style>img{max-width:320px !important;}</style></head>\n" +
//                            "<body style='word-wrap:break-word;'>\n"
//                            +content+
//                            "<script>var imgs = document.getElementsByTagName('img');for(var i=0;i<imgs.length;i++) {imgs[i].setAttribute('width', '100%');}</script>" +
//                            "</body>\n" +
//                            "</html>";
                    webView.loadDataWithBaseURL(null,content, "text/html", "utf-8", null);
//                    webView.loadData(s,"text/html", "utf-8");

                    SimpleDateFormat sdf = new SimpleDateFormat("发布日期：yyyy-MM-dd");
                    String dealtTime = sdf.format(new Date(theme.getCreatetime()));

                    date.setText(dealtTime+"    阅读 "+theme.getViewCount());

                    if (theme.getComments() != null) {
                        num.setText("评论 "+theme.getComments().size());
                    }
                    listView.addHeaderView(convertView);

                    tComs = (ArrayList<TCom>) theme.getComments();
                    adapter = new ThemeAdapter(ThemeActivity.this, tComs);
                    listView.setAdapter(adapter);
                }
            }
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

    @Override
    public void onClick(View v) {
        comment = editText.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            Utils.showToast(ThemeActivity.this,"请输入文字信息");
            return;
        }else {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
            ServerUtils.getThemeCom(id, comment,comCallBack);
        }
    }

    private RequestCallBack<String> comCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            ThemeBean themeBean = JsonUtils.jsonToPojo(json,ThemeBean.class);
            if (themeBean != null) {
                if (themeBean.getStatus() == 200) {
                    Utils.showToast(ThemeActivity.this,"评论成功");
                    editText.setText("");
                    TCom tCom = new TCom();
                    tCom.setC_content(comment);
                    Date date = new Date();
                    long time = date.getTime();
                    tCom.setC_createtime(time);
                    String name = SPUtil.getString(ThemeActivity.this, App.USER_NAME);
                    String pic = SPUtil.getString(ThemeActivity.this, App.IMAGE_POSITION);
                    tCom.setC_userName(name);
                    tCom.setC_portrait(pic);
                    tComs.add(tCom);
                    num.setText("评论 "+tComs.size());
                    adapter.notifyDataSetChanged();
                }else {
                    Utils.showToast(ThemeActivity.this,"评论失败");
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
}
