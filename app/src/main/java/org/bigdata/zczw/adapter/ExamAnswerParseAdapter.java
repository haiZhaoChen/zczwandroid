package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.ExamQuesModel;

import java.util.ArrayList;

public class ExamAnswerParseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ExamQuesModel> dataList;
    private ArrayList<Boolean> resultList;
    private ArrayList<ArrayList<Integer>> resultABCList;
    private String[] rightAnswers = new String[]{"A","B","C","D","E","F","G","X","Z","未答"};

    public ExamAnswerParseAdapter(Context context, ArrayList<ExamQuesModel> dataList,ArrayList<Boolean> resultList,ArrayList<ArrayList<Integer>> resABC){
        this.context = context;
        this.dataList = dataList;
        this.resultList = resultList;
        this.resultABCList = resABC;
    }



    @Override
    public int getCount() {
        if (dataList != null){
            return dataList.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exampro_parse,null);
            viewHolder.titleName = (TextView) convertView.findViewById(R.id.question_name);
            viewHolder.titleImgView = (ImageView)convertView.findViewById(R.id.title_img_name);
            viewHolder.questionA = (LinearLayout) convertView.findViewById(R.id.questionA_item);
            viewHolder.questionB = (LinearLayout) convertView.findViewById(R.id.questionB_item);
            viewHolder.questionC = (LinearLayout) convertView.findViewById(R.id.questionC_item);
            viewHolder.questionD = (LinearLayout) convertView.findViewById(R.id.questionD_item);
            viewHolder.questionE = (LinearLayout) convertView.findViewById(R.id.questionE_item);

           viewHolder.quesTextA = (TextView) convertView.findViewById(R.id.questionA_text_name);
           viewHolder.quesTextB = (TextView) convertView.findViewById(R.id.questionB_text_name);
           viewHolder.quesTextC = (TextView) convertView.findViewById(R.id.questionC_text_name);
           viewHolder.quesTextD = (TextView) convertView.findViewById(R.id.questionD_text_name);
           viewHolder.quesTextE = (TextView) convertView.findViewById(R.id.questionE_text_name);

           viewHolder.rightAnswer = (TextView)convertView.findViewById(R.id.exam_right_num);
           viewHolder.answerValue = (TextView)convertView.findViewById(R.id.exam_error_num);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        ExamQuesModel ques = dataList.get(position);

        int quesNum = ques.getOptions().size();


        switch (quesNum){
            case 1:
                viewHolder.questionA.setVisibility(View.VISIBLE);
                viewHolder.questionB.setVisibility(View.GONE);
                viewHolder.questionC.setVisibility(View.GONE);
                viewHolder.questionD.setVisibility(View.GONE);
                viewHolder.questionE.setVisibility(View.GONE);
                viewHolder.quesTextA.setText(ques.getOptions().get(0));


                break;
            case 2:
                viewHolder.questionA.setVisibility(View.VISIBLE);
                viewHolder.questionB.setVisibility(View.VISIBLE);
                viewHolder.questionC.setVisibility(View.GONE);
                viewHolder.questionD.setVisibility(View.GONE);
                viewHolder.questionE.setVisibility(View.GONE);
                viewHolder.quesTextA.setText(ques.getOptions().get(0));
                viewHolder.quesTextB.setText(ques.getOptions().get(1));

                break;
            case 3:
                viewHolder.questionA.setVisibility(View.VISIBLE);
                viewHolder.questionB.setVisibility(View.VISIBLE);
                viewHolder.questionC.setVisibility(View.VISIBLE);
                viewHolder.questionD.setVisibility(View.GONE);
                viewHolder.questionE.setVisibility(View.GONE);

                viewHolder.quesTextA.setText(ques.getOptions().get(0));
                viewHolder.quesTextB.setText(ques.getOptions().get(1));
                viewHolder.quesTextC.setText(ques.getOptions().get(2));

                break;
            case 4:
                viewHolder.questionA.setVisibility(View.VISIBLE);
                viewHolder.questionB.setVisibility(View.VISIBLE);
                viewHolder.questionC.setVisibility(View.VISIBLE);
                viewHolder.questionD.setVisibility(View.VISIBLE);
                viewHolder.questionE.setVisibility(View.GONE);

                viewHolder.quesTextA.setText(ques.getOptions().get(0));
                viewHolder.quesTextB.setText(ques.getOptions().get(1));
                viewHolder.quesTextC.setText(ques.getOptions().get(2));
                viewHolder.quesTextD.setText(ques.getOptions().get(3));

                break;
            case 5:
                viewHolder.questionA.setVisibility(View.VISIBLE);
                viewHolder.questionB.setVisibility(View.VISIBLE);
                viewHolder.questionC.setVisibility(View.VISIBLE);
                viewHolder.questionD.setVisibility(View.VISIBLE);
                viewHolder.questionE.setVisibility(View.VISIBLE);

                viewHolder.quesTextA.setText(ques.getOptions().get(0));
                viewHolder.quesTextB.setText(ques.getOptions().get(1));
                viewHolder.quesTextC.setText(ques.getOptions().get(2));
                viewHolder.quesTextD.setText(ques.getOptions().get(3));
                viewHolder.quesTextE.setText(ques.getOptions().get(4));
                break;
        }


        if (ques.getQuestionType()==1){
            //判断题
            viewHolder.titleImgView.setImageResource(R.drawable.exam_panduan);


        }else if (ques.getQuestionType()==2){
            //单选题
            viewHolder.titleImgView.setImageResource(R.drawable.exam_danxuan);


        }else if (ques.getQuestionType()==3){
            //多选题
            viewHolder.titleImgView.setImageResource(R.drawable.exam_duoxuan);

        }


        StringBuffer rightStr = new StringBuffer();
        for (Integer i : ques.getRightAnswers()){
            rightStr.append(rightAnswers[i]);
        }
        viewHolder.rightAnswer.setText(rightStr);

        if (position>=resultList.size()){
            //答案少于试题数
            viewHolder.answerValue.setText("未答 错误");

        }else {
            Boolean isRight = resultList.get(position);


            ArrayList<Integer> answers = resultABCList.get(position);
            StringBuffer an = new StringBuffer();
            Boolean isNoAnswer = false;
            for (Integer i:answers){
                if (i == 10){
                    isNoAnswer = true;
                }else {
                    an.append(rightAnswers[i]);
                    isNoAnswer = false;
                }
            }

            if (isRight){
                viewHolder.answerValue.setTextColor(0xff00ff00);
                viewHolder.answerValue.setText(an + " 正确");
            }else {
                viewHolder.answerValue.setTextColor(0xffff0000);
                if (isNoAnswer){
                    viewHolder.answerValue.setText("未答 错误");
                }else {
                    viewHolder.answerValue.setText(an +" 错误");
                }

            }


        }

        viewHolder.titleName.setText(ques.getTitle());

        return convertView;
    }

    private class ViewHolder{
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

        private TextView rightAnswer;
        private TextView answerValue;


    }
}
