package org.bigdata.zczw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.bigdata.zczw.R;
import org.bigdata.zczw.utils.AppManager;
/*
* webView
* */
public class WebInfoActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private WebSettings settings;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_info);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        if (getIntent().hasExtra("type")) {
            url = getIntent().getStringExtra("url");
            getSupportActionBar().setTitle("常见问题");
        }else {
            url = getIntent().getStringExtra("about");
            if (TextUtils.isEmpty(url)) {
                url = "http://a3.rabbitpre.com/m/bib6A2z7a";
            }
        }

        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_info_act);
        progressBar = (ProgressBar) findViewById(R.id.web_progressbar);

        settings=webView.getSettings();
        //支持Js
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        // 设置WebView的一些缩放功能点
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(WebChromeClient);
        webView.setInitialScale(100);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    private android.webkit.WebChromeClient WebChromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress > 40){
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.reload();
    }
}
