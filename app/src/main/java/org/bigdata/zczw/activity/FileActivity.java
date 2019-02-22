package org.bigdata.zczw.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.FileBean;
import org.bigdata.zczw.ui.CircleImageView;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.Utils;

import java.io.File;
import java.text.DecimalFormat;

import okhttp3.Call;

/*
* 动态文件下载页面
* */

public class FileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView wordName,wordSize;
    private CircleImageView imgWord;
    private TextView btnLode,btnOpen,txtProgress;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    private long id;
    private FileBean fileBean;
    private String url;
    private String name;
    private File fileResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        getSupportActionBar().setLogo(R.drawable.empty_logo);// actionbar 添加logo
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        getSupportActionBar().setTitle("文件浏览");
        AppManager.getAppManager().addActivity(this);

        initView();
        initData();
    }

    private void initView() {
        wordSize = (TextView) findViewById(R.id.txt_word_size_file);
        wordName = (TextView) findViewById(R.id.txt_word_name_file);
        btnLode = (TextView) findViewById(R.id.txt_lode_file);
        btnOpen = (TextView) findViewById(R.id.txt_open_file);
        txtProgress = (TextView) findViewById(R.id.txt_progress);
        imgWord = (CircleImageView) findViewById(R.id.img_word_type_file);

        linearLayout = (LinearLayout) findViewById(R.id.ll_progress_content);
        progressBar = (ProgressBar) findViewById(R.id.bar);

        btnLode.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
    }

    private void initData() {
        fileBean = (FileBean) getIntent().getSerializableExtra("file");
        id = getIntent().getLongExtra("id",0);
        url = fileBean.getUrl();
        name = fileBean.getName();

        wordName.setText(name);
        if (fileBean.getSize()>1024*1024) {
            String string1 = new DecimalFormat("0.00").format(fileBean.getSize()/1024.00/1024.00);
            wordSize.setText(string1 +"MB");
        }else {
            String string1 = new DecimalFormat("0.00").format(fileBean.getSize()/1024.00);
            wordSize.setText(string1+"KB");
        }
        switch (fileBean.getFileType()){
            case "txt":
                imgWord.setImageResource(R.drawable.icon_word_txt);
                break;
            case "doc":
            case "docx":
                imgWord.setImageResource(R.drawable.icon_word_doc);
                break;
            case "pdf":
                imgWord.setImageResource(R.drawable.icon_word_pdf);
                break;
            case "ppt":
            case "pptx":
                imgWord.setImageResource(R.drawable.icon_word_ppt);
                break;
            case "xls":
            case "xlsx":
                imgWord.setImageResource(R.drawable.icon_word_xls);
                break;
            case "rar":
            case "7z":
            case "zip":
                imgWord.setImageResource(R.drawable.icon_word_rar);
                break;
            default:
                imgWord.setImageResource(R.drawable.icon_word_other);
                break;
        }


        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/zczw/files");
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files=file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(name) && files[i].length() == fileBean.getSize()) {
                btnLode.setVisibility(View.GONE);
                btnOpen.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                fileResponse = files[i];
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_lode_file:
                btnLode.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                OkHttpUtils.get()
                        .url(url)
                        .build()
                        .execute(new FileCallBack(Environment.getExternalStorageDirectory().getPath() + "/zczw/files", name) {
                            @Override
                            public void inProgress(float progress) {
                                progressBar.setProgress((int)(100*progress));
                                txtProgress.setText("正在下载   "+(int)(100*progress)+"%");
                            }

                            @Override
                            public void onError(Call call, Exception e) {
                                Utils.showToast(FileActivity.this,"下载失败");
                                btnLode.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResponse(File response) {
                                fileResponse = response;
                                btnOpen.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                            }

                        });
                break;
            case R.id.txt_open_file:
                Uri uri = null;
                uri = Uri.fromFile(fileResponse);
                Utils.openFile(FileActivity.this,fileResponse,uri);
                break;
        }
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
