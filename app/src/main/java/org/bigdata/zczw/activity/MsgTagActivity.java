package org.bigdata.zczw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.MsgTag;
import org.bigdata.zczw.entity.MsgTags;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;

import java.util.ArrayList;
import java.util.Set;
/*
* 发动态 标签选择
* */
public class MsgTagActivity extends AppCompatActivity implements View.OnClickListener {

    private TagFlowLayout tagFlowLayout;
    private TextView textView;
    private ImageView imageView;

    private String [] mVals ;
    private LayoutInflater mInflater;
    private ArrayList<MsgTag> arrayList;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_tag);

        getSupportActionBar().hide();

        if (getIntent().hasExtra("topic")) {
            topic = getIntent().getStringExtra("topic");
        }


        mInflater = LayoutInflater.from(MsgTagActivity.this);
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        textView = (TextView) findViewById(R.id.txt_next_msg_tag_act);
        imageView = (ImageView) findViewById(R.id.img_back_act);

        ServerUtils.getMsgTag(requestCallBack);

        textView.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_next_msg_tag_act:
                Set<Integer> set = tagFlowLayout.getSelectedList();

                if (set==null|| set.size() == 0) {
                    Utils.showToast(MsgTagActivity.this,"请选择标签");
                    return;
                }

                Boolean isLuntan = false;
                for (int index : set) {
                    MsgTag tag = arrayList.get(index);
                    tag.setCheck(true);
                    if (tag.getId()>100001){
                        isLuntan = true;
                    }

                }

                Intent intent;

                if (true){
                    //如果包含张承论坛标签，进入选择时间页面
                    intent = new Intent(MsgTagActivity.this,MsgTimeTypeActivity.class);

                }else {
                    intent = new Intent(MsgTagActivity.this,MsgPowerActivity.class);
                }



                intent.putExtra("tag",arrayList);
                if (!TextUtils.isEmpty(topic)) {
                    intent.putExtra("topic",topic);
                }
                startActivity(intent);
                finish();

                break;
            case R.id.img_back_act:
                finish();
                break;
        }
    }

    private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            MsgTags bean = JsonUtils.jsonToPojo(json,MsgTags.class);
            if (bean != null && bean.getStatus() == 200 && bean.getData()!=null && bean.getData().size()>0) {
                arrayList = (ArrayList<MsgTag>) bean.getData();
                mVals = new String[arrayList.size()];
                for (int i = 0; i < arrayList.size(); i++) {
                    mVals[i] = arrayList.get(i).getName();
                }

                tagFlowLayout.setAdapter(new TagAdapter<String>(mVals) {

                    @Override
                    public View getView(FlowLayout flowLayout, int i, String s) {
                        TextView tv = (TextView) mInflater.inflate(R.layout.search_tag_text, tagFlowLayout, false);
                        tv.setText(s);
                        return tv;
                    }

                    @Override
                    public void onSelected(int position, View view) {
                        view.setBackground(getResources().getDrawable(R.drawable.frame_radius_yellow));
                        ((TextView)view).setTextColor(Color.parseColor("#e6731c"));
                        super.onSelected(position, view);
                    }

                    @Override
                    public void unSelected(int position, View view) {
                        view.setBackground(getResources().getDrawable(R.drawable.frame_radius_gray0));
                        ((TextView)view).setTextColor(Color.parseColor("#1e1e1e"));
                        super.unSelected(position, view);
                    }
                });

            }else {
                Utils.showToast(MsgTagActivity.this,"服务器异常");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


}
