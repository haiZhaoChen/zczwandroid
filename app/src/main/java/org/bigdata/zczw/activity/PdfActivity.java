package org.bigdata.zczw.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Regulation;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
/*
* 三标制度pdf阅读
* */
public class PdfActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener, View.OnClickListener {

    private PDFView pdfView;
    private TextView before,after;
    private ProgressBar progressBar;

    private String url ;
    private ArrayList<Regulation> arrayList;
    private int position;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 101:
                    pdfView.fromStream(is)
                            .defaultPage(0)
                            .onPageChange(PdfActivity.this)
                            .enableAnnotationRendering(false) // 提供注释（例如注释，颜色或形式）
                            .onLoad(PdfActivity.this)
                            .scrollHandle(new DefaultScrollHandle(PdfActivity.this))
                            .enableAntialiasing(true) // 在低分辨率屏幕上改善渲染效果
                            //在dp中的页面间距。要定义间距颜色，请设置视图背景
                            .spacing(10)
                            .onPageError(PdfActivity.this)
                            .load();
                    getSupportActionBar().setTitle(arrayList.get(position).getName());
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);


        arrayList = (ArrayList<Regulation>) getIntent().getSerializableExtra("list");
        position = getIntent().getIntExtra("position",0);


        pdfView = (PDFView) findViewById(R.id.pdfView);
        before = (TextView) findViewById(R.id.btn_before_pdf_act);
        after = (TextView) findViewById(R.id.btn_after_pdf_act);
        progressBar = (ProgressBar) findViewById(R.id.bar_pdf_act);

        before.setOnClickListener(this);
        after.setOnClickListener(this);

        initPdf(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_before_pdf_act:
                if (position == 0) {
                    Utils.showToast(PdfActivity.this,"当前为第一节");
                }else {
                    position--;
                    initPdf(position);
                }
                break;
            case R.id.btn_after_pdf_act:
                if (position >= arrayList.size()-1) {
                    Utils.showToast(PdfActivity.this,"已阅读最后一节");
                }else {
                    position++;
                    initPdf(position);
                }
                break;
        }
    }

    private void initPdf(int position) {
        progressBar.setVisibility(View.VISIBLE);
        url = arrayList.get(position).getUrl();
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL myURL= null;
                try {
                    myURL = new URL(url);
                    URLConnection ucon = myURL.openConnection();
                    is = ucon.getInputStream();
                    handler.sendEmptyMessage(101);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Utils.showToast(PdfActivity.this,"数据异常！");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onPageChanged(int i, int i1) {

    }

    @Override
    public void loadComplete(int i) {

    }

    @Override
    public void onPageError(int i, Throwable throwable) {
        Log.e("1111", "Cannot load page");
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

}
