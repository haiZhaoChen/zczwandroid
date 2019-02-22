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
import org.bigdata.zczw.adapter.ExamAdapter;
import org.bigdata.zczw.entity.Test;
import org.bigdata.zczw.entity.TestData;
import org.bigdata.zczw.entity.TestDataEntity;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.List;
/*
* 试卷列表
* */
public class TestListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<Test> testList;
    private ExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("试卷列表");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.test_list);
        ServerUtils.getExamList(exam);

        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(TestListActivity.this,QuestionSurveyActivity.class);
        intent.putExtra("url",testList.get(position).getUrl());
        startActivity(intent);
    }

    private  RequestCallBack<String> exam = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            TestData testData = JsonUtils.jsonToPojo(json, TestData.class);
            if(testData.getMsg().equals("OK")){
                TestDataEntity dataEntity = testData.getData();
                testList = dataEntity.getRecordList();
                adapter = new ExamAdapter(TestListActivity.this,testList);
                listView.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

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
}
