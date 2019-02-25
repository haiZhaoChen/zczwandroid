package org.bigdata.zczw.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.ExamAnswerParseAdapter;
import org.bigdata.zczw.entity.ExamPreModel;
import org.bigdata.zczw.entity.ExamQuesModel;
import org.bigdata.zczw.entity.ExamQuesModelBean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

public class ExamAnswerParseActivity extends AppCompatActivity {

    private PullToRefreshListView listView;
    //答案列表
    private ArrayList<Boolean> resultList;
    private ArrayList<ArrayList<Integer>> resultABCList;
    //考试类和考试下的试题列表
    private ExamPreModel examPreModel;
    private ArrayList<ExamQuesModel> examQuesList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam_answerparse);
        //设置属性
        getSupportActionBar().setTitle("查看试卷");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);
        //初始化页面和数据
        initView();
        initData();

    }

    private void initView(){
        listView = (PullToRefreshListView)findViewById(R.id.list_exam_parse);

    }

    private void initData(){
        //如果是从试题完成页面进入
        //从试题列表进入（如果已经做完题，会直接进入些页面）
        examPreModel = (ExamPreModel) getIntent().getSerializableExtra("exam");
        if (examPreModel.isMeJoin()){
            ServerUtils.getExamPageQues(examPreModel.getId()+"",callback);

        }else {
            resultList = (ArrayList<Boolean>) getIntent().getSerializableExtra("result");

            examQuesList = (ArrayList<ExamQuesModel>) getIntent().getSerializableExtra("answers");
            resultABCList = (ArrayList<ArrayList<Integer>>)getIntent().getSerializableExtra("resultABC") ;



            ExamAnswerParseAdapter examAnswerParseAdapter = new ExamAnswerParseAdapter(ExamAnswerParseActivity.this,examQuesList,resultList,resultABCList);
            listView.setAdapter(examAnswerParseAdapter);
        }



    }

    private RequestCallBack<String> callback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String jsonStr = responseInfo.result;
            ExamQuesModelBean modelBean = JsonUtils.jsonToPojo(jsonStr,ExamQuesModelBean.class);

            if (modelBean != null){
                if (modelBean.getStatus() == 200){
                    examQuesList = (ArrayList<ExamQuesModel>) modelBean.getData();

                    resultList = (ArrayList<Boolean>) getIntent().getSerializableExtra("result");


                    resultABCList = (ArrayList<ArrayList<Integer>>)getIntent().getSerializableExtra("resultABC") ;



                    ExamAnswerParseAdapter examAnswerParseAdapter = new ExamAnswerParseAdapter(ExamAnswerParseActivity.this,examQuesList,resultList,resultABCList);
                    listView.setAdapter(examAnswerParseAdapter);

                }
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
