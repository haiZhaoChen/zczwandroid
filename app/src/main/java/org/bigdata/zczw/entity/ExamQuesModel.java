package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ExamQuesModel implements Serializable{


/*
{"id":43,"questionType":1,"title":"判断题1","options":["正确","错误"],"rightAnswers":[0],"answers":"0,"}
* */

    private int id;
    private String title;
    private int questionType;
    private ArrayList<String> options;
    private ArrayList<Integer> rightAnswers;
    private String answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public ArrayList<Integer> getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(ArrayList<Integer> rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
