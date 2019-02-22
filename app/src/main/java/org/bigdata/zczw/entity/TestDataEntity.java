package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by darg on 2016/11/14.
 */
public class TestDataEntity {
    /**
     * pageCount : 1
     * recordList : [{"updid":null,"deleteflag":"1","createtime":1479107598000,"examname":"2","createid":2,"examid":2,"updtime":null,"url":"2"},{"updid":null,"deleteflag":"1","createtime":1479094039000,"examname":"发给","createid":1,"examid":1,"updtime":null,"url":"www.baidu.com"}]
     * recordCount : 2
     * pageSize : 20
     * currentPage : 1
     */
    private int pageCount;
    private List<Test> recordList;
    private int recordCount;
    private int pageSize;
    private int currentPage;

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setRecordList(List<Test> recordList) {
        this.recordList = recordList;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<Test> getRecordList() {
        return recordList;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

}