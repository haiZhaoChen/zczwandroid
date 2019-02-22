package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.ExamAnsweredPageAdapter;
import org.bigdata.zczw.entity.ExamPreModel;
import org.bigdata.zczw.entity.ExamQuesModel;
import org.bigdata.zczw.ui.DateScrollGridView;
import org.bigdata.zczw.utils.AppManager;

import java.io.Serializable;
import java.util.ArrayList;

public class ExamAnsweredActivity extends AppCompatActivity implements View.OnClickListener{

    private DateScrollGridView gridView;
    private TextView examScore,totalNum,rightNum;
    private Button submitBtn;
    //上一个页面传入的数据
    //答案列表
    private ArrayList<Boolean> resultList;
    private ArrayList<ArrayList<Integer>> resultABCList;
    //考试类和考试下的试题列表
    private ExamPreModel examPreModel;
    private ArrayList<ExamQuesModel> examQuesList;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_answeredpage);
        //设置属性
        getSupportActionBar().setTitle("答题报告");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);
        //初始化页面和数据
        initView();
        initData();
    }

    private void initView(){
        gridView = (DateScrollGridView)findViewById(R.id.item_grid_check_act);
        submitBtn = (Button)findViewById(R.id.ques_answers_list_btn);
        examScore = (TextView)findViewById(R.id.exam_score_text);
        totalNum = (TextView)findViewById(R.id.ques_total_num);
        rightNum = (TextView)findViewById(R.id.answer_total_num);

        submitBtn.setOnClickListener(this);
    }
    private void initData(){

        resultList = (ArrayList<Boolean>) getIntent().getSerializableExtra("result");
        examPreModel = (ExamPreModel) getIntent().getSerializableExtra("exam");
        examQuesList = (ArrayList<ExamQuesModel>) getIntent().getSerializableExtra("answers");
        resultABCList = (ArrayList<ArrayList<Integer>>)getIntent().getSerializableExtra("resultABC") ;

        ExamAnsweredPageAdapter examAnsweredPageAdapter = new ExamAnsweredPageAdapter(ExamAnsweredActivity.this,resultList);
        gridView.setAdapter(examAnsweredPageAdapter);

        totalNum.setText(examPreModel.getQuestionCount()+"");
        int i=0;
        for (Boolean b : resultList){
            if (b){
                i++;
            }
        }
        rightNum.setText(i+"");

        if (i*1.0/resultList.size()>= 0.6){
            examScore.setText("+5");
        }else {
            examScore.setText("+2");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ques_answers_list_btn:

                //跳到下一个页面，并finish
                Intent intent = new Intent(ExamAnsweredActivity.this,ExamAnswerParseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("result", (Serializable) resultList);
                bundle.putSerializable("resultABC",(Serializable)resultABCList);
                intent.putExtras(bundle);
                intent.putExtra("exam",examPreModel);
                intent.putExtra("answers",(Serializable) examQuesList);
                startActivity(intent);
                finish();
                break;
        }

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
