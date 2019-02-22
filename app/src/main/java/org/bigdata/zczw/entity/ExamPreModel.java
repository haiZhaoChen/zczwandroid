package org.bigdata.zczw.entity;

import java.io.Serializable;

public class ExamPreModel implements Serializable {
/*
*{"status":200,
*"msg":"OK",
*"data":[{"id":65,"title":"综合题库201811","examType":1,"costLimit":25,"questinCountPanDuan":3,"questinCountDanXuan":3,"questinCountDuoXuan":4,"questionCount":10,"createDate":1541001600000,"meJoin":false}]}
* */

    private int id;
    private String title;
    private int examType;
    private int costLimit;
    private int questinCountPanDuan;
    private int questinCountDanXuan;
    private int questinCountDuoXuan;
    private int questionCount;
    private long createDate;
    private boolean meJoin;

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

    public int getExamType() {
        return examType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
    }

    public int getCostLimit() {
        return costLimit;
    }

    public void setCostLimit(int costLimit) {
        this.costLimit = costLimit;
    }

    public int getQuestinCountPanDuan() {
        return questinCountPanDuan;
    }

    public void setQuestinCountPanDuan(int questinCountPanDuan) {
        this.questinCountPanDuan = questinCountPanDuan;
    }

    public int getQuestinCountDanXuan() {
        return questinCountDanXuan;
    }

    public void setQuestinCountDanXuan(int questinCountDanXuan) {
        this.questinCountDanXuan = questinCountDanXuan;
    }

    public int getQuestinCountDuoXuan() {
        return questinCountDuoXuan;
    }

    public void setQuestinCountDuoXuan(int questinCountDuoXuan) {
        this.questinCountDuoXuan = questinCountDuoXuan;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public boolean isMeJoin() {
        return meJoin;
    }

    public void setMeJoin(boolean meJoin) {
        this.meJoin = meJoin;
    }
}
