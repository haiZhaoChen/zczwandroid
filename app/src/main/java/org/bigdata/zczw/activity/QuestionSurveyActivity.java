package org.bigdata.zczw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
* 考试
* */
public class QuestionSurveyActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_survey);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("开始考试");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        init();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.web_view_question);
        progressBar = (ProgressBar) findViewById(R.id.focus_progressbar);

        url = getIntent().getStringExtra("url");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        // 设置WebView的一些缩放功能点
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(WebChromeClient);
        webView.setInitialScale(100);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
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

    private android.webkit.WebChromeClient WebChromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress>60){
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };
}
