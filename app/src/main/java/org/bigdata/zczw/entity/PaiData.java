package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2017/10/19.
 */

public class PaiData {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":4,"content":"测试","images":null,"video":{"id":null,"thumbnail":"http://zczwstorage.ewonline.org:8094/images/2017/10/19/1508379185105988.png","length":7659,"size":8597657,"path":"http://zczwstorage.ewonline.org:8094/videos/2017/10/19/1508379179229202.mp4"},"category":1,"tag":1,"createUserId":2067,"createDate":1508379179000,"author":{"id":2067,"name":"张涛","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/09/15/1505445058650387.jpg","phone":null,"unitsName":null}},{"id":3,"content":"测试图","images":["http://zczwstorage.ewonline.org:8094/images/2017/10/19/1508373011558426.jpg","http://zczwstorage.ewonline.org:8094/images/2017/10/19/1508373011671961.jpg","http://zczwstorage.ewonline.org:8094/images/2017/10/19/1508373011784354.jpg","http://zczwstorage.ewonline.org:8094/images/2017/10/19/1508373011918783.jpg","http://zczwstorage.ewonline.org:8094/images/2017/10/19/1508373012040669.jpg"],"video":null,"category":1,"tag":1,"createUserId":2067,"createDate":1508373012000,"author":{"id":2067,"name":"张涛","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/09/15/1505445058650387.jpg","phone":null,"unitsName":null}}]
     */

    private int status;
    private String msg;
    private List<Pai> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Pai> getData() {
        return data;
    }

    public void setData(List<Pai> data) {
        this.data = data;
    }

}
