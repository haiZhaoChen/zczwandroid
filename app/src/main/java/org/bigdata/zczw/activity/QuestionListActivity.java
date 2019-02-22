package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.QuestionAdapter;
import org.bigdata.zczw.entity.Question;
import org.bigdata.zczw.entity.Questions;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.List;
/*
* 常见问题列表
* */
public class QuestionListActivity extends AppCompatActivity {

    private ListView listView;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setTitle("常见问题列表");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView_question_list_act);

        ServerUtils.getQuestionList(qCallBack);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuestionListActivity.this,WebInfoActivity.class);
                intent.putExtra("url",questionList.get(position).getUrl());
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });
    }

    private RequestCallBack<String> qCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Questions questions = JsonUtils.jsonToPojo(json,Questions.class);
            if (questions != null && questions.getStatus() == 200) {
                questionList = questions.getData();
                questionAdapter = new QuestionAdapter(QuestionListActivity.this,questionList);
                listView.setAdapter(questionAdapter);
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
}
