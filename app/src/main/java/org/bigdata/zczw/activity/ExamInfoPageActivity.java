package org.bigdata.zczw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.ExamPreModel;
import org.bigdata.zczw.entity.ExamQuesModel;
import org.bigdata.zczw.entity.ExamQuesModelBean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ExamInfoPageActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ExamQuesModel> dataSource;
    private ArrayList<String> titleAndOptionS;
    private ExamPreModel examPreModel;
    private LinearLayout questionA;
    private LinearLayout questionB;
    private LinearLayout questionC;
    private LinearLayout questionD;
    private LinearLayout questionE;
    private TextView titleName;//题干
    private ImageView titleImgView;
    private TextView quesTextA;
    private TextView quesTextB;
    private TextView quesTextC;
    private TextView quesTextD;
    private TextView quesTextE;

    private ImageView quesImgA;
    private ImageView quesImgB;
    private ImageView quesImgC;
    private ImageView quesImgD;
    private ImageView quesImgE;

    private TextView examRightNum;
    private TextView examWrongNum;
    private TextView examAnswerNum;

    private ArrayList<ImageView> imageViewList;
    private ArrayList<LinearLayout> quesViewList;
    private int[] selectedImg;
    private int[] unSelectedImg;

    private TextView submitBtn;


    private int indexQues;//第几题
    //提交按扭的状态1.表示提交2.下一题3.查看全部试题
    private int submitState;
    //存放答案
    private ArrayList<String> answers;
    private Map<String,Boolean> isSelectMap;
    private int rightNum = 0;
    private int wrongNum = 0;
    //动画
    private Animation leftToRightBarAni;
    private ImageView imgBar;
    private Timer timer;
    private TimerTask task;
    //当前题目
    private ExamQuesModel ques;
    //存储答案
    private ArrayList<Boolean> resultList;
    //存储答案
    private ArrayList<ArrayList<Integer>> resultABCList;
    //进入测试接口
    private Boolean isTestExam=false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam_info);

        //设置属性
        getSupportActionBar().setTitle("参加考试");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);
        //初始化页面和数据
        indexQues = 0;
        initView();
        initData();

    }

    private void initData() {
        isSelectMap = new HashMap<>();
        answers = new ArrayList<>();
        resultList = new ArrayList<>();
        resultABCList = new ArrayList<>();
        //提交状态为1;
        submitState = 1;
        //获取考试试题列表
        examPreModel = (ExamPreModel) getIntent().getSerializableExtra("exam");
        if (examPreModel.getExamType()==1000){
            isTestExam = true;
            ServerUtils.getTestExamPageQues(testCallback);

        }else {
            ServerUtils.getExamPageQues(examPreModel.getId()+"",callback);
        }


        leftToRightBarAni.setDuration(examPreModel.getCostLimit()*1000);

    }

    private void initView(){

        titleName = (TextView) findViewById(R.id.question_name);
        titleImgView = (ImageView)findViewById(R.id.title_img_name);
        questionA = (LinearLayout) findViewById(R.id.questionA_item);
        questionB = (LinearLayout) findViewById(R.id.questionB_item);
        questionC = (LinearLayout) findViewById(R.id.questionC_item);
        questionD = (LinearLayout) findViewById(R.id.questionD_item);
        questionE = (LinearLayout) findViewById(R.id.questionE_item);
        quesViewList = new ArrayList<LinearLayout>();
        quesViewList.add(questionA);
        quesViewList.add(questionB);
        quesViewList.add(questionC);
        quesViewList.add(questionD);
        quesViewList.add(questionE);

        quesImgA = (ImageView) findViewById(R.id.questionA_img_name);
        quesImgB = (ImageView) findViewById(R.id.questionB_img_name);
        quesImgC = (ImageView) findViewById(R.id.questionC_img_name);
        quesImgD = (ImageView) findViewById(R.id.questionD_img_name);
        quesImgE = (ImageView) findViewById(R.id.questionE_img_name);

        quesTextA = (TextView) findViewById(R.id.questionA_text_name);
        quesTextB = (TextView) findViewById(R.id.questionB_text_name);
        quesTextC = (TextView) findViewById(R.id.questionC_text_name);
        quesTextD = (TextView) findViewById(R.id.questionD_text_name);
        quesTextE = (TextView) findViewById(R.id.questionE_text_name);

        submitBtn = (Button)findViewById(R.id.ques_submit_next);

        examRightNum =(TextView) findViewById(R.id.exam_right_num);
        examWrongNum =(TextView) findViewById(R.id.exam_error_num);
        examAnswerNum=(TextView) findViewById(R.id.exam_answer_num);

        imgBar = (ImageView)findViewById(R.id.left_to_right_bar);

        imageViewList = new ArrayList<ImageView>();
        imageViewList.add(quesImgA);
        imageViewList.add(quesImgB);
        imageViewList.add(quesImgC);
        imageViewList.add(quesImgD);
        imageViewList.add(quesImgE);

        selectedImg = new int[]{R.drawable.a_selected,R.drawable.b_selected,R.drawable.c_selected,R.drawable.d_selected,R.drawable.e_selected};
        unSelectedImg = new int[]{R.drawable.a_normal,R.drawable.b_normal,R.drawable.c_normal,R.drawable.d_normal,R.drawable.e_normal};

        leftToRightBarAni = AnimationUtils.loadAnimation(ExamInfoPageActivity.this, R.anim.left_to_right_bar);



        submitBtn.setOnClickListener(this);
        questionA.setOnClickListener(this);
        questionB.setOnClickListener(this);
        questionC.setOnClickListener(this);
        questionD.setOnClickListener(this);
        questionE.setOnClickListener(this);

    }

    //计时器
    private void timeStart(){

        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //清除动画
                        imgBar.clearAnimation();
                        //弹出弹窗，答题时间到
                        showNoticeDialog();

                    }
                });
            }
        };



        timer.schedule(task,examPreModel.getCostLimit()*1000);
    }

    /**
     * 显示时间到期对话框
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamInfoPageActivity.this);
        builder.setMessage("确认后可查看答案,本题按<错误>计算？");
        builder.setTitle("答题超时：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                answers.clear();
                //请求提交答案接口
                answers.add("10");
                //本地比较答案与所选是否一致
//                equalsAnswers();

                StringBuffer stringBuffer = new StringBuffer();
                for (String s : answers){
                    stringBuffer.append(s).append(",");

                }
                ServerUtils.submitAnswer(examPreModel.getId()+"",ques.getId()+"",stringBuffer.toString(),answerBack);
                //修改按钮状态
                if (indexQues < examPreModel.getQuestionCount()-1){
                    submitState=2;
                    submitBtn.setText("进入下一题");
                }else {
                    submitState=3;
                    submitBtn.setText("查看全部试题");
                }
                //停止时间停止动画
                imgBar.clearAnimation();
                timer.cancel();
                timer.purge();

                //显示正确答案
                for (Integer i : ques.getRightAnswers()){
                    LinearLayout l = quesViewList.get(i);
                    //144 232 89 0.7
                    l.setBackgroundColor(0xB390e859);
                }

                //把错误答案放入放里list
                resultList.add(false);
                ArrayList<Integer> empty = new ArrayList();
                empty.add(10);
                resultABCList.add(empty);


            }
        });

        builder.create().show();
    }

    /**
     * 如果在考试中强制返回就会按提交答卷处理
     * */

    private void showReturnNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamInfoPageActivity.this);
        builder.setMessage("确认后返回试装列表页面，此试卷按已经作答处理");
        builder.setTitle("答题提醒：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                setResult(10001);
                finish();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ques_submit_next:
                //点击提交
                if (submitState ==1){
                    //请求提交答案接口
                    if (answers.size()==0){
                        Utils.showToast(this, "请选择答案");
                        return;
                    }
                    //本地比较答案与所选是否一致
                    equalsAnswers();

                    StringBuffer stringBuffer = new StringBuffer();
                    for (String s : answers){
                        stringBuffer.append(s).append(",");

                    }
                    ServerUtils.submitAnswer(examPreModel.getId()+"",ques.getId()+"",stringBuffer.toString(),answerBack);
                    //修改按钮状态
                    if (indexQues<examPreModel.getQuestionCount()-1){
                        submitState=2;
                        submitBtn.setText("进入下一题");
                    }else {
                        submitState=3;
                        submitBtn.setText("查看全部试题");
                    }
                    //停止时间停止动画
                    imgBar.clearAnimation();
                    timer.cancel();
                    timer.purge();

                    //显示正确答案
                    for (Integer i : ques.getRightAnswers()){
                        LinearLayout l = quesViewList.get(i);
                        //144 232 89 0.7
                        l.setBackgroundColor(0xB390e859);
                    }

                }else if (submitState ==2){
                    //清除上一题的数据
                    answers.clear();
                    //清除正确答案显示
                    for (Integer i : ques.getRightAnswers()){
                        LinearLayout l = quesViewList.get(i);
                        //144 232 89 0.7
                        l.setBackgroundColor(0x00000000);
                    }
                    quesImgA.setImageResource(R.drawable.a_normal);
                    quesImgB.setImageResource(R.drawable.b_normal);
                    quesImgC.setImageResource(R.drawable.c_normal);
                    quesImgD.setImageResource(R.drawable.d_normal);
                    quesImgE.setImageResource(R.drawable.e_normal);


                    //切换下一题，并刷新数据
                    indexQues++;
                    examAnswerNum.setText((indexQues+1)+"/"+dataSource.size());
                    setQuestionIndex(indexQues);
                    //修改按钮状态
                    submitState=1;
                    submitBtn.setText("提交答案");



                }else if(submitState == 3){
                    //跳到下一个页面，并finish
                    Intent intent = new Intent(ExamInfoPageActivity.this,ExamAnsweredActivity.class);


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("result", (Serializable) resultList);
                    bundle.putSerializable("resultABC",(Serializable)resultABCList);
                    intent.putExtras(bundle);
                    intent.putExtra("exam",examPreModel);
                    intent.putExtra("isTestExam",isTestExam);
                    intent.putExtra("answers",(Serializable) dataSource);
                    startActivity(intent);
                    //关闭本页面
                    setResult(10001);
                    finish();

                }

                break;

            case R.id.questionA_item:
                if (submitState==1){
                    setMapValue("0",quesImgA,R.drawable.a_normal,R.drawable.a_selected);
                    typeIsDouble("0");
                }

                break;
            case R.id.questionB_item:
                if (submitState==1){
                    setMapValue("1",quesImgB,R.drawable.b_normal,R.drawable.b_selected);
                    typeIsDouble("1");
                }

                break;
            case R.id.questionC_item:
                if (submitState==1){
                    setMapValue("2",quesImgC,R.drawable.c_normal,R.drawable.c_selected);
                    typeIsDouble("2");
                }

                break;
            case R.id.questionD_item:
                if (submitState==1){
                    setMapValue("3",quesImgD,R.drawable.d_normal,R.drawable.d_selected);
                    typeIsDouble("3");
                }

                break;

            case R.id.questionE_item:
                if (submitState==1){
                    setMapValue("4",quesImgE,R.drawable.e_normal,R.drawable.e_selected);
                    typeIsDouble("4");
                }

                break;


        }

    }

    private boolean eques(ArrayList<Integer> a,ArrayList<Integer> b){

        if (a == null||a.size()==0 || b == null || b.size()==0){
            return false;
        }

        if (a.size() != b.size()){
            return false;
        }

        for (Integer i : a){
            if (!b.contains(i)){
                return false;
            }
        }

        return true;
    }


    //判断是否答对了题
    private void equalsAnswers(){

        ArrayList<Integer> a = new ArrayList<>();
        for (String s:answers){
            a.add(Integer.valueOf(s).intValue());
        }



        boolean isEqual = eques(a,ques.getRightAnswers());

        resultABCList.add(a);

        //是否是正确答案
        if (isEqual){
            rightNum++;
            resultList.add(true);


        }else{
            wrongNum++;
            resultList.add(false);
        }

        examRightNum.setText("正确 "+rightNum);
        examWrongNum.setText("错误 "+wrongNum);

    }

    //如果是单选，就把之前选中的删除掉
    private void typeIsDouble(String key){
        //多选
        if (ques.getQuestionType() != 3){
            if (answers.size()>0 && !answers.get(0).equalsIgnoreCase(key)){
                int v = Integer.valueOf(answers.get(0)).intValue();
                setMapValue(answers.get(0),imageViewList.get(v),unSelectedImg[v],selectedImg[v]);
            }

        }
    }


    private void setMapValue(String key,ImageView img,int unSelectId,int selectId){
        if (isSelectMap.get(key)){
            isSelectMap.put(key,false);
            img.setImageResource(unSelectId);
            answers.remove(key);
        }else {
            isSelectMap.put(key,true);
            img.setImageResource(selectId);
            answers.add(key);
        }
    }

    private RequestCallBack<String> answerBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //在这里判断如果已经答题了，返回的时候有弹窗
            case android.R.id.home:
                if (answers.size()>0){

                    showReturnNoticeDialog();

                }else {
                    setResult(10001);
                    finish();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
            if (answers.size()>0){

                showReturnNoticeDialog();
                return true;
            }else {
                setResult(10001);
                return super.dispatchKeyEvent(event);
            }



        }else {
            return super.dispatchKeyEvent(event);
        }


    }

    //测试请求返回
    private RequestCallBack<String> testCallback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String jsonStr = responseInfo.result;
            ExamQuesModelBean modelBean = JsonUtils.jsonToPojo(jsonStr,ExamQuesModelBean.class);

            if (modelBean != null){
                if (modelBean.getStatus() == 200){
                    dataSource = (ArrayList<ExamQuesModel>)modelBean.getData();
                    //设置题目
                    setQuestionIndex(indexQues);

                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
    //计算实际的题目数据


    private RequestCallBack<String> callback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String jsonStr = responseInfo.result;
            ExamQuesModelBean modelBean = JsonUtils.jsonToPojo(jsonStr,ExamQuesModelBean.class);

            if (modelBean != null){
                if (modelBean.getStatus() == 200){
                    dataSource = (ArrayList<ExamQuesModel>)modelBean.getData();
                    //设置题目
                    setQuestionIndex(indexQues);

                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private void setQuestionIndex(int a){

        //这里来初始化题目
        ques = dataSource.get(a);
        int quesNum = ques.getOptions().size();
        isSelectMap.clear();


        switch (quesNum){
            case 1:
                questionA.setVisibility(View.VISIBLE);
                questionB.setVisibility(View.GONE);
                questionC.setVisibility(View.GONE);
                questionD.setVisibility(View.GONE);
                questionE.setVisibility(View.GONE);

                quesTextA.setText(ques.getOptions().get(0));


                break;
            case 2:
                questionA.setVisibility(View.VISIBLE);
                questionB.setVisibility(View.VISIBLE);
                questionC.setVisibility(View.GONE);
                questionD.setVisibility(View.GONE);
                questionE.setVisibility(View.GONE);

                quesTextA.setText(ques.getOptions().get(0));
                quesTextB.setText(ques.getOptions().get(1));

                break;
            case 3:
                questionA.setVisibility(View.VISIBLE);
                questionB.setVisibility(View.VISIBLE);
                questionC.setVisibility(View.VISIBLE);
                questionD.setVisibility(View.GONE);
                questionE.setVisibility(View.GONE);

                quesTextA.setText(ques.getOptions().get(0));
                quesTextB.setText(ques.getOptions().get(1));
                quesTextC.setText(ques.getOptions().get(2));

                break;
            case 4:
                questionA.setVisibility(View.VISIBLE);
                questionB.setVisibility(View.VISIBLE);
                questionC.setVisibility(View.VISIBLE);
                questionD.setVisibility(View.VISIBLE);
                questionE.setVisibility(View.GONE);

                quesTextA.setText(ques.getOptions().get(0));
                quesTextB.setText(ques.getOptions().get(1));
                quesTextC.setText(ques.getOptions().get(2));
                quesTextD.setText(ques.getOptions().get(3));

                break;
            case 5:
                questionA.setVisibility(View.VISIBLE);
                questionB.setVisibility(View.VISIBLE);
                questionC.setVisibility(View.VISIBLE);
                questionD.setVisibility(View.VISIBLE);
                questionE.setVisibility(View.VISIBLE);

                quesTextA.setText(ques.getOptions().get(0));
                quesTextB.setText(ques.getOptions().get(1));
                quesTextC.setText(ques.getOptions().get(2));
                quesTextD.setText(ques.getOptions().get(3));
                quesTextE.setText(ques.getOptions().get(4));
                break;
        }

        for (int i = 0; i<quesNum;i++){
            isSelectMap.put(i+"",false);
        }



        if (ques.getQuestionType()==1){
            //判断题
            titleImgView.setImageResource(R.drawable.exam_panduan);


        }else if (ques.getQuestionType()==2){
            //单选题
            titleImgView.setImageResource(R.drawable.exam_danxuan);


        }else if (ques.getQuestionType()==3){
            //多选题
            titleImgView.setImageResource(R.drawable.exam_duoxuan);

        }



        titleName.setText(ques.getTitle());

        //开启计时，并启动动画效果
        imgBar.startAnimation(leftToRightBarAni);
        //开启时间
        timeStart();
    }



}
