package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.TopicNameAdapter;
import org.bigdata.zczw.entity.Topic;
import org.bigdata.zczw.entity.TopicBean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;

/*
* 发布话题选取搜索
* */

public class TopicSearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    private ListView listView;
    private TextView textView;
    private EditText editText;

    private String msg;
    private String edit;

    private boolean isNew;

    private ArrayList<Topic> topicList;
    private TopicNameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_search);

        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);

        msg = getIntent().getStringExtra("msg");
        listView = (ListView) findViewById(R.id.list_topic_search_act);
        textView = (TextView) findViewById(R.id.txt_search_cancel);
        editText = (EditText) findViewById(R.id.edit_topic_search_act);

        textView.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        editText.addTextChangedListener(this);

        topicList = new ArrayList<>();

        ServerUtils.getTopicHistory(historyCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_search_cancel:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Topic topic = topicList.get(position);
        msg = msg + topic.getName() + "# ";
        Intent intent = new Intent();
        intent.putExtra("msg",msg);
        intent.putExtra("topic",topic.getName());
        setResult(201,intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        edit = s.toString();
        if (TextUtils.isEmpty(edit)) {
            ServerUtils.getTopicHistory(historyCallback);
        }else {
            topicList.clear();
            ServerUtils.getTopicSearch(edit,searchCallback);
        }
    }

    private RequestCallBack<String> historyCallback =new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            TopicBean bean = JsonUtils.jsonToPojo(json,TopicBean.class);
            if (bean != null) {
                if (bean.getData() != null && bean.getData().size()>0) {
                    topicList = (ArrayList<Topic>) bean.getData();
                    adapter = new TopicNameAdapter(TopicSearchActivity.this,topicList);
                    listView.setAdapter(adapter);
                }else {
                    adapter = new TopicNameAdapter(TopicSearchActivity.this,topicList);
                    listView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> searchCallback =new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            TopicBean bean = JsonUtils.jsonToPojo(json,TopicBean.class);
            if (bean != null) {
                if (bean.getData() != null && bean.getData().size()>0) {
                    Topic topic = new Topic(0,edit);
                    topicList = (ArrayList<Topic>) bean.getData();

                    for (int i = 0; i < topicList.size(); i++) {
                        if (topicList.get(i).getName().equals(edit)) {
                            isNew = false;
                            topic.setNewb(false);
                        }else {
                            isNew = true;
                            topic.setNewb(true);
                        }
                    }
                    if (isNew) {
                        topicList.add(0,topic);
                    }

                    adapter = new TopicNameAdapter(TopicSearchActivity.this,topicList);
                    listView.setAdapter(adapter);
                }else {
                    topicList.clear();
                    Topic topic = new Topic(0,edit);
                    topic.setNewb(true);
                    topicList.add(topic);
                    adapter = new TopicNameAdapter(TopicSearchActivity.this,topicList);
                    listView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(msg) && msg.length()>0) {
            msg = msg.substring(0,msg.length()-1);
        }
        intent.putExtra("msg",msg);
        setResult(202,intent);
        super.onBackPressed();
    }
}
