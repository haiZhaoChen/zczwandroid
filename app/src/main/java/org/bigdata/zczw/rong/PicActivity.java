package org.bigdata.zczw.rong;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

import org.bigdata.zczw.R;
import org.bigdata.zczw.image.glide.OkHttpProgressGlideModule;
import org.bigdata.zczw.image.glide.ProgressTarget;
import org.bigdata.zczw.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PicActivity extends Activity {

    private Uri uri;

    private LargeImageView largeImageView;
    private TextView save;
    private ProgressBar progressBar;

    private OkHttpClient client;

    private String str[];

    private static final int IS_SUCCESS = 1;
    private static final int IS_FAIL = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IS_SUCCESS:
                    str = uri.toString().split("/");
                    String name = str[str.length-1];
                    byte[] bytes = (byte[]) msg.obj;
                    bytesToImageFile(bytes,name);
                    break;
                case IS_FAIL:
                    Utils.showToast(PicActivity.this,"保存失败");
                    break;
                default:
                    break;
            }
        }
    };

    private void bytesToImageFile(byte[] bytes,String name) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/zczw/images/"+name);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();
            Utils.showToast(PicActivity.this,"保存成功");
        } catch (Exception e) {
            Utils.showToast(PicActivity.this,"保存失败");
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        uri = getIntent().getParcelableExtra("uri");

        progressBar = (ProgressBar) findViewById(R.id.loading);
        largeImageView = (LargeImageView) findViewById(R.id.networkDemo_photoView_pic);
        save = (TextView) findViewById(R.id.txt_save_img);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncGet(uri.toString());
            }
        });

        final Glide glide = Glide.get(this);
        OkHttpProgressGlideModule a = new OkHttpProgressGlideModule();
        a.registerComponents(this, glide);

        Glide.with(this).load(uri).downloadOnly(new ProgressTarget<String, File>(uri.toString(), null) {
            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(long bytesRead, long expectedLength) {
                int p = 0;
                if (expectedLength >= 0) {
                    p = (int) (100 * bytesRead / expectedLength);
                }
            }

            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> animation) {
                super.onResourceReady(resource, animation);
                progressBar.setVisibility(View.GONE);
                largeImageView.setImage(new FileBitmapDecoderFactory(resource));
            }

            @Override
            public void getSize(SizeReadyCallback cb) {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }
        });

    }


    /**
     * 异步get,直接调用
     */
    private void asyncGet(String url) {
        client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message message = handler.obtainMessage();
                if (response.isSuccessful()) {
                    message.what = IS_SUCCESS;
                    message.obj = response.body().bytes();
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(IS_FAIL);
                }
            }
        });
    }

}
